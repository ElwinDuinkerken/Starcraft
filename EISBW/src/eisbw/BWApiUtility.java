package eisbw;

import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;
import jnibwapi.*;
import jnibwapi.types.*;
import jnibwapi.types.TechType.TechTypes;
import jnibwapi.types.UnitType.UnitTypes;

public class BWApiUtility {

    public JNIBWAPI bwapi;
    private final HashMap<String, UnitType> unitTypeMap = new HashMap<>();
    private final HashMap<String, TechType> techTypeMap = new HashMap<>();

    public BWApiUtility(JNIBWAPI api) {
        this.bwapi = api;
    }

    public double distanceSq(int unitId, int otherUnitId) {
        Unit unit = this.bwapi.getUnit(unitId);
        Unit otherUnit = this.bwapi.getUnit(otherUnitId);
        Point2D p1 = new Point(unit.getX(), unit.getY());
        Point2D p2 = new Point(otherUnit.getX(), otherUnit.getY());

        return p1.distanceSq(p2);
    }

    public String getUnitName(Unit u) {
        return (u.getType().getName() + u.getID()).replace("_", "").replace(" ", "");
    }
	
	public String getEISUnitType(Unit u) {
		String type = u.getType().getName().replace(" ", "");
		type = type.substring(0, 1).toLowerCase() + type.substring(1);
		return type;
	}
	

    public UnitType getUnitType(String type) {
        if (this.unitTypeMap.isEmpty()) {
            for (UnitType ut : UnitTypes.getAllUnitTypes()) {
                unitTypeMap.put(ut.getName(), ut);
            }
        }
        
        return this.unitTypeMap.get(type);
    }

    public TechType getTechType(String type) {
        if (this.techTypeMap.isEmpty()) {
            for (TechType tt : TechTypes.getAllTechTypes()) {
                techTypeMap.put(tt.getName(), tt);
            }
        }
        
        return this.techTypeMap.get(type);
    }
}
