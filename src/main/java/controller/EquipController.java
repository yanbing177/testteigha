package controller;

import com.opendesign.td.OdDbCircle;
import model.DatabaseTeigha;

import java.util.ArrayList;

/**
 * Created by youmiss on 9/1/2014.
 */
public class EquipController {
    ArrayList<OdDbCircle> TMLs;
    DatabaseTeigha db;
    String equpimentType;
    String equipment;

    public EquipController(String equipmentType, String equipment) {
        this.equpimentType = equipmentType;
        this.equipment = equipment;
        TMLs = new ArrayList<OdDbCircle>();
        db = DatabaseTeigha.getInstance(); // Save an instance for the database
        String drawing = equipment + ".dwg";
        db.loadDrawing(drawing);
        String layerName = equipmentType + "-" + equipment + "-" + "TEXT";
        db.dumpSupportedBlocks(layerName, TMLs);
    }

    public ArrayList<OdDbCircle> getTMLs() {
        return TMLs;
    }


}
