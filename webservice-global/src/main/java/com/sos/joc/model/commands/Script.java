//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{}include"/>
 *       &lt;/choice>
 *       &lt;attribute name="language" type="{}Name" />
 *       &lt;attribute name="use_engine">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="task"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="com_class" type="{}String" />
 *       &lt;attribute name="filename" type="{}File" />
 *       &lt;attribute name="java_class" type="{}String" />
 *       &lt;attribute name="recompile" type="{}Yes_no" />
 *       &lt;attribute name="encoding" type="{}Code_page_encoding" />
 *       &lt;attribute name="java_class_path" type="{}String" />
 *       &lt;attribute name="dll" type="{}String" />
 *       &lt;attribute name="dotnet_class" type="{}String" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "script")
public class Script {

    @XmlElementRef(name = "include", type = Include.class, required = false)
    @XmlMixed
    protected List<Object> content;
    @XmlAttribute(name = "language")
    protected String language;
    @XmlAttribute(name = "use_engine")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String useEngine;
    @XmlAttribute(name = "com_class")
    protected String comClass;
    @XmlAttribute(name = "filename")
    protected String filename;
    @XmlAttribute(name = "java_class")
    protected String javaClass;
    @XmlAttribute(name = "recompile")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String recompile;
    @XmlAttribute(name = "encoding")
    protected CodePageEncoding encoding;
    @XmlAttribute(name = "java_class_path")
    protected String javaClassPath;
    @XmlAttribute(name = "dll")
    protected String dll;
    @XmlAttribute(name = "dotnet_class")
    protected String dotnetClass;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * {@link Include }
     * 
     * 
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

    /**
     * Ruft den Wert der language-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Legt den Wert der language-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Ruft den Wert der useEngine-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseEngine() {
        return useEngine;
    }

    /**
     * Legt den Wert der useEngine-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseEngine(String value) {
        this.useEngine = value;
    }

    /**
     * Ruft den Wert der comClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComClass() {
        return comClass;
    }

    /**
     * Legt den Wert der comClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComClass(String value) {
        this.comClass = value;
    }

    /**
     * Ruft den Wert der filename-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Legt den Wert der filename-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilename(String value) {
        this.filename = value;
    }

    /**
     * Ruft den Wert der javaClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaClass() {
        return javaClass;
    }

    /**
     * Legt den Wert der javaClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaClass(String value) {
        this.javaClass = value;
    }

    /**
     * Ruft den Wert der recompile-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecompile() {
        return recompile;
    }

    /**
     * Legt den Wert der recompile-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecompile(String value) {
        this.recompile = value;
    }

    /**
     * Ruft den Wert der encoding-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CodePageEncoding }
     *     
     */
    public CodePageEncoding getEncoding() {
        return encoding;
    }

    /**
     * Legt den Wert der encoding-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CodePageEncoding }
     *     
     */
    public void setEncoding(CodePageEncoding value) {
        this.encoding = value;
    }

    /**
     * Ruft den Wert der javaClassPath-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJavaClassPath() {
        return javaClassPath;
    }

    /**
     * Legt den Wert der javaClassPath-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJavaClassPath(String value) {
        this.javaClassPath = value;
    }

    /**
     * Ruft den Wert der dll-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDll() {
        return dll;
    }

    /**
     * Legt den Wert der dll-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDll(String value) {
        this.dll = value;
    }

    /**
     * Ruft den Wert der dotnetClass-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDotnetClass() {
        return dotnetClass;
    }

    /**
     * Legt den Wert der dotnetClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDotnetClass(String value) {
        this.dotnetClass = value;
    }

}
