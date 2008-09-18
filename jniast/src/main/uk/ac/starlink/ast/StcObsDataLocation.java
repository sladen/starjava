/* ********************************************************
 * This file automatically generated by StcObsDataLocation.pl.
 *                   Do not edit.                         *
 **********************************************************/

package uk.ac.starlink.ast;


/**
 * Java interface to the AST StcObsDataLocation class
 *  - correspond to the IVOA ObsDataLocation class. 
 * The StcObsDataLocation class is a sub-class of Stc used to describe 
 * the coordinate space occupied by a particular observational dataset.
 * <p>
 * See http://hea-www.harvard.edu/~arots/nvometa/STC.html
 * <p>
 * An STC ObsDataLocation element specifies the extent of the
 * observation within a specified coordinate system, and also specifies 
 * the observatory location within a second coordinate system.
 * <p>
 * The AST StcObsDataLocation class inherits from Stc, and therefore 
 * an StcObsDataLocation can be used directly as an Stc. When used 
 * in this way, the StcObsDataLocation describes the location of the 
 * observation (not the observatory).
 * <p>
 * Eventually, this class will have a method for returning an Stc
 * describing the observatory location. However, AST currently does not 
 * include any classes of Frame for describing terrestrial or solar 
 * system positions. Therefore, the provision for returning observatory 
 * location as an Stc is not yet available. However, for terrestrial
 * observations, the position of the observatory can still be recorded 
 * using the GeoLon and GeoLat attributes of the SpecFrame encapsulated 
 * within the Stc representing the observation location (this assumes
 * the observatory is located at sea level).
 * 
 * 
 * @see  <a href='http://star-www.rl.ac.uk/cgi-bin/htxserver/sun211.htx/?xref_StcObsDataLocation'>AST StcObsDataLocation</a>  
 */
public class StcObsDataLocation extends Stc {

   /**
    * Constructs a new StcObsDataLocation.
    *
    * @param   region  the encapsulated region
    * @param   coords  the AstroCoords elements associated with this Stc
    */
   public StcObsDataLocation( Region region, AstroCoords[] coords ) {
       construct( region, astroCoordsToKeyMaps( coords ) );
   }
   private native void construct( Region region, KeyMap[] coordMaps );
}