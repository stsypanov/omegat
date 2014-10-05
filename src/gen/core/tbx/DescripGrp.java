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
 *         &lt;element ref="{}descrip"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}descripNote"/>
 *           &lt;element ref="{}admin"/>
 *           &lt;element ref="{}adminGrp"/>
 *           &lt;element ref="{}transacGrp"/>
 *           &lt;element ref="{}note"/>
 *           &lt;element ref="{}ref"/>
 *           &lt;element ref="{}xref"/>
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
    "descrip",
    "descripNoteOrAdminOrAdminGrp"
})
@XmlRootElement(name = "descripGrp")
public class DescripGrp {

    @XmlElement(required = true)
    protected Descrip descrip;
    @XmlElements({
        @XmlElement(name = "admin", type = Admin.class),
        @XmlElement(name = "adminGrp", type = AdminGrp.class),
        @XmlElement(name = "transacGrp", type = TransacGrp.class),
        @XmlElement(name = "ref", type = Ref.class),
        @XmlElement(name = "xref", type = Xref.class),
        @XmlElement(name = "note", type = Note.class),
        @XmlElement(name = "descripNote", type = DescripNote.class)
    })
    protected List<Object> descripNoteOrAdminOrAdminGrp;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the descrip property.
     * 
     * @return
     *     possible object is
     *     {@link Descrip }
     *     
     */
    public Descrip getDescrip() {
        return descrip;
    }

    /**
     * Sets the value of the descrip property.
     * 
     * @param value
     *     allowed object is
     *     {@link Descrip }
     *     
     */
    public void setDescrip(Descrip value) {
        this.descrip = value;
    }

    /**
     * Gets the value of the descripNoteOrAdminOrAdminGrp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the descripNoteOrAdminOrAdminGrp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescripNoteOrAdminOrAdminGrp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Admin }
     * {@link AdminGrp }
     * {@link TransacGrp }
     * {@link Ref }
     * {@link Xref }
     * {@link Note }
     * {@link DescripNote }
     * 
     * 
     */
    public List<Object> getDescripNoteOrAdminOrAdminGrp() {
        if (descripNoteOrAdminOrAdminGrp == null) {
            descripNoteOrAdminOrAdminGrp = new ArrayList<>();
        }
        return this.descripNoteOrAdminOrAdminGrp;
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
