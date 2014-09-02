package test;

import com.opendesign.core.ExSystemServices;
import com.opendesign.td.ExHostAppServices;
import com.opendesign.td.TD_Db;
import controller.EquipController;
import model.DatabaseTeigha;

/**
 * Created by youmiss on 8/30/2014.
 */
public class TestDatabaseTeigha {
    public static void main(String argv[]) {
        DatabaseTeigha databaseTeigha = DatabaseTeigha.getInstance();
//        databaseTeigha.loadDrawing("KAQ-0803.dwg");
        System.out.println(databaseTeigha.loadDrawing("KAQ-0803.dwg"));
        EquipController controller = new EquipController("PIPELINE", "KAQ-0803");
    }
}
