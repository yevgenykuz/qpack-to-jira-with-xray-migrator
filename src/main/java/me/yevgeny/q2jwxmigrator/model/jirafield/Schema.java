package me.yevgeny.q2jwxmigrator.model.jirafield;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "custom",
        "customId",
        "system",
        "items"
})
public class Schema {

    @JsonProperty("type")
    private String type;
    @JsonProperty("custom")
    private String custom;
    @JsonProperty("customId")
    private Integer customId;
    @JsonProperty("system")
    private String system;
    @JsonProperty("items")
    private String items;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("custom")
    public String getCustom() {
        return custom;
    }

    @JsonProperty("custom")
    public void setCustom(String custom) {
        this.custom = custom;
    }

    @JsonProperty("customId")
    public Integer getCustomId() {
        return customId;
    }

    @JsonProperty("customId")
    public void setCustomId(Integer customId) {
        this.customId = customId;
    }

    @JsonProperty("system")
    public String getSystem() {
        return system;
    }

    @JsonProperty("system")
    public void setSystem(String system) {
        this.system = system;
    }

    @JsonProperty("items")
    public String getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(String items) {
        this.items = items;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}