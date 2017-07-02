package eisbw.actions;

import java.util.List;

import bwapi.Unit;
import eis.iilang.Action;
import eis.iilang.Numeral;
import eis.iilang.Parameter;

/**
 * @author Danny & Harm - Loads a unit into a specified other unit.
 *
 */
public class Load extends StarcraftLoadingAction {
	/**
	 * The Load constructor.
	 *
	 * @param api
	 *            The BWAPI
	 */
	public Load(bwapi.Game api) {
		super(api);
	}

	@Override
	public void execute(Unit unit, Action action) {
		List<Parameter> parameters = action.getParameters();
		Unit target = this.api.getUnit(((Numeral) parameters.get(0)).getValue().intValue());

		unit.load(target, false);
	}

	@Override
	public String toString() {
		return "load(targetID)";
	}
}
