
package generated.hrs.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ec.europa.eu/sg/hrs/types}header"/>
 *         &lt;element name="request" type="{http://ec.europa.eu/sg/hrs/types}DocumentSearchByExpressionRequest"/>
 *         &lt;element name="documentRetrievalOptions" type="{http://ec.europa.eu/sg/hrs/types}DocumentRetrievalOptions" minOccurs="0"/>
 *         &lt;element name="sortOptions" type="{http://ec.europa.eu/sg/hrs/types}SortOptions" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "request",
    "documentRetrievalOptions",
    "sortOptions"
})
@XmlRootElement(name = "searchDocumentsByExpression")
public class SearchDocumentsByExpression
    implements Equals, HashCode, ToString
{

    @XmlElement(required = true)
    protected Header header;
    @XmlElement(required = true)
    protected DocumentSearchByExpressionRequest request;
    protected DocumentRetrievalOptions documentRetrievalOptions;
    protected SortOptions sortOptions;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link Header }
     *     
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link Header }
     *     
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentSearchByExpressionRequest }
     *     
     */
    public DocumentSearchByExpressionRequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentSearchByExpressionRequest }
     *     
     */
    public void setRequest(DocumentSearchByExpressionRequest value) {
        this.request = value;
    }

    /**
     * Gets the value of the documentRetrievalOptions property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentRetrievalOptions }
     *     
     */
    public DocumentRetrievalOptions getDocumentRetrievalOptions() {
        return documentRetrievalOptions;
    }

    /**
     * Sets the value of the documentRetrievalOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentRetrievalOptions }
     *     
     */
    public void setDocumentRetrievalOptions(DocumentRetrievalOptions value) {
        this.documentRetrievalOptions = value;
    }

    /**
     * Gets the value of the sortOptions property.
     * 
     * @return
     *     possible object is
     *     {@link SortOptions }
     *     
     */
    public SortOptions getSortOptions() {
        return sortOptions;
    }

    /**
     * Sets the value of the sortOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link SortOptions }
     *     
     */
    public void setSortOptions(SortOptions value) {
        this.sortOptions = value;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof SearchDocumentsByExpression)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final SearchDocumentsByExpression that = ((SearchDocumentsByExpression) object);
        {
            Header lhsHeader;
            lhsHeader = this.getHeader();
            Header rhsHeader;
            rhsHeader = that.getHeader();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "header", lhsHeader), LocatorUtils.property(thatLocator, "header", rhsHeader), lhsHeader, rhsHeader)) {
                return false;
            }
        }
        {
            DocumentSearchByExpressionRequest lhsRequest;
            lhsRequest = this.getRequest();
            DocumentSearchByExpressionRequest rhsRequest;
            rhsRequest = that.getRequest();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "request", lhsRequest), LocatorUtils.property(thatLocator, "request", rhsRequest), lhsRequest, rhsRequest)) {
                return false;
            }
        }
        {
            DocumentRetrievalOptions lhsDocumentRetrievalOptions;
            lhsDocumentRetrievalOptions = this.getDocumentRetrievalOptions();
            DocumentRetrievalOptions rhsDocumentRetrievalOptions;
            rhsDocumentRetrievalOptions = that.getDocumentRetrievalOptions();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "documentRetrievalOptions", lhsDocumentRetrievalOptions), LocatorUtils.property(thatLocator, "documentRetrievalOptions", rhsDocumentRetrievalOptions), lhsDocumentRetrievalOptions, rhsDocumentRetrievalOptions)) {
                return false;
            }
        }
        {
            SortOptions lhsSortOptions;
            lhsSortOptions = this.getSortOptions();
            SortOptions rhsSortOptions;
            rhsSortOptions = that.getSortOptions();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "sortOptions", lhsSortOptions), LocatorUtils.property(thatLocator, "sortOptions", rhsSortOptions), lhsSortOptions, rhsSortOptions)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            Header theHeader;
            theHeader = this.getHeader();
            strategy.appendField(locator, this, "header", buffer, theHeader);
        }
        {
            DocumentSearchByExpressionRequest theRequest;
            theRequest = this.getRequest();
            strategy.appendField(locator, this, "request", buffer, theRequest);
        }
        {
            DocumentRetrievalOptions theDocumentRetrievalOptions;
            theDocumentRetrievalOptions = this.getDocumentRetrievalOptions();
            strategy.appendField(locator, this, "documentRetrievalOptions", buffer, theDocumentRetrievalOptions);
        }
        {
            SortOptions theSortOptions;
            theSortOptions = this.getSortOptions();
            strategy.appendField(locator, this, "sortOptions", buffer, theSortOptions);
        }
        return buffer;
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            Header theHeader;
            theHeader = this.getHeader();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "header", theHeader), currentHashCode, theHeader);
        }
        {
            DocumentSearchByExpressionRequest theRequest;
            theRequest = this.getRequest();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "request", theRequest), currentHashCode, theRequest);
        }
        {
            DocumentRetrievalOptions theDocumentRetrievalOptions;
            theDocumentRetrievalOptions = this.getDocumentRetrievalOptions();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "documentRetrievalOptions", theDocumentRetrievalOptions), currentHashCode, theDocumentRetrievalOptions);
        }
        {
            SortOptions theSortOptions;
            theSortOptions = this.getSortOptions();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "sortOptions", theSortOptions), currentHashCode, theSortOptions);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

}