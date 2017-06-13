package com.budget.data;

import java.time.Instant;

/**
 * Created by Maverick on 27.03.2017.
 */
public class Expense extends Entry {

    public Expense(Integer id, Float value, Integer userID,
                   Instant timestamp, String name, String sourceName) {
        super(id, value, userID, timestamp, name, sourceName);
    }

    @Override
    public Float getValueAbsolute() {
        return -value;
    }
}
