package com.budget;

import com.budget.data.Entry;
import com.budget.data.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultKeyedValueDataset;
import org.jfree.data.general.DefaultKeyedValuesDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.util.Rotation;

import javax.naming.Name;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Array;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Maverick on 19.06.2017.
 */
public class ChartUtility {

    private TimeSeriesCollection datasetValueDate = null;
    private DefaultPieDataset datasetNames = null;
    private DefaultPieDataset datasetSources = null;

    public ChartUtility() {
        datasetValueDate = new TimeSeriesCollection();
        datasetNames = new DefaultPieDataset();
        datasetSources = new DefaultPieDataset();
    }

    public void generateDatasets(User user, FilterUtility filter) {

        if(user == null) {
            datasetValueDate.removeAllSeries();
            datasetNames.clear();
            datasetSources.clear();
            return;
        }

        ArrayList<Entry> entries =
                AppController.getInstance().getDbController().getEntriesForUser(
                        AppController.getInstance().getLoggedUser()
                );

        ArrayList<Float> values = new ArrayList<>();
        ArrayList<Instant> instants = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> sources = new ArrayList<>();
        for(Entry e : entries) {

            if(!filter.isEntryPassingFilters(e))
                continue;

            values.add(e.getValueAbsolute());
            instants.add(e.getTimestamp());
            names.add(e.getName());
            sources.add(e.getSourceName());
        }

        generateDataSetFromValuesAndDates(datasetValueDate, values, instants);
        generateDataSetFromStringArray(datasetNames, names);
        generateDataSetFromStringArray(datasetSources, sources);
    }

    public void drawToResponseValueDate(HttpServletResponse response) {
        drawToResponseCommonPre(response);
        JFreeChart chart = createTimeChart(datasetValueDate,
                "Expenses and earnings", "Dates", "Budget");
        drawToResponseCommonPost(response, chart, 1024, 600);
    }

    public void drawToResponseNames(HttpServletResponse response) {
        drawToResponseCommonPre(response);
        JFreeChart chart = createPieChart(datasetNames, "Names");
        drawToResponseCommonPost(response, chart, 1024, 400);
    }

    public void drawToResponseSources(HttpServletResponse response) {
        drawToResponseCommonPre(response);
        JFreeChart chart = createPieChart(datasetSources, "Sources");
        drawToResponseCommonPost(response, chart, 1024, 400);
    }

    private void drawToResponseCommonPre(HttpServletResponse response) {
        response.setContentType("image/png");
    }

    private void drawToResponseCommonPost(HttpServletResponse response, JFreeChart chart, int sizeX, int sizeY) {
        try {
            ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, sizeX, sizeY);
            response.getOutputStream().close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JFreeChart createTimeChart(TimeSeriesCollection dataset, String title,
                                              String timeLabel, String valueLabel) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, timeLabel, valueLabel, dataset,
                false, false, false);
        return chart;
    }

    private JFreeChart createPieChart(PieDataset pdSet, String title) {
        JFreeChart chart = ChartFactory.createPieChart(title, pdSet, true, false, false);

        PiePlot plot = (PiePlot)chart.getPlot();
        plot.setStartAngle(290.0);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setBackgroundAlpha(0.0f);

        return chart;
    }

    private void generateDataSetFromValuesAndDates(TimeSeriesCollection dataset,
                                                   List<Float> values, List<Instant> dates) {
        // clear dataset first
        dataset.removeAllSeries();

        assert (values.size() == dates.size());

        // assume there may be multiple values for each date
        Map<LocalDate, Float> uniqueValues = new HashMap<>();
        int elementCount = values.size();
        for(int i = 0; i < elementCount; ++i) {
            Instant ts = dates.get(i);
            LocalDate date = ts.atZone(ZoneId.systemDefault()).toLocalDate();
            Float val = values.get(i);

            if(uniqueValues.containsKey(date)) {
                uniqueValues.put(date, uniqueValues.get(date) + val);
            } else {
                uniqueValues.put(date, val);
            }
        }

        TimeSeries series = new TimeSeries("Date");

        int counter = 0;
        for (Map.Entry<LocalDate, Float> entry : uniqueValues.entrySet()) {
            LocalDate date = entry.getKey();
            series.add(new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear()), entry.getValue());
        }

        dataset.addSeries(series);
    }

    private void generateDataSetFromStringArray(DefaultPieDataset dpd, List<String> stringList) {
        dpd.clear();

        Map<String, Double> accumulators = new HashMap<>();
        for(String str : stringList) {
            if(accumulators.containsKey(str)) {
                accumulators.put(str, accumulators.get(str) + 1.0);
            } else {
                accumulators.put(str, 1.0);
            }
        }

        Double allStringCount = (double)stringList.size();
        Set<Map.Entry<String, Double>> entries = accumulators.entrySet();
        for(Map.Entry<String, Double> entry : entries) {
            Double percentVal = entry.getValue() / allStringCount;
            percentVal *= 100.0;

            dpd.setValue(entry.getKey(), percentVal);
        }
    }

    private PieDataset generateDummyDataSet() {
        DefaultPieDataset dpd = new DefaultPieDataset();
        dpd.setValue("Mac", 21);
        dpd.setValue("Linux", 30);
        dpd.setValue("Window", 40);
        dpd.setValue("Others", 9);
        return dpd;
    }
}
