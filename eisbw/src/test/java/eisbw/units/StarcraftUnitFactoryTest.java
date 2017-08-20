package eisbw.units;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bwapi.Unit;
import bwapi.UnitType;
import eisbw.BwapiUtility;

public class StarcraftUnitFactoryTest {
	private StarcraftUnitFactory factory;

	@Mock
	private Unit unit;
	@Mock
	private UnitType unitType;

	@Test
	public void test() {
		MockitoAnnotations.initMocks(this);
		this.factory = new StarcraftUnitFactory(null);
		when(this.unit.exists()).thenReturn(true);
		when(this.unit.isVisible()).thenReturn(true);
		when(this.unit.getType()).thenReturn(this.unitType);
		BwapiUtility.clearCache(this.unit);
		assertEquals(1, this.factory.create(this.unit).perceivers.size());
	}
}
