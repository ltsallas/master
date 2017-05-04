package wifi4eu.wifi4eu.common.dto.model;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class HelpdeskDTO {

    private Long issueId;
    @NotNull
    private String portal;
    private String topic;
    private String memberState;
    private Date date;
    private String assignedTo;
    private String status;
    private String from;
    private String issueSummary;
    private List<HelpdeskCommentDTO> comments;

    public HelpdeskDTO() {
    }

    public HelpdeskDTO(Long issueId, String portal, String topic, String memberState, Date date, String assignedTo, String status, String from, String issueSummary, List<HelpdeskCommentDTO> comments) {
        this.issueId = issueId;
        this.portal = portal;
        this.topic = topic;
        this.memberState = memberState;
        this.date = date;
        this.assignedTo = assignedTo;
        this.status = status;
        this.from = from;
        this.issueSummary = issueSummary;
        this.comments = comments;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getIssueSummary() {
        return issueSummary;
    }

    public void setIssueSummary(String issueSummary) {
        this.issueSummary = issueSummary;
    }

    public List<HelpdeskCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<HelpdeskCommentDTO> comments) {
        this.comments = comments;
    }
}
