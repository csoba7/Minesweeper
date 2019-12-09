import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.ActionEvent;

import org.junit.jupiter.api.Test;

class GameTest {
	GUI gui = new GUI();
	
	@Test
	void checkGameStatusTest() {
		gui.mines.add(15);
		gui.revealed.add(15);
		gui.checkGameStatus();
		assertEquals(true, gui.defeat);
		assertEquals(false, gui.victory);
	}

	@Test
	void whichBoxTest() {
		gui.mx = 0;
		gui.my = 0;
		assertEquals(-1, gui.whichBox());
	}
	
	@Test
	void resetTest() {
		gui.victory = true;
		gui.called = 15;
		gui.flagged.add(15);
		gui.reset();
		assertEquals(false, gui.victory);
		assertEquals(0, gui.called);
		assertEquals(true, gui.flagged.isEmpty());
	}
	
	@Test
	void readTest() {
		gui.read();
		assertEquals("Próba",gui.lines.get(2));
		assertEquals(5000,Integer.valueOf(gui.lines.get(3)));
	}
	
	@Test
	void writeTest() {
		gui.read();
		gui.lines.set(4, "Teszt");
		gui.lines.set(5, "151515");
		gui.write();
		gui.read();
		assertEquals("Teszt",gui.lines.get(4));
		assertEquals(151515,Integer.valueOf(gui.lines.get(5)));
		gui.lines.set(4, "Próba");
		gui.lines.set(5, "5000");
		gui.write();
	}
	
	@Test
	public void getNearNoTest() {
		gui.mines.add(0);
		assertEquals(1,gui.getNearNo(new int[] {0}, 1));
	}
	
	@Test
	public void nearNoTest() {
		gui.mines.add(0);
		assertEquals(9,gui.nearNo(0));
	}
	
	@Test
	public void diaTest() {
		GUI.diaAction da = gui.new diaAction();
		ActionEvent e = null;
		da.actionPerformed(e);
		assertEquals(16,gui.width);
	}
	
	@Test
	public void challTest() {
		GUI.challAction ca = gui.new challAction();
		ActionEvent e = null;
		ca.actionPerformed(e);
		assertEquals(100,gui.minesNo);
	}
	
	@Test
	public void woodTest() {
		gui.height = 16;
		GUI.woodAction wa = gui.new woodAction();
		ActionEvent e = null;
		wa.actionPerformed(e);
		assertEquals(8,gui.height);
	}
}
