/*
 * Copyright (C) 2004 Central Laboratory of the Research Councils
 *
 *  History:
 *     13-SEP-2004 (Peter W. Draper):
 *       Original version.
 */
package uk.ac.starlink.splat.iface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;

import uk.ac.starlink.splat.data.SpecDataFactory;

/**
 * Load a list of spectra into the {@link SplatBrowser}, or save a spectrum
 * using a thread to avoid blocking of the UI. A single instance of this class
 * exists so it is only possible to load or save one set of files at a time.
 *
 * @author Peter W. Draper
 * @version $Id$
 */
public class SpectrumIO
{
    //  XXX deficiencies: cannot stop during load of a spectrum. This would
    //  require a lot of work catching the problems with interrupting
    //  incomplete objects. The ProgressMonitor is hacked to display after a
    //  certain time, not just an amount of loading, this could be done better.

    /**
     * Private constructor so that only one instance can exist.
     */
    private SpectrumIO()
    {
        //  Do nothing.
    }

    /**
     * Instance of this class.
     */
    private static SpectrumIO instance = null;

    /**
     * Return the reference to the single instance of SpectrumIO.
     */
    public static SpectrumIO getInstance()
    {
        if ( instance == null ) {
            instance = new SpectrumIO();
        }
        return instance;
    }

    /**
     *  The global list of spectra and plots.
     */
    protected GlobalSpecPlotList globalList = GlobalSpecPlotList.getInstance();

    /**
     * The list of spectra to load.
     */
    private Vector queue = new Vector();

    /**
     * The Thread that the loading or saving is actually performed in.
     */
    private Thread loadThread = null;

    /**
     * The ProgressMonitor.
     */
    private int progressValue;
    private int progressMaximum;
    private ProgressMonitor progressMonitor = null;

    /**
     * The SplatBrowser to notify about any spectra that are loaded.
     */
    private SplatBrowser browser = null;

    /**
     * The type of the spectra to be loaded. This is an integer understood by
     * the SpecDataFactory.
     */
    private int usertype = SpecDataFactory.DEFAULT;

    /**
     * Whether the spectra should also be displayed.
     */
    private boolean display = true;

    /**
     * Name of last spectrum.
     */
    private String lastSpectrum = null;

    /**
     * Step to push ProgressMonitor into displaying more often (there's a need
     * to shift by 10% before it even thinks about displaying, which means
     * single spectra never get a ProgressMonitor).
     */
    private static int USTEP = 200;

    /**
     * Load an array of spectra whose specifications are contained in the
     * elements of an array of Strings. If the loading process takes a "long"
     * time then a ProgressMonitor dialog with be displayed, which can also be
     * cancelled (which causes the next spectrum not to be loaded, the
     * currently loaded spectrum will complete).
     */
    public void load( SplatBrowser browser, String[] spectra,
                      boolean display, int usertype )
    {
        setSpectra( spectra );
        this.browser = browser;
        this.usertype = usertype;
        this.display = display;
        loadSpectra();
    }

    /**
     * Set the spectra to load.
     */
    protected synchronized void setSpectra( String[] spectra )
    {
        queue.clear();
        if ( spectra != null ) {
            for ( int i = 0; i < spectra.length; i++ ) {
                queue.add( spectra[i] );
            }
        }
    }

    /**
     * Get the next spectrum to load. Returns null when none left.
     */
    protected synchronized String getSpectrum()
    {
        try {
            lastSpectrum = (String) queue.remove( 0 );
        }
        catch (ArrayIndexOutOfBoundsException e) {
            lastSpectrum = null;
        }
        return lastSpectrum;
    }

    /**
     * Load all the spectra that are waiting to be processed.
     * Use a new Thread so that we do not block the GUI or event threads.
     */
    protected void loadSpectra()
    {
        if ( ! queue.isEmpty() ) {
            initProgressMonitor( queue.size(), "Loading spectra..." );
            waitTimer = new Timer ( 250, new ActionListener()
                {
                    private int soFar = 0;
                    public void actionPerformed( ActionEvent e )
                    {
                        progressMonitor.setProgress( filesDone*USTEP + soFar );
                        soFar++;
                        if ( soFar >= USTEP ) soFar = 0;
                        progressMonitor.setNote( lastSpectrum );

                        //  Stop loading spectra if asked.
                        if (progressMonitor.isCanceled() ) {
                            waitTimer.stop();

                            //  If interrupted do not display already loaded
                            //  spectra and clear the queue.
                            if ( progressMonitor.isCanceled() ) {
                                display = false;
                                queue.clear();
                            }
                            //spectraLoadThread.interrupt();
                        }
                    }
                });

            //  Block the browser window and start the timer that updates the
            //  ProgressMonitor.
            browser.setWaitCursor();
            waitTimer.start();

            //  Now create the thread that reads the spectra.
            loadThread = new Thread( "Spectra loader" ) {
                    public void run() {
                        try {
                            addSpectra();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            //  Always tidy up and rewaken interface when
                            //  complete (including if an error is thrown).
                            browser.resetWaitCursor();
                            waitTimer.stop();
                            closeProgressMonitor();
                        }
                    }
                };

            //  Start loading spectra.
            loadThread.start();
        }
    }

    /**
     * Timer for used for event queue actions.
     */
    private Timer waitTimer;

    /**
     * Number of files which we have loaded.
     */
    private int filesDone = 0;

    /**
     * Load all the currently queued spectra into the global list of spectra
     * and inform the associated SplatBrowser to display.
     * given attempt to open the files using the type provided by the
     * user (in the open file dialog).
     */
    protected void addSpectra()
    {
        if ( queue.isEmpty() ) return;

        int validFiles = 0;
        int initialsize = queue.size();
        filesDone = 0;

        // Add all spectra to the browser until the queue is empty.
        while( ! queue.isEmpty() ) {
            if ( browser.addSpectrum( getSpectrum(), usertype ) ) {
                validFiles++;
            }
            filesDone++;
        }

        //  And now display them if we can.
        if ( display && validFiles > 0 ) {
            int count = globalList.specCount();
            browser.displayRange( count - initialsize, count -1 );
        }
    }

    /**
     * Save a given spectrum as a file. Use a thread so that we do not
     * block the GUI or event threads.
     *
     * @param globalIndex global list index of the spectrum to save.
     * @param target the file to write the spectrum into.
     */
    public void save( SplatBrowser browser, int globalIndex, String target )
    {
        final int localGlobalIndex = globalIndex;
        final String localTarget = target;
        final SplatBrowser localBrowser = browser;

        //  Monitor progress by checking the filesDone variable.
        initProgressMonitor( 1, "Saving spectrum..." );
        progressMonitor.setNote( "as " + localTarget );
        waitTimer = new Timer ( 250, new ActionListener()
            {
                int soFar = 0;
                public void actionPerformed( ActionEvent e )
                {
                    progressMonitor.setProgress( soFar );
                    if ( soFar > USTEP ) soFar = 0;
                }
            });
        browser.setWaitCursor();
        waitTimer.start();

        //  Now create the thread that saves the spectrum.
        Thread saveThread = new Thread( "Spectrum saver" ) {
                public void run() {
                    try {
                        localBrowser.saveSpectrum( localGlobalIndex,
                                                   localTarget );
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        //  Always tidy up and rewaken interface when
                        //  complete (including if an error is thrown).
                        localBrowser.resetWaitCursor();
                        waitTimer.stop();
                        closeProgressMonitor();
                    }
                }
            };
        //  Start saving spectrum.
        saveThread.start();
    }

    /**
     * Initialise the startup progress monitor.
     *
     * @param intervals the number of intervals (i.e. calls to
     *                   updateProgressMonitor) expected before action
     *                   is complete.
     * @param title the title for the monitor window.
     * @see #updateProgressMonitor
     */
    protected void initProgressMonitor( int intervals, String title )
    {
        closeProgressMonitor();
        progressValue = 0;
        progressMaximum = intervals * USTEP;
        progressMonitor = new ProgressMonitor( browser, title, "", 0,
                                               progressMaximum );
        progressMonitor.setMillisToDecideToPopup( 500 );
        progressMonitor.setMillisToPopup( 250 );
    }

    /**
     *  Update the progress monitor.
     *
     *  @param note note to show in the progress monitor dialog.
     *
     *  @see #initProgressMonitor
     */
    protected void updateProgressMonitor( String note )
    {
        progressMonitor.setProgress( ++progressValue );
        progressMonitor.setNote( note );
    }

    /**
     *  Close the progress monitor.
     *
     *  @see #initProgressMonitor
     */
    protected void closeProgressMonitor()
    {
        if ( progressMonitor != null ) {
            progressMonitor.close();
            progressMonitor = null;
        }
    }

}
