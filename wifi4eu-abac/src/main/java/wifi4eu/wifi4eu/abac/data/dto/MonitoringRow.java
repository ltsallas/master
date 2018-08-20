package wifi4eu.wifi4eu.abac.data.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import wifi4eu.wifi4eu.abac.data.entity.BudgetaryCommitment;
import wifi4eu.wifi4eu.abac.data.entity.Document;
import wifi4eu.wifi4eu.abac.data.entity.LegalCommitment;
import wifi4eu.wifi4eu.abac.data.entity.LegalEntity;
import wifi4eu.wifi4eu.abac.data.enums.AbacWorkflowStatus;
import wifi4eu.wifi4eu.abac.data.enums.DocumentWorkflowStatus;

public class MonitoringRow {
	
	private Long id;
	private String countryCode;
	private String municipality;
	private Long registrationNumber;
	private String lefStatus;
	private String bcStatus;
	private String lcStatus;
	private Boolean readyToBeCounterSigned;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date signatureDate;
	
	@JsonFormat(pattern="dd/MM/yyyy")
	private Date counterSignatureDate;
	
	public MonitoringRow() {
	}
	
	public MonitoringRow(LegalEntity legalEntity, BudgetaryCommitment budgetaryCommitment, LegalCommitment legalCommitment, Document grantAgreementDoc) {

		//Legal Entity data
		if(legalEntity != null) {
			this.setId(legalEntity.getId());
			if (legalEntity.getCountry() != null) {
				this.setCountryCode(legalEntity.getCountry().getIso2Code());
			}
			this.setMunicipality(legalEntity.getOfficialName());
			this.setRegistrationNumber(legalEntity.getRegistrationNumber());
			this.setSignatureDate(legalEntity.getSignatureDate());
			this.setLefStatus(legalEntity.getWfStatus());
		}

		//Budgetary Commitment Data
		if(budgetaryCommitment != null) {
			this.setBcStatus(budgetaryCommitment.getWfStatus());
		}

		//Legal Commitment Data
		if(legalCommitment != null) {
			this.setLcStatus(legalCommitment.getWfStatus());
		}

		//Grant Agreement Data
		if(grantAgreementDoc != null) {
			this.setSignatureDate(grantAgreementDoc.getPortalDate());
			this.setCounterSignatureDate(grantAgreementDoc.getCounterSignatureDate());
		}

		//TODO check in the USER REQUIREMENTS if these condtions are right
		readyToBeCounterSigned = legalEntity != null && budgetaryCommitment != null && grantAgreementDoc != null
								&& legalEntity.getWfStatus().equals(AbacWorkflowStatus.ABAC_VALID)
								&& budgetaryCommitment.getWfStatus().equals(AbacWorkflowStatus.ABAC_VALID)
								&& grantAgreementDoc.getWfStatus().equals(DocumentWorkflowStatus.IMPORTED);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMunicipality() {
		return municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public Long getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(Long registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getLefStatus() {
		return lefStatus;
	}

	public void setLefStatus(String lefStatus) {
		this.lefStatus = lefStatus;
	}
	
	public void setLefStatus(AbacWorkflowStatus lefStatus) {
		this.lefStatus = lefStatus.getTitle();
	}

	public String getBcStatus() {
		return bcStatus;
	}

	public void setBcStatus(String bcStatus) {
		this.bcStatus = bcStatus;
	}
	
	public void setBcStatus(AbacWorkflowStatus bcStatus) {
		this.bcStatus = bcStatus.getTitle();
	}

	public String getLcStatus() {
		return lcStatus;
	}

	public void setLcStatus(String lcStatus) {
		this.lcStatus = lcStatus;
	}
	
	public void setLcStatus(AbacWorkflowStatus lcStatus) {
		this.lcStatus = lcStatus.getTitle();
	}

	public Date getSignatureDate() {
		return signatureDate;
	}

	public void setSignatureDate(Date signatureDate) {
		this.signatureDate = signatureDate;
	}

	public Date getCounterSignatureDate() {
		return counterSignatureDate;
	}

	public void setCounterSignatureDate(Date counterSignatureDate) {
		this.counterSignatureDate = counterSignatureDate;
	}

	public Boolean getReadyToBeCounterSigned() {
		return readyToBeCounterSigned;
	}

	public void setReadyToBeCounterSigned(Boolean readyToBeCounterSigned) {
		this.readyToBeCounterSigned = readyToBeCounterSigned;
	}
}
