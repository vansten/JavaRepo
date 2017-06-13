package com.budget.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Created by Maverick on 2017-06-13.
 */
public class FilterForm {
    @NotNull
    private Float filterValueMin;
    @NotNull
    private Float filterValueMax;
    @NotNull
    @Size(min = 10)
    private String filterDateMin;
    @NotNull
    @Size(min = 10)
    private String filterDateMax;

    public Float getFilterValueMin() {
        return filterValueMin;
    }

    public void setFilterValueMin(Float filterValueMin) {
        this.filterValueMin = filterValueMin;
    }

    public Float getFilterValueMax() {
        return filterValueMax;
    }

    public void setfilterValueMax(Float filterValueMax) {
        this.filterValueMax = filterValueMax;
    }

    public String getFilterDateMin() {
        return filterDateMin;
    }

    public LocalDate getFilterDateMinAsLocalDate() {
        return parseLocalDate(filterDateMin);
    }

    public void setFilterDateMin(String filterDateMin) {
        this.filterDateMin = filterDateMin;
    }

    public String getFilterDateMax() {
        return filterDateMax;
    }

    public LocalDate getFilterDateMaxAsLocalDate() {
        return parseLocalDate(filterDateMax);
    }

    public void setFilterDateMax(String filterDateMax) {
        this.filterDateMax = filterDateMax;
    }

    private LocalDate parseLocalDate(String dateString) {
        return LocalDate.parse(dateString);
    }
}
