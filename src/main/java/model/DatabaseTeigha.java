package model;

import com.opendesign.core.ExSystemServices;
import com.opendesign.core.OdGiWorldDraw;
import com.opendesign.td.*;

import java.util.ArrayList;

/**
 * Created by youmiss on 8/30/2014.
 */
public class DatabaseTeigha {

    private static DatabaseTeigha databaseTeigha;
    private ExSystemServices systemServices;
    private ExHostAppServices hostApp;
    private OdDbDatabase db;
    private String drawingLoc;
    /*
     * Load necessary native libraries
     */
    static {
        try {
            System.loadLibrary("TD_ALLOC_4.00_10");
            System.loadLibrary("TD_ROOT_4.00_10");
            System.loadLibrary("SISL");
            System.loadLibrary("TD_GE_4.00_10");
            System.loadLibrary("TD_SpatialIndex_4.00_10");
            System.loadLibrary("TD_Gi_4.00_10");
            System.loadLibrary("TD_Gs_4.00_10");
            System.loadLibrary("TD_DBROOT_4.00_10");
            System.loadLibrary("TD_Br_4.00_10");
            System.loadLibrary("TD_DB_4.00_10");
            System.loadLibrary("TeighaJavaCore");
            System.loadLibrary("TeighaJavaDwg");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" + e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static DatabaseTeigha getInstance() {
        if (databaseTeigha == null)
            databaseTeigha = new DatabaseTeigha();
        return databaseTeigha;
    }

    private DatabaseTeigha() {
        init();
    }

    private void init() {
        /********************************************************************/
            /* Create a Service and HostApp instances.                          */
        /********************************************************************/
        systemServices = new ExSystemServices();
        hostApp = new ExHostAppServices();
        hostApp.disableOutput(true); //Disable progress meter

        /********************************************************************/
			/* Initialize Teigha.                                               */
        /********************************************************************/
        TD_Db.odInitialize(systemServices);
    }

    /**
     * Load a new drawing
     * @param drawingLoc the string location of the drawing
     * @return {@code true} if the drawing is a valid .dwg file, {@code false} otherwise.
     * Respective error messages are sent to the {@code System.err}
     */
    public boolean loadDrawing(String drawingLoc) {
        if (!hostApp.findFile(drawingLoc).isEmpty()) {
            db = hostApp.readFile(drawingLoc);
            if (db != null) {
                // Only save the new drawing location when it is valid.
                this.drawingLoc = drawingLoc;
                return true;
            } else {
                System.err.println("The drawing file: \"" + drawingLoc + "\" is invalid");
                return false;
            }
        } else {
            System.err.println("No such file: \"" + drawingLoc + "\"");
            return false;
        }
    }

    /**
     * Get the string location of the currenctly loaded drawing
     * @return the string location of the drawing
     */
    public String getDrawingLoc() {
        return drawingLoc;
    }

    public boolean isLoaded() {
        return db == null ? false : true;
    }

    /**
     * Dump all the circles in a particular layer and save those objects into a list
     * @param layerName
     * @param list
     */
    public void dumpSupportedBlocks(String layerName, ArrayList<OdDbCircle> list) {
        /**********************************************************************/
	    /* Get a SmartPointer to the BlockTable                               */
        /**********************************************************************/
        // changes for bug #14152 first part
        // explicitly create OdDbObject and blockTable object
        OdDbObject pId = db.getBlockTableId().safeOpenObject();
        OdDbBlockTable blockTable = OdDbBlockTable.cast(pId);

        /**********************************************************************/
	    /* Get a SmartPointer to a new SymbolTableIterator                    */
        /**********************************************************************/
        OdDbSymbolTableIterator blockIter = OdDbSymbolTableIterator.cast(blockTable.newIterator());

        /**********************************************************************/
	    /* Step through the BlockTable                                        */
        /**********************************************************************/
        for (blockIter.start(); !blockIter.done(); blockIter.step()) {
            OdDbBlockTableRecord block = OdDbBlockTableRecord.cast(blockIter.getRecordId().safeOpenObject());

            OdDbObjectIterator entityIter = OdDbObjectIterator.cast(block.newIterator());

            /********************************************************************/
		    /* Step through the BlockTableRecord                                */
            /********************************************************************/
            for (; !entityIter.done(); entityIter.step()) {
                OdDbObject obj = entityIter.objectId().openObject();
                OdDbEntity objEntity = OdDbEntity.cast(obj);
                if (!objEntity.layer().equalsIgnoreCase(layerName)) {
                    continue;
                }

                if (obj.isKindOf(OdDbCircle.desc())) { // obj instanceof OdDbCircle
                    OdDbCircle circle = OdDbCircle.cast(obj);
                    list.add(circle);
                }
                // change related to bug 14152
                // explicitly delete OdDbObject object
                obj.delete();
            }
        }
        // changes for bug #14152 second part
        // explicitly delete OdDbObject and blockTable object
        blockTable.delete();
        pId.delete();
    }
}
