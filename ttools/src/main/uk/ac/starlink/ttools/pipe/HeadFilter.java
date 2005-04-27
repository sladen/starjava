package uk.ac.starlink.ttools.pipe;

import java.util.Iterator;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.ttools.ArgException;

/**
 * Filter for picking only the first few rows of a table.
 *
 * @author   Mark Taylor (Starlink)
 * @since    7 Mar 2005
 */
public class HeadFilter implements ProcessingFilter {

    public String getName() {
        return "head";
    }

    public String getFilterUsage() {
        return "<nrows>";
    }

    public ProcessingStep createStep( Iterator argIt ) throws ArgException {
        if ( argIt.hasNext() ) {
            String countStr = (String) argIt.next();
            argIt.remove();
            long count;
            try {
                count = Long.parseLong( countStr );
            }
            catch ( NumberFormatException e ) {
                throw new ArgException( "Row count " + countStr + 
                                        " not numeric" );
            }
            if ( count < 0 ) {
                throw new ArgException( "Nrows must be >= 0" );
            }
            return new HeadStep( count );
        }
        else {
            throw new ArgException( "No row count given" );
        }
    }

    private static class HeadStep implements ProcessingStep {
        final long count_;

        HeadStep( long count ) {
            count_ = count;
        }

        public StarTable wrap( StarTable base ) {
            return new HeadTable( base, count_ );
        }
    }
}
