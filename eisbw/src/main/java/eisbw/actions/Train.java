package eisbw.actions;

import eis.exceptions.ActException;
import eis.iilang.*;
import java.util.LinkedList;
import jnibwapi.*;
import jnibwapi.types.UnitType;

public class Train extends StarcraftAction {

    public Train(JNIBWAPI api) {
        super(api);
    }

    @Override
    public boolean isValid(Action action) {
        LinkedList<Parameter> parameters = action.getParameters();
        if (parameters.size() == 1) {
            return parameters.get(0) instanceof Identifier && utility.getUnitType(((Identifier) parameters.get(0)).getValue()) != null;
        }
        return false;
    }

    @Override
    public boolean canExecute(Unit unit, Action action) {
        return unit.getType().isProduceCapable();
    }

    @Override
    public void execute(Unit unit, Action action) throws ActException {
        LinkedList<Parameter> parameters = action.getParameters();
        UnitType unitType = utility.getUnitType(((Identifier) parameters.get(0)).getValue());

        boolean result = unit.train(unitType); 
        if (!result) {
            throw new ActException(ActException.FAILURE);
        }
    }

    @Override
    public String toString() {
        return "train(Type)";
    }
}