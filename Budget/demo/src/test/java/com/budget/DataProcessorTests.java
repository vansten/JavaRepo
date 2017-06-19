package com.budget;

import com.budget.data.Earning;
import com.budget.data.Entry;
import com.budget.data.Expense;
import com.ritaja.xchangerate.api.CurrencyConverter;
import com.ritaja.xchangerate.api.CurrencyConverterBuilder;
import com.ritaja.xchangerate.util.Currency;
import com.ritaja.xchangerate.util.Strategy;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by krzys on 6/13/2017.
 */
public class DataProcessorTests {

    @Test
    public void TestExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        LocalDate ld1 = LocalDate.of(2015, Month.MAY, 31);
        LocalDate ld2 = LocalDate.of(2016, Month.DECEMBER, 10);
        LocalDate ld3 = LocalDate.of(2017, Month.FEBRUARY, 13);
        Instant i1 = ld1.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant i2 = ld2.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant i3 = ld3.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        expenses.add(new Expense(0, 5.0f, 0, i1, "tmp", "xyz"));
        expenses.add(new Expense(0, 1.0f, 0, i2, "tmp", "xyz"));
        expenses.add(new Expense(0, 120.0f, 0, i3, "tmp", "xyz"));

        DataSummary dataSummary = DataProcessor.calculateDataSummary(expenses, null);

        long days = ChronoUnit.DAYS.between(ld1, Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
        long months = ChronoUnit.MONTHS.between(ld1, Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
        long years = ChronoUnit.YEARS.between(ld1, Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;

        float expectedPerDay = (5.0f + 1.0f + 120.0f) / (float)days;
        assertTrue(expectedPerDay == dataSummary.getExpensesPerDay());
        float expectedPerMonth = (5.0f + 1.0f + 120.0f) / (float)months;
        assertTrue(expectedPerMonth == dataSummary.getExpensesPerMonth());
        float expectedPerYear = (5.0f + 1.0f + 120.0f) / (float)years;
        assertTrue(expectedPerYear == dataSummary.getExpensesPerYear());
    }

    @Test
    public void TestEarnings() {
        ArrayList<Earning> earnings = new ArrayList<>();
        LocalDate ld1 = LocalDate.of(2016, Month.OCTOBER, 31);
        LocalDate ld2 = LocalDate.of(2017, Month.MAY, 10);
        LocalDate ld3 = LocalDate.of(2017, Month.JUNE, 13);
        Instant i1 = ld1.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant i2 = ld2.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant i3 = ld3.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        earnings.add(new Earning(0, 50.0f, 0, i1, "tmp", "xyz"));
        earnings.add(new Earning(0, 10.0f, 0, i2, "tmp", "xyz"));
        earnings.add(new Earning(0, 20.0f, 0, i3, "tmp", "xyz"));

        DataSummary dataSummary = DataProcessor.calculateDataSummary(null, earnings);

        long days = ChronoUnit.DAYS.between(ld1, Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
        long months = ChronoUnit.MONTHS.between(ld1, Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
        long years = ChronoUnit.YEARS.between(ld1, Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;

        float expectedPerDay = (50.0f + 10.0f + 20.0f) / (float)days;
        assertTrue(expectedPerDay == dataSummary.getEarningsPerDay());
        float expectedPerMonth = (50.0f + 10.0f + 20.0f) / (float)months;
        assertTrue(expectedPerMonth == dataSummary.getEarningsPerMonth());
        float expectedPerYear = (50.0f + 10.0f + 20.0f) / (float)years;
        assertTrue(expectedPerYear == dataSummary.getEarningsPerYear());
    }


    @Test
    public void convertCurrency() {
        CurrencyConverter converter = new CurrencyConverterBuilder()
                .strategy(Strategy.YAHOO_FINANCE_FILESTORE)
                .buildConverter();

        converter.setRefreshRateSeconds(86400);

        try {
            BigDecimal output = converter.convertCurrency(new BigDecimal("100"), Currency.USD, Currency.EUR);
            assertNotNull(output);
            System.out.println(output);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void convertEntries() {
        CurrencyConverter converter = new CurrencyConverterBuilder()
                .strategy(Strategy.YAHOO_FINANCE_FILESTORE)
                .buildConverter();

        converter.setRefreshRateSeconds(86400);

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Entry> convertedEntries = new ArrayList<>();
        LocalDate ld1 = LocalDate.of(2016, Month.OCTOBER, 31);
        LocalDate ld2 = LocalDate.of(2017, Month.MAY, 10);
        LocalDate ld3 = LocalDate.of(2017, Month.JUNE, 13);
        Instant i1 = ld1.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant i2 = ld2.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant i3 = ld3.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        entries.add(new Earning(0, 50.0f, 0, i1, "tmp", "xyz"));
        entries.add(new Earning(0, 10.0f, 0, i2, "tmp", "xyz"));
        entries.add(new Earning(0, 20.0f, 0, i3, "tmp", "xyz"));

        entries.forEach(entry -> {System.out.println(entry.getValueAbsolute());});
        entries.forEach(entry -> {
            BigDecimal entryValue = BigDecimal.valueOf(entry.getValueAbsolute());
            Float converted = 0f;
            try {
                converted = converter.convertCurrency(entryValue, Currency.PLN, Currency.USD).floatValue();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            convertedEntries.add(new Entry(entry.getID(), converted, entry.getUserID(), entry.getTimestamp(),
                    entry.getName(), entry.getSourceName()));
        });
        convertedEntries.forEach(entry -> {System.out.println(entry.getValueAbsolute());});
    }


}
