package at.ac.univie.t0306.expensetracker.ui.reports.graphsUserInteraction;

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
 * Concrete decorator class, which enables scrolling and zooming on a graph
 */
public class GraphScrollingAndZooming extends GraphVisualizationUserInterfaceFeatures {

    private LineGraphAdapter lineGraphAdapter;
    private BarGraphAdapter barGraphAdapter;

    /**
     * Constructor with a barGraphAdapter
     *
     * @param graphVisualization
     * @param barGraphAdapter
     */
    public GraphScrollingAndZooming(GraphVisualization graphVisualization, BarGraphAdapter barGraphAdapter) {
        super(graphVisualization);
        this.barGraphAdapter = barGraphAdapter;
    }

    /**
     * Constructor with a lineGraphAdapter
     *
     * @param graphVisualization
     * @param lineGraphAdapter
     */
    public GraphScrollingAndZooming(GraphVisualization graphVisualization, LineGraphAdapter lineGraphAdapter) {
        super(graphVisualization);
        this.lineGraphAdapter = lineGraphAdapter;
    }

    /**
     * The actual work of plotting the data will be done by the specific graph adapter
     * The graph object is also decorated/enhanced with a UI feature
     */
    @Override
    public void plotData() {
        super.plotData();
        enableScrollingAndZooming();
    }

    /**
     * Helper function which enables scrolling and zooming
     * The actual work of enabling this feature will be done by the specific graph adapter
     */
    private void enableScrollingAndZooming() {
        if (barGraphAdapter != null)
            barGraphAdapter.enableScrollingAndZooming();
        else if (lineGraphAdapter != null)
            lineGraphAdapter.enableScrollingAndZooming();
    }
}
