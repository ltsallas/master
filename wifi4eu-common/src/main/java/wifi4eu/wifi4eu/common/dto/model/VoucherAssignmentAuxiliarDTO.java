package wifi4eu.wifi4eu.common.dto.model;

public class VoucherAssignmentAuxiliarDTO {

    private Integer id;
    private Long executionDate;
    private Integer status;

    public VoucherAssignmentAuxiliarDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Long executionDate) {
        this.executionDate = executionDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
