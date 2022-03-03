package at.ac.univie.t0306.expensetracker.ui.reports.graphsUserInteraction;

/*
Note: various ideas (both related to the UML design and to the actual implementation) of the Decorator pattern
were taken from the following websites:

https://refactoring.guru/design-patterns/decorator/
https://refactoring.guru/design-patterns/decorator/java/example
https://www.tutorialspoint.com/design_pattern/decorator_pattern.htm

*/

import at.ac.univie.t0306.expensetracker.ui.reports.graphs.GraphVisualization;

/**
 * BaseDecorator class for the concrete decorators of a LineGraph or BarGraph object
 */
public abstract class GraphVisualizationUserInterfaceFeatures implements GraphVisualization {
    private GraphVisualization graphVisualization;

    /**
     * Constructor which has as parameter an instance of the implemented interface GraphVisualization
     *
     * @param graphVisualization
     */
    public GraphVisualizationUserInterfaceFeatures(GraphVisualization graphVisualization) {
        this.graphVisualization = graphVisualization;
    }

    /**
     * The actual work of plotting the data will be done by the specific graph adapter
     */
    @Override
    public void plotData() {
        graphVisualization.plotData();
    }
}
