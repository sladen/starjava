package uk.ac.starlink.ttools.plot2.layer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import uk.ac.starlink.ttools.gui.ResourceIcon;
import uk.ac.starlink.ttools.plot.Range;
import uk.ac.starlink.ttools.plot2.AuxReader;
import uk.ac.starlink.ttools.plot2.AuxScale;
import uk.ac.starlink.ttools.plot2.DataGeom;
import uk.ac.starlink.ttools.plot2.Glyph;
import uk.ac.starlink.ttools.plot2.Surface;
import uk.ac.starlink.ttools.plot2.config.ConfigKey;
import uk.ac.starlink.ttools.plot2.config.ConfigMap;
import uk.ac.starlink.ttools.plot2.data.Coord;
import uk.ac.starlink.ttools.plot2.data.TupleSequence;
import uk.ac.starlink.ttools.plot2.geom.CubeSurface;
import uk.ac.starlink.ttools.plot2.paper.Paper;
import uk.ac.starlink.ttools.plot2.paper.PaperType2D;
import uk.ac.starlink.ttools.plot2.paper.PaperType3D;

/**
 * Draws a line between two related positions.
 *
 * @author   Mark Taylor
 * @since    28 Nov 2013
 */
public class PairLinkForm implements ShapeForm {

    public String getFormName() {
        return "Link";
    }

    public Icon getFormIcon() {
        return ResourceIcon.FORM_LINK2;
    }

    public int getPositionCount() {
        return 2;
    }

    public Coord[] getExtraCoords() {
        return new Coord[ 0 ];
    }

    public ConfigKey[] getConfigKeys() {
        return new ConfigKey[] {
        };
    }

    public Outliner createOutliner( ConfigMap config ) {
        return new LinkOutliner();
    }

    /**
     * Returns an uncoloured icon suitable for use in a legend.
     *
     * @return  legend icon
     */
    private static Icon createLegendIcon() {
        final int dist = 10;
        final int pad = 3;
        final int size = dist + 2 * pad;
        final Glyph glyph = getLineGlyph( dist, -dist );
        return new Icon() {
            public int getIconHeight() {
                return size;
            }
            public int getIconWidth() {
                return size;
            }
            public void paintIcon( Component c, Graphics g, int x, int y ) {
                int xoff = x + pad;
                int yoff = y + size - pad;
                g.translate( xoff, yoff );
                glyph.paintGlyph( g );
                g.translate( -xoff, -yoff );
            }
        };
    }

    /**
     * Returns a glyph to draw a line between the origin and a given point.
     *
     * @param   gx  destination X graphics coordinate
     * @param   gy  destination Y graphics coordinate
     * @return  glyph
     */
    private static Glyph getLineGlyph( int gx, int gy ) {
        return LineGlyph.getLineGlyph( gx, gy );
    }

    /**
     * Determines whether any part of a line between two points is contained
     * within a given rectangle.
     *
     * @param   box  boundary box
     * @param   p1   one end of line
     * @param   p2   other end of line
     * @return  false guarantees that no part of the line appears in the box;
     *          true means it might do
     */
    private static boolean lineMightCross( Rectangle box, Point p1, Point p2 ) {
        int xmin = box.x;
        int xmax = box.x + box.width;
        if ( getRegion( p1.x, xmin, xmax ) *
             getRegion( p2.x, xmin, xmax ) == 1 ) {
            return false;
        }
        int ymin = box.y;
        int ymax = box.y + box.height;
        if ( getRegion( p1.y, ymin, ymax ) *
             getRegion( p2.y, ymin, ymax ) == 1 ) {
            return false;
        }
        return true;
    }

    /**
     * Returns the region of a point with respect to an interval.
     * The return value is -1, 0, or 1 according to whether the point
     * is lower than, within, or higher than the interval bounds.
     *
     * @param   point   test value
     * @param   lo    region lower bound
     * @param   hi    region upper bound
     * @return  region code
     */
    private static int getRegion( int point, int lo, int hi ) {
        return point >= lo ? ( point <= hi ? 0
                                           : +1 )
                           : -1;
    }

    /**
     * Outliner implementation for this form.
     */
    private static class LinkOutliner extends PixOutliner {

        private final Icon icon_;

        /**
         * Constructor.
         */
        LinkOutliner() {
            icon_ = createLegendIcon();
        }

        public Icon getLegendIcon() {
            return icon_;
        }

        public Map<AuxScale,AuxReader> getAuxRangers( DataGeom geom ) {
            return new HashMap<AuxScale,AuxReader>();
        }

        public ShapePainter create2DPainter( final Surface surface,
                                             final DataGeom geom,
                                             Map<AuxScale,Range> auxRanges,
                                             final PaperType2D paperType ) {
            int ndim = surface.getDataDimCount();
            final double[] dpos1 = new double[ ndim ];
            final double[] dpos2 = new double[ ndim ];
            final Point gp1 = new Point();
            final Point gp2 = new Point();
            final int npc = geom.getPosCoords().length;
            final Rectangle bounds = surface.getPlotBounds();
            return new ShapePainter() {
                public void paintPoint( TupleSequence tseq, Color color,
                                        Paper paper ) {

                    /* Paint the line if any part of it falls within the
                     * plot bounds. */
                    if ( geom.readDataPos( tseq, 0, dpos1 ) &&
                         surface.dataToGraphics( dpos1, false, gp1 ) &&
                         geom.readDataPos( tseq, npc, dpos2 ) &&
                         surface.dataToGraphics( dpos2, false, gp2 ) &&
                         lineMightCross( bounds, gp1, gp2 ) ) {
                        Glyph glyph = getLineGlyph( gp2.x - gp1.x,
                                                    gp2.y - gp1.y );
                        paperType.placeGlyph( paper, gp1.x, gp1.y, glyph,
                                              color );
                    }
                }
            };
        }

        public ShapePainter create3DPainter( final CubeSurface surface,
                                             final DataGeom geom,
                                             Map<AuxScale,Range> auxRanges,
                                             final PaperType3D paperType ) {
            int ndim = surface.getDataDimCount();
            final double[] dpos1 = new double[ ndim ];
            final double[] dpos2 = new double[ ndim ];
            final Point gp1 = new Point();
            final Point gp2 = new Point();
            final double[] dz1 = new double[ 1 ];
            final double[] dz2 = new double[ 1 ];
            final int npc = geom.getPosCoords().length;
            return new ShapePainter() {
                public void paintPoint( TupleSequence tseq, Color color,
                                        Paper paper ) {

                    /* Paint the line if either end falls within the plot
                     * region.  It would require some additional arithmetic
                     * to cover the case where neither end is in the region
                     * but part of the line would be.  Note this can lead
                     * to part of the line sticking out of the cube.
                     * It's not really the right thing to do, but it's
                     * not too bad.  Additional work would be required
                     * as well to truncate it at the cube face. */
                    if ( geom.readDataPos( tseq, 0, dpos1 ) &&
                         geom.readDataPos( tseq, npc, dpos2 ) &&
                         ( surface.inRange( dpos1 ) ||
                           surface.inRange( dpos2 ) ) &&
                         surface.dataToGraphicZ( dpos1, false, gp1, dz1 ) &&
                         surface.dataToGraphicZ( dpos2, false, gp2, dz2 ) ) {
                        double z = 0.5 * ( dz1[ 0 ] + dz2[ 0 ] );
                        Glyph glyph = getLineGlyph( gp2.x - gp1.x,
                                                    gp2.y - gp1.y );
                        paperType.placeGlyph( paper, gp1.x, gp1.y, z, glyph,
                                              color );
                    }
                }
            };
        }

        @Override
        public int hashCode() {
            int code = -1045;
            return code;
        }

        @Override
        public boolean equals( Object o ) {
            if ( o instanceof LinkOutliner ) {
                LinkOutliner other = (LinkOutliner) o;
                return true;
            }
            else {
                return false;
            }
        }
    }
}
