package com.vincent;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Main {

    public static final String[] CENTER = {"SRC", "LIN", "MSC", "WAC", "EAC"};

    public static void main(String args[]) {
        WebCrawler webCrawler = new WebCrawler();
        JerseyClient client = JerseyClientBuilder.createClient();
        client.register(MultiPartFeature.class);
        executeBatches(webCrawler, client);
    }

    private static void executeBatches(WebCrawler webCrawler, JerseyClient client) {
        Arrays.stream(CENTER).forEach(center ->
                IntStream.rangeClosed(0, 32).forEach(week ->
                        IntStream.rangeClosed(0, 9).parallel().forEach(batchNumber -> {
                            List<CaseStatus> caseStatuses = new ArrayList<>();
                            try {
                                excuteEachBatch(webCrawler, client, center, week, batchNumber, caseStatuses);
                            } catch (Exception e) {
                                System.out.println("Failed fetch at " + caseStatuses.get(caseStatuses.size()-1).getCaseNumber());
                                e.printStackTrace();
                                throw e;
                            }
                            webCrawler.persist(caseStatuses);
                        })));
    }

    private static void excuteEachBatch(WebCrawler webCrawler, JerseyClient client, String center, int week, int batchNumber, List<CaseStatus> caseStatuses) {
        int numberOfErrors = 0;
        int lastErrorSequence = -1;
        for (int sequenceNumber = 0; sequenceNumber < 1000; sequenceNumber++) {
            String groupNumber = "2190" + String.format("%02d", week);
            int sequence = batchNumber * 1000 + sequenceNumber;

            CaseStatus caseStatus = webCrawler.analyzeData(client, center, groupNumber, sequence);
            caseStatuses.add(caseStatus);

            // skip if not able to analysis result when errors continuously happen
            if (caseStatus.getStatus() == null) {
                System.out.println(caseStatus);
            }

        }
    }

}
