package wifi4eu.wifi4eu.common.dto.model;

public class ApplicantListItemDTO {
    private Integer lauId;
    private Integer euRanking;
    private Integer countryRanking;
    private String countryCode;
    private String name;
    private Integer counter;
    private Boolean mediation;
    private Integer status;
    private Integer issueStatus;

    public ApplicantListItemDTO() {
    }

    public ApplicantListItemDTO(Integer lauId, Integer euRanking, Integer countryRanking, String countryCode, String name, Integer counter, Boolean mediation, Integer status, Integer issueStatus) {
        this.lauId = lauId;
        this.euRanking = euRanking;
        this.countryRanking = countryRanking;
        this.countryCode = countryCode;
        this.name = name;
        this.counter = counter;
        this.mediation = mediation;
        this.status = status;
        this.issueStatus = issueStatus;
    }

    public Integer getLauId() {
        return lauId;
    }

    public void setLauId(Integer lauId) {
        this.lauId = lauId;
    }

    public Integer getEuRanking() {
        return euRanking;
    }

    public void setEuRanking(Integer euRanking) {
        this.euRanking = euRanking;
    }

    public Integer getCountryRanking() {
        return countryRanking;
    }

    public void setCountryRanking(Integer countryRanking) {
        this.countryRanking = countryRanking;
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

    public Integer getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(Integer issueStatus) {
        this.issueStatus = issueStatus;
    }
}