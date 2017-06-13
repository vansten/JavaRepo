package com.budget;

import com.budget.data.Entry;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

// All filters are inclusive.
public class FilterUtility {

    public class MinMax<T extends Comparable<T>> {
        public T MinValue;
        public T MaxValue;

        MinMax(T minValue, T maxValue) {
            this.MinValue = minValue;
            this.MaxValue = maxValue;
        }

        boolean IsInRange(T val) {
            return val.compareTo(MinValue) >= 0 && val.compareTo(MaxValue) <= 0;
        }
    }

    public MinMax<Float> FilterValue;
    public MinMax<Long> FilterDate;

    FilterUtility() {
        FilterValue = new MinMax<>(-Float.MAX_VALUE, Float.MAX_VALUE);
        FilterDate = new MinMax<>(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean isValueInRange(Float value) {
        return FilterValue.IsInRange(value);
    }

    public boolean isDateInRange(LocalDate date) {
        return FilterDate.IsInRange(date.toEpochDay());
    }

    public boolean isEntryPassingFilters(Entry entry) {
        return isValueInRange(entry.getValue()) &&
                isDateInRange(entry.getTimestamp().atZone(ZoneId.systemDefault()).toLocalDate());
    }

}