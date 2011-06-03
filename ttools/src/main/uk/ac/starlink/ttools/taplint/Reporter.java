package uk.ac.starlink.ttools.taplint;

import java.util.Iterator;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import uk.ac.starlink.util.CountMap;

/**
 * Handles logging for validation messages.
 *
 * @author   Mark Taylor
 */
public class Reporter {

    private final PrintStream out_;
    private final int maxRepeat_;
    private final CountMap<String> codeMap_;
    private final CountMap<Type> typeMap_;
    private final NumberFormat countFormat_;
    private final String countXx_;
    private String scode_;
    private static final int CODE_LENGTH = 4;

    public Reporter( PrintStream out, int maxRepeat ) {
        out_ = out;
        maxRepeat_ = maxRepeat;
        int maxDigit = (int) Math.ceil( Math.log10( maxRepeat_ + 1 ) );
        countFormat_ = new DecimalFormat( repeatChar( '0', maxDigit ) );
        countXx_ = repeatChar( 'x', maxDigit );
        codeMap_ = new CountMap<String>();
        typeMap_ = new CountMap<Type>();
    }

    public void startSection( String scode, String message ) {
        out_.println();
        out_.println( "Section " + scode + ": " + message );
        scode_ = scode;
    }

    public String getSectionCode() {
        return scode_;
    }

    public void summariseUnreportedMessages( String scode ) {
        String dscode = "-" + scode + "-";
        int lds = dscode.length();
        for ( Iterator<String> it = codeMap_.keySet().iterator();
              it.hasNext(); ) {
            String ecode = it.next();
            if ( dscode.equals( ecode.substring( 1, 1 + lds ) ) ) {
                int count = codeMap_.getCount( ecode );
                if ( count > maxRepeat_ ) {
                    out_.println( ecode + "-" + countXx_
                                + " (" + count + " more)" );
                }
                it.remove();
            }
        }
    }

    public void endSection() {
        scode_ = null;
    }

    public void report( Type type, String code, String message ) {
        report( type, code, message, null );
    }

    public void report( Type type, String code, String message,
                        Throwable err ) {
        if ( message == null || message.trim().length() == 0 ) {
            message = "?";
        }
        if ( code == null || code.length() == 0 ) {
            code = createCode( message );
        }
        if ( code.length() > CODE_LENGTH ) {
            code = code.substring( 0, CODE_LENGTH );
        }
        if ( code.length() < CODE_LENGTH ) {
            code += repeatChar( 'X', CODE_LENGTH - code.length() );
        }
        assert code.length() == CODE_LENGTH;
        StringBuffer codeBuf = new StringBuffer();
        codeBuf.append( type.getChar() )
               .append( '-' );
        if ( scode_ != null ) {
            codeBuf.append( scode_ )
                   .append( '-' );
        }
        codeBuf.append( code );
        String ecode = codeBuf.toString();
        typeMap_.addItem( type );
        int count = codeMap_.addItem( ecode );
        if ( count < maxRepeat_ ) {
            String fcount = countFormat_.format( count );
            String[] lines = message.trim().split( "[\\n\\r]+" );
            for ( int il = 0; il < lines.length; il++ ) {
                String line = lines[ il ];
                if ( line.trim().length() > 0 ) {
                    StringBuffer sbuf = new StringBuffer();
                    sbuf.append( ecode )
                        .append( il == 0 ? '-' : '+' )
                        .append( fcount )
                        .append( ' ' )
                        .append( lines[ il ] );
                    out_.println( sbuf.toString() );
                }
            }
        }
    }

    public void summariseUnreportedTypes( String code, Type[] types ) {
        if ( types == null ) {
            types = typeMap_.keySet().toArray( new Type[ 0 ] );
        }
        StringBuffer sbuf = new StringBuffer();
        for ( int it = 0; it < types.length; it++ ) {
            Type type = types[ it ];
            if ( it > 0 ) {
                sbuf.append( ", " );
            }
            sbuf.append( type.toString() )
                .append( ": " )
                .append( typeMap_.getCount( type ) );
        }
        report( Type.SUMMARY, code, sbuf.toString() );
    }

    public void clear() {
        codeMap_.clear();
    }

    public String createCode( String msg ) {
        int hash = msg.hashCode();
        char[] chrs = new char[ CODE_LENGTH ];
        for ( int i = 0; i < CODE_LENGTH; i++ ) {
            chrs[ i ] = (char) ( 'A' + ( ( hash & 0x1f ) % ( 'Z' - 'A' ) ) );
            hash >>= 5;
        }
        return new String( chrs );
    }

    private static String repeatChar( char chr, int count ) {
        char[] chrs = new char[ count ];
        for ( int i = 0; i < count; i++ ) {
            chrs[ i ] = chr;
        }
        return new String( chrs );
    }

    public static enum Type {

        SUMMARY( 'S' ),
        INFO( 'I' ),
        WARNING( 'W' ),
        ERROR( 'E' ),
        FAILURE( 'F' );

        private final char chr_;

        private Type( char chr ) {
            chr_ = chr;
        }

        public char getChar() {
            return chr_;
        }
    }
}
