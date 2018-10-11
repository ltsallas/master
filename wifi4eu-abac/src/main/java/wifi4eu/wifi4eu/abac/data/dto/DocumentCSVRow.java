package wifi4eu.wifi4eu.abac.data.dto;

import wifi4eu.wifi4eu.abac.data.enums.DocumentType;

import java.util.Date;

public abstract class DocumentCSVRow {

	private Long documentPortalId;
	private String documentName;
	private String documentFileName;
	private String documentMimeType;
	private Date documentDate;
	private DocumentType documentType;
	private String aresReference;

	public Long getDocumentPortalId() {
		return documentPortalId;
	}

	public void setDocumentPortalId(Long documentPortalId) {
		this.documentPortalId = documentPortalId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}

	public String getDocumentMimeType() {
		return documentMimeType;
	}

	public void setDocumentMimeType(String documentMimeType) {
		this.documentMimeType = documentMimeType;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public String getAresReference() {
		return aresReference;
	}

	public void setAresReference(String aresReference) {
		this.aresReference = aresReference;
	}
}
