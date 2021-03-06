//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation,
// v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.18 at 06:18:59 PM IDT 
//


package com.yevgenyk.q2jwxmigrator.model.qpackwebobject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="steps">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="step" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="stepno" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="expectedresult" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="upper_limit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="lower_limit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="original_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="obj_order" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="obj_key" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="c_code" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parent_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="parent_add_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="isword" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="obj_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="obj_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="path" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="major_ver" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="minor_ver" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Release_ver" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Build_ver" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="version_display" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="locks" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Freese" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="parent_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Version_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Action_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="IsSolution" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Assigned_to" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Assigned_to_desc" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="obj_priority_desc" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="obj_status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="obj_status_desc" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "description",
        "steps"
})
@XmlRootElement(name = "object")
public class QpackWebObject {

    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected Steps steps;
    @XmlAttribute(name = "id")
    protected Integer id;
    @XmlAttribute(name = "original_id")
    protected Integer originalId;
    @XmlAttribute(name = "obj_order")
    protected Integer objOrder;
    @XmlAttribute(name = "obj_key")
    protected String objKey;
    @XmlAttribute(name = "c_code")
    protected String cCode;
    @XmlAttribute(name = "parent_id")
    protected Integer parentId;
    @XmlAttribute(name = "parent_add_id")
    protected Integer parentAddId;
    @XmlAttribute(name = "isword")
    protected Integer isword;
    @XmlAttribute(name = "obj_name")
    protected String objName;
    @XmlAttribute(name = "obj_type")
    protected String objType;
    @XmlAttribute(name = "path")
    protected String path;
    @XmlAttribute(name = "major_ver")
    protected Integer majorVer;
    @XmlAttribute(name = "minor_ver")
    protected Integer minorVer;
    @XmlAttribute(name = "Release_ver")
    protected Integer releaseVer;
    @XmlAttribute(name = "Build_ver")
    protected Integer buildVer;
    @XmlAttribute(name = "version_display")
    protected String versionDisplay;
    @XmlAttribute(name = "locks")
    protected Integer locks;
    @XmlAttribute(name = "Freese")
    protected Integer freese;
    @XmlAttribute(name = "parent_type")
    protected String parentType;
    @XmlAttribute(name = "Version_id")
    protected Integer versionId;
    @XmlAttribute(name = "Action_type")
    protected String actionType;
    @XmlAttribute(name = "IsSolution")
    protected Integer isSolution;
    @XmlAttribute(name = "Assigned_to")
    protected Integer assignedTo;
    @XmlAttribute(name = "Assigned_to_desc")
    protected String assignedToDesc;
    @XmlAttribute(name = "obj_priority_desc")
    protected String objPriorityDesc;
    @XmlAttribute(name = "obj_status")
    protected String objStatus;
    @XmlAttribute(name = "obj_status_desc")
    protected String objStatusDesc;

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the steps property.
     *
     * @return possible object is
     * {@link Steps }
     */
    public Steps getSteps() {
        return steps;
    }

    /**
     * Sets the value of the steps property.
     *
     * @param value allowed object is
     *              {@link Steps }
     */
    public void setSteps(Steps value) {
        this.steps = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the originalId property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getOriginalId() {
        return originalId;
    }

    /**
     * Sets the value of the originalId property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setOriginalId(Integer value) {
        this.originalId = value;
    }

    /**
     * Gets the value of the objOrder property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getObjOrder() {
        return objOrder;
    }

    /**
     * Sets the value of the objOrder property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setObjOrder(Integer value) {
        this.objOrder = value;
    }

    /**
     * Gets the value of the objKey property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getObjKey() {
        return objKey;
    }

    /**
     * Sets the value of the objKey property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setObjKey(String value) {
        this.objKey = value;
    }

    /**
     * Gets the value of the cCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCCode() {
        return cCode;
    }

    /**
     * Sets the value of the cCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCCode(String value) {
        this.cCode = value;
    }

    /**
     * Gets the value of the parentId property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * Sets the value of the parentId property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setParentId(Integer value) {
        this.parentId = value;
    }

    /**
     * Gets the value of the parentAddId property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getParentAddId() {
        return parentAddId;
    }

    /**
     * Sets the value of the parentAddId property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setParentAddId(Integer value) {
        this.parentAddId = value;
    }

    /**
     * Gets the value of the isword property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getIsword() {
        return isword;
    }

    /**
     * Sets the value of the isword property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setIsword(Integer value) {
        this.isword = value;
    }

    /**
     * Gets the value of the objName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getObjName() {
        return objName;
    }

    /**
     * Sets the value of the objName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setObjName(String value) {
        this.objName = value;
    }

    /**
     * Gets the value of the objType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getObjType() {
        return objType;
    }

    /**
     * Sets the value of the objType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setObjType(String value) {
        this.objType = value;
    }

    /**
     * Gets the value of the path property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Gets the value of the majorVer property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getMajorVer() {
        return majorVer;
    }

    /**
     * Sets the value of the majorVer property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setMajorVer(Integer value) {
        this.majorVer = value;
    }

    /**
     * Gets the value of the minorVer property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getMinorVer() {
        return minorVer;
    }

    /**
     * Sets the value of the minorVer property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setMinorVer(Integer value) {
        this.minorVer = value;
    }

    /**
     * Gets the value of the releaseVer property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getReleaseVer() {
        return releaseVer;
    }

    /**
     * Sets the value of the releaseVer property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setReleaseVer(Integer value) {
        this.releaseVer = value;
    }

    /**
     * Gets the value of the buildVer property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getBuildVer() {
        return buildVer;
    }

    /**
     * Sets the value of the buildVer property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setBuildVer(Integer value) {
        this.buildVer = value;
    }

    /**
     * Gets the value of the versionDisplay property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVersionDisplay() {
        return versionDisplay;
    }

    /**
     * Sets the value of the versionDisplay property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersionDisplay(String value) {
        this.versionDisplay = value;
    }

    /**
     * Gets the value of the locks property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getLocks() {
        return locks;
    }

    /**
     * Sets the value of the locks property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setLocks(Integer value) {
        this.locks = value;
    }

    /**
     * Gets the value of the freese property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getFreese() {
        return freese;
    }

    /**
     * Sets the value of the freese property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setFreese(Integer value) {
        this.freese = value;
    }

    /**
     * Gets the value of the parentType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getParentType() {
        return parentType;
    }

    /**
     * Sets the value of the parentType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setParentType(String value) {
        this.parentType = value;
    }

    /**
     * Gets the value of the versionId property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getVersionId() {
        return versionId;
    }

    /**
     * Sets the value of the versionId property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setVersionId(Integer value) {
        this.versionId = value;
    }

    /**
     * Gets the value of the actionType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * Sets the value of the actionType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setActionType(String value) {
        this.actionType = value;
    }

    /**
     * Gets the value of the isSolution property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getIsSolution() {
        return isSolution;
    }

    /**
     * Sets the value of the isSolution property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setIsSolution(Integer value) {
        this.isSolution = value;
    }

    /**
     * Gets the value of the assignedTo property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getAssignedTo() {
        return assignedTo;
    }

    /**
     * Sets the value of the assignedTo property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setAssignedTo(Integer value) {
        this.assignedTo = value;
    }

    /**
     * Gets the value of the assignedToDesc property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAssignedToDesc() {
        return assignedToDesc;
    }

    /**
     * Sets the value of the assignedToDesc property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAssignedToDesc(String value) {
        this.assignedToDesc = value;
    }

    /**
     * Gets the value of the objPriorityDesc property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getObjPriorityDesc() {
        return objPriorityDesc;
    }

    /**
     * Sets the value of the objPriorityDesc property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setObjPriorityDesc(String value) {
        this.objPriorityDesc = value;
    }

    /**
     * Gets the value of the objStatus property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getObjStatus() {
        return objStatus;
    }

    /**
     * Sets the value of the objStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setObjStatus(String value) {
        this.objStatus = value;
    }

    /**
     * Gets the value of the objStatusDesc property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getObjStatusDesc() {
        return objStatusDesc;
    }

    /**
     * Sets the value of the objStatusDesc property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setObjStatusDesc(String value) {
        this.objStatusDesc = value;
    }


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
     *         &lt;element name="step" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="stepno" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="expectedresult" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="upper_limit" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="lower_limit" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "step"
    })
    public static class Steps {

        @XmlElement(required = true)
        protected List<Step> step;

        /**
         * Gets the value of the step property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the step property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getStep().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Step }
         */
        public List<Step> getStep() {
            if (step == null) {
                step = new ArrayList<Step>();
            }
            return this.step;
        }


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
         *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="stepno" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="expectedresult" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="upper_limit" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="lower_limit" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "id",
                "stepno",
                "description",
                "expectedresult",
                "upperLimit",
                "lowerLimit"
        })
        public static class Step {

            @XmlElement(name = "ID")
            protected int id;
            protected int stepno;
            @XmlElement(required = true)
            protected String description;
            @XmlElement(required = true)
            protected String expectedresult;
            @XmlElement(name = "upper_limit", required = true)
            protected String upperLimit;
            @XmlElement(name = "lower_limit", required = true)
            protected String lowerLimit;

            /**
             * Gets the value of the id property.
             */
            public int getID() {
                return id;
            }

            /**
             * Sets the value of the id property.
             */
            public void setID(int value) {
                this.id = value;
            }

            /**
             * Gets the value of the stepno property.
             */
            public int getStepno() {
                return stepno;
            }

            /**
             * Sets the value of the stepno property.
             */
            public void setStepno(int value) {
                this.stepno = value;
            }

            /**
             * Gets the value of the description property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getDescription() {
                return description;
            }

            /**
             * Sets the value of the description property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setDescription(String value) {
                this.description = value;
            }

            /**
             * Gets the value of the expectedresult property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getExpectedresult() {
                return expectedresult;
            }

            /**
             * Sets the value of the expectedresult property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setExpectedresult(String value) {
                this.expectedresult = value;
            }

            /**
             * Gets the value of the upperLimit property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getUpperLimit() {
                return upperLimit;
            }

            /**
             * Sets the value of the upperLimit property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setUpperLimit(String value) {
                this.upperLimit = value;
            }

            /**
             * Gets the value of the lowerLimit property.
             *
             * @return possible object is
             * {@link String }
             */
            public String getLowerLimit() {
                return lowerLimit;
            }

            /**
             * Sets the value of the lowerLimit property.
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setLowerLimit(String value) {
                this.lowerLimit = value;
            }

        }

    }

}
