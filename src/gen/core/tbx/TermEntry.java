//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-520 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.21 at 11:57:29 AM JST 
//


package gen.core.tbx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;choice>
 *           &lt;choice maxOccurs="unbounded" minOccurs="0">
 *             &lt;element ref="{}descrip"/>
 *             &lt;element ref="{}descripGrp"/>
 *             &lt;element ref="{}admin"/>
 *             &lt;element ref="{}adminGrp"/>
 *             &lt;element ref="{}transacGrp"/>
 *             &lt;element ref="{}note"/>
 *             &lt;element ref="{}ref"/>
 *             &lt;element ref="{}xref"/>
 *           &lt;/choice>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element ref="{}langSet" maxOccurs="unbounded"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "descripOrDescripGrpOrAdmin",
    "langSet"
})
@XmlRootElement(name = "termEntry")
public class TermEntry {

    @XmlElements({
        @XmlElement(name = "descrip", type = Descrip.class),
        @XmlElement(name = "ref", type = Ref.class),
        @XmlElement(name = "xref", type = Xref.class),
        @XmlElement(name = "adminGrp", type = AdminGrp.class),
        @XmlElement(name = "descripGrp", type = DescripGrp.class),
        @XmlElement(name = "note", type = Note.class),
        @XmlElement(name = "transacGrp", type = TransacGrp.class),
        @XmlElement(name = "admin", type = Admin.class)
    })
    protected List<Object> descripOrDescripGrpOrAdmin;
    protected List<LangSet> langSet;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the descripOrDescripGrpOrAdmin property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the descripOrDescripGrpOrAdmin property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescripOrDescripGrpOrAdmin().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Descrip }
     * {@link Ref }
     * {@link Xref }
     * {@link AdminGrp }
     * {@link DescripGrp }
     * {@link Note }
     * {@link TransacGrp }
     * {@link Admin }
     * 
     * 
     */
    public List<Object> getDescripOrDescripGrpOrAdmin() {
        if (descripOrDescripGrpOrAdmin == null) {
            descripOrDescripGrpOrAdmin = new ArrayList<>();
        }
        return this.descripOrDescripGrpOrAdmin;
    }

    /**
     * Gets the value of the langSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the langSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLangSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LangSet }
     * 
     * 
     */
    public List<LangSet> getLangSet() {
        if (langSet == null) {
            langSet = new ArrayList<>();
        }
        return this.langSet;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
