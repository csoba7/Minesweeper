import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JOptionPane;

/**
 * A játék grafikus kezelõfelületének osztálya.
 * @author Bacso Krisztian
 *
 */
public class GUI extends JFrame {

	/**
	 * Random számhoz szükséges változó.
	 */
	Random rand = new Random();
	
	/**
	 * Idõzítõhöz szükséges dátum.
	 */
	Date startDate = new Date();
	
	/**
	 * Pálya szélessége, ami nehezségi szinttõl függ.
	 */
	int width = 8;
	
	/**
	 * Pálya magassága, ami a nehézségi szinttõl függ.
	 */
	int height = 8;
	
	/**
	 * Négyzetek közötti rés nagysága.
	 */
	int spacing = 2;
	
	/**
	 * Négyzetek nagysága.
	 */
	int sqrsize = 40;
	
	/**
	 * Aknák száma, ami a nehezségi szinttõl függ.
	 */
	int minesNo = 10;
	
	/**
	 * Egér X koordinátája.
	 */
	public int mx;
	
	/**
	 * Egér Y koordinátája.
	 */
	public int my;
	
	/**
	 * Játékban eltelt idõ másodpercben.
	 */
	public int sec = 0;
	
	/**
	 * Nyert-e a játékos.
	 */
	public boolean victory = false;
	
	/**
	 * Vesztett-e a játékos.
	 */
	public boolean defeat = false;
	
	/**
	 * A játék alaphelyzetbe állítás alatt van-e.
	 */
	public boolean reset = false;
	
	/**
	 * A felugró ablak meghívásainak a száma.
	 */
	public int called = 0;
	
	/**
	 * Aknák pozíciója.
	 */
	Set<Integer> mines = new HashSet<>();
	
	/**
	 * Négyzetek aknák szomsédságának számossága.
	 */
	List<Integer> near = new ArrayList<>();
	
	/**
	 * Felfedezett négyzetek pozóciója.
	 */
	Set<Integer> revealed = new HashSet<>();
	
	/**
	 * Zászlóval jelölt négyzetek pozíciója.
	 */
	Set<Integer> flagged = new HashSet<>();
	
	/**
	 * Beolvasott sorok tárolója.
	 */
	List<String> lines = new ArrayList<>();
	
	/**
	 * Új menüsor létrehozása.
	 */
	JMenuBar menubar = new JMenuBar();
	
	/**
	 * Új menü létrehozása.
	 */
	JMenu menu = new JMenu("Menü");
	
	/**
	 * Játék menü elem léterhozása.
	 */
	JMenuItem play = new JMenuItem("Játék");
	
	/**
	 * Fa nehézségi szint beállításához szükséges menü elem léterhozása.
	 */
	JMenuItem wood = new JMenuItem("Fa");
	
	/**
	 * Gyémánt nehézségi szint beállításához szükséges menü elem léterhozása.
	 */
	JMenuItem dia = new JMenuItem("Gyémánt");
	
	/**
	 * Kihívó nehézségi szint beállításához szükséges menü elem léterhozása.
	 */
	JMenuItem chall = new JMenuItem("Kihívó");
	
	/**
	 * Ranglista menü eleme léterhozása.
	 */
	JMenuItem rank = new JMenuItem("Ranglista");
	
	File file = new File("ranglista.txt");
	
	/**
	 * GUI osztály konstrukotra.
	 */
	public GUI() {
		
	/**
	 * Ablak címének beállítása.
	 */
	this.setTitle("Aknakeresõ");
	
	/**
	 * Ablak méretének beállítása.
	 */
	this.setSize(width*sqrsize+12,(height+3)*sqrsize);
	
	/**
	 * Menüsor hozzáadása az ablakhoz.
	 */
	this.setJMenuBar(menubar);
	
	/**
	 * Menü hozzáadása a menüsorhoz.
	 */
	menubar.add(menu);
	
	/**
	 * Játék elem hozzáadása a menühöz.
	 */
	menu.add(play);
	
	/**
	 * Fa nehezségi szint elem hozzáadása a menühöz.
	 */
	menu.add(wood);
	
	/**
	 * Gyémánt nehezségi szint elem hozzáadása a menühöz.
	 */
	menu.add(dia);
	
	/**
	 * Kihívó nehezségi szint elem hozzáadása a menühöz.
	 */
	menu.add(chall);
	
	/**
	 * Ranglista elem hozzáadása a menühöz.
	 */
	menu.add(rank);
	
	/**
	 * Listener hozzáadása a játék elemhez.
	 */
	play.addActionListener(new playAction());
	
	/**
	 * Listener hozzáadása a fa nehézségi szint elemhez.
	 */
	wood.addActionListener(new woodAction());
	
	/**
	 * Listener hozzáadása a gyémánt nehézségi szint elemhez.
	 */
	dia.addActionListener(new diaAction());
	
	/**
	 * Listener hozzáadása a kihívó nehézségi szint elemhez.
	 */
	chall.addActionListener(new challAction());
	
	/**
	 * Listener hozzáadása a ranglista elemhez.
	 */
	rank.addActionListener(new rankAction());
	
	/**
	 * Ablak bezárásának beállítása.
	 */
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	/**
	 * Ablak láthatóságának beállítása.
	 */
	this.setVisible(true);
	
	/**
	 * Ablak méretezhetõségének beállítása.
	 */
	this.setResizable(true);
	
	/**
	 * Random aknák generálása.
	 */
	while(mines.size() != minesNo)
		mines.add(rand.nextInt(width*height));
		
	/**
	 * Szomszédok számának hozzáadása a tárolóhoz.
	 */
	for(int i = 0; i < height*width;i++) {
		near.add(nearNo(i));
	}
	
	/**
	 * Szomszédok tároló kiírása.
	 */
	for(int i = 0; i < width*height; i++) {
		System.out.print(near.get(i)+ " ");
		if(i%width == width-1) System.out.println();
		}
	
	/**
	 * Tábla létrehozása.
	 */
	Board board = new Board();
	
	/**
	 * Tábla hozzáadása az ablakhoz.
	 */
	this.setContentPane(board);
	
	/**
	 * Egér mozgatásának lekövetéséhez szükséges változó.
	 */
	Move move = new Move();
	
	/**
	 * Listener hozzáadása az egér mozgatásához.
	 */
	this.addMouseMotionListener(move);
	
	/**
	 * Egér kattintásának figyeléséhez szükséges változó.
	 */
	Click click = new Click();
	
	/**
	 * Egér kattintáshoz adott Listener.
	 */
	this.addMouseListener(click);
}

	/**
	 * Tábla osztály, ahol megvalósul a játékhoz készült grafikák.
	 * @author Bacso Krisztian
	 *
	 */
	public class Board extends JPanel {
		/**
		 * Az elemek kirajzolásáért felelõs függvény, ahol a Graphics osztály függvényet használjuk.
		 * @param g Graphics osztály típusú változó szükséges.
		 */
		public void paintComponent(Graphics g) {
			g.setColor(Color.lightGray); //világosszürkére állítja a színt
			g.fillRect(0, 0, width*sqrsize, (height+1)*sqrsize); //háttér téglalap kirajzolása
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					g.setColor(Color.gray); //szürkére állítja a színt a négyzetekhez
					//if(mines.contains(j*width+i))
						//g.setColor(Color.red);
					if(revealed.contains(j*width+i)) { 
						g.setColor(Color.white);	//ha fel van tárva a négyzet fehérként jeleníti meg
						if(mines.contains(j*width+i))
							g.setColor(Color.red);	//ha fel van tárva a négyzet és aknát tartalmaz piros a négyzet
					}
					if(mx >= 2*spacing+i*sqrsize && mx < i*sqrsize+sqrsize+spacing && my >= 2*spacing+j*sqrsize+1.5*sqrsize+26 && my < j*sqrsize+2.5*sqrsize+26+2*spacing)
						g.setColor(Color.yellow); //ha az egér egy négyzet felett van akkor sárga a négyzet
					g.fillRect(spacing+i*sqrsize, spacing+j*sqrsize+sqrsize, sqrsize-2*spacing, sqrsize-2*spacing); //négyzetek létrehozása az adott színnel
					if(revealed.contains(j*width+i)) {
						g.setColor(Color.black);	//szín feketére állítása
						g.setFont(new Font("Tahoma", Font.BOLD, 25)); //szöveg stílusának és méretének beállítása
						if(mines.contains(j*width+i)) {
							g.drawString("¤", i*sqrsize+(sqrsize-15)/2, j*sqrsize+2*sqrsize-10); //aknát tartalmazó négyzetek a '¤' karaktert jelenítik meg
						}
						else if(revealed.contains(j*width+i)) {
							g.drawString(Integer.toString(near.get(j*width+i)), i*sqrsize+(sqrsize-15)/2, j*sqrsize+2*sqrsize-10); //szomszédos négyzetek kiírják hány aknával szomszédosak
						}
					}
						if(flagged.contains(j*width+i)){
							g.setColor(Color.black); //szín feketére állítása
							g.setFont(new Font("Tahoma", Font.BOLD, 25)); //szöveg stílusának és méretének beállítása
							g.drawString("×", i*sqrsize+(sqrsize-15)/2, j*sqrsize+2*sqrsize-10); //azokra a négyzetekre amiket jobb klikkel megjelöltek kiírja a '×' karaktert
					}
				}
			}
			
			//Idõzítõ
			
			g.setColor(Color.black); //szín feketére állítása
			g.fillRect(60,5,75,35);	//négyzet létrehozása
			if(!victory && !defeat)
			sec =(int) (new Date().getTime()-startDate.getTime()) / 1000; //számláló meghatározása ha nem ért véget a játék
			g.setColor(Color.white); //szín fehérre állítása
			g.setFont(new Font("Tahoma", Font.PLAIN,25)); //számlaló szöveg stílusának és méretének beállítása
			g.drawString(Integer.toString(sec), 63, 33); //számláló kiírása
			
			//Bomba számláló
			g.setColor(Color.black); //szín feketére állítása
			g.fillRect(5,5 ,50, 35);	//négyzet létrehozása
			g.setColor(Color.white);	//szín fehérre állítása
			g.setFont(new Font("Tahoma", Font.PLAIN,25));	//bomba számlaló szöveg stílusának és méretének beállítása
			g.drawString(Integer.toString(mines.size()-flagged.size()), 9, 32);	//bomba számláló kiírása
		}
	}
	
	/**
	 * Egér mozgatását figyelõ osztály, ahonna megkapjuk az egér koordinátáit.
	 * @author Bacso Krisztian
	 *
	 */
	public class Move implements MouseMotionListener{

		/**
		 * MousMotionListener miatt muszáj implementálni, de nem használjuk.
		 * @param e MouseEvent típusú változó, ami megadja, hogy mi tötrénik az egér mozgatásával
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * Az egér mozgását figyeli és visszaadja a az egér koordinátáit.
		 * @param e MouseEvent típusú változó, ami megadja, hogy mi tötrénik az egér mozgatásával.
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			//System.out.println("Mozog az egér");
			mx = e.getX();
			my = e.getY();
			//System.out.println("X: " + mx + " Y: " + my);
		}
		
	}
	
	
	/**
	 * Egér kattintásait figyelõ osztály. Ennek segítségével, tudjuk megadni, hogy mit csináljon az egér jobb és ball klikk esetén.
	 * @author Bacso Krisztian
	 *
	 */
	public class Click implements MouseListener{

		/**
		 * Bal és jobb klikk hatását tudjuk meghatározni.
		 * @param e MouseEvent típusú változó, ami megadja, hogy mi tötrénik az egér mozgatásával.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(SwingUtilities.isLeftMouseButton(e)) {	//bal klikk meghatározása
			if(whichBox() != -1 && !flagged.contains(whichBox()) && !victory && !defeat) //bal klikk hatására feltárja a négyzetet
				revealed.add(whichBox());
			
	/*		if(whichBox() != -1) {
		System.out.println("Az egeret megnyomták a " + whichBox() + ". dobozban");
			}
			else
				System.out.println("Dobozon kívüli nyomás");*/
			}
			
			if(SwingUtilities.isRightMouseButton(e)) 
			{
				if(whichBox() != -1 && !flagged.contains(whichBox()) && !revealed.contains(whichBox()) && !victory && !defeat)
					flagged.add(whichBox()); //jobb klikk hatására megjelöli a négyzetet
				else
					flagged.remove(whichBox()); //jobb klikk hatására leveszi a jelölést a négyzetrõl
			}
		}

		/**
		 * MouseListener miatt muszáj implementálni, de nem használjuk.
		 * @param e MouseEvent típusú változó, ami megadja, hogy mi tötrénik az egér mozgatásával
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		/**
		 * MouseListener miatt muszáj implementálni, de nem használjuk.
		 * @param e MouseEvent típusú változó, ami megadja, hogy mi tötrénik az egér mozgatásával
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * MouseListener miatt muszáj implementálni, de nem használjuk.
		 * @param e MouseEvent típusú változó, ami megadja, hogy mi tötrénik az egér mozgatásával
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * MouseListener miatt muszáj implementálni, de nem használjuk.
		 * @param e MouseEvent típusú változó, ami megadja, hogy mi tötrénik az egér mozgatásával
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * Az osztály azt figyeli, hogy rányomunk-e a menü játék elemére. Ha rányomtunk visszaállítja a játékot az adott szint alapértelmezettjére.
	 * @author Bacso Krisztian
	 *
	 */
	public class playAction implements ActionListener{

		/**
		 * A játék elem megfigyelést megvalósító függvény.
		 * @param e Egy ActionEvent változó, ami segít figyelni, hogy rákatintanak-e játék elemre.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			reset();
		}
		
	}
	
	/**
	 * Az osztály azt figyeli, hogy rányomunk-e a menü fa nehézségi szint elemére. Ha rányomtunk beállítja a fa nehézséget.
	 * @author Bacso Krisztian
	 *
	 */
	public class woodAction implements ActionListener{

		/**
		 * A fa nehézségi szint elem megfigyelést megvalósító függvény.
		 * @param e Egy ActionEvent változó, ami segít figyelni, hogy rákatintanak-e fa nehézségi szint elemre.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			width = 8;
			height = 8;
			minesNo = 10;
			reset();
		}
		
	}
	
	/**
	 * Az osztály azt figyeli, hogy rányomunk-e a menü gyémánt nehézségi szint elemére. Ha rányomtunk beállítja a gyémánt nehézséget.
	 * @author Bacso Krisztian
	 *
	 */
	public class diaAction implements ActionListener{

		/**
		 * A gyémánt nehézségi szint elem megfigyelést megvalósító függvény.
		 * @param e Egy ActionEvent változó, ami segít figyelni, hogy rákatintanak-e gyémánt nehézségi szint elemre.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			width = 16;
			height = 16;
			minesNo = 40;
			reset();
		}
		
	}
	
	/**
	 * Az osztály azt figyeli, hogy rányomunk-e a menü kihívó nehézségi szint elemére. Ha rányomtunk beállítja a kihívó nehezséget.
	 * @author Bacso Krisztian
	 *
	 */
	public class challAction implements ActionListener{

		/**
		 * A kihívó nehézségi szint elem megfigyelést megvalósító függvény.
		 * @param e Egy ActionEvent változó, ami segít figyelni, hogy rákatintanak-e kihívó nehézségi szint elemre.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			width = 30;
			height = 16;
			minesNo = 100;
			reset();
		}
		
	}
	
	/**
	 * Az osztály azt figyeli, hogy rányomunk-e a menü ranglista elemére. Ha rányomtunk egy felugró ablak megmutatja a ranglistát.
	 * @author Bacso Krisztian
	 *
	 */
	public class rankAction implements ActionListener{

		/**
		 * A ranglista elem megnyomásakor felugró ablakot valósítja meg.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			read();
			JOptionPane.showMessageDialog(null,"Fa: " + lines.get(0) + " "+ lines.get(1) + "\n" + "Gyémánt: " + lines.get(2) + " "+ lines.get(3) + "\n" + "Kihívó: " + lines.get(4) + " "+ lines.get(5));
		}
		
	}
	
	/**
	 * A függvény figyeli, hogy végetért-e a játék.
	 */
	public void checkGameStatus() {
		for(int i = 0; i < width*height;i++) {
			if(revealed.contains(i) && mines.contains(i)) {
				defeat = true;
				if(called == 0) {
					defeat();
					called++;
				}
			}
		}
		
		if(revealed.size() == (width*height-mines.size())) {
			victory = true;
			if(called == 0) {
				victory();
				called++;
			}
		}
	}
	
	/**
	 * A függvény egy felugró ablakban jelzi a játékosnak, hogy nyert és ha felkerült a ranglistrá megadhatja a nevét.
	 */
	public void victory() {
		JOptionPane.showMessageDialog(null, "Gratulálok Ön nyert!");
		read();	//beolvasás
		if(width == 8 && Integer.valueOf(lines.get(1)) > sec) {		//fa ranglista vizsgálata
		String name = JOptionPane.showInputDialog("Név");
		lines.set(0, name);
		lines.set(1, Integer.toString(sec));
		}
		
		if(width == 16 && Integer.valueOf(lines.get(3)) > sec) {	//gyémánt ranglista vizsgálata
			String name = JOptionPane.showInputDialog("Név");
			lines.set(2, name);
			lines.set(3, Integer.toString(sec));
			}
		
		if(width == 30 && Integer.valueOf(lines.get(5)) > sec) {	//kihívó ranglista vizsgálata
			String name = JOptionPane.showInputDialog("Név");
			lines.set(4, name);
			lines.set(5, Integer.toString(sec));
			}
		write();	//fájlba írás
	}
	
	/**
	 * Vereség esetén egy felugró ablak üzen a játékosnak.
	 */
	public void defeat() {
		JOptionPane.showMessageDialog(null, "Sajnos ez most nem sikerült. Sok sikert a következõ játékhoz!");
	}
	
	/**
	 * Visszaállítja alaphelyzetbe a játékot.
	 */
	public void reset() {
		called = 0;
		reset = true;
		victory = false;
		defeat = false;
		mines.clear();
		near.clear();
		flagged.clear();
		revealed.clear();
		startDate = new Date();
		while(mines.size() != minesNo)
			mines.add(rand.nextInt(width*height));		
		for(int i = 0; i < height*width;i++) {
			near.add(nearNo(i));
		}
		reset = false;
	}
	
	/**
	 * Meghatározza, hogy hanyadik négyzet felett van az egér.
	 * @return Ha négyzet felett van vissza adja annak a szánát, ha nem négyzet felett van -1-et ad vissza.
	 */
	public int whichBox() {
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(mx >= 2*spacing+i*sqrsize && mx < i*sqrsize+sqrsize && my >= 2*spacing+j*sqrsize+26+1.5*sqrsize && my < j*sqrsize+2.5*sqrsize+26)
					return j*width+i;
			}
		}
		return -1;
	}
	
	/**
	 * Beolvassa a ranglistához szükséges fájlt és a lines tárolóba teszi azokat.
	 */
	public void read() {
		try {
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				lines.add(scan.nextLine());
			}
			scan.close();
			}
			catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
	}
	
	/**
	 * A lines tárolót kiírja az adott fájlba.
	 */
	public void write() {
		try {
		FileWriter fw = new FileWriter("ranglista.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i = 0; i < 6; i++)
			bw.write(lines.get(i) + "\n");
		bw.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Adott négyzet pozíciójára meghatározza, hogy hány akna szomszédja van.
	 * @param pos Integer típusú változó, ami a négyzet pozíciójat jelenti.
	 * @return Az akna szomszédok számát határozza meg.
	 */
	public int nearNo(int pos) {
		if(mines.contains(pos)) return 9;
		else if(pos == 0) {
			return getNearNo(new int[] {1,width,width+1},3); //bal felsõ négyzet
		}
		else if(pos == width-1) {
			return getNearNo(new int[] {width-2,2*width-1,2*width-2},3); //jobb felsõ négyzet
		}

		else if(pos == (height-1)*width) {
			return getNearNo(new int[] {pos+1,pos-width,pos-width+1},3); //bal alsó négyzet
		}

		else if(pos == height*width-1) {
			return getNearNo(new int[] {pos-1,pos-width,pos-width-1},3); //jobb alsó négyzet
		}
		else if(pos%width == 0 && pos != 0 && pos != (height-1)*width) {
			return getNearNo(new int[] {pos-width,pos-width+1,pos+1,pos+width+1,pos+width},5); //bal szélsõ oszlop
		}
		else if(pos%width == width-1 && pos != width-1 && pos != height*width-1) {
			return getNearNo(new int[] {pos-width,pos-width-1,pos-1,pos+width-1,pos+width},5); //jobb szélsõ oszlop
		}
		else if(0 < pos && pos < width-1 && pos != 0 && pos != width-1) {
			return getNearNo(new int[] {pos-1,pos+width-1,pos+width,pos+width+1,pos+1},5); //felsõ sor
		}
		else if((height-1)*width < pos && pos < width*height-1 && pos != (height-1)*width && pos != width*height-1) {
			return getNearNo(new int[] {pos-1,pos-width-1,pos-width,pos-width+1,pos+1},5);	//alsó sor
		}
		else return getNearNo(new int[] {pos-width-1,pos-width,pos-width+1,pos-1,pos+1,pos+width-1,pos+width,pos+width+1},8); //maradék négyzetek
	}
	
	/**
	 * Meghatározza, hogy hány akna van az adott tömb négyzeteiben.
	 * @param tmb Meghatározandó négyzetek tömbje.
	 * @param size Tömb mérete.
	 * @return Aknák száma a több négyzeteiben.
	 */
	public int getNearNo(int[] tmb, int size) {
		int ret = 0;
		for(int i = 0; i < size; i++) {
			if(mines.contains(tmb[i])) ret++;
		}
		return ret;
	}
}
