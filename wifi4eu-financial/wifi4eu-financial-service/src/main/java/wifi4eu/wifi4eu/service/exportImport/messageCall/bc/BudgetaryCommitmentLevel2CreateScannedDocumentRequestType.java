
package wifi4eu.wifi4eu.service.exportImport.messageCall.bc;

import eu.europa.ec.budg.abac.message.v1.MessageRequestType;
import eu.europa.ec.budg.abac.scanned_document.v1.ScannedDocumentType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para BudgetaryCommitmentLevel2CreateScannedDocumentRequestType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="BudgetaryCommitmentLevel2CreateScannedDocumentRequestType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.ec.europa.eu/budg/abac/message/v1}MessageRequestType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LocalKey" type="{http://www.ec.europa.eu/budg/abac/simple_type/v1}LocalKeyType"/&gt;
 *         &lt;element name="ScannedDocument" type="{http://www.ec.europa.eu/budg/abac/scanned_document/v1}ScannedDocumentType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BudgetaryCommitmentLevel2CreateScannedDocumentRequestType", propOrder = {
    "localKey",
    "scannedDocument"
})
public class BudgetaryCommitmentLevel2CreateScannedDocumentRequestType
    extends MessageRequestType
{

    @XmlElement(name = "LocalKey", required = true)
    protected String localKey;
    @XmlElement(name = "ScannedDocument", required = true)
    protected ScannedDocumentType scannedDocument;

    /**
     * Obtiene el valor de la propiedad localKey.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalKey() {
        return localKey;
    }

    /**
     * Define el valor de la propiedad localKey.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalKey(String value) {
        this.localKey = value;
    }

    /**
     * Obtiene el valor de la propiedad scannedDocument.
     * 
     * @return
     *     possible object is
     *     {@link ScannedDocumentType }
     *     
     */
    public ScannedDocumentType getScannedDocument() {
        return scannedDocument;
    }

    /**
     * Define el valor de la propiedad scannedDocument.
     * 
     * @param value
     *     allowed object is
     *     {@link ScannedDocumentType }
     *     
     */
    public void setScannedDocument(ScannedDocumentType value) {
        this.scannedDocument = value;
    }

}