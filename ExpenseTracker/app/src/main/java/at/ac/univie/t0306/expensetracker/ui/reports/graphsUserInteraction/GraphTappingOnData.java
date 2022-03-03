package at.ac.univie.t0306.expensetracker.ui.reports.graphsUserInteraction;

import android.app.Activity;

import at.ac.univie.t0306.expensetracker.ui.reports.graphs.BarGraphAdapter;
import at.ac.univie.t0306.expensetracker.ui.reports.graphs.GraphVisualization;
import at.ac.univie.t0306.expensetracker.ui.reports.graphs.LineGraphAdapter;

/*
Note: various ideas (both related to the UML design and to the actual implementation) of the Decorator pattern
were taken from the following websites:

https://refactoring.guru/design-patterns/decorator/
https://refactoring.guru/design-patterns/decorator/java/example
https://www.tutorialspoint.com/design_pattern/decorator_pattern.htm

*/

/**
 * Concrete decorator class, which enables tapping on data of a graph
 */
public class GraphTappingOnData extends GraphVisualizationUserInterfaceFeatures {

    private LineGraphAdapter lineGraphAdapter;
    private BarGraphAdapter barGraphAdapter;
    Activity fragmentActivity;

    /**
     * Constructor with a barGraphAdapter
     *
     * @param graphVisualization
     * @param barGraphAdapter
     * @param fragmentActivity
     */
    public GraphTappingOnData(GraphVisualization graphVisualization, BarGraphAdapter barGraphAdapter, Activity fragmentActivity) {
        super(graphVisualization);
        this.barGraphAdapter = barGraphAdapter;
        this.fragmentActivity = fragmentActivity;
    }

    /**
     * Constructor with a lineGraphAdapter
     *
     * @param graphVisualization
     * @param lineGraphAdapter
     * @param fragmentActivity
     */
    public GraphTappingOnData(GraphVisualization graphVisualization, LineGraphAdapter lineGraphAdapter, Activity fragmentActivity) {
        super(graphVisualization);
        this.lineGraphAdapter = lineGraphAdapter;
        this.fragmentActivity = fragmentActivity;
    }

    /**
     * The actual work of plotting the data will be done by the specific graph adapter
     * The graph object is also decorated/enhanced with a UI feature
     */
    @Override
    public void plotData() {
        super.plotData();
        enableTapObserver(fragmentActivity);
    }

    /**
     * Helper function which enables the tap observer
     * The actual work of enabling this feature will be done by the specific graph adapter
     *
     * @param fragmentActivity
     */
    private void enableTapObserver(Activity fragmentActivity) {
        // discover which type of graph should be enhanced with the UI features
        if (barGraphAdapter != null)
            barGraphAdapter.enableTapObserver(fragmentActivity);
        else if (lineGraphAdapter != null)
            lineGraphAdapter.enableTapObserver(fragmentActivity);
    }
}
