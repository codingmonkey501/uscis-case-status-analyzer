package com.vincent;

import org.glassfish.jersey.client.JerseyClient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {

    public static final String[] FORM_TYPES = {
            "I-129CW",
            "I-129F",
            "I-600A",
            "I-601A",
            "I-765V",
            "I-485J",
            "I-800A",
            "I-821D",
            "I-90",
            "I-102",
            "I-129",
            "I-130",
            "I-131",
            "I-140",
            "I-212",
            "I-360",
            "I-485",
            "I-526",
            "I-539",
            "I-600",
            "I-601",
            "I-612",
            "I-730",
            "I-751",
            "I-765",
            "I-800",
            "I-817",
            "I-821",
            "I-824",
            "I-829",
            "I-914",
            "I-918",
            "I-924",
            "I-929",
    };


    public static String REG_DATE = "((?:Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|(Nov|Dec)(?:ember)?) \\d{1,2}, \\d{4}),";
    public static String REG_TRACK = "is (\\d{20,24}).";
    public static String URL = "https://egov.uscis.gov/casestatus/mycasestatus.do";
    public static String SQLITE_URL = "jdbc:sqlite:E:/work/sqlite/uscis.db";

    public CaseStatus analyzeData(JerseyClient client, String center, String groupNumber, int sequenceId) {
        String sequence = String.format("%04d", sequenceId);
        String receiptNumber = center + groupNumber + sequence;
        String response = fetchUscisStatus(client, receiptNumber);

        int start = response.lastIndexOf("/casestatus/images/my_logo.png");
        int left = response.indexOf("<div", start);
        int right = response.indexOf("/div>", left) + 5;

        String statusSection = response.substring(left, right);
        String status = statusSection.substring(statusSection.indexOf("<h1>") + 4, statusSection.indexOf("</h1>"));
        status = status.isEmpty() ? null : status;

        String message = statusSection.substring(statusSection.indexOf("<p>") + 3, statusSection.indexOf("</p>"));

        int formTypeIndex = matchCaseType(message);
        String formType = formTypeIndex == -1 ? "Unknown" : FORM_TYPES[formTypeIndex];

        Matcher dateMatcher = Pattern.compile(REG_DATE).matcher(message);
        LocalDate date = null;
        if (dateMatcher.find()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                date = LocalDate.parse(dateMatcher.group(1), formatter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LocalDate fetchDay = LocalDate.now();

        Matcher trackingNumberMatcher = Pattern.compile(REG_TRACK).matcher(message);
        String trackingNumber = null;
        if (trackingNumberMatcher.find()) {
            try {
                trackingNumber = trackingNumberMatcher.group(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        CaseStatus caseStatus = new CaseStatus(status, message, formTypeIndex, formType, date, fetchDay, center, groupNumber, sequence, trackingNumber);
        return caseStatus;
    }

    private String fetchUscisStatus(Client client, String receiptNumber) {
        WebTarget target = client.target(URL);
        Form formData = new Form();
        formData.param("appReceiptNum", receiptNumber);
        formData.param("caseStatusSearchBtn", "CHECK+STATUS");
        formData.param("changeLocale", "");
        Response response = target.request(MediaType.TEXT_HTML_TYPE).post((Entity.form(formData)));
        return response.readEntity(String.class);
    }

    private int matchCaseType(String message) {
        if (message == null || message.isEmpty()) return -1;

        for (int i = 0; i < FORM_TYPES.length; i++) {
            if (message.contains(FORM_TYPES[i])) {
                return i;
            }
        }
        return -1;
    }

    public void persist(List<CaseStatus> caseStatuses) {
        if (caseStatuses.isEmpty()) return;

        try (Connection conn = DriverManager.getConnection(SQLITE_URL)) {
            conn.setAutoCommit(false);
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement preparedStatement = conn.prepareStatement("insert into CaseStatus(status, form_type, status_date, fetch_day, center, group_number, sequence_number, tracking_number) values (?,?,?,?,?,?,?,?)");

            for (CaseStatus caseStatus : caseStatuses) {
                preparedStatement.setString(1, caseStatus.getStatus());
                preparedStatement.setInt(2, caseStatus.getFormTypeIndex());
                preparedStatement.setString(3, caseStatus.getStatusDate() != null ? caseStatus.getStatusDate().toString() : null);
                preparedStatement.setString(4, caseStatus.getFetchDay() != null ? caseStatus.getFetchDay().toString() : null);
                preparedStatement.setString(5, caseStatus.getCenter());
                preparedStatement.setString(6, caseStatus.getGroupNumber());
                preparedStatement.setString(7, caseStatus.getSequence());
                preparedStatement.setString(8, caseStatus.getTrackingNumber());
                preparedStatement.addBatch();
            }
            int[] rows = preparedStatement.executeBatch();
            conn.commit();
            System.out.println("Done! rows=" + rows.length + " end at " + caseStatuses.get(caseStatuses.size()-1).getCaseNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
