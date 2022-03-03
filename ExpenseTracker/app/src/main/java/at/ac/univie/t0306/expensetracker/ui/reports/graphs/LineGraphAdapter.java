package at.ac.univie.t0306.expensetracker.ui.reports.graphs;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import at.ac.univie.t0306.expensetracker.R;

/*
Note: various ideas (both related to the UML design and to the actual implementation) of the Adapter pattern
were taken from the following websites:

https://refactoring.guru/design-patterns/adapter
https://refactoring.guru/design-patterns/adapter/java/example
https://www.tutorialspoint.com/design_pattern/adapter_pattern.htm

*/

/**
 * Adapter which lets the LineGraph class and the used Third Party Visualization Library jjoe64/GraphView work together
 */
public class LineGraphAdapter implements GraphVisualization {

    private GraphView lineGraph;
    private Map<OffsetDateTime, Double> datesMap;
    private LineGraphSeries<DataPoint> series;

    /**
     * Constructor with a datesMap as a parameter (a map with dates and the sums spent/received on each specific day)
     *
     * @param datesMap
     */
    public LineGraphAdapter(Map<OffsetDateTime, Double> datesMap) {
        this.datesMap = datesMap;
    }

    /**
     * Sets up the view for the graph
     *
     * @param root
     */
    public void setView(View root) {
        // CODE TAKEN FROM START <1>
        /*
        The lines for initializing the graph view were were taken from:
        https://github.com/jjoe64/GraphView/wiki/Simple-graph
         */
        lineGraph = (GraphView) root.findViewById(R.id.line_graph_elem);
        // CODE TAKEN FROM END <1>
    }

    /**
     * Actually plots the data with the help of the Third Party Visualization Library
     */
    @Override
    public void plotData() {
        DataPoint[] coordinates = obtainCoordinates(datesMap);

        // CODE TAKEN FROM START <1>
        /*
        The lines for initializing the graph view were were taken from:
        https://github.com/jjoe64/GraphView/wiki/Simple-graph
         */
        series = new LineGraphSeries<>(coordinates);
        lineGraph.removeAllSeries();
        lineGraph.addSeries(series);
        // CODE TAKEN FROM END <1>

        setLineGraphStyling(series);
        setAxesBounds(coordinates);
        formatLabels();
    }

    /**
     * Obtains data-points coordinates for the graph
     *
     * @param datesMap
     * @return
     */
    private DataPoint[] obtainCoordinates(Map<OffsetDateTime, Double> datesMap) {

        // CODE TAKEN FROM START <2>
        /*
        The idea of how to obtain an array containing the elements of a stream was taken from:
        https://stackoverflow.com/questions/23079003/how-to-convert-a-java-8-stream-to-an-array

        The code for converting OffsetDateTime to Date has been taken from:
        https://stackoverflow.com/questions/41480485/how-to-convert-zoneddatetime-offsetdatetime-to-date-using-threetenabp
        */
        DataPoint coordinates[] = datesMap.entrySet().stream().map((elem) -> new DataPoint(new Date(elem.getKey().toInstant().toEpochMilli()),
                (double) elem.getValue())).toArray(DataPoint[]::new);
        // CODE TAKEN FROM END <2>


        // CODE TAKEN FROM START <9>
        /*
        The line of code for sorting an array of objects was taken from:
        https://www.baeldung.com/java-sorting-arrays
        */
        Arrays.sort(coordinates, Comparator.comparing(DataPoint::getX));
        // CODE TAKEN FROM END <9>
        return coordinates;
    }

    /**
     * Sets axes bounds for the graph
     *
     * @param coordinates
     */
    private void setAxesBounds(DataPoint[] coordinates) {
        // CODE TAKEN FROM START <5>
        /*
        The code for setting axis bounds and for enabling scrolling and zooming has been taken directly from
        https://github.com/jjoe64/GraphView/wiki/Zooming-and-scrolling
        */


        lineGraph.getViewport().setXAxisBoundsManual(true);
        if (coordinates.length >= 2) {
            lineGraph.getViewport().setMinX(coordinates[0].getX());
            lineGraph.getViewport().setMaxX(coordinates[coordinates.length - 1].getX());
        }
        Double maxYDataPoint = Stream.of(coordinates).map(elem -> elem.getY()).max(Double::compare).orElse(10000.0);
        Double minYDataPoint = Stream.of(coordinates).map(elem -> elem.getY()).min(Double::compare).orElse(-10000.0);
        lineGraph.getViewport().setYAxisBoundsManual(true);
        lineGraph.getViewport().setMinY(minYDataPoint - 100.0);
        lineGraph.getViewport().setMaxY(maxYDataPoint + 100.0);

        // CODE TAKEN FROM END <5>
    }

    /**
     * Formats the labels for a user-friendly view
     */
    private void formatLabels() {

        // CODE TAKEN FROM START <4>
        /*
        The customization of the chart labels has been taken directly from
        https://github.com/jjoe64/GraphView/wiki/Labels-and-label-formatter
        */
        lineGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + "€";
                }
            }
        });
        // CODE TAKEN FROM END <4>
        lineGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
    }

    /**
     * Sets the graph styling for a user-friendly view
     *
     * @param series
     */
    private void setLineGraphStyling(LineGraphSeries<DataPoint> series) {
        // CODE TAKEN FROM START <10>
        /*
        The lines of code for styling the line graph were taken from:
        https://github.com/jjoe64/GraphView/wiki/Line-Graph
        */
        series.setColor(Color.BLUE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        // CODE TAKEN FROM END <10>
    }

    /**
     * Actually enables graph scrolling and zooming with the help of the Third Party Visualization Library
     */
    public void enableScrollingAndZooming() {
        lineGraph.getViewport().setScalable(true);
        lineGraph.getViewport().scrollToEnd();
    }

    /**
     * Actually enables the tap observer with the help of the Third Party Visualization Library
     *
     * @param lineGraphFragmentActivity
     */
    public void enableTapObserver(Activity lineGraphFragmentActivity) {

        // CODE TAKEN FROM START <6>
        /*
        The code for creating a data tap listener has been taken directly from
        https://github.com/jjoe64/GraphView/wiki/Tap-listener-on-data
        */
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                // CODE TAKEN FROM START <12>
                /*
                The code for working with (simple) date formats and strings was taken from:
                https://www.javatpoint.com/java-date-to-string
                */
                DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy");
                String strDate = dateFormat.format(dataPoint.getX());
                // CODE TAKEN FROM END <12>
                Toast.makeText(lineGraphFragmentActivity, strDate + " -> " + dataPoint.getY() + "€", Toast.LENGTH_SHORT).show();
            }
        });
        // CODE TAKEN FROM END <6>
    }

}
