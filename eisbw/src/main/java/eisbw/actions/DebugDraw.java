package eisbw.actions;

import eis.iilang.Action;
import eis.iilang.Parameter;
import eisbw.BwapiAction;
import eisbw.Game;
import eisbw.debugger.draw.CustomDrawUnit;
import eisbw.debugger.draw.IDraw;
import jnibwapi.JNIBWAPI;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

/**
 * @author Danny & Harm - Enable or disable drawing text above a certain unit.
 *
 */
@SuppressWarnings("deprecation")
public class DebugDraw extends StarcraftAction {
	private final Game game;

	/**
	 * The DebugText constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public DebugDraw(JNIBWAPI api, Game game) {
		super(api);
		this.game = game;
	}

	@Override
	public boolean isValid(Action action) {
		List<Parameter> parameters = action.getParameters();
		return parameters.size() == 1;
	}

	@Override
	public boolean canExecute(UnitType type, Action action) {
		return true;
	}

	@Override
	public void execute(Unit unit, Action action) {
		// Empty, since we override execute(BwapiAction).
	}

	@Override
	public void execute(BwapiAction action) {
		List<Parameter> parameters = action.getAction().getParameters();
		String text = StringEscapeUtils.unescapeJava(parameters.get(0).toProlog());
		String name = action.getAgentName();

		IDraw draw = new CustomDrawUnit(this.game, action.getUnit(), text);
		this.game.addDraw(name, draw);
		if (!text.isEmpty()) {
			this.game.toggleDraw(name);
		}
	}

	@Override
	public String toString() {
		return "debugdraw(Text)";
	}
}
