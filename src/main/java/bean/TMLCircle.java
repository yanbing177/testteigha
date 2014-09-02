package bean;

import com.opendesign.core.OdGePoint3d;
import com.opendesign.td.OdDbCircle;

/**
 * Created by youmiss on 9/1/2014.
 */
public class TMLCircle {
    private double x;
    private double y;
    private double z;
    private double radius;

    public TMLCircle(OdDbCircle odDbCircle) {
        OdGePoint3d center = odDbCircle.center();
        x = center.getX() - 5.54;
        y = center.getY() - 7.75;
        z = center.getZ();
        radius = odDbCircle.radius();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getRadius() {
        return radius;
    }
}
