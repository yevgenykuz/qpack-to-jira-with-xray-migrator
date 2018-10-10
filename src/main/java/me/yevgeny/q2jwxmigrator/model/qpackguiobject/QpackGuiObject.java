
package me.yevgeny.q2jwxmigrator.model.qpackguiobject;

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
 *         &lt;element name="section" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="path">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="item" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                                     &lt;attribute name="obj_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="obj_version" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                                     &lt;attribute name="obj_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="work_item_section">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="item">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                                     &lt;attribute name="section_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                                     &lt;attribute name="section_order" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="subscribers" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="discussion">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="data_ind" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="field" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="tooltip" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="section" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="web_order" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                           &lt;attribute name="title" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="show_path" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="is_star" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="gantt_id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="customPrefix" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="freeze" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="head" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="deleted" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="project_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="allow_branch" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="original_id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="is_lock" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="force_checkout" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="status_is_freeze" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="is_change_version" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="type_description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "section"
})
@XmlRootElement(name = "object")
public class QpackGuiObject {

    @XmlElement(required = true)
    protected List<Section> section;
    @XmlAttribute(name = "id")
    protected Integer id;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "show_path")
    protected Integer showPath;
    @XmlAttribute(name = "is_star")
    protected Integer isStar;
    @XmlAttribute(name = "gantt_id")
    protected String ganttId;
    @XmlAttribute(name = "customPrefix")
    protected String customPrefix;
    @XmlAttribute(name = "freeze")
    protected Integer freeze;
    @XmlAttribute(name = "head")
    protected Integer head;
    @XmlAttribute(name = "deleted")
    protected Integer deleted;
    @XmlAttribute(name = "project_id")
    protected Integer projectId;
    @XmlAttribute(name = "allow_branch")
    protected Integer allowBranch;
    @XmlAttribute(name = "original_id")
    protected Integer originalId;
    @XmlAttribute(name = "is_lock")
    protected Integer isLock;
    @XmlAttribute(name = "force_checkout")
    protected Integer forceCheckout;
    @XmlAttribute(name = "status_is_freeze")
    protected Integer statusIsFreeze;
    @XmlAttribute(name = "is_change_version")
    protected Integer isChangeVersion;
    @XmlAttribute(name = "type_description")
    protected String typeDescription;

    /**
     * Gets the value of the section property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the section property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSection().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QpackGuiObject.Section }
     *
     *
     */
    public List<Section> getSection() {
        if (section == null) {
            section = new ArrayList<Section>();
        }
        return this.section;
    }

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the showPath property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getShowPath() {
        return showPath;
    }

    /**
     * Sets the value of the showPath property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setShowPath(Integer value) {
        this.showPath = value;
    }

    /**
     * Gets the value of the isStar property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getIsStar() {
        return isStar;
    }

    /**
     * Sets the value of the isStar property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setIsStar(Integer value) {
        this.isStar = value;
    }

    /**
     * Gets the value of the ganttId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getGanttId() {
        return ganttId;
    }

    /**
     * Sets the value of the ganttId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setGanttId(String value) {
        this.ganttId = value;
    }

    /**
     * Gets the value of the customPrefix property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCustomPrefix() {
        return customPrefix;
    }

    /**
     * Sets the value of the customPrefix property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCustomPrefix(String value) {
        this.customPrefix = value;
    }

    /**
     * Gets the value of the freeze property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getFreeze() {
        return freeze;
    }

    /**
     * Sets the value of the freeze property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setFreeze(Integer value) {
        this.freeze = value;
    }

    /**
     * Gets the value of the head property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getHead() {
        return head;
    }

    /**
     * Sets the value of the head property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setHead(Integer value) {
        this.head = value;
    }

    /**
     * Gets the value of the deleted property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getDeleted() {
        return deleted;
    }

    /**
     * Sets the value of the deleted property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setDeleted(Integer value) {
        this.deleted = value;
    }

    /**
     * Gets the value of the projectId property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * Sets the value of the projectId property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setProjectId(Integer value) {
        this.projectId = value;
    }

    /**
     * Gets the value of the allowBranch property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getAllowBranch() {
        return allowBranch;
    }

    /**
     * Sets the value of the allowBranch property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setAllowBranch(Integer value) {
        this.allowBranch = value;
    }

    /**
     * Gets the value of the originalId property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getOriginalId() {
        return originalId;
    }

    /**
     * Sets the value of the originalId property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setOriginalId(Integer value) {
        this.originalId = value;
    }

    /**
     * Gets the value of the isLock property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getIsLock() {
        return isLock;
    }

    /**
     * Sets the value of the isLock property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setIsLock(Integer value) {
        this.isLock = value;
    }

    /**
     * Gets the value of the forceCheckout property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getForceCheckout() {
        return forceCheckout;
    }

    /**
     * Sets the value of the forceCheckout property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setForceCheckout(Integer value) {
        this.forceCheckout = value;
    }

    /**
     * Gets the value of the statusIsFreeze property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getStatusIsFreeze() {
        return statusIsFreeze;
    }

    /**
     * Sets the value of the statusIsFreeze property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setStatusIsFreeze(Integer value) {
        this.statusIsFreeze = value;
    }

    /**
     * Gets the value of the isChangeVersion property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getIsChangeVersion() {
        return isChangeVersion;
    }

    /**
     * Sets the value of the isChangeVersion property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setIsChangeVersion(Integer value) {
        this.isChangeVersion = value;
    }

    /**
     * Gets the value of the typeDescription property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTypeDescription() {
        return typeDescription;
    }

    /**
     * Sets the value of the typeDescription property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTypeDescription(String value) {
        this.typeDescription = value;
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
     *         &lt;element name="path">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="item" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                           &lt;attribute name="obj_name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="obj_version" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                           &lt;attribute name="obj_type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="work_item_section">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="item">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                           &lt;attribute name="section_name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                           &lt;attribute name="section_order" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="subscribers" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="discussion">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="data_ind" type="{http://www.w3.org/2001/XMLSchema}int" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="field" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="tooltip" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="section" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="web_order" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                 &lt;attribute name="title" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "path",
        "workItemSection",
        "subscribers",
        "discussion",
        "field"
    })
    public static class Section {

        @XmlElement(required = true)
        protected QpackGuiObject.Section.Path path;
        @XmlElement(name = "work_item_section", required = true)
        protected QpackGuiObject.Section.WorkItemSection workItemSection;
        @XmlElement(required = true)
        protected String subscribers;
        @XmlElement(required = true)
        protected QpackGuiObject.Section.Discussion discussion;
        @XmlElement(required = true)
        protected List<Field> field;
        @XmlAttribute(name = "name")
        protected String name;
        @XmlAttribute(name = "type")
        protected String type;

        /**
         * Gets the value of the path property.
         *
         * @return
         *     possible object is
         *     {@link QpackGuiObject.Section.Path }
         *
         */
        public QpackGuiObject.Section.Path getPath() {
            return path;
        }

        /**
         * Sets the value of the path property.
         *
         * @param value
         *     allowed object is
         *     {@link QpackGuiObject.Section.Path }
         *
         */
        public void setPath(QpackGuiObject.Section.Path value) {
            this.path = value;
        }

        /**
         * Gets the value of the workItemSection property.
         *
         * @return
         *     possible object is
         *     {@link QpackGuiObject.Section.WorkItemSection }
         *
         */
        public QpackGuiObject.Section.WorkItemSection getWorkItemSection() {
            return workItemSection;
        }

        /**
         * Sets the value of the workItemSection property.
         *
         * @param value
         *     allowed object is
         *     {@link QpackGuiObject.Section.WorkItemSection }
         *
         */
        public void setWorkItemSection(QpackGuiObject.Section.WorkItemSection value) {
            this.workItemSection = value;
        }

        /**
         * Gets the value of the subscribers property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getSubscribers() {
            return subscribers;
        }

        /**
         * Sets the value of the subscribers property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setSubscribers(String value) {
            this.subscribers = value;
        }

        /**
         * Gets the value of the discussion property.
         *
         * @return
         *     possible object is
         *     {@link QpackGuiObject.Section.Discussion }
         *
         */
        public QpackGuiObject.Section.Discussion getDiscussion() {
            return discussion;
        }

        /**
         * Sets the value of the discussion property.
         *
         * @param value
         *     allowed object is
         *     {@link QpackGuiObject.Section.Discussion }
         *
         */
        public void setDiscussion(QpackGuiObject.Section.Discussion value) {
            this.discussion = value;
        }

        /**
         * Gets the value of the field property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the field property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getField().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link QpackGuiObject.Section.Field }
         *
         *
         */
        public List<Field> getField() {
            if (field == null) {
                field = new ArrayList<Field>();
            }
            return this.field;
        }

        /**
         * Gets the value of the name property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the type property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setType(String value) {
            this.type = value;
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
         *       &lt;attribute name="data_ind" type="{http://www.w3.org/2001/XMLSchema}int" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Discussion {

            @XmlAttribute(name = "data_ind")
            protected Integer dataInd;

            /**
             * Gets the value of the dataInd property.
             *
             * @return
             *     possible object is
             *     {@link Integer }
             *
             */
            public Integer getDataInd() {
                return dataInd;
            }

            /**
             * Sets the value of the dataInd property.
             *
             * @param value
             *     allowed object is
             *     {@link Integer }
             *
             */
            public void setDataInd(Integer value) {
                this.dataInd = value;
            }

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
         *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="tooltip" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="section" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="web_order" type="{http://www.w3.org/2001/XMLSchema}int" />
         *       &lt;attribute name="title" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Field {

            @XmlAttribute(name = "type")
            protected String type;
            @XmlAttribute(name = "name")
            protected String name;
            @XmlAttribute(name = "tooltip")
            protected String tooltip;
            @XmlAttribute(name = "section")
            protected String section;
            @XmlAttribute(name = "web_order")
            protected Integer webOrder;
            @XmlAttribute(name = "title")
            protected String title;

            /**
             * Gets the value of the type property.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Gets the value of the name property.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the tooltip property.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getTooltip() {
                return tooltip;
            }

            /**
             * Sets the value of the tooltip property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setTooltip(String value) {
                this.tooltip = value;
            }

            /**
             * Gets the value of the section property.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getSection() {
                return section;
            }

            /**
             * Sets the value of the section property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setSection(String value) {
                this.section = value;
            }

            /**
             * Gets the value of the webOrder property.
             *
             * @return
             *     possible object is
             *     {@link Integer }
             *
             */
            public Integer getWebOrder() {
                return webOrder;
            }

            /**
             * Sets the value of the webOrder property.
             *
             * @param value
             *     allowed object is
             *     {@link Integer }
             *
             */
            public void setWebOrder(Integer value) {
                this.webOrder = value;
            }

            /**
             * Gets the value of the title property.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getTitle() {
                return title;
            }

            /**
             * Sets the value of the title property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setTitle(String value) {
                this.title = value;
            }

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
         *         &lt;element name="item" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}int" />
         *                 &lt;attribute name="obj_name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="obj_version" type="{http://www.w3.org/2001/XMLSchema}int" />
         *                 &lt;attribute name="obj_type" type="{http://www.w3.org/2001/XMLSchema}string" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "item"
        })
        public static class Path {

            @XmlElement(required = true)
            protected List<Item> item;

            /**
             * Gets the value of the item property.
             *
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the item property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getItem().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link QpackGuiObject.Section.Path.Item }
             *
             *
             */
            public List<Item> getItem() {
                if (item == null) {
                    item = new ArrayList<Item>();
                }
                return this.item;
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
             *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}int" />
             *       &lt;attribute name="obj_name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="obj_version" type="{http://www.w3.org/2001/XMLSchema}int" />
             *       &lt;attribute name="obj_type" type="{http://www.w3.org/2001/XMLSchema}string" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             *
             *
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Item {

                @XmlAttribute(name = "ID")
                protected Integer id;
                @XmlAttribute(name = "obj_name")
                protected String objName;
                @XmlAttribute(name = "obj_version")
                protected Integer objVersion;
                @XmlAttribute(name = "obj_type")
                protected String objType;

                /**
                 * Gets the value of the id property.
                 *
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *
                 */
                public Integer getID() {
                    return id;
                }

                /**
                 * Sets the value of the id property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *
                 */
                public void setID(Integer value) {
                    this.id = value;
                }

                /**
                 * Gets the value of the objName property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getObjName() {
                    return objName;
                }

                /**
                 * Sets the value of the objName property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setObjName(String value) {
                    this.objName = value;
                }

                /**
                 * Gets the value of the objVersion property.
                 *
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *
                 */
                public Integer getObjVersion() {
                    return objVersion;
                }

                /**
                 * Sets the value of the objVersion property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *
                 */
                public void setObjVersion(Integer value) {
                    this.objVersion = value;
                }

                /**
                 * Gets the value of the objType property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getObjType() {
                    return objType;
                }

                /**
                 * Sets the value of the objType property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setObjType(String value) {
                    this.objType = value;
                }

            }

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
         *         &lt;element name="item">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}int" />
         *                 &lt;attribute name="section_name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *                 &lt;attribute name="section_order" type="{http://www.w3.org/2001/XMLSchema}int" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            "item"
        })
        public static class WorkItemSection {

            @XmlElement(required = true)
            protected QpackGuiObject.Section.WorkItemSection.Item item;

            /**
             * Gets the value of the item property.
             *
             * @return
             *     possible object is
             *     {@link QpackGuiObject.Section.WorkItemSection.Item }
             *
             */
            public QpackGuiObject.Section.WorkItemSection.Item getItem() {
                return item;
            }

            /**
             * Sets the value of the item property.
             *
             * @param value
             *     allowed object is
             *     {@link QpackGuiObject.Section.WorkItemSection.Item }
             *
             */
            public void setItem(QpackGuiObject.Section.WorkItemSection.Item value) {
                this.item = value;
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
             *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}int" />
             *       &lt;attribute name="section_name" type="{http://www.w3.org/2001/XMLSchema}string" />
             *       &lt;attribute name="section_order" type="{http://www.w3.org/2001/XMLSchema}int" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Item {

                @XmlAttribute(name = "ID")
                protected Integer id;
                @XmlAttribute(name = "section_name")
                protected String sectionName;
                @XmlAttribute(name = "section_order")
                protected Integer sectionOrder;

                /**
                 * Gets the value of the id property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *     
                 */
                public Integer getID() {
                    return id;
                }

                /**
                 * Sets the value of the id property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *     
                 */
                public void setID(Integer value) {
                    this.id = value;
                }

                /**
                 * Gets the value of the sectionName property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSectionName() {
                    return sectionName;
                }

                /**
                 * Sets the value of the sectionName property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSectionName(String value) {
                    this.sectionName = value;
                }

                /**
                 * Gets the value of the sectionOrder property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Integer }
                 *     
                 */
                public Integer getSectionOrder() {
                    return sectionOrder;
                }

                /**
                 * Sets the value of the sectionOrder property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Integer }
                 *     
                 */
                public void setSectionOrder(Integer value) {
                    this.sectionOrder = value;
                }

            }

        }

    }

}
