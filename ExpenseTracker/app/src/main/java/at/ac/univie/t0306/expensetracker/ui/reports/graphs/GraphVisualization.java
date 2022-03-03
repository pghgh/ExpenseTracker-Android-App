package at.ac.univie.t0306.expensetracker.ui.reports.graphs;

/**
 * Interface implemented by various graph types (in this case, by the BarGraph and LineGraph classes)
 */
public interface GraphVisualization {

    /**
     * Method which will be actually implemented by a specific graph adapter
     */
    void plotData();
}
