package eisbw.debugger.draw;

import java.util.List;

import eis.iilang.Numeral;
import eis.iilang.Parameter;
import eis.iilang.Percept;
import eis.iilang.TruthValue;
import eisbw.Game;
import eisbw.percepts.perceivers.ConstructionSitePerceiver;
import jnibwapi.BaseLocation;
import jnibwapi.ChokePoint;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Position.PosType;
import jnibwapi.Region;
import jnibwapi.types.RaceType.RaceTypes;
import jnibwapi.util.BWColor;

/**
 * @author Harm & Danny.
 *
 */
public class DrawMapInfo extends IDraw {
	/**
	 * Draw map information (bases, chokepoints, construction sites).
	 *
	 * @param game
	 *            The current game.
	 */
	public DrawMapInfo(Game game) {
		super(game);
	}

	@Override
	protected void doDraw(JNIBWAPI api) {
		drawRegions(api);
		drawBases(api);
		drawChokepoints(api);
		drawConstructionSites(api);
	}

	private void drawRegions(JNIBWAPI api) {
		for (Region region : api.getMap().getRegions()) {
			// FIXME: very expensive draw
			// Position[] p = region.getPolygon();
			// for (int j = 0; j < p.length; ++j) {
			// api.drawLine(p[j], p[(j + 1) % p.length], BWColor.Green, false);
			// }
			api.drawText(region.getCenter(), "Region " + region.getID() + " (" + region.getCenter().getBX() + ","
					+ region.getCenter().getBY() + ")", false);
		}
	}

	private void drawBases(JNIBWAPI api) {
		for (BaseLocation base : api.getMap().getBaseLocations()) {
			api.drawCircle(base.getCenter(), 75, BWColor.Purple, false, false);
			Position pos = base.getPosition();
			api.drawText(pos, pos.getBX() + ", " + pos.getBY(), false);
			if (base.isStartLocation()) {
				api.drawText(base.getCenter(), "Starting Location", false);
			}
		}
	}

	private void drawChokepoints(JNIBWAPI api) {
		for (ChokePoint cp : api.getMap().getChokePoints()) {
			api.drawLine(cp.getFirstSide(), cp.getSecondSide(), BWColor.Yellow, false);
			api.drawCircle(cp.getCenter(), (int) cp.getRadius(), BWColor.Red, false, false);
			api.drawText(cp.getCenter(), "(" + cp.getCenter().getBX() + "," + cp.getCenter().getBY() + ")", false);
		}
	}

	private void drawConstructionSites(JNIBWAPI api) {
		List<Percept> percepts = this.game.getConstructionSites();
		int size = ConstructionSitePerceiver.steps;
		boolean isTerran = (api.getSelf().getRace() == RaceTypes.Terran);
		for (Percept percept : percepts) {
			List<Parameter> params = percept.getParameters();
			int xpos = ((Numeral) params.get(0)).getValue().intValue();
			int ypos = ((Numeral) params.get(1)).getValue().intValue();
			if (isTerran) {
				api.drawBox(new Position(xpos, ypos, PosType.BUILD),
						new Position(xpos + size, ypos + size, PosType.BUILD), BWColor.Blue, false, false);
			} else {
				boolean bool = ((TruthValue) params.get(3)).getBooleanValue();
				if (bool) {
					api.drawBox(new Position(xpos, ypos, PosType.BUILD),
							new Position(xpos + size, ypos + size, PosType.BUILD), BWColor.Blue, false, false);
				} else {
					api.drawBox(new Position(xpos, ypos, PosType.BUILD),
							new Position(xpos + size, ypos + size, PosType.BUILD), BWColor.Red, false, false);
				}
			}
		}
	}
}
