package uk.ac.starlink.ttools.build;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xml.sax.SAXException;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.StarTableOutput;
import uk.ac.starlink.table.WrapperRowSequence;
import uk.ac.starlink.table.WrapperStarTable;
import uk.ac.starlink.task.DoubleParameter;
import uk.ac.starlink.task.IntegerParameter;
import uk.ac.starlink.task.InvokeUtils;
import uk.ac.starlink.task.Parameter;
import uk.ac.starlink.task.Task;
import uk.ac.starlink.ttools.Formatter;
import uk.ac.starlink.ttools.Stilts;
import uk.ac.starlink.ttools.filter.ProcessingFilter;
import uk.ac.starlink.ttools.filter.StepFactory;
import uk.ac.starlink.ttools.mode.ProcessingMode;
import uk.ac.starlink.ttools.task.Calc;
import uk.ac.starlink.ttools.task.ConsumerTask;
import uk.ac.starlink.ttools.task.MapEnvironment;
import uk.ac.starlink.util.LoadException;
import uk.ac.starlink.util.ObjectFactory;

/**
 * Writes a Jython module which facilitates use of STILTS functionality
 * from Jython.
 * The <code>main</code> method will write the jython source code
 * to standard output.
 *
 * @author   Mark Taylor
 * @since    12 Feb 2010
 */
public class JyStilts {

    private final Stilts stilts_;
    private final Formatter formatter_;
    private final Map clazzMap_;
    private final String[] imports_;
    private final Map paramAliasMap_;
    private static final String paramAliasDictName_ = "_param_alias_dict";

    /** Java classes which are used by python source code. */
    private static final Class[] IMPORT_CLASSES = new Class[] {
        java.lang.System.class,
        java.lang.reflect.Array.class,
        java.util.ArrayList.class,
        uk.ac.starlink.table.StarTable.class,
        uk.ac.starlink.table.StarTableFactory.class,
        uk.ac.starlink.table.StarTableOutput.class,
        uk.ac.starlink.table.WrapperStarTable.class,
        uk.ac.starlink.table.WrapperRowSequence.class,
        uk.ac.starlink.task.InvokeUtils.class,
        uk.ac.starlink.ttools.Stilts.class,
        uk.ac.starlink.ttools.filter.StepFactory.class,
        uk.ac.starlink.ttools.task.MapEnvironment.class,
    };

    /**
     * Constructor.
     *
     * @param  stilts  stilts instance defining available tasks etc
     */
    public JyStilts( Stilts stilts ) {
        stilts_ = stilts;
        formatter_ = new Formatter();

        /* Prepare python imports. */
        clazzMap_ = new HashMap();
        Class[] clazzes = IMPORT_CLASSES;
        List importList = new ArrayList();
        importList.add( "import jarray.array" );
        for ( int ic = 0; ic < clazzes.length; ic++ ) {
            Class clazz = clazzes[ ic ];
            String importName = clazz.getName();
            importList.add( "import " + importName );
            clazzMap_.put( clazz, importName );
        }
        imports_ = (String[]) importList.toArray( new String[ 0 ] );

        /* Some parameter names need to be aliased because they are python
         * reserved words. */
        paramAliasMap_ = new HashMap();
        paramAliasMap_.put( "in", "in_" );
    }

    /**
     * Generates python source giving module header lines.
     *
     * @return  python source code lines
     */
    private String[] header() {
        return new String[] {
            "'''stilts allows access to STILTS commands.",
            "",
            "See the manual, http://www.starlink.ac.uk/stilts/sun256/",
            "for full information about the various commands.",
            "'''",
            "",
        };
    }

    /**
     * Generates python source giving statements which
     * import java classes required for the rest of the python source
     * generated by this class.
     *
     * @return  python source code lines
     */
    private String[] imports() {
        return imports_;
    }

    /**
     * Returns the python name under which a given Java class has been
     * imported into python for use by this class.
     *
     * @param  clazz  java class
     * @return  python name for <code>clazz</code>
     */
    private String getImportName( Class clazz ) {
        String cname = (String) clazzMap_.get( clazz );
        if ( cname == null ) {
            throw new IllegalArgumentException( "Class " + clazz.getName()
                                              + " not imported" );
        }
        return cname;
    }

    /**
     * Generates python source defining utility functions.
     *
     * @return  python source code lines
     */
    private String[] defUtils() {
        List lineList = new ArrayList( Arrays.asList( new String[] {

            /* Row iterator class used for providing iterability in tables. */
            "class _row_iterator(object):",
            "    def __init__(self, rowseq):",
            "        self.rowseq = rowseq",
            "    def next(self):",
            "        if self.rowseq.next():",
            "            return self.rowseq.getRow()",
            "        else:",
            "            raise StopIteration",
            "",

            /* WrapperStarTable implementation which is iterable in python. */
            "class _iterable_star_table("
                   + getImportName( WrapperStarTable.class )
                   + ", jy_star_table):",
            "    def __init__(self, base):",
            "        " + getImportName( WrapperStarTable.class )
                       + ".__init__(self, base)",
            "    def __iter__(self):",
            "        return _row_iterator(self.getRowSequence())",
            "    def __str__(self):",
            "        return str(self.getName())"
                          + " + '(?x' + str(self.getColumnCount()) + ')'",
            "",

            /* WrapperStarTable implementation which is a python container. */
            "class _random_star_table(_iterable_star_table):",
            "    def __init__(self, base):",
            "        _iterable_star_table.__init__(self, base)",
            "    def __len__(self):",
            "        return int(self.getRowCount())",
            "    def __getitem__(self, key):",
            "        return self.getRow(key)",
            "    def __str__(self):",
            "        return str(self.getName())"
                          + " + '(' + str(self.getRowCount()) + 'x'"
                          + " + str(self.getColumnCount()) + ')'",
            "",

            /* Execution environment implementation. */
            "class _jy_environment(" + getImportName( MapEnvironment.class )
                                     + "):",
            "    def __init__(self, grab_output=False):",
            "        " + getImportName( MapEnvironment.class )
                       + ".__init__(self)",
            "        superobj = super(_jy_environment, self)",
            "        if grab_output:",
            "            self._out = " + getImportName( MapEnvironment.class )
                                       + ".getOutputStream(self)",
            "        else:",
            "            self._out = " + getImportName( System.class ) + ".out",
            "        self._err = " + getImportName( System.class ) + ".err",
            "    def getOutputStream(self):",
            "        return self._out",
            "    def getErrorStream(self):",
            "        return self._err",
            "",

            /* Returns a StarTable with suitable python decoration. */
            "def import_star_table(table):",
            "    '''Imports a StarTable instance for use with JyStilts.",
            "",
            "    This factory function takes an instance of the Java class",
            "    " + StarTable.class.getName(),
            "    and returns an instance of a wrapper subclass which has some",
            "    decorations useful in a python environment.",
            "    This includes stilts cmd_* and mode_* methods, as well as",
            "    python-friendly standard methods to make it behave as an",
            "    iterable, and where possible a container, of data rows.",
            "    '''",
            "    if table.isRandom():",
            "        return _random_star_table(table)",
            "    else:",
            "        return _iterable_star_table(table)",
            "",

            /* Takes a python value and returns a value suitable for passing
             * to a java Stilts execution environment. */
            "def _map_env_value(pval):",
            "    if pval is None:",
            "        return None",
            "    elif pval is True:",
            "        return 'true'",
            "    elif pval is False:",
            "        return 'false'",
            "    elif isinstance(pval, " + getImportName( StarTable.class )
                                       + "):",
            "        return pval",
            "    elif _is_container(pval, " + getImportName( StarTable.class )
                                              + "):",
            "        return jarray.array(pval, "
                                      +  getImportName( StarTable.class ) + ")",
            "    else:",
            "        return str(pval)",
            "",

            /* Utility method to determine if a python object can be treated
             * as a container. */
            "def _is_container(value, type):",
            "    valdir = dir(value)",
            "    if '__iter__' in valdir and '__len__' in valdir:",
            "        for item in value:",
            "            if not isinstance(item, type):",
            "                return False",
            "        return True",
            "    else:",
            "        return False",
            "",
 
            /* Stilts class instance. */
            "_stilts = " + getImportName( Stilts.class ) + "()",
            "",

            /* Set up verbosity. */
            getImportName( InvokeUtils.class ) + ".configureLogging(0, False)",
            "",
        } ) );

        /* Creates and populates a dictionary mapping parameter names to
         * their aliases where appropriate. */
        lineList.add( paramAliasDictName_ + " = {}" );
        for ( Iterator it = paramAliasMap_.entrySet().iterator();
              it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            lineList.add( paramAliasDictName_
                        + "['" + entry.getValue() + "']='"
                               + entry.getKey() + "'" );   // sic
        }
        lineList.add( "" );

        /* Return source line list array. */
        return (String[]) lineList.toArray( new String[ 0 ] );
    }

    /**
     * Generates python source which checks version mismatches between the
     * module generated by this class and the runtime java library.
     *
     * @return   python source code lines
     */
    private String[] defVersionCheck() {
        return new String[] {
            "_stilts_lib_version = _stilts.getVersion()",
            "_stilts_script_version = '" + stilts_.getVersion() + "'",
            "if _stilts_lib_version != _stilts_script_version:",
            "    print('WARNING: STILTS script/class library version mismatch"
                             + " (' + _stilts_script_version + '/'"
                                + " + _stilts_lib_version + ').')",
            "    print('         This may or may not cause trouble.')",
        };
    }

    /**
     * Generates python source defining a wrapper class for use with StarTables.
     * This can be applied to every StarTable generated by JyStilts
     * to provide additional functionality.
     * It supplies the various filters and modes as methods.
     *
     * @param  cname  class name
     * @return   python source code lines
     */
    private String[] defTableClass( String cname )
            throws LoadException, SAXException {
        List lineList = new ArrayList();
        lineList.add( "class " + cname + "(object):" );

        /* Add filters as methods. */
        ObjectFactory filterFactory =
            StepFactory.getInstance().getFilterFactory();
        String[] filterNames = filterFactory.getNickNames();
        for ( int i = 0; i < filterNames.length; i++ ) {
            String name = filterNames[ i ];
            String[] filterLines = defCmd( "cmd_" + name, name );
            lineList.addAll( Arrays.asList( prefixLines( "    ",
                                                         filterLines ) ) );
        }

        /* Add modes as methods. */
        ObjectFactory modeFactory = stilts_.getModeFactory();
        String[] modeNames = modeFactory.getNickNames();
        for ( int i = 0; i < modeNames.length; i++ ) {
            String name = modeNames[ i ];
            String[] modeLines = defMode( "mode_" + name, name );
            lineList.addAll( Arrays.asList( prefixLines( "    ",
                                                         modeLines ) ) );
        }

        /* Add special write method.  This is just an alias for mode_out. */
        String[] writeLines = defMode( "write", "out" );
        lineList.addAll( Arrays.asList( prefixLines( "    ", writeLines ) ) );

        /* Return the source code lines. */
        return (String[]) lineList.toArray( new String[ 0 ] );
    }

    /**
     * Generates python source defining the table read function.
     *
     * @param  fname  name of function
     * @return  python source code lines
     */
    private String[] defRead( String fname ) {
        return new String[] {
            "def " + fname + "(location, fmt='(auto)', random=False):",
            "    '''Reads a table from a file or URL.",
            "",
            "    If the table is FITS or VOTable format, only the location",
            "    needs to be supplied.  Otherwise, the fmt argument must",
            "    give the file format.",
            "    The random argument determines whether random access is",
            "    required for the table.  Setting it true may improve",
            "    efficiency, but perhaps at the cost of memory usage and",
            "    load time for large tables.",
            "",
            "    The result of the function is a JyStilts table object.",
            "    '''",
            "    table = " + getImportName( StarTableFactory.class )
                           + "(random)"
                           + ".makeStarTable(location, fmt)",
            "    return import_star_table(table)",
        };
    }

    /**
     * Generates python source defining the table write function.
     *
     * @param  fname  name of function
     * @return  python source code lines
     */
    private String[] defWrite( String fname ) {
        return new String[] {
            "def " + fname + "(table, location=None, fmt=None):",
            "    '''Writes a table to a file.'''",
            "    if location is None:",
            "        location = '-'",
            "    " + getImportName( StarTableOutput.class ) + "()"
                   + ".writeStarTable(table, location, fmt)",
        };
    }

    /**
     * Generates python source defining the general table filter function.
     *
     * @param  fname  name of function
     * @return  python source code lines
     */
    private String[] defFilter( String fname ) {
        return new String[] {
            "def " + fname + "(table, cmd):",
            "    '''Applies a filter operation to a table "
                 + "and returns the result.",
            "    In most cases, it's better to use one of the cmd_* functions.",
            "    '''",
            "    step = " + getImportName( StepFactory.class )
                          + ".getInstance().createStep(cmd)",
            "    return import_star_table(step.wrap(table))",
        };
    }

    /**
     * Generates python source defining a specific table filter function.
     *
     * @param  fname  name of function
     * @param  filterNickName  name under which the filter is known in the
     *         filter ObjectFactory
     * @return  python source code lines
     */
    private String[] defCmd( String fname, String filterNickName )
            throws LoadException, SAXException {
        ProcessingFilter filter =
            (ProcessingFilter) StepFactory.getInstance()
                                          .getFilterFactory()
                                          .createObject( filterNickName );
        String usage = filter.getUsage();
        boolean hasUsage = usage != null && usage.trim().length() > 0;
        List lineList = new ArrayList();
        if ( hasUsage ) {
            lineList.add( "def " + fname + "(table, *args):" );
        }
        else {
            lineList.add( "def " + fname + "(table):" );
        }
        lineList.add( "    '''\\" );
        lineList.addAll( Arrays.asList( formatXml( filter
                                                  .getDescription() ) ) );
        lineList.add( "" );
        lineList.add( "The filtered table is returned." );
        if ( hasUsage ) {
            lineList.add( "" );
            lineList.add( "args is a list with words as elements:" );
            lineList.addAll( Arrays
                            .asList( prefixLines( "    ",
                                                  filter.getUsage() ) ) );
        }
        lineList.add( "'''" );
        lineList.add( "    pfilt = " + getImportName( StepFactory.class )
                           + ".getInstance()"
                           + ".getFilterFactory()"
                           + ".createObject(\"" + filterNickName + "\")" );
        lineList.add( "    sargs = [str(a) for a in args]" );
        lineList.add( "    argList = " + getImportName( ArrayList.class )
                                       + "(sargs)" );
        lineList.add( "    step = pfilt.createStep(argList.iterator())" );
        lineList.add( "    return import_star_table(step.wrap(table))" );
        return (String[]) lineList.toArray( new String[ 0 ] );
    }

    /**
     * Generates python source defining an output mode function.
     *
     * @param  fname  name of function
     * @param  modeNickName  name under which the mode is known in the
     *         mode ObjectFactory
     * @return  python source code lines
     */
    private String[] defMode( String fname, String modeNickName )
            throws LoadException, SAXException {
        ProcessingMode mode =
            (ProcessingMode) stilts_.getModeFactory()
                                    .createObject( modeNickName );

        /* Assemble mandatory and optional parameters. */
        Parameter[] params = mode.getAssociatedParameters();
        List lineList = new ArrayList();
        List mandArgList = new ArrayList();
        List optArgList = new ArrayList();
        for ( int ip = 0; ip < params.length; ip++ ) {
            Parameter param = params[ ip ];
            String name = param.getName();
            String sdflt = getDefaultString( param );
            if ( sdflt == null ) {
                mandArgList.add( new Arg( param, name ) );
            }
            else {
                optArgList.add( new Arg( param, name + "=" + sdflt ) );
            }
        }

        /* Begin function definition. */
        List argList = new ArrayList();
        argList.addAll( mandArgList );
        argList.addAll( optArgList );
        StringBuffer sbuf = new StringBuffer()
            .append( "def " )
            .append( fname )
            .append( "(table" );
        for ( Iterator it = argList.iterator(); it.hasNext(); ) {
            Arg arg = (Arg) it.next();
            sbuf.append( ", " );
            sbuf.append( arg.formalArg_ );
        }
        sbuf.append( "):" );

        /* Add doc string. */
        lineList.add( sbuf.toString() );
        lineList.add( "    '''\\" );
        lineList.addAll( Arrays.asList( formatXml( mode.getDescription() ) ) );
        lineList.addAll( Arrays.asList( getParamDocs( params ) ) );
        lineList.add( "'''" );

        /* Create and populate execution environment. */
        lineList.add( "    env = _jy_environment()" );
        for ( Iterator it = argList.iterator(); it.hasNext(); ) {
            Arg arg = (Arg) it.next();
            Parameter param = arg.param_;
            String name = param.getName();
            lineList.add( "    env.setValue('" + name + "', "
                                          + "_map_env_value(" + name + "))" );
        }

        /* Create and invoke a suitable TableConsumer. */
        lineList.add( "    mode = _stilts"
                              + ".getModeFactory()"
                              + ".createObject('" + modeNickName + "')" );
        lineList.add( "    consumer = mode.createConsumer(env)" );
        lineList.add( "    consumer.consume(table)" );

        /* Return the source code lines. */
        return (String[]) lineList.toArray( new String[ 0 ] );
    }

    /**
     * Generates python source defining a function for invoking a STILTS task.
     *
     * @param  fname  name of function
     * @param  taskNickName  name under which the task is known in the task
     *         ObjectFactory
     * @return  python source code lines
     */
    private String[] defTask( String fname, String taskNickName )
            throws LoadException, SAXException {
        Task task =
            (Task) stilts_.getTaskFactory().createObject( taskNickName );
        boolean isConsumer = task instanceof ConsumerTask;
        boolean returnOutput = task instanceof Calc;
        List lineList = new ArrayList();

        /* Get a list of mandatory and optional parameters which we will
         * declare as part of the python function definition. 
         * Currently, we refrain from declaring most of the optional
         * arguments, preferring to use just a catch-all **kwargs.
         * This is because a few of the parameters declared by a Stilts
         * task are dummies of one kind or another, so declaring them
         * is problematic and/or confusing. 
         * The ones we do declare are the positional arguments, which
         * tends to be just a few non-problematic an mandatory ones,
         * as well as any which have had their names aliased, so that
         * this is clear from the documentation. */
        Parameter[] params = task.getParameters();
        List mandArgList = new ArrayList();
        List optArgList = new ArrayList();
        int iPos = 0;
        for ( int ip = 0; ip < params.length; ip++ ) {
            Parameter param = params[ ip ];
            String name = param.getName();
            String pname = paramAliasMap_.containsKey( name )
                         ? (String) paramAliasMap_.get( name )
                         : name;
            boolean byPos = false;
            int pos = param.getPosition();
            if ( pos > 0 ) {
                iPos++;
                assert pos == iPos;
                byPos = true;
            }
            if ( byPos || paramAliasMap_.containsKey( name ) ) {
                String sdflt = getDefaultString( param );
                if ( sdflt == null ) {
                    mandArgList.add( new Arg( param, pname ) );
                }
                else {
                    optArgList.add( new Arg( param, pname + "=" + sdflt ) );
                }
            }
        }

        /* Begin the function definition. */
        List argList = new ArrayList();
        argList.addAll( mandArgList );
        argList.addAll( optArgList );
        StringBuffer sbuf = new StringBuffer()
            .append( "def " )
            .append( fname )
            .append( "(" );
        for ( Iterator it = argList.iterator(); it.hasNext(); ) {
            Arg arg = (Arg) it.next();
            sbuf.append( arg.formalArg_ )
                .append( ", " );
        }
        sbuf.append( "**kwargs" )
            .append( "):" );
        lineList.add( sbuf.toString() );

        /* Write the doc string. */
        lineList.add( "    '''\\" );
        lineList.add( task.getPurpose() + "." );
        if ( isConsumer ) {
            lineList.add( "" );
            lineList.add( "The return value is the resulting table." );
        }
        else if ( returnOutput ) {
            lineList.add( "" );
            lineList.add( "The return value is the output string." );
        }
        lineList.addAll( Arrays.asList( getParamDocs( params ) ) );
        lineList.add( "'''" );

        /* Create the task object. */
        if ( isConsumer ) {

            /* If the task is a ConsumerTask, use an instance of a subclass
             * of the relevant Task class (the relevant classes all happen
             * to have suitable no-arg constructors).  This is a hack to
             * permit access to the createProducer method, which has 
             * protected access in the ConsumerTask interface (at least
             * in some stilts versions). */
            Class taskClazz = task.getClass();
            String taskClazzName = taskClazz.getName();
            String taskSubName = "_" + fname + "_task";
            lineList.add( "    import " + taskClazzName );
            lineList.add( "    class " + taskSubName
                                       + "(" + taskClazzName + "):" );
            lineList.add( "        def __init__(self):" );
            lineList.add( "            " + taskClazzName + ".__init__(self)" );
            lineList.add( "    task = " + taskSubName + "()" );
        }
        else {

            /* Do it the simple, and more respectable way, if the hack
             * is not required. */
            lineList.add( "    task = _stilts"
                                    + ".getTaskFactory()"
                                    + ".createObject('" + taskNickName + "')" );
        }

        /* Create the stilts execution environment. */
        if ( returnOutput ) {
            lineList.add( "    env = _jy_environment(grab_output=True)" );
        }
        else {
            lineList.add( "    env = _jy_environment()" );
        }

        /* Populate the environment from the mandatory and optional arguments
         * of the python function.  Watch out for aliased parameters. */
        for ( Iterator it = mandArgList.iterator(); it.hasNext(); ) {
            Arg arg = (Arg) it.next();
            Parameter param = arg.param_;
            String name = param.getName();
            String pname = paramAliasMap_.containsKey( name )
                         ? (String) paramAliasMap_.get( name )
                         : name;
            lineList.add( "    env.setValue('" + name + "', "
                                          + "_map_env_value(" + pname + "))" );
        }
        lineList.add( "    for kw in kwargs.iteritems():" );
        lineList.add( "        key = kw[0]" );
        lineList.add( "        value = kw[1]" );
        lineList.add( "        if key in " + paramAliasDictName_ + ":" );
        lineList.add( "            key = " + paramAliasDictName_ + "[key]" );
        lineList.add( "        env.setValue(key, _map_env_value(value))" );

        /* For a consumer task, create a result table and return it. */
        if ( isConsumer ) {
            lineList.add( "    table = task.createProducer(env).getTable()" );
            lineList.add( "    return import_star_table(table)" );
        }

        /* Otherwise execute the task in the usual way. */
        else {
            lineList.add( "    task.createExecutable(env).execute()" );

            /* If we're returning the output text, retrieve it from the
             * environment, tidy it up, and return it.  Otherwise, the
             * return is None. */
            if ( returnOutput ) {
                lineList.add( "    txt = env.getOutputText()" );
                lineList.add( "    return str(txt.strip())" );
            }
        }

        /* Return the source code lines. */
        return (String[]) lineList.toArray( new String[ 0 ] );
    }

    /**
     * Returns the python-optional-argument-type default value string for
     * a parameter.  May be null in the case that the argument must be
     * supplied.
     *
     * @param  param  STILTS parameter
     * @param  default value, suitable for insertion into python source
     */
    private String getDefaultString( Parameter param ) {
        String dflt = param.getDefault();
        boolean isDfltNull = dflt == null || dflt.trim().length() == 0;
        boolean nullable = param.isNullPermitted();
        if ( nullable || ! isDfltNull ) {
            if ( isDfltNull ) {
                return "None";
            }
            else if ( param instanceof IntegerParameter ||
                      param instanceof DoubleParameter ) {
                return dflt;
            }
            else {
                return "'" + dflt + "'";
            }
        }
        else {
            return null;
        }
    }

    /**
     * Formats XML text for output to python source, to be inserted
     * within string literal quotes.
     *
     * @param  xml  xml text
     * @return  python source code lines for string literal content
     */
    private String[] formatXml( String xml ) throws SAXException {

        /* Shorten the lines by csub characters, so they don't overrun when
         * formatted with indentation by python help. */
        int csub = 8;
        String text = formatter_.formatXML( xml, csub );
        List lineList = new ArrayList();
        for ( Iterator lineIt = lineIterator( text ); lineIt.hasNext(); ) {
            String line = (String) lineIt.next();
            if ( line.trim().length() == 0 ) {
                lineList.add( "" );
            }
            else {
                assert "        ".equals( line.substring( 0, csub ) );
                lineList.add( line.substring( csub ) );
            }
        }
        return (String[]) lineList.toArray( new String[ 0 ] );
    }

    /**
     * Returns documentation for an array of parameters suitable for
     * insertion into a python literal doc string.
     *
     * @param  params  parameters to document
     * @return  lines of doc text
     */
    private String[] getParamDocs( Parameter[] params ) throws SAXException {
        if ( params.length == 0 ) {
            return new String[ 0 ];
        }
        List lineList = new ArrayList();
        lineList.add( "" );
        lineList.add( "Parameters:" );
        StringBuffer sbuf = new StringBuffer();
        sbuf.append( "<dl>" );
        for ( int i = 0; i < params.length; i++ ) {
            sbuf.append( UsageWriter.xmlItem( params[ i ] ) );
        }
        sbuf.append( "</dl>" );
        lineList.addAll( Arrays.asList( formatXml( sbuf.toString() ) ) );
        return (String[]) lineList.toArray( new String[ 0 ] );
    }

    /**
     * Add a fixed prefix to each line of an input string.
     *
     * @param  prefix  per-line prefix
     * @param  text  text block, with lines terminated by newline characters
     * @return  python source code lines for string literal content
     */
    private String[] prefixLines( String prefix, String text ) {
        List lineList = new ArrayList();
        for ( Iterator lineIt = lineIterator( text ); lineIt.hasNext(); ) {
            lineList.add( prefix + (String) lineIt.next() );
        }
        return (String[]) lineList.toArray( new String[ 0 ] );
    }

    /**
     * Adds a fixed prefix to each element of a string array.
     *
     * @param  prefix  per-line prefix
     * @param  lines  input line array
     * @return  output line array
     */
    private String[] prefixLines( String prefix, String[] lines ) {
        List lineList = new ArrayList();
        for ( int i = 0; i < lines.length; i++ ) {
            lineList.add( prefix + lines[ i ] );
        }
        return (String[]) lineList.toArray( new String[ 0 ] );
    }

    /**
     * Outputs an array of lines through a writer.
     *
     * @param  lines  line array
     * @param  writer  destination stream
     */
    private void writeLines( String[] lines, Writer writer )
            throws IOException {
        BufferedWriter bw = new BufferedWriter( writer );
        for ( int i = 0; i < lines.length; i++ ) {
            bw.write( lines[ i ] );
            bw.newLine();
        }
        bw.newLine();
        bw.flush();
    }

    /**
     * Outputs the python source code for the stilts module.
     *
     * @param  writer  destination stream
     */
    public void writeModule( Writer writer )
            throws IOException, LoadException, SAXException {
        writeLines( header(), writer );
        writeLines( imports(), writer );
        writeLines( defTableClass( "jy_star_table" ), writer );
        writeLines( defUtils(), writer );
        writeLines( defVersionCheck(), writer );
        writeLines( defRead( "tread" ), writer );
        writeLines( defWrite( "twrite" ), writer );
        writeLines( defFilter( "tfilter" ), writer );

        /* Write task wrappers. */
        ObjectFactory taskFactory = stilts_.getTaskFactory();
        String[] taskNames = taskFactory.getNickNames();
        for ( int i = 0; i < taskNames.length; i++ ) {
            String name = taskNames[ i ];
            String[] taskLines = defTask( name, name );
            writeLines( taskLines, writer );
        }

        /* Write filter wrappers. */
        ObjectFactory filterFactory =
            StepFactory.getInstance().getFilterFactory();
        String[] filterNames = filterFactory.getNickNames();
        for ( int i = 0; i < filterNames.length; i++ ) {
            String name = filterNames[ i ];
            String[] filterLines = defCmd( "cmd_" + name, name );
            writeLines( filterLines, writer );
        }

        /* Write mode wrappers. */
        ObjectFactory modeFactory = stilts_.getModeFactory();
        String[] modeNames = modeFactory.getNickNames();
        for ( int i = 0; i < modeNames.length; i++ ) {
            String name = modeNames[ i ];
            String[] modeLines = defMode( "mode_" + name, name );
            writeLines( modeLines, writer );
        }
    }

    /**
     * Writes jython source code for a <code>stilts.py</code> module
     * to standard output.
     * No arguments.
     */
    public static void main( String[] args )
            throws IOException, LoadException, SAXException {
        new JyStilts( new Stilts() )
           .writeModule( new OutputStreamWriter( System.out ) );
    }

    /**
     * Convenience class which aggregates a parameter and its string 
     * representation in a python function definition formal parameter
     * list.
     */
    private static class Arg {
        final Parameter param_;
        final String formalArg_;
        Arg( Parameter param, String formalArg ) {
            param_ = param;
            formalArg_ = formalArg;
        }
    }

    /**
     * Returns an iterator over newline-separated lines in a string.
     * Unlike using StringTokenizer, empty lines will be included in
     * the output.
     *
     * @param  text  input text
     * @return   iterator over lines
     */
    private static Iterator lineIterator( final String text ) {
        return new Iterator() {
            private int pos_;
            public boolean hasNext() {
                return pos_ < text.length();
            }
            public Object next() {
                int nextPos = text.indexOf( '\n', pos_ );
                if ( nextPos < 0 ) {
                    nextPos = text.length();
                }
                String line = text.substring( pos_, nextPos );
                pos_ = nextPos + 1;
                return line;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Adapter to turn an OutputStream into a Writer.
     * Any attempt to write a non-ASCII character generates an IOException.
     */
    private static class OutputStreamWriter extends Writer {
        private final OutputStream out_;

        /**
         * Constructor.
         *
         * @param  out  destination stream
         */
        OutputStreamWriter( OutputStream out ) {
            out_ = out;
        }

        public void write( char[] cbuf, int off, int len ) throws IOException {
            byte[] buf = new byte[ len ];
            for ( int i = 0; i < len; i++ ) {
                buf[ i ] = toByte( cbuf[ off + i ] );
            }
            out_.write( buf, 0, len );
        }

        public void write( int c ) throws IOException {
            out_.write( toByte( (char) c ) );
        }

        public void flush() throws IOException {
            out_.flush();
        }

        public void close() throws IOException {
            out_.close();
        }

        /**
         * Turns a char into a byte, throwing an exception in case of
         * narrowing issues.
         *
         * @param  c  character
         * @return  equivalent ASCII byte
         */
        private byte toByte( char c ) throws IOException {
            if ( c >= 0 && c <= 127 ) {
                return (byte) c;
            }
            else {
                throw new IOException(
                    "Non-ASCII character 0x" + Integer.toHexString( (int) c ) );
            }
        }
    }
}
