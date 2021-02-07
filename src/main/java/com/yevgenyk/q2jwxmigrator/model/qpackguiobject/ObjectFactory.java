
package com.yevgenyk.q2jwxmigrator.model.qpackguiobject;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.yevgenyk.q2jwxmigrator.model.qpackGuiObject package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.yevgenyk.q2jwxmigrator.model.qpackGuiObject
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QpackGuiObject }
     * 
     */
    public QpackGuiObject createObject() {
        return new QpackGuiObject();
    }

    /**
     * Create an instance of {@link QpackGuiObject.Section }
     * 
     */
    public QpackGuiObject.Section createObjectSection() {
        return new QpackGuiObject.Section();
    }

    /**
     * Create an instance of {@link QpackGuiObject.Section.WorkItemSection }
     * 
     */
    public QpackGuiObject.Section.WorkItemSection createObjectSectionWorkItemSection() {
        return new QpackGuiObject.Section.WorkItemSection();
    }

    /**
     * Create an instance of {@link QpackGuiObject.Section.Path }
     * 
     */
    public QpackGuiObject.Section.Path createObjectSectionPath() {
        return new QpackGuiObject.Section.Path();
    }

    /**
     * Create an instance of {@link QpackGuiObject.Section.Discussion }
     * 
     */
    public QpackGuiObject.Section.Discussion createObjectSectionDiscussion() {
        return new QpackGuiObject.Section.Discussion();
    }

    /**
     * Create an instance of {@link QpackGuiObject.Section.Field }
     * 
     */
    public QpackGuiObject.Section.Field createObjectSectionField() {
        return new QpackGuiObject.Section.Field();
    }

    /**
     * Create an instance of {@link QpackGuiObject.Section.WorkItemSection.Item }
     * 
     */
    public QpackGuiObject.Section.WorkItemSection.Item createObjectSectionWorkItemSectionItem() {
        return new QpackGuiObject.Section.WorkItemSection.Item();
    }

    /**
     * Create an instance of {@link QpackGuiObject.Section.Path.Item }
     * 
     */
    public QpackGuiObject.Section.Path.Item createObjectSectionPathItem() {
        return new QpackGuiObject.Section.Path.Item();
    }

}
