package uk.ac.starlink.topcat.plot2;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import uk.ac.starlink.topcat.TopcatListener;
import uk.ac.starlink.ttools.plot2.Plotter;
import uk.ac.starlink.ttools.plot2.data.Coord;

/**
 * FormLayerControl in which a single fixed form control is used.
 * It still allows per-subset configuration of different layers
 * using the same form.
 *
 * @author   Mark Taylor
 * @since    9 Jan 2014
 */
public class SingleFormLayerControl extends FormLayerControl {

    private final FormControl formControl_;

    /**
     * Constructor.
     *
     * @param  posCoordPanel  panel for entering table and basic positional
     *                        coordinates
     * @param  autoPopulate  if true, when the table is changed an attempt
     *                       will be made to initialise the coordinate fields
     *                       with some suitable values
     * @param  nextSupplier  manages global dispensing for some style options
     * @param  tcListener  listener for TopcatEvents
     * @param  controlIcon  icon for control stack
     * @param  plotter    plotter
     * @param  baseConfigger  configuration source for some global config
     *                        options
     */
    public SingleFormLayerControl( PositionCoordPanel posCoordPanel,
                                   boolean autoPopulate,
                                   NextSupplier nextSupplier,
                                   TopcatListener tcListener, Icon controlIcon,
                                   Plotter plotter, Configger baseConfigger ) {
        super( posCoordPanel, autoPopulate, nextSupplier, tcListener,
               controlIcon );
        formControl_ =
            new SimpleFormControl( baseConfigger, plotter, new Coord[ 0 ] );
        formControl_.addActionListener( getActionForwarder() );
        JScrollPane formScroller = new JScrollPane( formControl_.getPanel() );
        formScroller.setHorizontalScrollBarPolicy( JScrollPane
                                                  .HORIZONTAL_SCROLLBAR_NEVER );
        addControlTab( "Form", formScroller, false );
    }

    protected FormControl[] getActiveFormControls() {
        return new FormControl[] { formControl_ };
    }
}
