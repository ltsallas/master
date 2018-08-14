package wifi4eu.wifi4eu.entity.registration;

import wifi4eu.wifi4eu.entity.user.*;

import javax.persistence.*;

@Entity
@Table(name = "legal_files")
public class LegalFiles {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "registration")
	private int registration;

	@Column(name = "data")
	private String fileData;

	@Column(name = "type")
	private int fileType;

	public LegalFiles() {
	}

	public LegalFiles(int registration, String fileData, int fileType) {
		this.registration = registration;
		this.fileData = fileData;
		this.fileType = fileType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getRegistration() {
		return registration;
	}

	public void setRegistration(int registration) {
		this.registration = registration;
	}

	public String getFileData() {
		return fileData;
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
}