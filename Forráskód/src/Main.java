import javax.swing.JOptionPane;

public class Main implements Runnable {
	/**
	 * Grafikus interf�sz l�trehoz�sa.
	 */
	GUI gui = new GUI();
/**
 * Main f�ggv�ny, ahol a j�t�k fut.
 */
	public static void main(String[] args) {
		new Thread(new Main()).start();
	}
/**
 * J�t�k fut�s��rt fel�s f�ggv�ny
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
