package com.budget.data;

import java.time.Instant;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;

        Entry entry = (Entry) o;

        return id.equals(entry.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void toList(List<Object> list) {
        list.clear();
        list.add(getID());
        list.add(getName());
        list.add(getValueAbsolute());
        list.add(Date.from(getTimestamp()));
        list.add(getSourceName());
    }

    public static void getFieldNames(List<String> names) {
        names.clear();
        names.add("ID");
        names.add("Name");
        names.add("Value");
        names.add("Timestamp");
        names.add("Source");
    }
}
