package at.ac.univie.t0306.expensetracker.ui.reports.graphs;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

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
 * Adapter which lets the BarGraph class and the used Third Party Visualization Library jjoe64/GraphView work together
 */

public class BarGraphAdapter implements GraphVisualization {

    private GraphView barGraph;
    private Map<String, Double> categoriesMap;
    private BarGraphSeries<DataPoint> series;

    /**
     * Constructor with a categoriesMap as a parameter (a map with categories and the sums spent/received on each category)
     *
     * @param categoriesMap
     */
    public BarGraphAdapter(Map<String, Double> categoriesMap) {
        this.categoriesMap = categoriesMap;
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
        barGraph = (GraphView) root.findViewById(R.id.bar_graph_elem);
        // CODE TAKEN FROM END <1>
    }


    /**
     * Actually plots the data with the help of the Third Party Visualization Library
     */
    @Override
    public void plotData() {
        DataPoint[] coordinates = obtainCoordinates(categoriesMap);

        // CODE TAKEN FROM START <1>
        /*
        The lines for initializing the graph view were were taken from:
        https://github.com/jjoe64/GraphView/wiki/Simple-graph
         */
        series = new BarGraphSeries<>(coordinates);
        barGraph.removeAllSeries();
        barGraph.addSeries(series);
        // CODE TAKEN FROM END <1>

        setBarGraphStyling(series);
        setAxesBounds(coordinates);
        formatLabels();


    }

    /**
     * Obtains data-points coordinates for the graph
     *
     * @param categoriesMap
     * @return
     */
    private DataPoint[] obtainCoordinates(Map<String, Double> categoriesMap) {
        // CODE TAKEN FROM START <2>
        /*
        The idea of using an array with one element as a local variable which will be modified inside a
        lambda expression was taken from:
        https://stackoverflow.com/questions/30026824/modifying-local-variable-from-inside-lambda
        The idea of how to obtain an array containing the elements of a stream was taken from:
        https://stackoverflow.com/questions/23079003/how-to-convert-a-java-8-stream-to-an-array
        */
        int[] xAxisValue = {0};
        DataPoint coordinates[] = categoriesMap.values().stream().map((elem) -> new DataPoint(xAxisValue[0]++, (double) elem)).toArray(DataPoint[]::new);
        // CODE TAKEN FROM END <2>
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

        double maxXDataPoint = coordinates.length;
        Double maxYDataPoint = Stream.of(coordinates).map(elem -> elem.getY()).max(Double::compare).orElse(10000.0);
        Double minYDataPoint = Stream.of(coordinates).map(elem -> elem.getY()).min(Double::compare).orElse(-10000.0);

        barGraph.getViewport().setXAxisBoundsManual(true);
        barGraph.getViewport().setYAxisBoundsManual(true);

        barGraph.getViewport().setMinX(0);
        if (coordinates.length <= 0)
            maxXDataPoint = 1.0;
        barGraph.getViewport().setMaxX(maxXDataPoint);

        barGraph.getViewport().setMinY(minYDataPoint - 100.0);
        barGraph.getViewport().setMaxY(maxYDataPoint + 100.0);

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
        barGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
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
        barGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
    }

    /**
     * Sets the graph styling for a user-friendly view
     *
     * @param series
     */
    private void setBarGraphStyling(BarGraphSeries<DataPoint> series) {
        // CODE TAKEN FROM START <3>
        /*
        The styling of the BarGraph was taken directly from an example featured on
        the repository of the used Chart/Graph library https://github.com/jjoe64/GraphView/wiki/Bar-Graph
        */
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                if (data.getY() <= 0)
                    return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(-data.getY() * 255 / 6), 100);
                else
                    return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });

        series.setSpacing(10);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        // CODE TAKEN FROM END <3>
    }

    /**
     * Actually enables graph scrolling and zooming with the help of the Third Party Visualization Library
     */
    public void enableScrollingAndZooming() {
        barGraph.getViewport().setScalable(true);
    }

    /**
     * Actually enables the tap observer with the help of the Third Party Visualization Library
     *
     * @param barGraphFragmentActivity
     */
    public void enableTapObserver(Activity barGraphFragmentActivity) {
        // CODE TAKEN FROM START <6>
        /*
        The code for creating a data tap listener has been taken directly from
        https://github.com/jjoe64/GraphView/wiki/Tap-listener-on-data
        */
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                String categoryTitle = categoriesMap.entrySet().stream()
                        .filter(e -> e.getValue() == dataPoint.getY())
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);
                Toast.makeText(barGraphFragmentActivity, categoryTitle + " -> " + dataPoint.getY() + "€", Toast.LENGTH_SHORT).show();
            }
        });
        // CODE TAKEN FROM END <6>
    }

}
