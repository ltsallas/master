package wifi4eu.wifi4eu.common.dto.model;

import java.util.ArrayList;
import java.util.List;

public class BankAccountDTO {

    private int id;
    private int supplierId;

    private String accountName;
    private String iban;

    private String bankName;
    private String bankStreet;
    private String bankNumber;
    private String bankCity;
    private String bankCountry;

    private String accountHolderStreet;
    private String accountHolderNumber;
    private String accountHolderCity;
    private String accountHolderCountry;

    private Integer status;
    private String rejectionCause;

    private boolean sameAddressAsSupplier;

    private List<BankAccountDocumentDTO> bankAccountDocumentDTOList = new ArrayList<>();

    public BankAccountDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankStreet() {
        return bankStreet;
    }

    public void setBankStreet(String bankStreet) {
        this.bankStreet = bankStreet;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getBankCountry() {
        return bankCountry;
    }

    public void setBankCountry(String bankCountry) {
        this.bankCountry = bankCountry;
    }

    public String getAccountHolderStreet() {
        return accountHolderStreet;
    }

    public void setAccountHolderStreet(String accountHolderStreet) {
        this.accountHolderStreet = accountHolderStreet;
    }

    public String getAccountHolderNumber() {
        return accountHolderNumber;
    }

    public void setAccountHolderNumber(String accountHolderNumber) {
        this.accountHolderNumber = accountHolderNumber;
    }

    public String getAccountHolderCity() {
        return accountHolderCity;
    }

    public void setAccountHolderCity(String accountHolderCity) {
        this.accountHolderCity = accountHolderCity;
    }

    public String getAccountHolderCountry() {
        return accountHolderCountry;
    }

    public void setAccountHolderCountry(String accountHolderCountry) {
        this.accountHolderCountry = accountHolderCountry;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRejectionCause() {
        return rejectionCause;
    }

    public void setRejectionCause(String rejectionCause) {
        this.rejectionCause = rejectionCause;
    }

    public List<BankAccountDocumentDTO> getBankAccountDocumentDTOList() {
        return bankAccountDocumentDTOList;
    }

    public void setBankAccountDocumentDTOList(List<BankAccountDocumentDTO> bankAccountDocumentDTOList) {
        this.bankAccountDocumentDTOList = bankAccountDocumentDTOList;
    }

    public boolean isSameAddressAsSupplier() {
        return sameAddressAsSupplier;
    }

    public void setSameAddressAsSupplier(boolean sameAddressAsSupplier) {
        this.sameAddressAsSupplier = sameAddressAsSupplier;
    }
}