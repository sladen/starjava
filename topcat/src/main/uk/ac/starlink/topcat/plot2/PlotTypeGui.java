package uk.ac.starlink.topcat.plot2;

/**
 * Provides PlotType-specific aspects of the GUI, used by the 
 * generic PlotWindow GUI.
 *
 * @author   Mark Taylor
 * @since    12 Mar 2013
 */
public interface PlotTypeGui<P,A> {

    /**
     * Returns a user control for axis configuration.
     *
     * @param  stack   control stack into which control will be integrated
     * @return  new axis control for this plot type
     */
    AxisController<P,A> createAxisController( ControlStack stack );

    /**
     * Returns a user panel for entering basic standard data positions.
     *
     * @param   npos  number of groups of positional coordinates for entry
     * @return   new position entry panel for this plot type
     */
    PositionCoordPanel createPositionCoordPanel( int npos );

    /**
     * Indicates whether this plot type supports selectable point positions.
     * Normally the return is true, but if this plot type never plots
     * points that can be identified by a screen X,Y position, return false.
     *
     * @return  false iff this plot type never supports selectable points
     */
    boolean hasPositions();
}
