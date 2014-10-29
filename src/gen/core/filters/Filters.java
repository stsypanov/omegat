//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.06.02 at 04:25:52 PM JST 
//


package gen.core.filters;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}filter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="removeTags" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="removeSpacesNonseg" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="preserveSpaces" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="ignoreFileContext" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "filters"
})
@XmlRootElement(name = "filters")
public class Filters {

    @XmlElement(name = "filter")
    protected List<Filter> filters;
    @XmlAttribute(name = "removeTags")
    protected Boolean removeTags;
    @XmlAttribute(name = "removeSpacesNonseg")
    protected Boolean removeSpacesNonseg;
    @XmlAttribute(name = "preserveSpaces")
    protected Boolean preserveSpaces;
    @XmlAttribute(name = "ignoreFileContext")
    protected Boolean ignoreFileContext;

    /**
     * Gets the value of the filters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Filter }
     * 
     * 
     */
    public List<Filter> getFilters() {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        return this.filters;
    }

    /**
     * Gets the value of the removeTags property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRemoveTags() {
        if (removeTags == null) {
            return true;
        } else {
            return removeTags;
        }
    }

    /**
     * Sets the value of the removeTags property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRemoveTags(Boolean value) {
        this.removeTags = value;
    }

    /**
     * Gets the value of the removeSpacesNonseg property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isRemoveSpacesNonseg() {
        if (removeSpacesNonseg == null) {
            return true;
        } else {
            return removeSpacesNonseg;
        }
    }

    /**
     * Sets the value of the removeSpacesNonseg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRemoveSpacesNonseg(Boolean value) {
        this.removeSpacesNonseg = value;
    }

    /**
     * Gets the value of the preserveSpaces property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isPreserveSpaces() {
        if (preserveSpaces == null) {
            return false;
        } else {
            return preserveSpaces;
        }
    }

    /**
     * Sets the value of the preserveSpaces property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPreserveSpaces(Boolean value) {
        this.preserveSpaces = value;
    }

    /**
     * Gets the value of the ignoreFileContext property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIgnoreFileContext() {
        if (ignoreFileContext == null) {
            return false;
        } else {
            return ignoreFileContext;
        }
    }

    /**
     * Sets the value of the ignoreFileContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIgnoreFileContext(Boolean value) {
        this.ignoreFileContext = value;
    }

}
