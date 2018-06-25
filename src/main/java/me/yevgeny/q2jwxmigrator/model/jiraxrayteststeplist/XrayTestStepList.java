package me.yevgeny.q2jwxmigrator.model.jiraxrayteststeplist;

import me.yevgeny.q2jwxmigrator.model.qpackwebobject.QpackWebObject;
import net.sf.json.JSONArray;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class XrayTestStepList {

    private List<Step> steps = null;

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("steps", steps).toString();
    }

    public String toJson() {
        JSONArray jsonArray = JSONArray.fromObject(getSteps());
        return "{\"steps\":" + jsonArray + "}";
    }
}