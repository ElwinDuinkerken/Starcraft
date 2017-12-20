package eisbw;

import org.openbw.bwapi4j.unit.PlayerUnit;

import eis.iilang.Action;

public class BwapiAction {
	private final PlayerUnit unit;
	private final Action action;

	public BwapiAction(PlayerUnit unit, Action action) {
		this.unit = unit;
		this.action = action;
	}

	public PlayerUnit getUnit() {
		return this.unit;
	}

	public Action getAction() {
		return this.action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.action == null) ? 0 : this.action.hashCode());
		result = prime * result + ((this.unit == null) ? 0 : this.unit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null || !(obj instanceof BwapiAction)) {
			return false;
		}
		BwapiAction other = (BwapiAction) obj;
		if (this.action == null) {
			if (other.action != null) {
				return false;
			}
		} else if (!this.action.equals(other.action)) {
			return false;
		}
		if (this.unit == null) {
			if (other.unit != null) {
				return false;
			}
		} else if (!this.unit.equals(other.unit)) {
			return false;
		}
		return true;
	}
}
