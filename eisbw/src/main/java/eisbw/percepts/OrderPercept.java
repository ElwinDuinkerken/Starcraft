package eisbw.percepts;

import eis.iilang.Identifier;
import eis.iilang.Numeral;

/**
 * @author Danny & Harm - The Order Percept.
 *
 */
public class OrderPercept extends StarcraftPercept {
	private static final long serialVersionUID = 1L;

	/**
	 * @param primary
	 *            The name of the primary order.
	 * @param targetUnit
	 *            The id of the primary order target (-1 if none)
	 * @param targetX
	 *            The X coordinate of the primary order target (-1 if none)
	 * @param targetY
	 *            The Y coordinate of the primary order target (-1 if none)
	 * @param region
	 *            The region of the primary order target's coordinates (-1 if none)
	 * @param secondary
	 *            The name of the secondary order.
	 */
	public OrderPercept(String primary, int targetUnit, int targetX, int targetY, int targetRegion, String secondary) {
		super(Percepts.ORDER, new Identifier(primary), new Numeral(targetUnit), new Numeral(targetX),
				new Numeral(targetY), new Numeral(targetRegion), new Identifier(secondary));
	}
}