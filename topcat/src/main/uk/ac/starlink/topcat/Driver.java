package uk.ac.starlink.topcat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.Tables;
import uk.ac.starlink.table.WrapperStarTable;
import uk.ac.starlink.util.ErrorDialog;
import uk.ac.starlink.util.DataSource;
import uk.ac.starlink.util.Loader;
import uk.ac.starlink.util.URLDataSource;

/**
 * Main class for invoking the TOPCAT application from scratch.
 * Contains some useful static configuration-type methods as well
 * as the {@link #main} method itself.
 *
 * @author   Mark Taylor (Starlink)
 * @since    9 Mar 2004
 */
public class Driver {

    private static boolean standalone = false;
    private static boolean securityChecked;
    private static Boolean canRead;
    private static Boolean canWrite;
    private static StarTable[] demoTables;
    private static Logger logger = Logger.getLogger( "uk.ac.starlink.topcat" );
    private static StarTableFactory tabfact = new StarTableFactory( true );
    private static ControlWindow control;

    /**
     * Determines whether TableViewers associated with this class should
     * act as a standalone application.  If <tt>standalone</tt> is set
     * true, then it will be possible to exit the JVM using menu items
     * etc in the viewer.  Otherwise, no normal activity within the
     * TableViewer GUI will cause a JVM exit.
     *
     * @param  standalone  whether this class should act as a standalone
     *         application
     */
    public static void setStandalone( boolean standalone ) {
        Driver.standalone = standalone;
    }

    /**
     * Indicates whether the TableViewer application is standalone or not.
     *
     * @return  whether this should act as a standalone application.
     */
    public static boolean isStandalone() {
        return standalone;
    }

    /**
     * Indicates whether the security context will permit reads from local
     * disk.
     *
     * @return  true iff reads are permitted
     */
    public static boolean canRead() {
        checkSecurity();
        return canRead.booleanValue();
    }

    /**
     * Indicates whether the security context will permit writes to local
     * disk.
     *
     * @return  true iff writes are permitted
     */
    public static boolean canWrite() {
        checkSecurity();
        return canWrite.booleanValue();
    }

    /**
     * Talks to the installed security manager to find out what is and
     * is not permitted.
     */
    private static void checkSecurity() {
        if ( ! securityChecked ) {
            SecurityManager sman = System.getSecurityManager();
            if ( sman == null ) {
                canRead = Boolean.TRUE;
                canWrite = Boolean.TRUE;
            }
            else {
                String readDir;
                String writeDir;
                try { 
                    readDir = System.getProperty( "user.home" );
                }
                catch ( SecurityException e ) {
                    readDir = ".";
                }
                try {
                    writeDir = System.getProperty( "java.io.tmpdir" );
                }
                catch ( SecurityException e ) {
                    writeDir = ".";
                }
                try {
                    sman.checkRead( readDir );
                    canRead = Boolean.TRUE;
                }
                catch ( SecurityException e ) {
                    canRead = Boolean.FALSE;
                }
                try {
                    sman.checkWrite( new File( writeDir, "tOpCTeSt.tmp" )
                                    .toString() );
                    canWrite = Boolean.TRUE;
                }
                catch ( SecurityException e ) {
                    canWrite = Boolean.FALSE;
                }
            }
            assert canRead != null;
            assert canWrite != null;
        }
    }

    /**
     * Main method for TOPCAT invocation.
     * Under normal circumstances this will pop up a ControlWindow and
     * populate it with tables named in the arguments.
     *
     * @param  args  list of table specifications
     */
    public static void main( String[] args ) {
        String cmdname;
        try {
            Loader.loadProperties();
            cmdname = System.getProperty( "uk.ac.starlink.topcat.cmdname" );
        }
        catch ( SecurityException e ) {
            // never mind
            cmdname = null;
        }
        if ( cmdname == null ) {
            cmdname = Driver.class.getName();
        }
        String usage = new StringBuffer()
              .append( "Usage:\n" )
              .append( "   " + cmdname + "[-demo] [table ...]\n" )
              .toString();
        setStandalone( true );

        /* Process flags.
         * If all we need to do is print a usage message and exit, do it 
         * directly without any GUI action. */
        boolean demo = false;
        for ( int i = 0; i < args.length; i++ ) {
            if ( args[ i ].startsWith( "-h" ) ) {
                System.out.println( usage );
                System.exit( 0 );
            }
            else if ( args[ i ].equals( "-demo" ) ) {
                demo = true;
                args[ i ] = null;
            }
            else if ( args[ i ].startsWith( "-" ) ) {
                System.err.println( usage );
                System.exit( 1 );
            }
        }

        /* Start up the GUI now. */
        getControlWindow();

        /* Start up with demo data if requested. */
        if ( demo ) {
            StarTable[] demoTables = getDemoTables();
            for ( int i = 0; i < demoTables.length; i++ ) {
                StarTable table = demoTables[ i ];
                if ( table != null ) {
                    addTableLater( table, "[Demo]:" + table.getName() );
                }
            }
        }

        /* Try to interpret each command line argument as a table
         * specification. */
        for ( int i = 0; i < args.length; i++ ) {
            final String arg = args[ i ];
            if ( arg != null ) {
                try {
                    StarTable startab = tabfact.makeStarTable( arg );
                    if ( startab == null ) {
                        System.err.println( "No table \"" + arg + "\"" );
                    }
                    else {
                        addTableLater( Tables.randomTable( startab ), arg );
                    }
                }
                catch ( final Exception e ) {
                    final String msg = "Can't open table \"" + arg + "\"";
                    System.err.println( msg );
                    SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            ErrorDialog.showError( e, msg, getControlWindow() );
                        }
                    } );
                }
            }
        }
    }

    /**
     * Returns the ControlWindow used by this application.  It is
     * constructed lazily, which means if it's never needed (say if 
     * we're just printing a usage message), the GUI
     * never has to start up.
     *
     * @return  control window
     */
    private static ControlWindow getControlWindow() {
        if ( control == null ) {
            control = ControlWindow.getInstance();
            control.setTableFactory( tabfact );
        }
        return control;
    }

    /**
     * Schedules a table for posting to the Control Window in the event
     * dispatch thread.  
     *
     * @param  table  the table to add
     * @param  location  location string indicating the provenance of
     *         <tt>table</tt> - preferably a URL or filename or something
     */
    private static void addTableLater( final StarTable table,
                                       final String location ) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                getControlWindow().addTable( table, location, false );
            }
        } );
    }

    /**
     * Returns a set of example StarTables suitable for demonstration
     * purposes.  They will all have random access.
     * If one of the demo tables can't be created for some
     * reason (e.g. the required resource is missing) the corresponding
     * element in the returned array will be <tt>null</tt>.
     *
     * @return  array of demo tables
     */
    static StarTable[] getDemoTables() {
        String base = LoadQueryWindow.DEMO_LOCATION + '/';
        String[] demoNames = new String[] {
            "863sub.fits",
            "vizier.xml.gz#6",
            "cover.xml",
            "tables.fit.gz#2",
        };
        int ntab = demoNames.length;
        if ( demoTables == null ) {
            demoTables = new StarTable[ ntab ];
            for ( int i = 0; i < ntab; i++ ) {
                final String demoName = demoNames[ i ];
                try {
                    int fragIndex = demoName.indexOf( '#' );
                    String name; 
                    String frag;
                    if ( fragIndex > 0 ) {
                        name = demoName.substring( 0, fragIndex );
                        frag = demoName.substring( fragIndex + 1 );
                    }
                    else {
                        name = demoName;
                        frag = null;
                    }
                    URL url = Driver.class.getClassLoader()
                                    .getResource( base + name );
                    if ( url != null ) {
                        DataSource datsrc = 
                            DataSource.makeDataSource( url.toString() );
                        if ( frag != null ) {
                            datsrc.setPosition( frag );
                        }
                        StarTable table = tabfact.makeStarTable( datsrc );
                        table = new WrapperStarTable( table ) {
                            public String getName() {
                                return demoName;
                            }
                        };
                        demoTables[ i ] = Tables.randomTable( table );
                    }
                    else {
                        logger.warning( "Demo table resource not located: " +
                                        base + demoName );
                    }
                }
                catch ( IOException e ) {
                    logger.warning( "Demo table " + demoName + " not loaded: "
                                  + e.toString() );
                }
            }
        }
        return demoTables;
    }
}
