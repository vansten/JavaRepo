package com.budget.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Maverick on 2017-06-13.
 */
public class FilterForm {

    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @NotNull
    private Float valueMin;
    @NotNull
    private Float valueMax;
    @NotNull
    @Size(min = 10)
    private String dateMin;
    @NotNull
    @Size(min = 10)
    private String dateMax;

    public Float getValueMin() {
        return valueMin;
    }

    public void setValueMin(Float valueMin) {
        this.valueMin = valueMin;
    }

    public Float getValueMax() {
        return valueMax;
    }

    public void setValueMax(Float valueMax) {
        this.valueMax = valueMax;
    }

    public String getDateMin() {
        return dateMin;
    }

    public LocalDate getDateMinAsLocalDate() {
        return LocalDate.parse(dateMin, DATE_FORMATTER);
    }

    public void setDateMin(String dateMin) {
        this.dateMin = dateMin;
    }

    public void setDateMinAsLocalDate(LocalDate ld) {
        this.dateMin = ld.format(DATE_FORMATTER);
    }

    public String getDateMax() {
        return dateMax;
    }

    public LocalDate getDateMaxAsLocalDate() {
        return LocalDate.parse(dateMax, DATE_FORMATTER);
    }

    public void setDateMax(String dateMax) {
        this.dateMax = dateMax;
    }

    public void setDateMaxAsLocalDate(LocalDate ld) {
        this.dateMax = ld.format(DATE_FORMATTER);
    }
}
