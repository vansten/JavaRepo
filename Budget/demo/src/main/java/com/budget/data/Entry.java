package com.budget.data;

import java.time.Instant;
import java.util.GregorianCalendar;

/**
 * Created by Maverick on 27.03.2017.
 */
public class Entry {

    protected Integer id;
    protected Float value;
    protected Integer userID;

    protected Instant timestamp;
    protected String name;
    protected String sourceName;

    protected Entry(Integer id, Float value, Integer userID,
                 Instant timestamp, String name, String sourceName)
    {
        this.id = id;
        this.value = value;
        this.userID = userID;
        this.timestamp = timestamp;
        this.name = name;
        this.sourceName = sourceName;
    }

    public Integer getID() {
        return id;
    }

    public Float getValue() {
        return value;
    }

    // This function is intended to return value for Earnings and -value for Expenses.
    public Float getValueAbsolute() {
        return value;
    }

    public Integer getUserID() {
        return userID;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public String getSourceName() {
        return sourceName;
    }
}
