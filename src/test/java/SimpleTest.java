
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTest{
	@Test
    public void test() throws IOException{
		
		Battle battle = new Battle();
		
		Grandpa grandpa=new Grandpa("Grandpa",battle,true);
		HuLuWa huluwa=new HuLuWa(1,HuLuWa.Color.red,battle,true);
		Scorpion scorpion=new Scorpion("Scorpion",battle,true);
		
		battle.placeCreature(grandpa,1,1);
		battle.placeCreature(huluwa,2,2);
		battle.placeCreature(scorpion,3,3);
		
		assertTrue(battle.getLand().getObj(1,1) instanceof Grandpa);
		assertEquals(battle.getCreatures().size(),3);
		assertTrue(grandpa.hasAliveCompanion(2, 2));
		assertTrue(battle.getLand().isEmpty(0,0));
		assertFalse(grandpa.hasAliveCompanion(0, 0));
		assertTrue(grandpa.hasAliveEnemy(3, 3));
		assertTrue(grandpa.isHuman(grandpa));
		assertFalse(scorpion.isHuman(scorpion));
	}
}