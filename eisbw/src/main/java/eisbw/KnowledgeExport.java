package eisbw;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.type.Race;
import org.openbw.bwapi4j.type.TechType;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.type.UpgradeType;
import org.openbw.bwapi4j.type.WeaponType;

public class KnowledgeExport {

	public static void export() {
		String export = "";
		for (final UnitType type : UnitType.values()) {
			switch (type.getId()) {
			// TERRAN UNITS
			case 0: // marine
			case 1: // ghost
			case 2: // vulture
			case 3: // goliath
			case 5: // siege tank
			case 7: // scv
			case 8: // wraith
			case 9: // science vessel
			case 11: // dropship
			case 12: // battlecruiser
			case 13: // spider mine (given to vultures)
			case 32: // firebat
			case 34: // medic
			case 58: // valkyrie
				// ZERG UNITS
			case 35: // larva
			case 36: // egg
			case 37: // zergling
			case 38: // hydralisk
			case 39: // ultralisk
			case 41: // drone
			case 42: // overlord
			case 43: // mutalisk
			case 44: // guardian
			case 45: // queen
			case 46: // defiler
			case 47: // scourge
			case 59: // cocoon
			case 62: // devourer
			case 97: // lurker egg
			case 103: // lurker
				// PROTOSS UNITS
			case 60: // corsair
			case 61: // dark templar
			case 63: // dark archon
			case 64: // probe
			case 65: // zealot
			case 66: // dragoon
			case 67: // high templar
			case 68: // archon
			case 69: // shuttle
			case 70: // scout
			case 71: // arbiter
			case 72: // carrier
			case 73: // interceptor (produced by carriers)
			case 83: // reaver
			case 84: // observer
			case 85: // scarab (produced by reavers)
				// TERRAN BUILDINGS
			case 106: // command center
			case 107: // comsat station
			case 108: // nuclear silo
			case 109: // supply depot
			case 110: // refinery
			case 111: // barracks
			case 112: // academy
			case 113: // factory
			case 114: // startport
			case 115: // control tower
			case 116: // science facility
			case 117: // covert ops
			case 118: // physics lab
			case 120: // machine shop
			case 122: // engineering bay
			case 123: // armory
			case 124: // missile turret
			case 125: // bunker
				// ZERG BUILDINGS
			case 131: // hatchery
			case 132: // lair
			case 133: // hive
			case 134: // nydus canal
			case 135: // hydralisk den
			case 136: // defiler mound
			case 137: // greater spire
			case 138: // queens nest
			case 139: // evolution chamber
			case 140: // ultralisk cavern
			case 141: // spire
			case 142: // spawning pool
			case 143: // creep colony
			case 144: // spore colony
			case 146: // sunken colony
			case 149: // extractor
				// PROTOSS BUILDINGS
			case 154: // nexus
			case 155: // robotics facility
			case 156: // pylon
			case 157: // assimilator
			case 159: // observatory
			case 160: // gateway
			case 162: // photon cannon
			case 163: // citadel of adun
			case 164: // cybernetics core
			case 165: // templar archives
			case 166: // forge
			case 167: // stargate
			case 169: // fleet beacon
			case 170: // arbiter tribunal
			case 171: // robotics support bay
			case 172: // shield battery
				// SPELLS
			case 33: // scanner sweep
			case 105: // disruption web
			case 202: // dark swarm
				export += getUnitType(type);
				export += getUnitCosts(type);
				export += getUnitStats(type);
				export += getUnitMetrics(type);
				if (type.canAttack()) {
					export += getUnitCombat(type);
				}
				export += "\n";
				break;
			default:
				break;
			}
		}
		for (final TechType type : TechType.values()) {
			if ((type.getId() >= 0 && type.getId() <= 25) || (type.getId() >= 27 && type.getId() == 46)) {
				export += getTechType(type);
				export += getTechCosts(type);
				export += getTechCombat(type);
				export += "\n";
			}
		}
		for (final UpgradeType type : UpgradeType.values()) {
			if (type.getId() >= 0 && type.getId() <= 54) {
				export += getUpgradeType(type);
				export += getUpgradeCosts(type);
				export += "\n";
			}
		}
		try {
			Files.write(Paths.get(new File("export.pl").toURI()), export.getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getUnitType(UnitType type) {
		final Race race = type.getRace();
		return String.format("unit('%s',%s).\n", BwapiUtility.getName(type), race.toString().toLowerCase());
	}

	private static String getUnitCosts(UnitType type) {
		String requirements = "[";
		boolean hadFirst = false;
		TechType tech = type.requiredTech();
		if (tech.getId() <= 32) {
			requirements += "'" + BwapiUtility.getName(tech) + "'";
			hadFirst = true;
		}
		for (UnitType unit : type.requiredUnits()) {
			if (unit.getId() > 202) {
				continue;
			}
			if (hadFirst) {
				requirements += ",";
			} else {
				hadFirst = true;
			}
			requirements += "'" + BwapiUtility.getName(unit) + "'";
		}
		requirements += "]";
		return String.format("costs('%s',%d,%d,%d,%d,%s).\n", BwapiUtility.getName(type), type.mineralPrice(),
				type.gasPrice(), type.supplyRequired() - type.supplyProvided(), type.buildTime(), requirements);
	}

	private static String getUnitStats(UnitType type) {
		List<String> conditionlist = new LinkedList<>();
		if (type.isBuilding()) {
			conditionlist.add("building");
		}
		if (type.canMove()) {
			conditionlist.add("canMove");
		}
		if (type.isFlyingBuilding()) {
			conditionlist.add("canLift");
		}
		if (type.isBurrowable()) {
			conditionlist.add("canBurrow");
		}
		if (type.isDetector()) {
			conditionlist.add("canDetect");
		}
		if (type.isFlyer()) {
			conditionlist.add("flies");
		}
		if (type.isMechanical()) {
			conditionlist.add("mechanical");
		}
		if (type.isOrganic()) {
			conditionlist.add("organic");
		}
		if (type.requiresCreep()) {
			conditionlist.add("requiresCreep");
		}
		if (type.requiresPsi()) {
			conditionlist.add("requiresPsi");
		}
		if (type.isRobotic()) {
			conditionlist.add("robotic");
		}
		if (type.isAddon()) {
			conditionlist.add("addon");
		}
		if (type.isSpell()) {
			conditionlist.add("spell");
		}
		if (type.canProduce() || type == UnitType.Terran_Nuclear_Silo) {
			conditionlist.add("canTrain");
		}
		Collections.sort(conditionlist);
		String conditions = "[";
		boolean hadFirst = false;
		for (String condition : conditionlist) {
			if (hadFirst) {
				conditions += ",";
			} else {
				hadFirst = true;
			}
			conditions += "'" + condition + "'";
		}
		conditions += "]";
		return String.format("stats('%s',%d,%d,%d,%d,%s).\n", BwapiUtility.getName(type),
				type.isInvincible() ? 0 : type.maxHitPoints(), type.isInvincible() ? 0 : type.maxShields(),
				type.maxEnergy(), (int) (type.topSpeed() * 10), conditions);
	}

	private static String getUnitMetrics(UnitType type) {
		int spaceRequired = (type.spaceRequired() >= 255) ? 0 : type.spaceRequired();
		int spaceProvided = (type.spaceProvided() >= 255) ? 0 : type.spaceProvided();
		return String.format("metrics('%s',%d,%d,%d,%d).\n", BwapiUtility.getName(type), type.tileWidth(),
				type.tileHeight(), new Position(type.sightRange(), 0).toTilePosition().getX(),
				spaceRequired - spaceProvided);
	}

	private static String getUnitCombat(UnitType type) {
		WeaponType ground = type.groundWeapon();
		WeaponType air = type.airWeapon();
		WeaponType generic = (ground == null || ground == WeaponType.Unknown || ground == WeaponType.None) ? air
				: ground;
		return String.format("combat('%s',%d,%d,%d,%d,%d).\n", BwapiUtility.getName(type),
				type.maxGroundHits() * ground.damageAmount() * ground.damageFactor(),
				type.maxAirHits() * air.damageAmount() * air.damageFactor(), generic.damageCooldown(),
				new Position(generic.maxRange(), 0).toTilePosition().getX(),
				new Position(generic.medianSplashRadius(), 0).toTilePosition().getX());
	}

	private static String getTechType(TechType type) {
		return String.format("upgrade('%s',%s).\n", BwapiUtility.getName(type),
				type.getRace().toString().toLowerCase());
	}

	private static String getTechCosts(TechType type) {
		String required = (type.whatResearches().getId() > 202) ? ""
				: ("'" + BwapiUtility.getName(type.whatResearches()) + "'");
		return String.format("costs('%s',%d,%d,%d,%d,%s).\n", BwapiUtility.getName(type), type.mineralPrice(),
				type.gasPrice(), type.energyCost(), type.researchTime(), "[" + required + "]");
	}

	private static String getTechCombat(TechType type) {
		WeaponType weapon = type.getWeapon();
		if (weapon == WeaponType.Unknown || weapon == WeaponType.None) {
			return "";
		} else {
			return String.format("combat('%s',%d,%d,%d,%d,%d).\n", BwapiUtility.getName(type),
					weapon.targetsGround() ? (weapon.damageAmount() * weapon.damageFactor()) : 0,
					weapon.targetsAir() ? (weapon.damageAmount() * weapon.damageFactor()) : 0, weapon.damageCooldown(),
					new Position(weapon.maxRange(), 0).toTilePosition().getX(),
					new Position(weapon.medianSplashRadius(), 0).toTilePosition().getX());
		}
	}

	private static String getUpgradeType(UpgradeType type) {
		if (type.maxRepeats() > 1) {
			String returned = "";
			for (int i = 1; i <= type.maxRepeats(); ++i) {
				returned += String.format("upgrade('%s',%s).\n", BwapiUtility.getName(type) + " " + i,
						type.getRace().toString().toLowerCase());
			}
			return returned;
		} else {
			return String.format("upgrade('%s',%s).\n", BwapiUtility.getName(type),
					type.getRace().toString().toLowerCase());
		}
	}

	private static String getUpgradeCosts(UpgradeType type) {
		String name = BwapiUtility.getName(type);
		String required = (type.whatUpgrades().getId() > 202) ? ""
				: ("'" + BwapiUtility.getName(type.whatUpgrades()) + "'");
		String returned = "";
		for (int i = 0; i < type.maxRepeats(); ++i) {
			String toAdd = (type.maxRepeats() == 1) ? "" : (" " + (i + 1));
			returned += String.format("costs('%s',%d,%d,%d,%d,%s).\n", name + toAdd, type.mineralPrice(i),
					type.gasPrice(i), 0, type.upgradeTime(i), "[" + required + "]");
		}
		return returned;
	}
}
