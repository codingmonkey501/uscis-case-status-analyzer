package com.vincent;

import java.time.LocalDate;
import java.util.Objects;

public class CaseStatus {
    private String status;
    private String message;
    private int formTypeIndex = -1;
    private String formType;
    private LocalDate statusDate;
    private LocalDate fetchDay;
    private String center;
    private String groupNumber;
    private String sequence;
    private String trackingNumber;

    public CaseStatus() {
    }

    public CaseStatus(String status, String message, int formTypeIndex, String formType, LocalDate statusDate, LocalDate fetchDay, String center, String groupNumber, String sequence, String trackingNumber) {
        this.status = status;
        this.message = message;
        this.formTypeIndex = formTypeIndex;
        this.formType = formType;
        this.statusDate = statusDate;
        this.fetchDay = fetchDay;
        this.center = center;
        this.groupNumber = groupNumber;
        this.sequence = sequence;
        this.trackingNumber = trackingNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CaseStatus that = (CaseStatus) o;
        return formTypeIndex == that.formTypeIndex &&
                Objects.equals(status, that.status) &&
                Objects.equals(statusDate, that.statusDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, formTypeIndex, statusDate);
    }

    @Override
    public String toString() {
        return "CaseStatus{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", formTypeIndex=" + formTypeIndex +
                ", formType='" + formType + '\'' +
                ", statusDate=" + statusDate +
                ", fetchDay=" + fetchDay +
                ", center='" + center + '\'' +
                ", groupNumber='" + groupNumber + '\'' +
                ", sequence='" + sequence + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFormTypeIndex() {
        return formTypeIndex;
    }

    public void setFormTypeIndex(int formTypeIndex) {
        this.formTypeIndex = formTypeIndex;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public LocalDate getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(LocalDate statusDate) {
        this.statusDate = statusDate;
    }

    public LocalDate getFetchDay() {
        return fetchDay;
    }

    public void setFetchDay(LocalDate fetchDay) {
        this.fetchDay = fetchDay;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCaseNumber() {
        return this.getCenter() + this.getGroupNumber() + this.getSequence();
    }
}
