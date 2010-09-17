package uk.ac.starlink.topcat;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.load.FileChooserTableLoadDialog2;
import uk.ac.starlink.table.load.FilestoreTableLoadDialog2;
import uk.ac.starlink.table.load.LocationTableLoadDialog2;
import uk.ac.starlink.table.load.SQLTableLoadDialog2;
import uk.ac.starlink.table.load.SystemBrowser;
import uk.ac.starlink.table.load.TableLoadClient;
import uk.ac.starlink.table.load.TableLoadDialog2;
import uk.ac.starlink.table.load.TableLoadWorker;
import uk.ac.starlink.table.load.TableLoader;
import uk.ac.starlink.vo.RegistryTableLoadDialog2;
import uk.ac.starlink.util.Loader;
import uk.ac.starlink.util.gui.ErrorDialog;
import uk.ac.starlink.util.gui.ShrinkWrapper;

/**
 * Window which displays the main gui from which to load tables into the
 * application.  It contains toolbar buttons etc for different individual
 * load dialogues.
 */
public class LoadWindow extends AuxWindow {

    private final ToggleButtonModel stayOpenModel_;
    private final TableLoadDialog2[] knownDialogs_;
    private final List<Action> actList_;
    private TableLoadClient latestClient_;

    /**
     * Name of the system property which can be used to specify the class
     * names of additional {@link TableLoadDialog2} implementations.
     * Each must have a no-arg constructor.  Multiple classnames should be
     * separated by colons.
     */
    public static final String LOAD_DIALOGS_PROPERTY = "startable.load.dialogs";

    /** Class names for the TableLoadDialogs known by default. */
    public final String[] DIALOG_CLASSES = new String[] {
        "uk.ac.starlink.table.load.FilestoreTableLoadDialog2",
        "uk.ac.starlink.datanode.tree.TreeTableLoadDialog2",
        "uk.ac.starlink.table.load.FileChooserTableLoadDialog2",
        "uk.ac.starlink.table.load.LocationTableLoadDialog2",
        "uk.ac.starlink.table.load.SQLTableLoadDialog2",
        TopcatConeSearchDialog.class.getName(),
        TopcatSiapTableLoadDialog.class.getName(),
        TopcatSsapTableLoadDialog.class.getName(),
        "uk.ac.starlink.vo.RegistryTableLoadDialog2",
        "uk.ac.starlink.topcat.vizier.VizierTableLoadDialog2",
        "uk.ac.starlink.topcat.contrib.gavo.GavoTableLoadDialog2",
    };

    /**
     * Constructor.
     *
     * @param   parent  parent component
     * @param   tfact  table factory
     */
    public LoadWindow( Component parent, final StarTableFactory tfact ) {
        super( "Load New Table", parent );

        /* Define action for whether to stay open after loading. */
        stayOpenModel_ =
            new ToggleButtonModel( "Stay Open", ResourceIcon.DO_WHAT,
                                   "Keep window open even after " +
                                   "successful load" );
        getToolBar().add( stayOpenModel_.createToolbarButton() );
        getToolBar().addSeparator();

        /* Create and place components for loading by entering location. */
        JComponent locBox = Box.createVerticalBox();
        final LocationTableLoadDialog2 locTld = new LocationTableLoadDialog2();
        LoaderAction locAct =
            new LoaderAction( "OK", null,
                              "Load table by giving its filename or URL" ) {
            public TableLoader createTableLoader() {
                return locTld.createTableLoader();
            }
        };
        locTld.configure( tfact, locAct );
        JComponent formatLine = Box.createHorizontalBox();
        formatLine.add( new JLabel( "Format: " ) );
        formatLine.add( new ShrinkWrapper( locTld.createFormatSelector() ) );
        formatLine.add( Box.createHorizontalGlue() );
        locBox.add( formatLine );
        JComponent locLine = Box.createHorizontalBox();
        locLine.add( new JLabel( "Location: " ) );
        locLine.add( Box.createHorizontalStrut( 5 ) );
        locLine.add( locTld.getLocationField() );
        locLine.add( new JButton( locAct ) );
        locBox.add( Box.createVerticalStrut( 5 ) );
        locBox.add( locLine );
        getMainArea().add( locBox, BorderLayout.NORTH );

        /* Prepare actions for all known dialogues. */
        actList_ = new ArrayList<Action>();
        knownDialogs_ =
            (TableLoadDialog2[])
            Loader.getClassInstances( DIALOG_CLASSES, LOAD_DIALOGS_PROPERTY,
                                      TableLoadDialog2.class )
           .toArray( new TableLoadDialog2[ 0 ] );
        for ( int i = 0; i < knownDialogs_.length; i++ ) {
            actList_.add( new DialogAction( knownDialogs_[ i ], tfact ) );
        }

        /* Prepare action for system browser load. */
        Action sysAct = new LoaderAction( "System load", ResourceIcon.SYSTEM,
                                          "Load table using system browser") {
            private final SystemBrowser browser_ = new SystemBrowser();
            public TableLoader createTableLoader() {
                String format = locTld.getSelectedFormat();
                return browser_.showLoadDialog( LoadWindow.this, format );
            }
        };
        actList_.add( 1, sysAct );

        /* Add actions to toolbar. */
        List<Action> toolList = new ArrayList<Action>( actList_ );
        toolList.remove( getDialogAction( RegistryTableLoadDialog2.class ) );
        toolList.remove( getDialogAction( FileChooserTableLoadDialog2.class ) );
        toolList.remove( getDialogAction( LocationTableLoadDialog2.class ) );
        for ( Action act : toolList ) {
            getToolBar().add( act );
        }
        getToolBar().addSeparator();

        /* Add a menu for actions. */
        JMenu actMenu = new JMenu( "DataSources" );
        actMenu.setMnemonic( KeyEvent.VK_D );
        for ( Action act : actList_ ) {
            actMenu.add( act );
        }
        getJMenuBar().add( actMenu );

        /* Add larger buttons for the most common load types. */
        List<Action> commonList = new ArrayList<Action>();
        commonList.add( getDialogAction( FilestoreTableLoadDialog2.class ) );
        commonList.add( sysAct );
        List<JButton> buttList = new ArrayList<JButton>();
        int buttw = 0;
        for ( Action act : commonList ) {
            JButton butt = new JButton( act );
            buttList.add( butt );
            buttw = Math.max( buttw, butt.getPreferredSize().width );
        }
        JComponent buttBox = Box.createVerticalBox();
        for ( JButton butt : buttList ) {
            Dimension max = butt.getMaximumSize();
            max.width = buttw;
            butt.setMaximumSize( max );
            buttBox.add( Box.createVerticalStrut( 5 ) );
            buttBox.add( butt );
        }
        JComponent buttLine =
            new JPanel( new FlowLayout( FlowLayout.RIGHT, 0, 0 ) );
        buttLine.add( buttBox );
        buttLine.setAlignmentX( LEFT_ALIGNMENT );
        getMainArea().add( buttLine, BorderLayout.SOUTH );

        /* Demo actions. */
        JMenu demoMenu = new JMenu( "Examples" );
        demoMenu.setMnemonic( KeyEvent.VK_X );
        demoMenu.add( new AbstractAction( "Load Example Table" ) {
            public void actionPerformed( ActionEvent evt ) {
                String demoPath = TopcatUtils.DEMO_LOCATION + "/"
                                + TopcatUtils.DEMO_TABLE;
                String loc = getClass().getClassLoader()
                                       .getResource( demoPath ).toString();
                try {
                    StarTable table = tfact.makeStarTable( loc );
                    ControlWindow.getInstance().addTable( table, loc, true );
                    if ( ! stayOpenModel_.isSelected() ) {
                        LoadWindow.this.dispose();
                    }
                }
                catch ( IOException e ) {
                    ErrorDialog.showError( LoadWindow.this, "Load Failure", e,
                                           "Can't load " + loc + "??" );
                }
            }
        } );
        Action treeDemoAct = new DialogAction( new DemoLoadDialog(), tfact );
        treeDemoAct.putValue( Action.SMALL_ICON, null );
        demoMenu.add( treeDemoAct );
        getJMenuBar().add( demoMenu );

        /* Add standard help actions. */
        addHelp( "LoadWindow" );
    }

    /**
     * Returns list of dialogues known by this window.
     *
     * @return  dialogue list
     */
    public TableLoadDialog2[] getKnownDialogs() {
        return knownDialogs_;
    }

    /**
     * Returns a TableLoadDialog in the list known by this window which 
     * has a given class.
     *
     * @param  clazz  class, some subclass of TableLoadDialog
     * @return  existing dialog instance of clazz, or null
     */
    public TableLoadDialog2 getKnownDialog( Class clazz ) {
        if ( ! TableLoadDialog2.class.isAssignableFrom( clazz ) ) {
            throw new IllegalArgumentException();
        }
        for ( int i = 0; i < knownDialogs_.length; i++ ) {
            TableLoadDialog2 tld = knownDialogs_[ i ];
            if ( clazz.isAssignableFrom( tld.getClass() ) ) {
                return tld;
            }
        }
        return null;
    }

    /**
     * Returns the action associated with a TableLoadDialog of a given class,
     * if one is currently in use by this window.
     *
     * @param  tldClazz  class, some subclass of TableLoadDialog
     * @return  action which invokes an instance of tldClazz, if one is in use
     */
    public Action getDialogAction( Class tldClazz ) {
        if ( ! TableLoadDialog2.class.isAssignableFrom( tldClazz ) ) {
            throw new IllegalArgumentException();
        }
        for ( Action act : actList_ ) {
            if ( act instanceof DialogAction ) {
                DialogAction dact = (DialogAction) act;
                if ( tldClazz
                    .isAssignableFrom( dact.getLoadDialog().getClass() ) ) {
                    return dact;
                }
            }
        }
        assert false;
        return null;
    }

    /**
     * Action to display a given TableLoadDialog.
     */
    private class DialogAction extends BasicAction {
        private final TableLoadDialog2 tld_;
        private final TableLoadWindow win_;

        /**
         * Constructor.
         *
         * @param  tld  load dialogue
         * @param  tfact  table factory
         */
        DialogAction( TableLoadDialog2 tld, StarTableFactory tfact ) {
            super( tld.getName(), tld.getIcon(), tld.getDescription() );
            tld_ = tld;
            win_ = new TableLoadWindow( LoadWindow.this, tld, tfact ) {
                protected TableLoadClient createTableLoadClient() {
                    return adjustClient( (TopcatLoadClient)
                                         super.createTableLoadClient() );
                }
            };
            if ( ! tld.isAvailable() ) {
                setEnabled( false );
            }
        }

        public void actionPerformed( ActionEvent evt ) {
            win_.setVisible( true );
        }

        /**
         * Returns the dialogue associated with this action.
         *
         * @return  dialogue
         */
        public TableLoadDialog2 getLoadDialog() {
            return tld_;
        }
    }

    /**
     * Abstract action which can pop up a window for loading.
     * It basically mediates between a supplied TableLoader and a 
     * TopcatLoadClient.
     * Concrete implementations must implement {@link #createTableLoader}.
     */
    private abstract class LoaderAction extends BasicAction {

        /**
         * Constructor.
         *
         * @param  name   action name
         * @param  icon   action icon
         * @param  description  action description (tooltip)
         */
        LoaderAction( String name, Icon icon, String description ) {
            super( name, icon, description );
        }

        /**
         * Returns a TableLoader to provide the table(s) to load.
         *
         * @return  table loader
         */
        protected abstract TableLoader createTableLoader();

        public void actionPerformed( ActionEvent evt ) {
            TableLoader loader = createTableLoader();
            if ( loader == null ) {
                return;
            }
            final TopcatLoadClient client =
                new TopcatLoadClient( LoadWindow.this,
                                      ControlWindow.getInstance() );
            final JFrame win = new JFrame( "Loading Table..." );
            win.setLocationRelativeTo( LoadWindow.this );
            Container main = win.getContentPane();
            TableLoadWorker worker =
                    new TableLoadWorker( loader, adjustClient( client ) ) {
                protected void finish( boolean cancelled ) {
                    win.dispose();
                    super.finish( cancelled );
                }
            };
            main.setLayout( new BoxLayout( main, BoxLayout.Y_AXIS ) );
            main.add( new JLabel( "Loading Table " + loader.getLabel() ) );
            main.add( new JButton( worker.getCancelAction() ) );
            main.add( worker.getProgressBar() );
            win.pack();
            win.setVisible( true );
            worker.start();
        }
    }

    /**
     * Tweaks a load client so that it (possibly) closes this window when
     * it has completed a load sequence.
     *
     * @param  loadClient  input client
     * @return   adjusted client
     */
    private TableLoadClient adjustClient( final TopcatLoadClient loadClient ) {
        return new TableLoadClient() {
            public StarTableFactory getTableFactory() {
                return loadClient.getTableFactory();
            }
            public void startSequence() {
                latestClient_ = this;
                loadClient.startSequence();
            }
            public void setLabel( String label ) {
                loadClient.setLabel( label );
            }
            public boolean loadSuccess( StarTable table ) {
                return loadClient.loadSuccess( table );
            }
            public boolean loadFailure( Throwable error ) {
                return loadClient.loadFailure( error );
            }
            public void endSequence( boolean cancelled ) {
                loadClient.endSequence( cancelled );
                if ( ! cancelled && loadClient.getLoadCount() > 0 &&
                     ! stayOpenModel_.isSelected() && latestClient_ == this ) {
                    LoadWindow.this.dispose();
                    latestClient_ = null;
                }
            }
        };
    }
}
