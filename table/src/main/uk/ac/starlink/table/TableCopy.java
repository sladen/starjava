package uk.ac.starlink.table;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.starlink.util.Loader;

/**
 * Class containing <tt>main</tt> method for copying tables.
 *
 * @author   Mark Taylor (Starlink)
 */
public class TableCopy {

    /**
     * Copies a table from one format to another.
     * See usage message (invoke with '-help') for details.
     */
    public static void main( String[] args ) throws IOException {

        /* Ensure that the properties are loaded; this may contain JDBC
         * configuration which should be got before DriverManager might be
         * initialised. */
        Loader.loadProperties();

        /* Get the factory objects. */
        StarTableFactory treader = new StarTableFactory( false );
        StarTableOutput twriter = new StarTableOutput();

        /* Construct the usage message. */
        String cmdname = System.getProperty( "uk.ac.starlink.table.cmdname" );
        if ( cmdname == null ) {
            cmdname = "TableCopy";
        }
        String usage = new StringBuffer()
             .append( "\nUsage: " )
             .append( cmdname )
             .append( " [-disk]" )
             .append( " [-debug]" )
             .append( " [-h[elp]]" )
             .append( " [-v[erbose]]" )
             .append( "\n                " )
             .append( " [-ifmt <in-format>]" )
             .append( " [-ofmt <out-format>]" )
             .append( "\n                " )
             .append( " <in-table> <out-table>\n" )
             .toString();

        /* Construct the help message. */
        StringBuffer help = new StringBuffer( usage );
        help.append( "\n   Known in-formats:\n" );
        for ( Iterator it = treader.getKnownFormats().iterator();
              it.hasNext(); ) {
            help.append( "      " )
                .append( ( (String) it.next() ).toLowerCase() )
                .append( "\n" );
        }
        help.append( "\n   Known out-formats:\n" );
        for ( Iterator it = twriter.getKnownFormats().iterator(); 
              it.hasNext(); ) {
            help.append( "      " )
                .append( ( (String) it.next() ).toLowerCase() )
                .append( "\n" );
        }

        /* Process the command line arguments. */
        String ifmt = null;
        String ofmt = null;
        String iloc = null;
        String oloc = null;
        boolean debug = false;
        boolean verbose = false;
        for ( Iterator it = Arrays.asList( args ).iterator(); it.hasNext(); ) {
            String arg = (String) it.next();
            if ( arg.startsWith( "-" ) && arg.length() > 1 ) {
                if ( arg.equals( "-ifmt" ) ) {
                    if ( it.hasNext() ) {
                        ifmt = (String) it.next();
                    }
                    else {
                        System.err.println( usage );
                        System.exit( 1 );
                    }
                }
                else if ( arg.equals( "-ofmt" ) ) {
                    if ( it.hasNext() ) {
                        ofmt = (String) it.next();
                    }
                    else {
                        System.err.println( usage );
                        System.exit( 1 );
                    }
                }
                else if ( arg.equals( "-disk" ) ) {
                    treader.setStoragePolicy( StoragePolicy.PREFER_DISK );
                }
                else if ( arg.equals( "-debug" ) ) {
                    debug = true;
                }
                else if ( arg.equals( "-v" ) || arg.equals( "-verbose" ) ) {
                    verbose = true;
                }
                else if ( arg.equals( "-h" ) || arg.equals( "-help" ) ) {
                    System.out.println( help );
                    System.exit( 0 );
                }
                else {
                    System.err.println( usage );
                    System.exit( 1 );
                }
            }
            else if ( iloc == null ) {
                iloc = arg;
            }
            else if ( oloc == null ) {
                oloc = arg;
            }
            else {
                System.err.println( usage );
                System.exit( 1 );
            }
        }

        /* Adjust logging level if necessary. */
        if ( verbose ) {
            Logger.getLogger( "uk.ac.starlink" ).setLevel( Level.INFO );
        }

        /* Check we have input and output table locations. */
        if ( iloc == null || oloc == null ) {
            System.err.println( usage );
            System.exit( 1 );
        }

        /* Get the input table. */
        try {
            StarTable startab = treader.makeStarTable( iloc, ifmt );
            if ( verbose ) {
                startab = new ProgressLineStarTable( startab, System.err );
            }

            /* Write it to the requested destination. */
            twriter.writeStarTable( startab, oloc, ofmt );

        /* In the event of an error getting the format, write it out 
         * without the (user-intimidating) stacktrace. */
        }
        catch ( IOException e ) {
            if ( debug ) {
                e.printStackTrace( System.err );
            }
            else {
                System.err.println();
                System.err.println( e.getMessage() );
                System.err.println( usage );
            }
            System.exit( 1 );
        }
    }
 
}
