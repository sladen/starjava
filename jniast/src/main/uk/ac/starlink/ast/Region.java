/* ********************************************************
 * This file automatically generated by Region.pl.
 *                   Do not edit.                         *
 **********************************************************/

package uk.ac.starlink.ast;


/**
 * Java interface to the AST Region class
 *  - represents a region within a coordinate system. 
 * This class provides the basic facilities for describing a region within 
 * a specified coordinate system. However, the Region class does not
 * have a constructor function of its own, as it is simply a container 
 * class for a family of specialised sub-classes such as Circle, Box, etc, 
 * which implement Regions with particular shapes.
 * <p>
 * All sub-classes of Region require a Frame to be supplied when the Region
 * is created. This Frame describes the coordinate system in which the
 * Region is defined, and is referred to as the "encapsulated Frame" below. 
 * Constructors will also typically required one or more positions to be 
 * supplied which define the location and extent of the region. These 
 * positions must be supplied within the encapsulated Frame.
 * <p>
 * The Region class inherits from the Frame class, and so a Region can be 
 * supplied where-ever a Frame is expected. In these cases, supplying a 
 * Region is equivalent to supplying a reference to its encapsulated Frame. 
 * Thus all the methods of the Frame class can be used on the Region class. 
 * For instance, the 
 * astFormat function
 * may be used on a Region to format an axis value. 
 * <p>
 * In addition, since Frame inherits from Mapping, a Region is also a sort 
 * of Mapping. Transforming positions by supplying a Region to one of the
 * astTran<X> functions
 * is the way to determine if a given position is inside or outside the 
 * Region. When used as a Mapping, most classes of Frame are equivalent to 
 * a UnitMap. However, the Region class modifies this behaviour so that a 
 * Region acts like a UnitMap only for input positions which are within the 
 * area represented by the Region. Input positions which are outside the 
 * area produce bad output values (i.e. the output values are equal to 
 * AST__BAD). This behaviour is the same for both the forward and the 
 * inverse transformation. In this sense the "inverse transformation"
 * is not a true inverse of the forward transformation, since applying
 * the forward transformation to a point outside the Region, and then
 * applying the inverse transformation results, in a set of AST__BAD axis
 * values rather than the original axis values.
 * <p>
 * If the coordinate system represented by the Region is changed (by
 * changing the values of one or more of the attribute which the Region
 * inherits from its encapsulated Frame), the area represented by 
 * the Region is mapped into the new coordinate system. For instance, let's 
 * say a Circle (a subclass of Region) is created, a SkyFrame being
 * supplied to the constructor so that the Circle describes a circular 
 * area on the sky in FK4 equatorial coordinates. Since Region inherits 
 * from Frame, the Circle will have a System attribute and this attribute 
 * will be set to "FK4". If the System attribute of the Region is then 
 * changed from FK4 to FK5, the circular area represented by the Region 
 * will automatically be mapped from the FK4 system into the FK5 system.
 * In general, changing the coordinate system in this way may result in the 
 * region changing shape - for instance, a circle may change into an 
 * ellipse if the transformation from the old to the new coordinate system 
 * is linear but with different scales on each axis. Thus the specific 
 * class of a Region cannot be used as a guarantee of the shape in any
 * particular coordinate system. If the 
 * astSimplify function
 * is used on a Region, it will endeavour to return a new Region of 
 * a sub-class which accurately describes the shape in the current
 * coordinate system of the Region (but this may not always be possible).
 * <p>
 * It is possible to negate an existing Region so that it represents all 
 * areas of the encapsulated Frame except for the area specified when 
 * the Region was created.
 * 
 * 
 * @see  <a href='http://star-www.rl.ac.uk/cgi-bin/htxserver/sun211.htx/?xref_Region'>AST Region</a>  
 */
public abstract class Region extends Frame {

    /**
     * Package-private default constructor for abstract class.
     */
    Region() {
    }
    /** 
     * Returns the bounding box of Region.   
     * This function 
     * returns the upper and lower limits of a box which just encompasses
     * the supplied Region. The limits are returned as axis values within
     * the Frame represented by the Region. The value of the Negated
     * attribute is ignored (i.e. it is assumed that the Region has not
     * been negated).
     * <h4>Notes</h4>
     *  The value of the Negated attribute is ignored (i.e. it is assumed that 
     * he Region has not been negated).
     *  If an axis has no extent on an axis then the lower limit will be
     * eturned larger than the upper limit. Note, this is different to an
     * xis which has a constant value (in which case both lower and upper 
     * imit will be returned set to the constant value).
     * 
     * @return  
     *          A two-element array of <tt>naxes</tt>-element arrays giving
     *          the bounds of a box which contains this region.  The first
     *          element is the lower bounds, and the second element is the
     *          upper bounds.  If there is no limit on a lower/upper bound
     *          on a given axis, the corresponding value will be the
     *          lowest negative/highest positive possible value.
     *       
     * @throws  AstException  if an error occurred in the AST library
     */
    public native double[][] getRegionBounds(  );

    /** 
     * Obtain a pointer to the encapsulated Frame within a Region.   
     * This function returns a pointer to the Frame represented by a
     * Region.
     * <h4>Notes</h4>
     * <br> - A null Object pointer (AST__NULL) will be returned if this
     * function is invoked with the AST error status set, or if it
     * should fail for any reason.
     * @return  A pointer to a deep copy of the Frame represented by the Region.
     * Using this pointer to modify the Frame will have no effect on
     * the Region. To modify the Region, use the Region pointer directly.
     * 
     * @throws  AstException  if an error occurred in the AST library
     */
    public native Frame getRegionFrame(  );

    /** 
     * Obtain uncertainty information from a Region.   
     * This function returns a Region which represents the uncertainty
     * associated with positions within the supplied Region. See
     * astSetUnc
     * for more information about Region uncertainties and their use.
     * <h4>Notes</h4>
     * <br> - If uncertainty information is associated with a Region, and the 
     * coordinate system described by the Region is subsequently changed
     * (e.g. by changing the value of its System attribute, or using the 
     * astMapRegion
     * function), then the uncertainty information returned by this function 
     * will be modified so that it refers to the coordinate system currently 
     * described by the supplied Region.
     * <br> - A null Object pointer (NULL) will be returned if this
     * function is invoked with the AST error status set, or if it
     * should fail for any reason.
     * 
     * @param   def
     * Controls what is returned if no uncertainty information has been
     * associated explicitly with the supplied Region. If
     * a non-zero value
     * is supplied, then the default uncertainty Region used internally 
     * within AST is returned. This will usually be a small fraction of the 
     * bounding box of the supplied Region. If
     * zero is supplied, then NULL
     * will be returned (without error).
     * 
     * @return  A pointer to a Region describing the uncertainty in the supplied
     * Region.
     * 
     * @throws  AstException  if an error occurred in the AST library
     */
    public native Region getUnc( boolean def );

    /** 
     * Transform a Region into a new Frame using a given Mapping.   
     * This function returns a pointer to a new Region which corresponds to
     * supplied Region described by some other specified coordinate system. A
     * Mapping is supplied which transforms positions between the old and new
     * coordinate systems. The new Region may not be of the same class as
     * the original region.
     * <h4>Notes</h4>
     * <br> - The uncertainty associated with the supplied Region is modified 
     * using the supplied Mapping.
     * <br> - A null Object pointer (AST__NULL) will be returned if this
     * function is invoked with the AST error status set, or if it
     * should fail for any reason.
     * @param   map
     * Pointer to a Mapping which transforms positions from the
     * coordinate system represented by the supplied Region to the 
     * coordinate system specified by 
     * "frame".
     * The supplied Mapping should define both forward and inverse 
     * transformations, and these transformations should form a genuine
     * inverse pair. That is, transforming a position using the forward
     * transformation and then using the inverse transformation should
     * produce the original input position. Some Mapping classes (such
     * as PermMap, MathMap, SphMap) can result in Mappings for which this 
     * is not true.
     * 
     * @param   frame
     * Pointer to a Frame describing the coordinate system in which 
     * the new Region is required.
     * 
     * @return  A pointer to a new Region. This Region will represent the area
     * within the coordinate system specified by
     * "frame"
     * which corresponds to the supplied Region.
     * 
     * @throws  AstException  if an error occurred in the AST library
     */
    public native Region mapRegion( Mapping map, Frame frame );

    /** 
     * Negate the area represented by a Region.   
     * This function negates the area represented by a Region. That is,
     * points which were previously inside the region will then be
     * outside, and points which were outside will be inside. This is
     * acomplished by toggling the state of the Negated attribute for
     * the supplied region.
     * 
     * @throws  AstException  if an error occurred in the AST library
     */
    public native void negate(  );

    /** 
     * Test if two regions overlap each other.   
     * This function returns an integer value indicating if the two
     * supplied Regions overlap. The two Regions are converted to a commnon
     * coordinate system before performing the check. If this conversion is 
     * not possible (for instance because the two Regions represent areas in
     * different domains), then the check cannot be performed and a zero value 
     * is returned to indicate this.
     * <h4>Notes</h4>
     * <br> - The returned values 5 and 6 do not check the value of the Closed 
     * attribute in the two Regions. 
     * <br> - A value of zero will be returned if this function is invoked with the 
     * AST error status set, or if it should fail for any reason.
     * @param   other
     * Other region for comparison with this one
     * @return  
     *          value indicating overlap status - it will be one of the
     *          OVERLAP_* static final integers defined in the Region class
     *       
     * @throws  AstException  if an error occurred in the AST library
     */
    public native int overlap( Region other );

    /** 
     * Mask a region of a data grid.   
     * This is a set of functions for masking out regions within gridded data 
     * (e.g. an image). The functions modifies a given data grid by
     * assigning a specified value to all samples which are inside (or outside 
     * if "inside" is zero) 
     * the specified Region.
     * <p>
     * You should use a masking function which matches the numerical
     * type of the data you are processing by replacing <X> in
     * the generic function name astMask<X> by an appropriate 1- or
     * 2-character type code. For example, if you are masking data
     * with type "float", you should use the function astMaskF (see
     * the "Data Type Codes" section below for the codes appropriate to
     * other numerical types).
     * <h4>Notes</h4>
     * <br> - A value of zero will be returned if this function is invoked
     * with the global error status set, or if it should fail for any
     * reason.
     * 
     * @param   map
     * Pointer to a Mapping. The forward transformation should map
     * positions in the coordinate system of the supplied Region
     * into pixel coordinates as defined by the 
     * "lbnd" and "ubnd" parameters. A NULL pointer
     * can be supplied if the coordinate system of the supplied Region 
     * corresponds to pixel coordinates. This is equivalent to
     * supplying a UnitMap.
     * <p>
     * The number of inputs for this Mapping (as given by its Nin attribute) 
     * should match the number of axes in the supplied Region (as given
     * by the Naxes attribute of the Region).
     * The number of outputs for the Mapping (as given by its Nout attribute) 
     * should match the number of
     * grid dimensions given by the value of "ndim"
     * below. 
     * 
     * @param   inside
     * A boolean value which indicates which pixel are to be masked. If 
     * a non-zero value 
     * is supplied, then all grid pixels with centres inside the supplied 
     * Region are assigned the value given by
     * "val",
     * and all other pixels are left unchanged. If 
     * zero 
     * is supplied, then all grid pixels with centres not inside the supplied 
     * Region are assigned the value given by
     * "val",
     * and all other pixels are left unchanged. Note, the Negated
     * attribute of the Region is used to determine which pixel are
     * inside the Region and which are outside. So the inside of a Region 
     * which has not been negated is the same as the outside of the 
     * corresponding negated Region.
     * <p>
     * For types of Region such as PointList which have zero volume,
     * pixel centres will rarely fall exactly within the Region. For
     * this reason, the inclusion criterion is changed for zero-volume
     * Regions so that pixels are included (or excluded) if any part of
     * the Region passes through the pixel. For a PointList, this means
     * that pixels are included (or excluded) if they contain at least
     * one of the points listed in the PointList.
     * 
     * @param   ndim
     * The number of dimensions in the input grid. This should be at
     * least one.
     * 
     * @param   lbnd
     * Pointer to an array of integers, with "ndim" elements,
     * containing the coordinates of the centre of the first pixel
     * in the input grid along each dimension.
     * 
     * @param   ubnd
     * Pointer to an array of integers, with "ndim" elements,
     * containing the coordinates of the centre of the last pixel in
     * the input grid along each dimension.
     * <p>
     * Note that "lbnd" and "ubnd" together define the shape
     * and size of the input grid, its extent along a particular
     * (j'th) dimension being ubnd[j]-lbnd[j]+1 (assuming the
     * index "j" to be zero-based). They also define
     * the input grid's coordinate system, each pixel having unit
     * extent along each dimension with integral coordinate values
     * at its centre.
     * 
     * @param   in
     * Pointer to an array, with one element for each pixel in the
     * input grid, containing the data to be masked.  The
     * numerical type of this array should match the 1- or
     * 2-character type code appended to the function name (e.g. if
     * you are using astMaskF, the type of each array element
     * should be "float").
     * <p>
     * The storage order of data within this array should be such
     * that the index of the first grid dimension varies most
     * rapidly and that of the final dimension least rapidly
     * (i.e. Fortran array indexing is used).
     * <p>
     * On exit, the samples specified by
     * "inside" are set to the value of "val".
     * All other samples are left unchanged.
     * 
     * @param   val
     * specifies the value used to flag the masked data.
     *             This should be an object of the wrapper class corresponding
     *             to the array type of the <code>in</code> array.
     *          
     * @return  The number of pixels to which a value of 
     * "badval" 
     * has been assigned.
     * 
     * @throws  AstException  if an error occurred in the AST library
     */
    public int mask( Mapping map, boolean inside, int ndim, int[] lbnd, int[] ubnd, Object in, Number val ){
        Class type = in.getClass().getComponentType();
        try {
            if ( type == byte.class ) {
                return maskB( map, inside, ndim, lbnd, ubnd, 
                              (byte[]) in, ((Byte) val).byteValue() );
            }
            else if ( type == short.class ) {
                return maskS( map, inside, ndim, lbnd, ubnd,
                              (short[]) in, ((Short) val).shortValue() );
            }
            else if ( type == int.class ) {
                return maskI( map, inside, ndim, lbnd, ubnd,
                              (int[]) in, ((Integer) val).intValue() );
            }
            else if ( type == long.class ) {
                return maskL( map, inside, ndim, lbnd, ubnd,
                              (long[]) in, ((Long) val).longValue() );
            }
            else if ( type == float.class ) {
                return maskF( map, inside, ndim, lbnd, ubnd,
                              (float[]) in, ((Float) val).floatValue() );
            }
            else if ( type == double.class ) {
                return maskD( map, inside, ndim, lbnd, ubnd,
                              (double[]) in, ((Double) val).doubleValue() );
            }
            else {
                throw new ClassCastException( "dummy ClassCastException" );
            }
        }
        catch ( ClassCastException e ) {
            throw new IllegalArgumentException( "Bad class " + in.getClass() +
                                                " for map 'in' param" );
        }
    }
    /**
     * Masking method specific to byte data.
     *
     * @see #mask
     */
    public native int maskB( Mapping map, boolean inside, int ndim,
                                    int[] lbnd, int[] ubnd,
                                    byte[] in, byte val );
    /**
     * Masking method specific to short data.
     *
     * @see #mask
     */
    public native int maskS( Mapping map, boolean inside, int ndim,
                                    int[] lbnd, int[] ubnd,
                                    short[] in, short val );
    /**
     * Masking method specific to int data.
     *
     * @see #mask
     */
    public native int maskI( Mapping map, boolean inside, int ndim,
                                    int[] lbnd, int[] ubnd,
                                    int[] in, int val );
    /**
     * Masking method specific to long data.
     *
     * @see #mask
     */
    public native int maskL( Mapping map, boolean inside, int ndim,
                                    int[] lbnd, int[] ubnd,
                                    long[] in, long val );
    /**
     * Masking method specific to float data.
     *
     * @see #mask
     */
    public native int maskF( Mapping map, boolean inside, int ndim,
                                    int[] lbnd, int[] ubnd,
                                    float[] in, float val );
    /**
     * Masking method specific to double data.
     *
     * @see #mask
     */
    public native int maskD( Mapping map, boolean inside, int ndim,
                                    int[] lbnd, int[] ubnd,
                                    double[] in, double val );

    /** No overlap could be determined because the other region could not
     *  be mapped into the coordinate system of this one. */
    public static final int OVERLAP_UNKNOWN = 0;

    /** There is no overlap between this region and the other. */
    public static final int OVERLAP_NONE = 1;

    /** This region is completely inside the other one. */
    public static final int OVERLAP_INSIDE = 2;

    /** The other region is completely inside this one. */
    public static final int OVERLAP_OUTSIDE = 3;

    /** There is partial overlap between this region and the other. */
    public static final int OVERLAP_PARTIAL = 4;

    /** The regions are identical to within their uncertainties. */
    public static final int OVERLAP_SAME = 5;

    /** The other region is the exact negation of this one to within 
     *  their uncertainties. */
    public static final int OVERLAP_NEGATE = 6;
    /** 
     * Store uncertainty information in a Region.   
     * Each Region (of any class) can have an "uncertainty" which specifies 
     * the uncertainties associated with the boundary of the Region. This
     * information is supplied in the form of a second Region. The uncertainty 
     * in any point on the boundary of a Region is found by shifting the 
     * associated "uncertainty" Region so that it is centred at the boundary 
     * point being considered. The area covered by the shifted uncertainty 
     * Region then represents the uncertainty in the boundary position. 
     * The uncertainty is assumed to be the same for all points.
     * <p>
     * The uncertainty is usually specified when the Region is created, but
     * this
     * function
     * allows it to be changed at any time. 
     * 
     * @param   unc
     * Pointer to the new uncertainty Region. This must be of a class for
     * which all instances are centro-symetric (e.g. Box, Circle, Ellipse, 
     * etc.) or be a Prism containing centro-symetric component Regions.
     * A deep copy of the supplied Region will be taken, so subsequent 
     * changes to the uncertainty Region using the supplied pointer will 
     * have no effect on the Region 
     * "this".
     * 
     * @throws  AstException  if an error occurred in the AST library
     */
    public native void setUnc( Region unc );

    /**
     * Get 
     * should the area adapt to changes in the coordinate system.  
     * The coordinate system represented by a Region may be changed by
     * assigning new values to attributes such as System, Unit, etc.
     * For instance, a Region representing an area on the sky in ICRS 
     * coordinates may have its System attribute changed so that it
     * represents (say) Galactic coordinates instead of ICRS. This
     * attribute controls what happens when the coordinate system
     * represented by a Region is changed in this way.
     * <p>
     * If Adaptive is non-zero (the default), then area represented by the
     * Region adapts to the new coordinate system. That is, the numerical 
     * values which define the area represented by the Region are changed 
     * by mapping them from the old coordinate system into the new coordinate 
     * system. Thus the Region continues to represent the same physical
     * area.
     * <p>
     * If Adaptive is zero, then area represented by the Region does not adapt
     * to the new coordinate system. That is, the numerical values which
     * define the area represented by the Region are left unchanged. Thus 
     * the physical area represented by the Region will usually change.
     * <p>
     * As an example, consider a Region describe a range of wavelength from
     * 2000 Angstrom to 4000 Angstrom. If the Unit attribute for the Region 
     * is changed from Angstrom to "nm" (nanometre), what happens depends
     * on the setting of Adaptive. If Adaptive is non-zero, the Mapping
     * from the old to the new coordinate system is found. In this case it
     * is a simple scaling by a factor of 0.1 (since 1 Angstrom is 0.1 nm).
     * This Mapping is then used to modify the numerical values within the
     * Region, changing 2000 to 200 and 4000 to 400. Thus the modified
     * region represents 200 nm to 400 nm, the same physical space as 
     * the original 2000 Angstrom to 4000 Angstrom. However, if Adaptive 
     * had been zero, then the numerical values would not have been changed,
     * resulting in the final Region representing 2000 nm to 4000 nm.
     * <p>
     * Setting Adaptive to zero can be necessary if you want correct
     * inaccurate attribute settings in an existing Region. For instance,
     * when creating a Region you may not know what Epoch value to use, so
     * you would leave Epoch unset resulting in some default value being used.
     * If at some later point in the application, the correct Epoch value
     * is determined, you could assign the correct value to the Epoch
     * attribute. However, you would first need to set Adaptive temporarily 
     * to zero, because otherwise the area represented by the Region would
     * be Mapped from the spurious default Epoch to the new correct Epoch,
     * which is not what is required.
     * 
     *
     * @return  this object's Adaptive attribute
     */
    public boolean getAdaptive() {
        return getB( "Adaptive" );
    }

    /**
     * Set 
     * should the area adapt to changes in the coordinate system.  
     * The coordinate system represented by a Region may be changed by
     * assigning new values to attributes such as System, Unit, etc.
     * For instance, a Region representing an area on the sky in ICRS 
     * coordinates may have its System attribute changed so that it
     * represents (say) Galactic coordinates instead of ICRS. This
     * attribute controls what happens when the coordinate system
     * represented by a Region is changed in this way.
     * <p>
     * If Adaptive is non-zero (the default), then area represented by the
     * Region adapts to the new coordinate system. That is, the numerical 
     * values which define the area represented by the Region are changed 
     * by mapping them from the old coordinate system into the new coordinate 
     * system. Thus the Region continues to represent the same physical
     * area.
     * <p>
     * If Adaptive is zero, then area represented by the Region does not adapt
     * to the new coordinate system. That is, the numerical values which
     * define the area represented by the Region are left unchanged. Thus 
     * the physical area represented by the Region will usually change.
     * <p>
     * As an example, consider a Region describe a range of wavelength from
     * 2000 Angstrom to 4000 Angstrom. If the Unit attribute for the Region 
     * is changed from Angstrom to "nm" (nanometre), what happens depends
     * on the setting of Adaptive. If Adaptive is non-zero, the Mapping
     * from the old to the new coordinate system is found. In this case it
     * is a simple scaling by a factor of 0.1 (since 1 Angstrom is 0.1 nm).
     * This Mapping is then used to modify the numerical values within the
     * Region, changing 2000 to 200 and 4000 to 400. Thus the modified
     * region represents 200 nm to 400 nm, the same physical space as 
     * the original 2000 Angstrom to 4000 Angstrom. However, if Adaptive 
     * had been zero, then the numerical values would not have been changed,
     * resulting in the final Region representing 2000 nm to 4000 nm.
     * <p>
     * Setting Adaptive to zero can be necessary if you want correct
     * inaccurate attribute settings in an existing Region. For instance,
     * when creating a Region you may not know what Epoch value to use, so
     * you would leave Epoch unset resulting in some default value being used.
     * If at some later point in the application, the correct Epoch value
     * is determined, you could assign the correct value to the Epoch
     * attribute. However, you would first need to set Adaptive temporarily 
     * to zero, because otherwise the area represented by the Region would
     * be Mapped from the spurious default Epoch to the new correct Epoch,
     * which is not what is required.
     * 
     *
     * @param  adaptive   the Adaptive attribute of this object
     */
    public void setAdaptive( boolean adaptive ) {
       setB( "Adaptive", adaptive );
    }

    /**
     * Get 
     * region negation flag.  
     * This attribute controls whether a Region represents the "inside" or
     * the "outside" of the area which was supplied when the Region was
     * created. If the attribute value is zero (the default), the Region
     * represents the inside of the original area. However, if it is non-zero, 
     * it represents the outside of the original area. The value of this
     * attribute may be toggled using the 
     * astNegate function.
     * <p>
     * Note, whether the boundary is considered to be inside the Region or
     * not is controlled by the Closed attribute. Changing the value of
     * the Negated attribute does not change the value of the Closed attribute.
     * Thus, if Region is closed, then the boundary of the Region will be 
     * inside the Region, whatever the setting of the Negated attribute.
     * 
     *
     * @return  this object's Negated attribute
     */
    public boolean getNegated() {
        return getB( "Negated" );
    }

    /**
     * Set 
     * region negation flag.  
     * This attribute controls whether a Region represents the "inside" or
     * the "outside" of the area which was supplied when the Region was
     * created. If the attribute value is zero (the default), the Region
     * represents the inside of the original area. However, if it is non-zero, 
     * it represents the outside of the original area. The value of this
     * attribute may be toggled using the 
     * astNegate function.
     * <p>
     * Note, whether the boundary is considered to be inside the Region or
     * not is controlled by the Closed attribute. Changing the value of
     * the Negated attribute does not change the value of the Closed attribute.
     * Thus, if Region is closed, then the boundary of the Region will be 
     * inside the Region, whatever the setting of the Negated attribute.
     * 
     *
     * @param  negated   the Negated attribute of this object
     */
    public void setNegated( boolean negated ) {
       setB( "Negated", negated );
    }

    /**
     * Get 
     * should the boundary be considered to be inside the region.  
     * This attribute controls whether points on the boundary of a Region 
     * are considered to be inside or outside the region. If the attribute 
     * value is non-zero (the default), points on the boundary are considered 
     * to be inside the region (that is, the Region is "closed"). However, 
     * if the attribute value is zero, points on the bounary are considered
     * to be outside the region.
     * 
     *
     * @return  this object's Closed attribute
     */
    public boolean getClosed() {
        return getB( "Closed" );
    }

    /**
     * Set 
     * should the boundary be considered to be inside the region.  
     * This attribute controls whether points on the boundary of a Region 
     * are considered to be inside or outside the region. If the attribute 
     * value is non-zero (the default), points on the boundary are considered 
     * to be inside the region (that is, the Region is "closed"). However, 
     * if the attribute value is zero, points on the bounary are considered
     * to be outside the region.
     * 
     *
     * @param  closed   the Closed attribute of this object
     */
    public void setClosed( boolean closed ) {
       setB( "Closed", closed );
    }

    /**
     * Get 
     * number of points used to represent the boundary of a Region.  
     * This attribute controls how many points are used when creating a
     * mesh of points covering the boundary of a Region. This mesh is used
     * primarily when testing for overlap with a second Region: each point in 
     * the mesh is checked to see if it is inside or outside the second Region.
     * Thus, the reliability of the overlap check depends on the value assigned 
     * to this attribute. If the value used is very low, it is possible for 
     * overlaps to go unnoticed. High values produce more reliable results, but 
     * can result in the overlap test being very slow. The default value is 200 
     * for two dimensional Regions and 2000 for three or more dimensional 
     * Regions (this attribute is not used for 1-dimensional regions since the 
     * boundary of a simple 1-d Region can only ever have two points). A
     * value of five is used if the supplied value is less than five.
     * 
     *
     * @return  this object's MeshSize attribute
     */
    public int getMeshSize() {
        return getI( "MeshSize" );
    }

    /**
     * Set 
     * number of points used to represent the boundary of a Region.  
     * This attribute controls how many points are used when creating a
     * mesh of points covering the boundary of a Region. This mesh is used
     * primarily when testing for overlap with a second Region: each point in 
     * the mesh is checked to see if it is inside or outside the second Region.
     * Thus, the reliability of the overlap check depends on the value assigned 
     * to this attribute. If the value used is very low, it is possible for 
     * overlaps to go unnoticed. High values produce more reliable results, but 
     * can result in the overlap test being very slow. The default value is 200 
     * for two dimensional Regions and 2000 for three or more dimensional 
     * Regions (this attribute is not used for 1-dimensional regions since the 
     * boundary of a simple 1-d Region can only ever have two points). A
     * value of five is used if the supplied value is less than five.
     * 
     *
     * @param  meshSize   the MeshSize attribute of this object
     */
    public void setMeshSize( int meshSize ) {
       setI( "MeshSize", meshSize );
    }

    /**
     * Get 
     * fraction of the Region which is of interest.  
     * This attribute indicates the fraction of the Region which is of
     * interest. AST does not use this attribute internally for any purpose.
     * Typically, it could be used to indicate the fraction of the Region for
     * which data is available.
     * <p>
     * The supplied value must be in the range 0.0 to 1.0, and the default
     * value is 1.0 (except as noted below).
     * 
     *
     * @return  this object's FillFactor attribute
     */
    public double getFillFactor() {
        return getD( "FillFactor" );
    }

    /**
     * Set 
     * fraction of the Region which is of interest.  
     * This attribute indicates the fraction of the Region which is of
     * interest. AST does not use this attribute internally for any purpose.
     * Typically, it could be used to indicate the fraction of the Region for
     * which data is available.
     * <p>
     * The supplied value must be in the range 0.0 to 1.0, and the default
     * value is 1.0 (except as noted below).
     * 
     *
     * @param  fillFactor   the FillFactor attribute of this object
     */
    public void setFillFactor( double fillFactor ) {
       setD( "FillFactor", fillFactor );
    }

    /**
     * Get 
     * is the Region bounded.  
     * This is a read-only attribute indicating if the Region is bounded.
     * A Region is bounded if it is contained entirely within some
     * finite-size bounding box.
     * 
     *
     * @return  this object's Bounded attribute
     */
    public boolean getBounded() {
        return getB( "Bounded" );
    }

}