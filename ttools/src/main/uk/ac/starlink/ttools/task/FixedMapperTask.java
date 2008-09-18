package uk.ac.starlink.ttools.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uk.ac.starlink.task.Environment;
import uk.ac.starlink.task.Parameter;
import uk.ac.starlink.task.TaskException;
import uk.ac.starlink.ttools.mode.ProcessingMode;

/**
 * MapperTask which has a fixed number of input tables.
 * Each input table gets its own numbered table parameter and (if requested)
 * filter parameter - in1, in2, in3, ... and icmd1, icmd2, icmd3, ...
 * 
 * @author   Mark Taylor
 * @since    14 Sep 2006
 */
public class FixedMapperTask extends MapperTask {

    private final InputTableParameter[] inTableParams_;
    private final FilterParameter[] inFilterParams_;

    /**
     * Constructor.
     *
     * @param   purpose  one-line description of the purpose of the task
     * @param   outMode  processing mode which determines the destination of
     *          the processed table
     * @param   useOutFilter allow specification of filters for output table
     * @param   mapper   object which defines mapping transformation
     * @param   nIn      number of input tables
     * @param   useInFilters  whether to use input filter parameters
     */
    public FixedMapperTask( String purpose, ProcessingMode outMode,
                            boolean useOutFilter, TableMapper mapper,
                            int nIn, boolean useInFilters ) {
        super( purpose, outMode, useOutFilter, mapper );
        List paramList = new ArrayList();

        /* Input table parameters. */
        inTableParams_ = new InputTableParameter[ nIn ];
        for ( int i = 0; i < nIn; i++ ) {
            int i1 = i + 1;
            String ord = getOrdinal( i1 );
            InputTableParameter inParam = new InputTableParameter( "in" + i1 );
            inTableParams_[ i ] = inParam;
            inParam.setPosition( i1 );
            inParam.setUsage( "<table" + i1 + ">" );
            inParam.setPrompt( "Location of " + ord + " input table" );
            inParam.setDescription( inParam.getDescription()
                                   .replaceFirst( "the input table",
                                                  "the " + ord +
                                                  " input table" ) );
            InputFormatParameter fmtParam = inParam.getFormatParameter();
            fmtParam.setDescription( fmtParam.getDescription()
                                    .replaceFirst( "the input table",
                                                   "the " + ord +
                                                   " input table" ) );
            paramList.add( fmtParam );
            paramList.add( inParam );
        }

        /* Input filter parameters. */
        inFilterParams_ = new FilterParameter[ nIn ];
        if ( useInFilters ) {
            for ( int i = 0; i < nIn; i++ ) {
                int i1 = i + 1;
                FilterParameter fp = new FilterParameter( "icmd" + i1 );
                inFilterParams_[ i ] = fp;
                fp.setPrompt( "Processing command(s) for input table " + i1 );
                fp.setDescription( new String[] {
                    "<p>Commands to operate on the " + getOrdinal( i1 ),
                    "input table, before any other processing takes place.",
                    "</p>",
                    fp.getDescription(),
                } );
                paramList.add( fp );
            }
        }

        /* Store full parameter list. */
        getParameterList().addAll( 0, paramList );
    }

    protected InputTableSpec[] getInputSpecs( Environment env )
            throws TaskException {
        int nIn = inTableParams_.length;
        InputTableSpec[] specs = new InputTableSpec[ nIn ];
        for ( int i = 0; i < nIn; i++ ) {
            InputTableParameter tableParam = inTableParams_[ i ];
            FilterParameter filterParam = inFilterParams_[ i ];
            specs[ i ] =
                InputTableSpec
               .createSpec( tableParam.stringValue( env ),
                            filterParam == null ? null
                                                : filterParam.stepsValue( env ),
                            tableParam.tableValue( env ) );
        }
        return specs;
    }

    /**
     * Returns the string representation of the ordinal number corresponding
     * to a given integer.
     *
     * @param  i  number
     * @return   ordinal
     */
    private static String getOrdinal( int i ) {
        switch ( i ) {
            case 1: return "first";
            case 2: return "second";
            case 3: return "third";
            case 4: return "fourth";
            case 5: return "fifth";
            default: return "next";
        }
    }
}