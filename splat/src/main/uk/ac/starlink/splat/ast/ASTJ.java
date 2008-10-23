/*
 * Copyright (C) 1999-2005 Central Laboratory of the Research Councils
 * Copyright (C) 2006 Particle Physics and Astronomy Research Council
 * Copyright (C) 2007 Science and Technology Facilities Council
 *
 * History:
 *    21-SEP-1999 (Peter W. Draper):
 *       Original version.
 *    29-MAY-2002 (Peter W. Draper):
 *       Converted to use the JNIAST package, rather than my JNI
 *       wrappers. Removed all native functions.
 *    18-FEB-2005 (Peter W. Draper):
 *       Removed all graphics functions.
 */
package uk.ac.starlink.splat.ast;

import java.io.Serializable;
import java.util.logging.Logger;

import uk.ac.starlink.ast.AstException;
import uk.ac.starlink.ast.AstObject;
import uk.ac.starlink.ast.AstPackage;
import uk.ac.starlink.ast.CmpFrame;
import uk.ac.starlink.ast.CmpMap;
import uk.ac.starlink.ast.DSBSpecFrame;
import uk.ac.starlink.ast.FluxFrame;
import uk.ac.starlink.ast.Frame;
import uk.ac.starlink.ast.FrameSet;
import uk.ac.starlink.ast.LutMap;
import uk.ac.starlink.ast.Mapping;
import uk.ac.starlink.ast.SpecFluxFrame;
import uk.ac.starlink.ast.SpecFrame;
import uk.ac.starlink.ast.UnitMap;

/**
 * Java interface for AST manipulations based on a frameset (type of
 * operations needed to support WCS coordinates).  The graphics facilities (if
 * used) are provided by the Grf class. This should also be initialised before
 * use and a reference to the object passed.
 *
 * @author Peter W. Draper
 * @version $Id$
 * @since 29-MAY-2002
 */
public class ASTJ
    implements Serializable
{
    // Logger.
    private static Logger logger =
        Logger.getLogger( "uk.ac.starlink.splat.ast.ASTJ" );

    //  ============
    //  Constructors
    //  ============

    /**
     *  Default constructor
     */
    public ASTJ()
    {
        //  Do nothing.
    }

    /**
     *  Initialise from an AST frameset reference.
     */
    public ASTJ( FrameSet astref )
    {
        setRef( astref );
    }

    //
    //  ================
    //  Static variables
    //  ================
    //

    /**
     *  Serialization version ID string (generated by serialver on
     *  original star.jspec.ast.ASTJ class).
     */
    static final long serialVersionUID = -3317278370985073002L;

    //  ===================
    //  Protected variables
    //  ===================

    /**
     *  Reference to Ast frameset.
     */
    protected FrameSet astRef = null;

    /**
     * Size of buffers used when transferring potentially large chunks
     * of data.
     */
    protected static final int MAXDIM = 7;

    /**
     * Smallest value between 1.0 and next double precision value.
     */
    protected static final double EPSILON = 2.2204460492503131e-16;

    /**
     * Whether the first axis of the current FrameSet is a SpecFrame.
     */
    protected boolean isSpecFrameSet = false;
    protected boolean isSpecFrame = false;

    /**
     * Whether the first axis of the current FrameSet is a DSBSpecFrame.
     */
    protected boolean isDSBSpecFrameSet = false;
    protected boolean isDSBSpecFrame = false;


    //  ====================
    //  Class public methods
    //  ====================

    /**
     * Check if JNIAST is available.
     */
    public static boolean isAvailable()
    {
        return AstPackage.isAvailable();
    }


    /**
     *  Set the main AST frameset.
     *
     *  @param astRef reference to the AST frameset.
     */
    public void setRef( FrameSet astRef )
    {
        this.astRef = astRef;
        isSpecFrameSet = false;
        isDSBSpecFrameSet = false;
    }

    /**
     *  Get a copy of the current Ast frameset.
     */
    public FrameSet getRef()
    {
        return astRef;
    }

    /**
     *  Print the frameset to System.out.
     */
    public void astWrite()
    {
        if ( astRef != null ) {
            ASTChannel.astWrite( astRef );
        }
    }

    /**
     * Return if the an axis of the current FrameSet is of a given Class.
     */
    public boolean isAxisClass( int axis, Class clazz )
    {
        return clazz.isInstance( pickAxis( axis ) );
    }

    /**
     * Return if the first axis of the current FrameSet is a SpecFrame.
     * Cached request, so should be very fast after first call.
     */
    public boolean isFirstAxisSpecFrame()
    {
        if ( ! isSpecFrameSet ) {
            isSpecFrame = ( pickAxis( 1 ) instanceof SpecFrame );
            isSpecFrameSet = true;
        }
        return isSpecFrame;
    }

    /**
     * Return if the first axis of the current FrameSet is a DSBSpecFrame.
     * Cached request, so should be very fast after first call.
     */
    public boolean isFirstAxisDSBSpecFrame()
    {
        if ( ! isDSBSpecFrameSet ) {
            isDSBSpecFrame =  ( pickAxis( 1 ) instanceof DSBSpecFrame );
            isDSBSpecFrameSet = true;
        }
        return isDSBSpecFrame;
    }

    /**
     * Return an axis from the current FrameSet.
     */
    public Frame pickAxis( int axis )
    {
        int iaxes[] = new int[1];
        iaxes[0] = axis;
        return astRef.pickAxes( 1, iaxes, null );
    }

    /**
     * Return the index of a domain in a FrameSet.
     */
    public static int findDomain( FrameSet astRef, String domain )
    {
        if ( astRef == null || domain == null || "".equals( domain ) ) {
            return -1;
        }

        int current = astRef.getCurrent();
        int nframe = astRef.getNframe();
        for ( int i = 1; i <= nframe; i++ ) {
            astRef.setCurrent( i );
            if ( domain.equalsIgnoreCase( astRef.getDomain() ) ) {
                astRef.setCurrent( current );
                return i;
            }
        }

        //  No match return -1;
        astRef.setCurrent( current );
        return -1;
    }

    /**
     * Return the index of a domain in the current FrameSet.
     */
    public int findDomain( String domain )
    {
        return findDomain( astRef, domain );
    }

    /**
     *  Create an AST frame and return a reference to it.
     *
     *  @param naxes  number of frame axes required.
     *  @param attrib list of attributes for the frame.
     *
     *  @return reference to the AST frame.
     */
    public Frame astFrame( int naxes, String attrib )
    {
        Frame frame = new Frame( naxes );
        frame.set( attrib );
        return frame;
    }

    /**
     *  Create an AST frameset and return a reference to it.
     *
     *  @param frame  the initial AST frame
     *  @param attrib list of attributes for the frameset.
     *
     *  @return reference to the AST frameset.
     */
    public FrameSet astFrameSet( Frame frame, String attrib )
    {
        FrameSet frameSet = new FrameSet( frame );
        frameSet.set( attrib );
        return frameSet;
    }

    /**
     *  Add a frame to an AST frameset.
     *
     *  @param frameset reference to AST frameset
     *  @param insert position of related frame in frameset
     *  @param map reference to an AST mapping
     *  @param frame reference to the new AST frame
     */
    public void astAddFrame( FrameSet frameset, int insert,
                             Mapping map, Frame frame )
    {
        frameset.addFrame( insert, map, frame );
    }


    /**
     *  Clone an AST reference of some kind.
     *
     *  @deprecated Use direct ".clone()" method
     */
    public static AstObject astClone( AstObject ref )
    {
        if ( ref != null ) {
            return (AstObject) ref.clone();
        }
        return null;
    }

    /**
     *  Copy an AST reference of some kind.
     *
     *  @deprecated Use direct ".copy()" method
     */
    public static AstObject astCopy( AstObject ref )
    {
        if ( ref != null ) {
            return ref.copy();
        }
        return null;
    }

    /**
     *  Set the attributes of an AST object
     *
     *  @param astRef reference to AST object (such as an AstFrame,
     *                AstPlot etc.)
     *  @param settings the settings to apply.
     *
     *  @deprecated Use direct ".set" method
     */
    public static void astSet( AstObject astRef, String settings )
    {
        astRef.set( settings );
    }

    /**
     *  Get an attribute of an AST object as String.
     *
     *  @param astRef reference to AST object (such as an AstFrame,
     *                AstPlot etc.)
     *  @param attrib the AST attribute to return.
     *
     *  @return value of attribute
     *
     *  @deprecated Use direct ".getC" method
     */
    public static String astGet( AstObject astRef, String attrib )
    {
        return astRef.getC( attrib );
    }

    /**
     *  Get an attribute of an AST object as floating point value.
     *
     *  @param astRef reference to AST object (such as an AstFrame,
     *                AstPlot etc.)
     *  @param attrib the AST attribute to return.
     *
     *  @return value of attribute
     *
     *  @deprecated Use direct ".getD" method
     */
    public static double astGetD( AstObject astRef, String attrib )
    {
        return astRef.getD( attrib );
    }

    /**
     *  Show an AST object on standard output (debugging).
     *
     *  @deprecated Use direct ".show()" method
     */
    public static void astShow( AstObject astRef )
    {
        astRef.show();
    }

    /**
     *  Transform a set of 1D positions using an AstMapping.
     *  The mapping must have nin and nout equal to 1.
     *
     *  @param mapping the AstMapping (frameset or Plot).
     *  @param points  the set of positions to transform.
     *  @param forward whether to use the forward or inverse
     *                 transform.
     *
     *  @return double [] array of transformed positions.
     *
     *  @deprecated Use direct ".tran1" method of mapping
     */
    public static double[] astTran1( Mapping mapping,
                                     double[] points,
                                     boolean forward )
    {
        return mapping.tran1( points.length, points, forward );
    }

    /**
     *  Transform a set of 2D positions using an AstMapping.
     *  The points are stored in the arrays like [x0,y0,x1,y1...].
     *
     *  @param mapping the AstMapping (frameset or Plot).
     *  @param points  the set of 2D positions to transform.
     *  @param forward whether to use the forward or inverse
     *                 transform.
     *
     *  @return double [] array of transformed positions.
     *
     *  @deprecated Use direct ".tran2" method of mapping.
     */
    public static double[][] astTran2( Mapping mapping, double[] points,
                                       boolean forward )
    {
        //  Unpack the points.
        int npoints = points.length / 2;
        double[] x = new double[npoints];
        double[] y = new double[npoints];
        int n = 0;
        for ( int i = 0; i < npoints; i++ ) {
            x[i] = points[n++];
            y[i] = points[n++];
        }
        return mapping.tran2( npoints, x, y, forward );
    }

    /**
     *  Format a coordinate value for a frame axis.
     *
     *  @param axis the axis number (start at 1).
     *  @param frame an AST frame or frameset.
     *  @param value the value to be formatted.
     *
     *  @return the formatted value.
     *
     *  @deprecated Use the direct ".format()" method of Frame
     */
    public static String astFormat( int axis, Frame frame, double value )
    {
        return frame.format( axis, value );
    }

    /**
     *  Unformat a coordinate value for a frame axis.
     *
     *  @param axis the axis number (start at 1).
     *  @param frame an AST frame or frameset.
     *  @param value the value to be unformatted.
     *
     *  @return a double precision conversion of input value.
     *
     *  @deprecated Use the direct ".unformat()" method of Frame
     */
    public static double astUnFormat( int axis, Frame frame, String value )
    {
        return frame.unformat( axis, value );
    }

    /**
     *  Extract a 1D mapping from the current AST frameset.
     *
     *  @param axis the axis whose mapping is to be extracted.
     *
     *  @return the mapping
     */
    public Mapping get1DMapping( int axis )
    {
        return get1DMapping( astRef, axis );
    }

    /**
     *  Extract a 1D mapping from a FrameSet.
     *
     *  @param frameset reference to the AST frameset.
     *  @param axis the axis whose mapping is to be extracted.
     *
     *  @return the 1D mapping
     */
    static public Mapping get1DMapping( FrameSet frameset, int axis )
        throws AstException
    {
        FrameSet framecopy = extract1DFrameSet( frameset, axis );

        // And return the new, simplified, mapping
        Mapping map1 = framecopy.getMapping( FrameSet.AST__BASE,
                                             FrameSet.AST__CURRENT );
        Mapping map2 = map1.simplify();
        return map2;
    }

    /**
     *  Extract a 1D spectral axis frameset from a makeSpectral frameset.
     *
     *  @param frameset reference to the makeSpectral frameset.
     *
     *  @return the 1D frameset
     */
    static public FrameSet get1DFrameSet( FrameSet frameset, int axis )
    {
        // TODO: make attempt to remove makeSpectral signature? When it's been
        // created from defaults?
        return extract1DFrameSet( frameset, axis );
    }

    /**
     *  Convert the current frameset into a one suitable for displaying a
     *  spectrum.
     *
     *  @param axis  the axis to select as the spectral dimension.
     *  @param start first position in input base frame coordinates (GRID)
     *  @param end   last position in input base frame coordinates (GRID)
     *  @param label label for the data units (can be blank).
     *  @param units data units (can be blank), if given these will be used to
     *               construct a FluxFrame which will be part of a
     *               SpecFluxFrame, if a SpecFrame can also be derived.
     *  @param dist  Spectral coordinates should be shown as a
     *               distance (rather than coordinate).
     *  @param dofind if true, then search for a SpecFrame using findFrame, if
     *                the spectral axis of the current frame isn't a SpecFrame.
     *
     *
     *  @return the new frameset for displaying a spectrum
     *
     *  @throws Exception if the attempt fails you may find out why
     */
    public FrameSet makeSpectral( int axis, int start, int end, String label,
                                  String units, boolean dist, boolean dofind )
        throws AstException
    {
        if ( astRef == null ) {
            return null;
        }
        FrameSet result = null;

        // Create a mapping to transform from GRID positions to spectral
        // coordinates and data values.
        // ==================================================================

        // Get a simplified mapping from the base to current frames. This will
        // be used to transform from GRID coordinates to spectral
        // coordinates. The complication is when the mapping isn't 1D.
        Mapping map = astRef.getMapping( FrameSet.AST__BASE,
                                         FrameSet.AST__CURRENT );
        Mapping smap = map.simplify();

        // Save a pointer to the current frame.
        Frame cfrm = astRef.getFrame( FrameSet.AST__CURRENT );

        // See how many axes the current frame has.
        int nax = cfrm.getNaxes();

        // And how many input coordinates the base frame needs (ideally 1).
        int nin = astRef.getNin();
        Mapping xmap = null;
        if ( nax != 1 || nin != 1 ) {

            // Multidimensional input/output. Use a LutMap for the coordinate
            // measurement along the GRID positions as we want to avoid the
            // case when just using a PermMap to lose a second axis makes the
            // mapping not invertable (i.e. for sky coordinates not providing
            // RA and DEC inputs will always give AST__BAD).

            // Get memory for transformed GRID positions.
            int dim = end - start;
            double[] grid = new double[ dim * nin ];

            // Generate dim positions stepped along the GRID axis pixels
            for ( int i = 0; i < dim; i++ ) {
                for ( int j = 0; j < nin; j++ ) {
                    grid[dim*j+i] = i + 1;
                }
            }

            // Transform these GRID positions into the current frame.
            double[] coords = smap.tranN( dim, nin, grid, true, nax );

            double[] lutcoords = new double[ dim ];
            if ( dist ) {
                // Get the distance from the first GRID position to each
                // of the others (remember the projected positions could
                // be multidimensional and we need to "remove" this by
                // converting to a distance along the projected GRID line)
                coord2Dist( cfrm, dim, nax, coords, lutcoords );
            }
            else {
                // Want coordinate, not distance so again transform from
                // coords to this measure (for same reason as above).
                coord2Oned( cfrm, axis - 1, dim, nax, coords, lutcoords );
            }

            // Create the LutMap for axis 1
            xmap = new LutMap( lutcoords, 1.0, 1.0 );
        }
        else {
            // 1D already, so the simplified mapping should do.
            xmap = smap;
        }

        //  The data units map onto themselves directly so use a unitmap.
        UnitMap unitMap = new UnitMap( 1 );

        //  Create a compound of these two mappings.
        map = new CmpMap( xmap, unitMap, false );

        // Get a Frame representing the input coordinates, uses a GRID axis of
        // the base frame and a default axis.
        int[] iaxes = new int[2];
        iaxes[0] = 1;
        iaxes[1] = 0;
        Frame frame1 = astRef.getFrame( FrameSet.AST__BASE );
        frame1 = frame1.pickAxes( iaxes.length, iaxes, null );

        // Set up label, symbol and units for axis 2 (the data values axis).
        frame1.setC( "Symbol(2)", "Data" );
        if ( ! label.equals( "" )  ) {
            frame1.setC( "Label(2)", label );
        }
        else {
            frame1.setC( "Label(2)", "Data value" );
        }
        if ( ! units.equals( "" ) ) {
            frame1.setC( "Unit(2)", units );
        }

        // Clear domain and title which are now incorrect
        frame1.clear( "Domain" );
        frame1.clear( "Title" );

        // Create the coordinate or distance versus data frame.
        // ====================================================

        // Use selected axis from the current frame as the spectral axis.
        // This may be a SpecFrame, which adds units understanding.
        Frame f1 = getSpectralAxisFrame( axis, dofind );
        boolean haveSpecFrame = f1 instanceof SpecFrame;

        // If we have data units that can be understood (as some kind of flux
        // in general), then create a FluxFrame using them, but only if we
        // also have a SpecFrame. A FluxFrame without a SpecFrame may not be
        // very useful as unit transformations will fail anyway (they require
        // a spectral coordinate, which is why you need both to create a
        // SpecFluxFrame).  Otherwise we use a plain Frame to just represent
        // the data values.
        Frame f2 = null;
        if ( haveSpecFrame &&
             ! units.equals( "" ) && ! units.equals( "unknown" ) ) {
            try {
                f2 = createFluxFrame( units, null, true );
            }
            catch (AstException e) {
                //  Units are invalid.
                f2 = null;
                logger.info( "Data units unknown: " + units  + " (" +
                             e.getMessage() + ")" );
            }
        }
        boolean haveFluxFrame = true;
        if ( f2 == null ) {
            haveFluxFrame = false;
            f2 = new Frame( 1 );
        }

        // Create the full frame using the spectral frame and flux frame.
        Frame frame2 = null;
        if ( haveSpecFrame && haveFluxFrame ) {
            frame2 = new SpecFluxFrame( (SpecFrame) f1, (FluxFrame) f2 );
        } else {
            frame2 = new CmpFrame( f1, f2 );
        }

        // Clear digits and format, unless set in the input frameset. These
        // can make a mess of SkyFrame formatting otherwise.
        if ( ! cfrm.test( "Format(" + axis + ")" ) ) {
            frame2.clear( "format(1)" );
        }
        if ( ! cfrm.test( "Digits(" + axis + ")" ) ) {
            frame2.clear( "digits(1)" );
        }

        // If using distance then set appropriate attributes
        if ( dist ) {
            frame2.setC( "Label(1)", "Offset" );
            frame2.setC( "Symbol(1)", "Offset" );
        }

        // Set symbol, label and units for second axis, if not already done.
        frame2.setC( "Symbol(2)", "Data" );
        if ( ! label.equals( "" ) ) {
            frame2.setC( "Label(2)", label );
        }
        else {
            frame2.setC( "Label(2)", "Data value" );
        }

        // If this is a FluxFrame then the units are already set (and active).
        if ( ! haveFluxFrame && ! units.equals( "" ) ) {
            frame2.setC( "Unit(2)", units );
        }

        // The domain of this frame is "DATAPLOT" (as in KAPPA)
        frame2.setC( "Domain", "DATAPLOT" );

        // Now create the output frameset, which has frame2 as current
        // and frame1 as base.
        result = new FrameSet( frame1 );
        result.addFrame( FrameSet.AST__BASE, map, frame2 );

        return result;
    }

    /**
     * Attempt to create a FluxFrame using the given units and or
     * attributes. If throwError is true then a failure results in an
     * AstException being thrown, otherwise a null is returned on failure.
     */
    public static FluxFrame createFluxFrame( String units, String attributes,
                                             boolean throwError )
        throws AstException
    {
        try {
            FluxFrame f = new FluxFrame();
            if ( units != null ) {
                f.setUnit( 1, units );
            }
            if ( attributes != null ) {
                f.set( attributes );
            }

            // Get the default System value from the FluxFrame. This will
            // depend on the units. If the units do not correspond to any of
            // the supported flux systems, then an exception will be thrown.
            String system = f.getSystem();
            return f;
        }
        catch (AstException e) {
            // Either return null or throw an exception, depends on throwError.
            if ( throwError ) {
                throw e;
            }
        }
        return null;
    }

    /**
     * Extract a spectral axis from the current FrameSet.
     * <p>
     * The return is a SpecFrame if any reason to create one can be deduced,
     * unless dofind is false, in which case the axis is just extracted.
     * <p>
     * Otherwise, if the selected axis of the current frame is a SpecFrame,
     * then that is returned, if not a search is made for a SpecFrame.
     * <p>
     * Next an attempt to create a SpecFrame is created using various
     * heuristics (from sample code provided by David Berry, these use guesses
     * from the available units). As a last resort the original Frame is
     * returned, if this is supposed to be a SpecFrame then the user will need
     * to set this manually.  
     * <p> 
     * Finally if a SpecFrame is created then an attempt to attach this to the
     * current FrameSet will be made. This makes the SpecFrame available
     * immediately in future and correctly updates the FrameSet when it is
     * written out in any way.
     */
    public Frame getSpectralAxisFrame( int axis, boolean dofind )
    {
        SpecFrame result = new SpecFrame();
        if ( astRef == null ) {
            return result;
        }

        // Pick out the axis that should be a SpecFrame.
        Frame picked = pickAxis( axis );

        // Nothing to do if this is a SpecFrame, or do find is false.
        if ( picked instanceof SpecFrame || ! dofind ) {
            return picked;
        }

        // Before pushing on, attempt to find a SpecFrame within
        // original FrameSet.
        Frame randomGuess = astRef.findFrame( result, "" );
        if ( randomGuess != null ) {
            return randomGuess;
        }

        // Take a guess at creating a spectral axis from the picked axis.
        String label = picked.getC( "Label(1)" );
        label = label.toLowerCase();
        boolean goodLabel = true;

        if ( label.indexOf( "wave" ) != -1 ) {
            // Wavelength.
            result.setC( "System", "Wave" );
        }
        else if ( label.indexOf( "freq" ) != -1 ) {
            // Frequency.
            result.setC( "System", "Freq" );
        }
        else if ( label.indexOf( "v" ) != -1 ) {
            //  Velocity of some kind.
            if ( label.indexOf( "rad" ) != -1 ) {
                // Radio velocity.
                result.setC( "System", "Vrad" );
            }
            else if ( label.indexOf( "op" ) != -1 ) {
                // Optical velocity.
                result.setC( "System", "Vopt" );
            }
            else {
                // Relativistic velocity.
                result.setC( "System", "Velo" );
            }
        }
        else if ( label.indexOf( "ener" ) != -1 ) {
            // Energy.
            result.setC( "System", "Energy" );
        }
        else if ( label.indexOf( "wavn" ) != -1 ) {
            // Wave number.
            result.setC( "System", "Wavn" );
        }
        else if ( label.indexOf( "awav" ) != -1 ) {
            // Wavelength in air.
            result.setC( "System", "Awav" );
        }
        else if ( label.indexOf( "zopt" ) != -1 ) {
            // Redshift.
            result.setC( "System", "Zopt" );
        }
        else if ( label.indexOf( "beta" ) != -1 ) {
            // Beta factor.
            result.setC( "System", "Beta" );
        }
        else {
            // Label not recognized.
            goodLabel = false;
        }

        // If the label was recognised, we need to check any units. If this
        // fails then we do not create a SpecFrame. If the label hasn't been
        // recognised then try a guess based on any units.
        // Note that we to actually use a frame to cause a check of the
        // validity of the units, hence calls to findFrame.
        String unit = picked.getC( "Unit(1)" );
        SpecFrame simpleSpecFrame = new SpecFrame();
        if ( goodLabel ) {
            if ( unit != null && ( ! unit.equals( "" ) ) ) {
                try {
                    result.setC( "Unit", unit );
                    result.findFrame( simpleSpecFrame, "" );
                }
                catch (AstException e) {
                    // Units do not match system. Use the default frame.
                    return picked;
                }
            }
        }
        else {
            // The label was not recognised. Try to derive a SpecFrame from
            // any units that are around.
            // Check the units by applying them to some likely systems.
            if ( unit == null || unit.equals( "" ) ) {
                // No units, so return the default original frame.
                return picked;
            }
            else {
                result.setC( "System", "Wave" );
                result.setC( "Unit", unit );
                if ( ! checkSpecFrame( result ) ) {
                    result.setC( "System", "Freq" );
                    result.setC( "Unit", unit );
                    if ( ! checkSpecFrame( result ) ) {
                        result.setC( "System", "Vopt" );
                        result.setC( "Unit", unit );
                        if ( ! checkSpecFrame( result ) ) {
                            // Default is the original frame. User will have
                            // to set any SpecFrame attributes interactively.
                            return picked;
                        }
                    }
                }
            }
        }

        //  Created a SpecFrame, also need to attach this to the current
        //  FrameSet.
        Frame cfrm = astRef.getFrame( FrameSet.AST__CURRENT );
        int nax = cfrm.getNaxes();
        if ( nax == 1 ) {
            astRef.addFrame( FrameSet.AST__CURRENT, new UnitMap( 1 ), result );
        }
        else {
            //  Need to pick out axes and add in our SpecFrame.
            Frame newFrame = null;
            if ( axis > 1 ) {
                // SpecFrame somewhere in middle or at top.
                int iaxes[] = new int[ nax - 1 ];
                int i = 1;
                for ( i = 1; i < axis; i++ ) {
                    iaxes[ i - 1 ] = i;
                }
                Frame part1 = astRef.pickAxes( i - 1, iaxes, null );
                newFrame = new CmpFrame( part1, result );
                if ( axis < nax ) {
                    // Not at top, more needed to get to end.
                    int j = 1;
                    for ( i = axis + 1; i <= nax; i++, j++ ) {
                        iaxes[ j - 1 ] = i;
                    }
                    Frame part3 = astRef.pickAxes( j - 1, iaxes, null );
                    newFrame = new CmpFrame( newFrame, part3 );
                }
            }
            else {
                // SpecFrame is first.
                int iaxes[] = new int[ nax ];
                int i = 1;
                for ( i = 2; i <= nax; i++ ) {
                    iaxes[ i - 1 ] = i;
                }
                Frame part2 = astRef.pickAxes( i - 2, iaxes, null );
                newFrame = new CmpFrame( result, part2 );
            }
            astRef.addFrame( FrameSet.AST__CURRENT, new UnitMap( nax ),
                             newFrame );
        }
        return result;
    }

    /**
     *   Check if a SpecFrame is valid. Use when not sure if the system and
     *   units are matched correctly.
     */
    public static boolean checkSpecFrame( SpecFrame specFrame )
    {
        boolean ok = false;
        try {
            SpecFrame copy = (SpecFrame) specFrame.copy();
            copy.clear( "Unit" );
            FrameSet fs = copy.convert( specFrame, "" );
            ok = true;
        }
        catch (AstException e) {
            ok = false;
        }
        return ok;
    }


    /**
     * Creates a FrameSet that only has one input and one output axis
     * using a given base axis as the selector.
     */
    public static FrameSet extract1DFrameSet( FrameSet frameset, int axis )
    {
        // Determine the current number of input and output axes and
        // take a copy of the input frameset
        int nin = frameset.getNin();
        int nout = frameset.getNout();
        FrameSet framecopy = (FrameSet) frameset.copy();

        // The requested axis must be valid, if not we adopt the
        // default of axis 1
        int[] iaxis = { axis };
        if ( iaxis[0] > nin ) {
            iaxis[0] = 1;
        }
        else if ( iaxis[0] < 1 ) {
            iaxis[0] = 1;
        }

        // If base frame has more than one axis then select the given
        // one.  This is easy, just pick a frame with the appropriate
        // axes and put it back, note that we have to pick the current
        // frame, so swap things around a little.
        if ( nin != 1 ) {
            framecopy.invert();
            Mapping[] joined = new Mapping[1];
            Frame tmpframe = framecopy.pickAxes( iaxis.length, iaxis, joined );
            framecopy.addFrame( FrameSet.AST__CURRENT, joined[0], tmpframe );
            framecopy.invert();
        }

        // Select an axis in the current frame and tack this onto the
        // end. Same procedure as above, just no inversion. This used
        // to attempt to pick the most significant axes, but the core
        // was actually broken and isn't repeated here.
        if ( nout != 1 ) {
            Mapping[] joined = new Mapping[1];
            Frame tmpframe = framecopy.pickAxes( iaxis.length, iaxis, joined );
            framecopy.addFrame( FrameSet.AST__CURRENT, joined[0], tmpframe );
        }
        return framecopy;
    }

    /**
     * Convert and normalize a series of coordinates measured along a
     * line (in some N-d frame coordinate system) into coordinates of
     * one of the axis of the frame (say wavelength, RA or DEC).
     *
     * @param frame the Frame
     * @param iaxis the selected axis
     * @param nax the number of axes in the frame.
     * @param dim the number of positions
     * @param pos an array holding the co-ordinates at npos positions
     *            within the supplied Frame (shape [nax, dim]).
     *     axval  The normalised coordinates
     */
    protected static void coord2Oned( Frame frame, int iaxis,
                                      int dim, int nax,
                                      double[] pos, double[] axval )
    {
        double[] work = new double[MAXDIM];

        for ( int i = 0; i < dim; i++ ) {

            /*  Copy and normalize this position */
            for ( int j = 0; j < nax; j++ ) {
                work[j] = pos[dim*j+i];
            }
            frame.norm( work );

            /*  Store the selected coordinate */
            axval[i] = work[iaxis];
        }
    }

    /**
     * Convert a series of coordinates measured along a line (in some
     * N-d frame coordinate system) into distances between the
     * positions. Note that Geodesic distances within the supplied
     * Frame are used.
     *
     * @param frame  An AST pointer to the frame.
     * @param npos   The size of the first dimension of the pos array.
     * @param nax    The number of axes in the frame.
     * @param pos    An array holding the co-ordinates at npos positions
     *               within the supplied Frame (shape [nax, npos]).
     * @param dis    The distance along the path to each position, starting
     *               at the first position.
     */
    static void coord2Dist( Frame frame, int npos, int nax, double[] pos,
                            double[] dis )
    {
        double[] p1 = new double[MAXDIM];
        double[] p2 = new double[MAXDIM];

        // Store the first position
        for ( int j = 0; j < nax; j++ ) {
            p1[j] = pos[npos*j];
        }

        // First distance is zero
        dis[0] = 0.0;

        // Now offset to all other positions
        for ( int i = 1; i < npos; i++ ) {

            // If the previous distance is known
            if ( dis[i-1] != AstObject.AST__BAD ) {

                // Store next position
                for ( int j = 0; j < nax; j++ ) {
                    p2[j] = pos[npos*j+i];
                }

                // Add distance between these positions to the
                // distance to the last position

                double inc = frame.distance( p1, p2 );
                if ( inc != AstObject.AST__BAD ) {
                    dis[i] = dis[i-1] + inc;
                }
                else {
                    dis[i] = AstObject.AST__BAD;
                }

                // This position becomes previous one
                for ( int j = 0; j < nax; j++ ) {
                    p1[j] = p2[j];
                }
            }
            else {
                // Previous distance was BAD, so this is
                dis[i] = AstObject.AST__BAD;
            }
        }
    }
}
