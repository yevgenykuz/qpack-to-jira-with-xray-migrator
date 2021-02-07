package com.yevgenyk.q2jwxmigrator.model.qpackobject;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Iterator;
import java.util.List;

@XmlRootElement(name = "object")
public class QpackObject {
    private int id;
    private String action;
    private String ALManalyticsBIurl;
    private List<QpackObjectField> fields;

    @XmlAttribute(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlAttribute(name = "action")
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @XmlAttribute(name = "ALManalyticsBIurl")
    public String getALManalyticsBIurl() {
        return ALManalyticsBIurl;
    }

    public void setALManalyticsBIurl(String aLManalyticsBIurl) {
        ALManalyticsBIurl = aLManalyticsBIurl;
    }

    @XmlElement(name = "Field")
    public List<QpackObjectField> getFields() {
        return fields;
    }

    public void setFields(List<QpackObjectField> fields) {
        this.fields = fields;
    }

    public String getFieldValue(String fieldName) {
        for (QpackObjectField field : fields) {
            if (field.getName().equals(fieldName)) {
                return field.getValue();
            }
        }

        return null;
    }

    public void addFieldToObjectFields(String fieldName, String fieldValue) {
        QpackObjectField qpackObjectPathField = new QpackObjectField();
        qpackObjectPathField.setName(fieldName);
        qpackObjectPathField.setValue(fieldValue);
        List<QpackObjectField> updatedObjectFields = this.getFields();
        updatedObjectFields.add(qpackObjectPathField);
        this.setFields(updatedObjectFields);
    }

    public void removeFieldFromObjectFields(String fieldName) {
        List<QpackObjectField> updatedObjectFields = this.getFields();
        for (Iterator<QpackObjectField> iterator = updatedObjectFields.iterator(); iterator.hasNext(); ) {
            QpackObjectField value = iterator.next();
            if (value.getName().equals(fieldName)) {
                iterator.remove();
            }
        }

        this.setFields(updatedObjectFields);
    }

    public void renameField(String oldName, String newName) {
        String fieldValue = this.getFieldValue(oldName);
        if (null != fieldValue) {
            this.removeFieldFromObjectFields(oldName);
            this.addFieldToObjectFields(newName, fieldValue);
        }
    }
}
