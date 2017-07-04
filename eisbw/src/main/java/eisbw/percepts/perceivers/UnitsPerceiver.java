package eisbw.percepts.perceivers;

import java.util.HashMap;
import java.util.HashSet;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eis.eis2java.translation.Filter;
//import eis.iilang.Identifier;
//import eis.iilang.Parameter;
import eis.iilang.Percept;
import eisbw.BwapiUtility;
import eisbw.percepts.AttackingPercept;
import eisbw.percepts.EnemyPercept;
import eisbw.percepts.FriendlyPercept;
import eisbw.percepts.MineralFieldPercept;
import eisbw.percepts.Percepts;
import eisbw.percepts.ResourcesPercept;
import eisbw.percepts.UnderConstructionPercept;
import eisbw.percepts.VespeneGeyserPercept;
import eisbw.units.ConditionHandler;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Unit;
import jnibwapi.types.UnitType.UnitTypes;

/**
 * @author Danny & Harm - The perceiver which handles all the unit percepts.
 *
 */
public class UnitsPerceiver extends Perceiver {
	private final Map<Unit, Integer> regionCache = new HashMap<>();

	/**
	 * @param api
	 *            The BWAPI.
	 */
	public UnitsPerceiver(JNIBWAPI api) {
		super(api);
	}

	@Override
	public Map<PerceptFilter, Set<Percept>> perceive(Map<PerceptFilter, Set<Percept>> toReturn) {
		resourcesPercepts(toReturn);
		unitsPercepts(toReturn);
		return toReturn;
	}

	private void resourcesPercepts(Map<PerceptFilter, Set<Percept>> toReturn) {
		Player self = this.api.getSelf();
		if (self != null) { // for tests
			Set<Percept> resourcePercept = new HashSet<>(1);
			resourcePercept.add(new ResourcesPercept(self.getMinerals(), self.getGas(), self.getSupplyUsed(),
					self.getSupplyTotal()));
			toReturn.put(new PerceptFilter(Percepts.RESOURCES, Filter.Type.ON_CHANGE), resourcePercept);
		}
		Set<Percept> minerals = new HashSet<>();
		Set<Percept> geysers = new HashSet<>();
		for (Unit u : this.api.getNeutralUnits()) {
			if (BwapiUtility.isValid(u) && u.getType().isMineralField()) {
				int region = getRegion(u);
				MineralFieldPercept mineralfield = new MineralFieldPercept(u.getID(), u.getResources(),
						u.getPosition().getBX(), u.getPosition().getBY(), region);
				minerals.add(mineralfield);
			} else if (BwapiUtility.isValid(u) && u.getType().getID() == UnitTypes.Resource_Vespene_Geyser.getID()) {
				int region = getRegion(u);
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(),
						u.getPosition().getBX(), u.getPosition().getBY(), region);
				geysers.add(geyser);
			}
		}
		for (Unit u : this.api.getMyUnits()) {
			if (BwapiUtility.isValid(u) && u.getType().isRefinery()) {
				int region = getRegion(u);
				VespeneGeyserPercept geyser = new VespeneGeyserPercept(u.getID(), u.getResources(),
						u.getPosition().getBX(), u.getPosition().getBY(), region);
				geysers.add(geyser);

			}
		}
		toReturn.put(new PerceptFilter(Percepts.MINERALFIELD, Filter.Type.ALWAYS), minerals);
		toReturn.put(new PerceptFilter(Percepts.VESPENEGEYSER, Filter.Type.ALWAYS), geysers);
	}

	private void unitsPercepts(Map<PerceptFilter, Set<Percept>> toReturn) {
		Set<Percept> newunitpercepts = new HashSet<>();
		Set<Percept> friendlypercepts = new HashSet<>();
		Set<Percept> enemypercepts = new HashSet<>();
		Set<Percept> attackingpercepts = new HashSet<>();

		// perceive friendly units
		setUnitPercepts(this.api.getMyUnits(), newunitpercepts, friendlypercepts, attackingpercepts);
		// perceive enemy units
		setUnitPercepts(this.api.getEnemyUnits(), null, enemypercepts, attackingpercepts);

		if (!friendlypercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.FRIENDLY, Filter.Type.ALWAYS), friendlypercepts);
		}
		if (!enemypercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.ENEMY, Filter.Type.ALWAYS), enemypercepts);
		}
		if (!attackingpercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.ATTACKING, Filter.Type.ALWAYS), attackingpercepts);
		}
		if (!newunitpercepts.isEmpty()) {
			toReturn.put(new PerceptFilter(Percepts.UNDERCONSTRUCTION, Filter.Type.ALWAYS), newunitpercepts);
		}
	}

	/**
	 * @param u
	 * @return The region for the given unit (from a cache if it was seen before).
	 */
	private int getRegion(Unit u) {
		Integer region = this.regionCache.get(u);
		if (region == null) {
			region = BwapiUtility.getRegion(u, this.api.getMap());
			this.regionCache.put(u, region);
		}
		return region.intValue();
	}

	/**
	 * Sets some of the generic Unit percepts.
	 *
	 * @param units
	 *            The perceived units
	 * @param newunitpercepts
	 *            - list with newUnitPercepts; if this is passed (not null) we
	 *            assume we want friendly units in unitpercepts
	 * @param unitpercepts
	 *            - list with unitPercepts
	 * @param attackingpercepts
	 *            - list with attackingPercepts
	 * @param percepts
	 *            The list of percepts
	 * @param toReturn
	 *            - the map that will be returned
	 */
	private void setUnitPercepts(List<Unit> units, Set<Percept> newunitpercepts, Set<Percept> unitpercepts,
			Set<Percept> attackingpercepts) {
		for (Unit u : units) {
			if (!BwapiUtility.isValid(u)) {
				continue;
			}
			ConditionHandler conditionHandler = new ConditionHandler(this.api, u);
			if (newunitpercepts != null) {
				String unittype = (u.getType().getID() == UnitTypes.Zerg_Egg.getID()) ? u.getBuildType().getName()
						: BwapiUtility.getName(u.getType());
				unitpercepts.add(new FriendlyPercept(u.getID(), unittype, conditionHandler.getConditions()));
				if (u.isBeingConstructed()) {
					int region = BwapiUtility.getRegion(u, this.api.getMap());
					newunitpercepts.add(new UnderConstructionPercept(u.getID(), u.getHitPoints() + u.getShields(),
							u.getPosition().getBX(), u.getPosition().getBY(), region));
				}
			} else {
				int region = BwapiUtility.getRegion(u, this.api.getMap());
				unitpercepts.add(new EnemyPercept(u.getID(), BwapiUtility.getName(u.getType()), u.getHitPoints(),
						u.getShields(), u.getEnergy(), conditionHandler.getConditions(), u.getPosition().getBX(),
						u.getPosition().getBY(), region));
				if (u.getType().isAttackCapable()) {
					Unit target = (u.getTarget() == null) ? u.getOrderTarget() : u.getTarget();
					if (target != null && !units.contains(target)) {
						attackingpercepts.add(new AttackingPercept(u.getID(), target.getID()));
					}
				}
			}
		}
	}
}
