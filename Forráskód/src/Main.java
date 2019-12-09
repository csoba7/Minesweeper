import javax.swing.JOptionPane;

public class Main implements Runnable {
	/**
	 * Grafikus interfész létrehozása.
	 */
	GUI gui = new GUI();
/**
 * Main függvény, ahol a játék fut.
 */
	public static void main(String[] args) {
		new Thread(new Main()).start();
	}
/**
 * Játék futásáért felõs függvény
 */
	@Override
	public void run() {
		while (true) {
			gui.repaint();
			if(!gui.reset)
				gui.checkGameStatus();
	}
	}
}
