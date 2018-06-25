
package me.yevgeny.q2jwxmigrator.model.jirafield;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "custom",
        "orderable",
        "navigable",
        "searchable",
        "clauseNames",
        "schema"
})
public class JiraField {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("custom")
    private Boolean custom;
    @JsonProperty("orderable")
    private Boolean orderable;
    @JsonProperty("navigable")
    private Boolean navigable;
    @JsonProperty("searchable")
    private Boolean searchable;
    @JsonProperty("clauseNames")
    private List<String> clauseNames = null;
    @JsonProperty("schema")
    private Schema schema;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("custom")
    public Boolean getCustom() {
        return custom;
    }

    @JsonProperty("custom")
    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    @JsonProperty("orderable")
    public Boolean getOrderable() {
        return orderable;
    }

    @JsonProperty("orderable")
    public void setOrderable(Boolean orderable) {
        this.orderable = orderable;
    }

    @JsonProperty("navigable")
    public Boolean getNavigable() {
        return navigable;
    }

    @JsonProperty("navigable")
    public void setNavigable(Boolean navigable) {
        this.navigable = navigable;
    }

    @JsonProperty("searchable")
    public Boolean getSearchable() {
        return searchable;
    }

    @JsonProperty("searchable")
    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    @JsonProperty("clauseNames")
    public List<String> getClauseNames() {
        return clauseNames;
    }

    @JsonProperty("clauseNames")
    public void setClauseNames(List<String> clauseNames) {
        this.clauseNames = clauseNames;
    }

    @JsonProperty("schema")
    public Schema getSchema() {
        return schema;
    }

    @JsonProperty("schema")
    public void setSchema(Schema schema) {
        this.schema = schema;
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