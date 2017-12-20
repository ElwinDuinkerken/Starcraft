package eisbw.debugger.draw;

import java.util.logging.Logger;

import org.openbw.bwapi4j.MapDrawer;

import eisbw.Game;

/**
 * @author Danny & Harm - The abstract class for the drawing classes.
 *
 */
public abstract class IDraw {
	protected Logger logger = Logger.getLogger("StarCraft Logger"); // overriden
																	// in test

	protected final Game game;
	private boolean toggle = false;

	/**
	 * The IDraw constructor.
	 *
	 * @param game
	 *            the current game.
	 */
	public IDraw(Game game) {
		this.game = game;
	}

	protected abstract void doDraw(MapDrawer api);

	/**
	 * Draw on the map.
	 *
	 * @param api
	 *            - the StarCraft API.
	 */
	public void draw(MapDrawer api) {
		if (this.toggle) {
			doDraw(api);
		}
	}

	/**
	 * Handles the toggling of the drawing.
	 */
	public void toggle() {
		this.toggle = !this.toggle;
	}
}
