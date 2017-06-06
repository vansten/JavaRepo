package com.budget.forms;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by krzys on 6/6/2017.
 */
public class EntryForm
{
    @NotNull
    @Size(min = 2, max = 80)
    private String name;

    @NotNull
    @Size(min = 2, max = 20)
    private String sourceName;

    @NotNull
    @DecimalMin("0.0")
    private Double value;

    public String getName() { return name; }

    public String getSourceName() { return sourceName; }

    public Double getValue() { return value; }

    public void setName(String name) { this.name = name; }

    public void setSourceName(String sourceName) { this.sourceName = sourceName; }

    public void setValue(Double value) { this.value = value; }

    @Override
    public String toString() {
        return "Entry(Name: " + this.name + ", Category: " + this.sourceName + ", Value: " + this.value + ")";
    }
}
