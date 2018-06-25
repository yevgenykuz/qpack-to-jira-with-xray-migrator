package me.yevgeny.q2jwxmigrator.model.jiraxrayteststeplist;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Step {

    private Integer index;
    private String step;
    private String result;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("index", index).append("step", step).append("result", result)
                .toString();
    }
}