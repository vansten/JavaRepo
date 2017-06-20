package com.budget;

import com.budget.data.Entry;
import com.budget.data.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

// All filters are inclusive.
public class FilterUtility {

    public static class MinMax<T extends Comparable<T>> {
        public T MinValue;
        public T MaxValue;

        MinMax(T minValue, T maxValue) {
            this.MinValue = minValue;
            this.MaxValue = maxValue;
        }

        MinMax(MinMax<T> copy) {
            this.MinValue = copy.MinValue;
            this.MaxValue = copy.MaxValue;
        }

        boolean equals(MinMax<T> other) { return this.MinValue == other.MinValue && this.MaxValue == other.MaxValue; }
        boolean IsInRange(T val) {
            return val.compareTo(MinValue) >= 0 && val.compareTo(MaxValue) <= 0;
        }
        void swapValues() {
            T tmp = MaxValue;
            MaxValue = MinValue;
            MinValue = tmp;
        }
    }

    private static final MinMax<Float> FILTERVALUE_BASE = new MinMax<>(-1000000.0f, 1000000.0f);
    private static final MinMax<Long> FILTERDATE_BASE =
            new MinMax<>(LocalDate.of(1970, 1, 1).toEpochDay(),
            LocalDate.of(2999, 1, 1).toEpochDay());


    public MinMax<Float> FilterValue;
    public MinMax<Long> FilterDate;

    FilterUtility() {
        FilterValue = new MinMax<Float>(FILTERVALUE_BASE);
        FilterDate = new MinMax<Long>(FILTERDATE_BASE);
}

    public void updateFromUserEntries(boolean totalReset) {

        User loggedUser = AppController.getInstance().getLoggedUser();

        if(loggedUser == null)
        {
            return;
        }

        // get min and max values for this user's data
        ArrayList<Entry> entries =
                AppController.getInstance().getDbController().getEntriesForUser(
                        AppController.getInstance().getLoggedUser()
                );

        if(totalReset) {
            FilterValue = new MinMax<Float>(FILTERVALUE_BASE);
            FilterDate = new MinMax<Long>(FILTERDATE_BASE);
        }

        if(FilterValue.equals(FILTERVALUE_BASE)) {
            FilterValue.swapValues();

            for(Entry e : entries) {
                if(e.getValueAbsolute() < FilterValue.MinValue)
                    FilterValue.MinValue = e.getValueAbsolute();
                if(e.getValueAbsolute() > FilterValue.MaxValue)
                    FilterValue.MaxValue = e.getValueAbsolute();
            }
        }

        if(FilterDate.equals(FILTERDATE_BASE)) {
            FilterDate.swapValues();

            for(Entry e : entries) {
                Long timeStamp = e.getTimestamp().atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay();
                if (timeStamp < FilterDate.MinValue)
                    FilterDate.MinValue = timeStamp;
                if (timeStamp > FilterDate.MaxValue)
                    FilterDate.MaxValue = timeStamp;
            }
        }
    }

    public boolean isValueInRange(Float value) {
        return FilterValue.IsInRange(value);
    }

    public boolean isDateInRange(LocalDate date) {
        return FilterDate.IsInRange(date.toEpochDay());
    }

    public boolean isEntryPassingFilters(Entry entry) {
        return isValueInRange(entry.getValueAbsolute()) &&
                isDateInRange(entry.getTimestamp().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public LocalDate getDateMin() {
        return LocalDate.ofEpochDay(FilterDate.MinValue);
    }

    public LocalDate getDateMax() {
        return LocalDate.ofEpochDay(FilterDate.MaxValue);
    }

    public void setDateMin(LocalDate date) {
        FilterDate.MinValue = date.toEpochDay();
    }

    public void setDateMax(LocalDate date) {
        FilterDate.MaxValue = date.toEpochDay();
    }

}