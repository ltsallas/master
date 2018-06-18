package wifi4eu.wifi4eu.common.dto.model;

import java.util.List;

public class ApplicantListItemDTO {
    private Integer lauId;
    private String countryCode;
    private String name;
    private Integer counter;
    private Boolean mediation;
    private Integer status;
    private List<Integer> issueStatus;
    private Long applicationDate;
    private String invalidateReason;

    public ApplicantListItemDTO() {
    }

    public ApplicantListItemDTO(Integer lauId, String countryCode, String name, Integer counter, Boolean mediation, Integer status, List<Integer> issueStatus, Long applicationDate, String invalidateReason) {
        this.lauId = lauId;
        this.countryCode = countryCode;
        this.name = name;
        this.counter = counter;
        this.mediation = mediation;
        this.status = status;
        this.issueStatus = issueStatus;
        this.applicationDate = applicationDate;
        this.invalidateReason = invalidateReason;
    }

    public Integer getLauId() {
        return lauId;
    }

    public void setLauId(Integer lauId) {
        this.lauId = lauId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Boolean getMediation() {
        return mediation;
    }

    public void setMediation(Boolean mediation) {
        this.mediation = mediation;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(List<Integer> issueStatus) {
        this.issueStatus = issueStatus;
    }

    public Long getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Long applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getInvalidateReason() {
        return invalidateReason;
    }

    public void setInvalidateReason(String invalidateReason) {
        this.invalidateReason = invalidateReason;
    }
}