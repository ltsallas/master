package wifi4eu.wifi4eu.abac.entity;

import wifi4eu.wifi4eu.abac.service.AbacWorkflowStatusEnum;

public class MonitoringRow {
	
	private Long id;
	private String region;
	private String countryCode;
	private AbacWorkflowStatusEnum wfStatus;
	
	public MonitoringRow() {
	}
	
	public MonitoringRow(Long id, String region, String countryCode, AbacWorkflowStatusEnum wfStatus) {
		super();
		this.id = id;
		this.region = region;
		this.countryCode = countryCode;
		this.wfStatus = wfStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public AbacWorkflowStatusEnum getWfStatus() {
		return wfStatus;
	}

	public void setWfStatus(AbacWorkflowStatusEnum wfStatus) {
		this.wfStatus = wfStatus;
	}
	
}
