package com.budget.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Created by Maverick on 2017-06-13.
 */
public class FilterForm {
    @NotNull
    private Float minValue;
    @NotNull
    private Float maxValue;
    @NotNull
    @Size(min = 10)
    private String minDate;
    @NotNull
    @Size(min = 10)
    private String maxDate;

    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(Float minValue) {
        this.minValue = minValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Float maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinDate() {
        return minDate;
    }

    public LocalDate getMinDateAsLocalDate() {
        return parseLocalDate(minDate);
    }

    public void setMinDate(String minDate) {
        this.minDate = minDate;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public LocalDate getMaxDateAsLocalDate() {
        return parseLocalDate(maxDate);
    }

    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    private LocalDate parseLocalDate(String dateString) {
        return LocalDate.parse(dateString);
    }
}
