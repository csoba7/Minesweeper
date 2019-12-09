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
 * A j�t�k grafikus kezel�fel�let�nek oszt�lya.
 * @author Bacso Krisztian
 *
 */
public class GUI extends JFrame {

	/**
	 * Random sz�mhoz sz�ks�ges v�ltoz�.
	 */
	Random rand = new Random();
	
	/**
	 * Id�z�t�h�z sz�ks�ges d�tum.
	 */
	Date startDate = new Date();
	
	/**
	 * P�lya sz�less�ge, ami nehezs�gi szintt�l f�gg.
	 */
	int width = 8;
	
	/**
	 * P�lya magass�ga, ami a neh�zs�gi szintt�l f�gg.
	 */
	int height = 8;
	
	/**
	 * N�gyzetek k�z�tti r�s nagys�ga.
	 */
	int spacing = 2;
	
	/**
	 * N�gyzetek nagys�ga.
	 */
	int sqrsize = 40;
	
	/**
	 * Akn�k sz�ma, ami a nehezs�gi szintt�l f�gg.
	 */
	int minesNo = 10;
	
	/**
	 * Eg�r X koordin�t�ja.
	 */
	public int mx;
	
	/**
	 * Eg�r Y koordin�t�ja.
	 */
	public int my;
	
	/**
	 * J�t�kban eltelt id� m�sodpercben.
	 */
	public int sec = 0;
	
	/**
	 * Nyert-e a j�t�kos.
	 */
	public boolean victory = false;
	
	/**
	 * Vesztett-e a j�t�kos.
	 */
	public boolean defeat = false;
	
	/**
	 * A j�t�k alaphelyzetbe �ll�t�s alatt van-e.
	 */
	public boolean reset = false;
	
	/**
	 * A felugr� ablak megh�v�sainak a sz�ma.
	 */
	public int called = 0;
	
	/**
	 * Akn�k poz�ci�ja.
	 */
	Set<Integer> mines = new HashSet<>();
	
	/**
	 * N�gyzetek akn�k szoms�ds�g�nak sz�moss�ga.
	 */
	List<Integer> near = new ArrayList<>();
	
	/**
	 * Felfedezett n�gyzetek poz�ci�ja.
	 */
	Set<Integer> revealed = new HashSet<>();
	
	/**
	 * Z�szl�val jel�lt n�gyzetek poz�ci�ja.
	 */
	Set<Integer> flagged = new HashSet<>();
	
	/**
	 * Beolvasott sorok t�rol�ja.
	 */
	List<String> lines = new ArrayList<>();
	
	/**
	 * �j men�sor l�trehoz�sa.
	 */
	JMenuBar menubar = new JMenuBar();
	
	/**
	 * �j men� l�trehoz�sa.
	 */
	JMenu menu = new JMenu("Men�");
	
	/**
	 * J�t�k men� elem l�terhoz�sa.
	 */
	JMenuItem play = new JMenuItem("J�t�k");
	
	/**
	 * Fa neh�zs�gi szint be�ll�t�s�hoz sz�ks�ges men� elem l�terhoz�sa.
	 */
	JMenuItem wood = new JMenuItem("Fa");
	
	/**
	 * Gy�m�nt neh�zs�gi szint be�ll�t�s�hoz sz�ks�ges men� elem l�terhoz�sa.
	 */
	JMenuItem dia = new JMenuItem("Gy�m�nt");
	
	/**
	 * Kih�v� neh�zs�gi szint be�ll�t�s�hoz sz�ks�ges men� elem l�terhoz�sa.
	 */
	JMenuItem chall = new JMenuItem("Kih�v�");
	
	/**
	 * Ranglista men� eleme l�terhoz�sa.
	 */
	JMenuItem rank = new JMenuItem("Ranglista");
	
	File file = new File("ranglista.txt");
	
	/**
	 * GUI oszt�ly konstrukotra.
	 */
	public GUI() {
		
	/**
	 * Ablak c�m�nek be�ll�t�sa.
	 */
	this.setTitle("Aknakeres�");
	
	/**
	 * Ablak m�ret�nek be�ll�t�sa.
	 */
	this.setSize(width*sqrsize+12,(height+3)*sqrsize);
	
	/**
	 * Men�sor hozz�ad�sa az ablakhoz.
	 */
	this.setJMenuBar(menubar);
	
	/**
	 * Men� hozz�ad�sa a men�sorhoz.
	 */
	menubar.add(menu);
	
	/**
	 * J�t�k elem hozz�ad�sa a men�h�z.
	 */
	menu.add(play);
	
	/**
	 * Fa nehezs�gi szint elem hozz�ad�sa a men�h�z.
	 */
	menu.add(wood);
	
	/**
	 * Gy�m�nt nehezs�gi szint elem hozz�ad�sa a men�h�z.
	 */
	menu.add(dia);
	
	/**
	 * Kih�v� nehezs�gi szint elem hozz�ad�sa a men�h�z.
	 */
	menu.add(chall);
	
	/**
	 * Ranglista elem hozz�ad�sa a men�h�z.
	 */
	menu.add(rank);
	
	/**
	 * Listener hozz�ad�sa a j�t�k elemhez.
	 */
	play.addActionListener(new playAction());
	
	/**
	 * Listener hozz�ad�sa a fa neh�zs�gi szint elemhez.
	 */
	wood.addActionListener(new woodAction());
	
	/**
	 * Listener hozz�ad�sa a gy�m�nt neh�zs�gi szint elemhez.
	 */
	dia.addActionListener(new diaAction());
	
	/**
	 * Listener hozz�ad�sa a kih�v� neh�zs�gi szint elemhez.
	 */
	chall.addActionListener(new challAction());
	
	/**
	 * Listener hozz�ad�sa a ranglista elemhez.
	 */
	rank.addActionListener(new rankAction());
	
	/**
	 * Ablak bez�r�s�nak be�ll�t�sa.
	 */
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	/**
	 * Ablak l�that�s�g�nak be�ll�t�sa.
	 */
	this.setVisible(true);
	
	/**
	 * Ablak m�retezhet�s�g�nek be�ll�t�sa.
	 */
	this.setResizable(true);
	
	/**
	 * Random akn�k gener�l�sa.
	 */
	while(mines.size() != minesNo)
		mines.add(rand.nextInt(width*height));
		
	/**
	 * Szomsz�dok sz�m�nak hozz�ad�sa a t�rol�hoz.
	 */
	for(int i = 0; i < height*width;i++) {
		near.add(nearNo(i));
	}
	
	/**
	 * Szomsz�dok t�rol� ki�r�sa.
	 */
	for(int i = 0; i < width*height; i++) {
		System.out.print(near.get(i)+ " ");
		if(i%width == width-1) System.out.println();
		}
	
	/**
	 * T�bla l�trehoz�sa.
	 */
	Board board = new Board();
	
	/**
	 * T�bla hozz�ad�sa az ablakhoz.
	 */
	this.setContentPane(board);
	
	/**
	 * Eg�r mozgat�s�nak lek�vet�s�hez sz�ks�ges v�ltoz�.
	 */
	Move move = new Move();
	
	/**
	 * Listener hozz�ad�sa az eg�r mozgat�s�hoz.
	 */
	this.addMouseMotionListener(move);
	
	/**
	 * Eg�r kattint�s�nak figyel�s�hez sz�ks�ges v�ltoz�.
	 */
	Click click = new Click();
	
	/**
	 * Eg�r kattint�shoz adott Listener.
	 */
	this.addMouseListener(click);
}

	/**
	 * T�bla oszt�ly, ahol megval�sul a j�t�khoz k�sz�lt grafik�k.
	 * @author Bacso Krisztian
	 *
	 */
	public class Board extends JPanel {
		/**
		 * Az elemek kirajzol�s��rt felel�s f�ggv�ny, ahol a Graphics oszt�ly f�ggv�nyet haszn�ljuk.
		 * @param g Graphics oszt�ly t�pus� v�ltoz� sz�ks�ges.
		 */
		public void paintComponent(Graphics g) {
			g.setColor(Color.lightGray); //vil�gossz�rk�re �ll�tja a sz�nt
			g.fillRect(0, 0, width*sqrsize, (height+1)*sqrsize); //h�tt�r t�glalap kirajzol�sa
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					g.setColor(Color.gray); //sz�rk�re �ll�tja a sz�nt a n�gyzetekhez
					//if(mines.contains(j*width+i))
						//g.setColor(Color.red);
					if(revealed.contains(j*width+i)) { 
						g.setColor(Color.white);	//ha fel van t�rva a n�gyzet feh�rk�nt jelen�ti meg
						if(mines.contains(j*width+i))
							g.setColor(Color.red);	//ha fel van t�rva a n�gyzet �s akn�t tartalmaz piros a n�gyzet
					}
					if(mx >= 2*spacing+i*sqrsize && mx < i*sqrsize+sqrsize+spacing && my >= 2*spacing+j*sqrsize+1.5*sqrsize+26 && my < j*sqrsize+2.5*sqrsize+26+2*spacing)
						g.setColor(Color.yellow); //ha az eg�r egy n�gyzet felett van akkor s�rga a n�gyzet
					g.fillRect(spacing+i*sqrsize, spacing+j*sqrsize+sqrsize, sqrsize-2*spacing, sqrsize-2*spacing); //n�gyzetek l�trehoz�sa az adott sz�nnel
					if(revealed.contains(j*width+i)) {
						g.setColor(Color.black);	//sz�n feket�re �ll�t�sa
						g.setFont(new Font("Tahoma", Font.BOLD, 25)); //sz�veg st�lus�nak �s m�ret�nek be�ll�t�sa
						if(mines.contains(j*width+i)) {
							g.drawString("�", i*sqrsize+(sqrsize-15)/2, j*sqrsize+2*sqrsize-10); //akn�t tartalmaz� n�gyzetek a '�' karaktert jelen�tik meg
						}
						else if(revealed.contains(j*width+i)) {
							g.drawString(Integer.toString(near.get(j*width+i)), i*sqrsize+(sqrsize-15)/2, j*sqrsize+2*sqrsize-10); //szomsz�dos n�gyzetek ki�rj�k h�ny akn�val szomsz�dosak
						}
					}
						if(flagged.contains(j*width+i)){
							g.setColor(Color.black); //sz�n feket�re �ll�t�sa
							g.setFont(new Font("Tahoma", Font.BOLD, 25)); //sz�veg st�lus�nak �s m�ret�nek be�ll�t�sa
							g.drawString("�", i*sqrsize+(sqrsize-15)/2, j*sqrsize+2*sqrsize-10); //azokra a n�gyzetekre amiket jobb klikkel megjel�ltek ki�rja a '�' karaktert
					}
				}
			}
			
			//Id�z�t�
			
			g.setColor(Color.black); //sz�n feket�re �ll�t�sa
			g.fillRect(60,5,75,35);	//n�gyzet l�trehoz�sa
			if(!victory && !defeat)
			sec =(int) (new Date().getTime()-startDate.getTime()) / 1000; //sz�ml�l� meghat�roz�sa ha nem �rt v�get a j�t�k
			g.setColor(Color.white); //sz�n feh�rre �ll�t�sa
			g.setFont(new Font("Tahoma", Font.PLAIN,25)); //sz�mlal� sz�veg st�lus�nak �s m�ret�nek be�ll�t�sa
			g.drawString(Integer.toString(sec), 63, 33); //sz�ml�l� ki�r�sa
			
			//Bomba sz�ml�l�
			g.setColor(Color.black); //sz�n feket�re �ll�t�sa
			g.fillRect(5,5 ,50, 35);	//n�gyzet l�trehoz�sa
			g.setColor(Color.white);	//sz�n feh�rre �ll�t�sa
			g.setFont(new Font("Tahoma", Font.PLAIN,25));	//bomba sz�mlal� sz�veg st�lus�nak �s m�ret�nek be�ll�t�sa
			g.drawString(Integer.toString(mines.size()-flagged.size()), 9, 32);	//bomba sz�ml�l� ki�r�sa
		}
	}
	
	/**
	 * Eg�r mozgat�s�t figyel� oszt�ly, ahonna megkapjuk az eg�r koordin�t�it.
	 * @author Bacso Krisztian
	 *
	 */
	public class Move implements MouseMotionListener{

		/**
		 * MousMotionListener miatt musz�j implement�lni, de nem haszn�ljuk.
		 * @param e MouseEvent t�pus� v�ltoz�, ami megadja, hogy mi t�tr�nik az eg�r mozgat�s�val
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * Az eg�r mozg�s�t figyeli �s visszaadja a az eg�r koordin�t�it.
		 * @param e MouseEvent t�pus� v�ltoz�, ami megadja, hogy mi t�tr�nik az eg�r mozgat�s�val.
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			//System.out.println("Mozog az eg�r");
			mx = e.getX();
			my = e.getY();
			//System.out.println("X: " + mx + " Y: " + my);
		}
		
	}
	
	
	/**
	 * Eg�r kattint�sait figyel� oszt�ly. Ennek seg�ts�g�vel, tudjuk megadni, hogy mit csin�ljon az eg�r jobb �s ball klikk eset�n.
	 * @author Bacso Krisztian
	 *
	 */
	public class Click implements MouseListener{

		/**
		 * Bal �s jobb klikk hat�s�t tudjuk meghat�rozni.
		 * @param e MouseEvent t�pus� v�ltoz�, ami megadja, hogy mi t�tr�nik az eg�r mozgat�s�val.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(SwingUtilities.isLeftMouseButton(e)) {	//bal klikk meghat�roz�sa
			if(whichBox() != -1 && !flagged.contains(whichBox()) && !victory && !defeat) //bal klikk hat�s�ra felt�rja a n�gyzetet
				revealed.add(whichBox());
			
	/*		if(whichBox() != -1) {
		System.out.println("Az egeret megnyomt�k a " + whichBox() + ". dobozban");
			}
			else
				System.out.println("Dobozon k�v�li nyom�s");*/
			}
			
			if(SwingUtilities.isRightMouseButton(e)) 
			{
				if(whichBox() != -1 && !flagged.contains(whichBox()) && !revealed.contains(whichBox()) && !victory && !defeat)
					flagged.add(whichBox()); //jobb klikk hat�s�ra megjel�li a n�gyzetet
				else
					flagged.remove(whichBox()); //jobb klikk hat�s�ra leveszi a jel�l�st a n�gyzetr�l
			}
		}

		/**
		 * MouseListener miatt musz�j implement�lni, de nem haszn�ljuk.
		 * @param e MouseEvent t�pus� v�ltoz�, ami megadja, hogy mi t�tr�nik az eg�r mozgat�s�val
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		/**
		 * MouseListener miatt musz�j implement�lni, de nem haszn�ljuk.
		 * @param e MouseEvent t�pus� v�ltoz�, ami megadja, hogy mi t�tr�nik az eg�r mozgat�s�val
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * MouseListener miatt musz�j implement�lni, de nem haszn�ljuk.
		 * @param e MouseEvent t�pus� v�ltoz�, ami megadja, hogy mi t�tr�nik az eg�r mozgat�s�val
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * MouseListener miatt musz�j implement�lni, de nem haszn�ljuk.
		 * @param e MouseEvent t�pus� v�ltoz�, ami megadja, hogy mi t�tr�nik az eg�r mozgat�s�val
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * Az oszt�ly azt figyeli, hogy r�nyomunk-e a men� j�t�k elem�re. Ha r�nyomtunk vissza�ll�tja a j�t�kot az adott szint alap�rtelmezettj�re.
	 * @author Bacso Krisztian
	 *
	 */
	public class playAction implements ActionListener{

		/**
		 * A j�t�k elem megfigyel�st megval�s�t� f�ggv�ny.
		 * @param e Egy ActionEvent v�ltoz�, ami seg�t figyelni, hogy r�katintanak-e j�t�k elemre.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			reset();
		}
		
	}
	
	/**
	 * Az oszt�ly azt figyeli, hogy r�nyomunk-e a men� fa neh�zs�gi szint elem�re. Ha r�nyomtunk be�ll�tja a fa neh�zs�get.
	 * @author Bacso Krisztian
	 *
	 */
	public class woodAction implements ActionListener{

		/**
		 * A fa neh�zs�gi szint elem megfigyel�st megval�s�t� f�ggv�ny.
		 * @param e Egy ActionEvent v�ltoz�, ami seg�t figyelni, hogy r�katintanak-e fa neh�zs�gi szint elemre.
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
	 * Az oszt�ly azt figyeli, hogy r�nyomunk-e a men� gy�m�nt neh�zs�gi szint elem�re. Ha r�nyomtunk be�ll�tja a gy�m�nt neh�zs�get.
	 * @author Bacso Krisztian
	 *
	 */
	public class diaAction implements ActionListener{

		/**
		 * A gy�m�nt neh�zs�gi szint elem megfigyel�st megval�s�t� f�ggv�ny.
		 * @param e Egy ActionEvent v�ltoz�, ami seg�t figyelni, hogy r�katintanak-e gy�m�nt neh�zs�gi szint elemre.
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
	 * Az oszt�ly azt figyeli, hogy r�nyomunk-e a men� kih�v� neh�zs�gi szint elem�re. Ha r�nyomtunk be�ll�tja a kih�v� nehezs�get.
	 * @author Bacso Krisztian
	 *
	 */
	public class challAction implements ActionListener{

		/**
		 * A kih�v� neh�zs�gi szint elem megfigyel�st megval�s�t� f�ggv�ny.
		 * @param e Egy ActionEvent v�ltoz�, ami seg�t figyelni, hogy r�katintanak-e kih�v� neh�zs�gi szint elemre.
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
	 * Az oszt�ly azt figyeli, hogy r�nyomunk-e a men� ranglista elem�re. Ha r�nyomtunk egy felugr� ablak megmutatja a ranglist�t.
	 * @author Bacso Krisztian
	 *
	 */
	public class rankAction implements ActionListener{

		/**
		 * A ranglista elem megnyom�sakor felugr� ablakot val�s�tja meg.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			read();
			JOptionPane.showMessageDialog(null,"Fa: " + lines.get(0) + " "+ lines.get(1) + "\n" + "Gy�m�nt: " + lines.get(2) + " "+ lines.get(3) + "\n" + "Kih�v�: " + lines.get(4) + " "+ lines.get(5));
		}
		
	}
	
	/**
	 * A f�ggv�ny figyeli, hogy v�get�rt-e a j�t�k.
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
	 * A f�ggv�ny egy felugr� ablakban jelzi a j�t�kosnak, hogy nyert �s ha felker�lt a ranglistr� megadhatja a nev�t.
	 */
	public void victory() {
		JOptionPane.showMessageDialog(null, "Gratul�lok �n nyert!");
		read();	//beolvas�s
		if(width == 8 && Integer.valueOf(lines.get(1)) > sec) {		//fa ranglista vizsg�lata
		String name = JOptionPane.showInputDialog("N�v");
		lines.set(0, name);
		lines.set(1, Integer.toString(sec));
		}
		
		if(width == 16 && Integer.valueOf(lines.get(3)) > sec) {	//gy�m�nt ranglista vizsg�lata
			String name = JOptionPane.showInputDialog("N�v");
			lines.set(2, name);
			lines.set(3, Integer.toString(sec));
			}
		
		if(width == 30 && Integer.valueOf(lines.get(5)) > sec) {	//kih�v� ranglista vizsg�lata
			String name = JOptionPane.showInputDialog("N�v");
			lines.set(4, name);
			lines.set(5, Integer.toString(sec));
			}
		write();	//f�jlba �r�s
	}
	
	/**
	 * Veres�g eset�n egy felugr� ablak �zen a j�t�kosnak.
	 */
	public void defeat() {
		JOptionPane.showMessageDialog(null, "Sajnos ez most nem siker�lt. Sok sikert a k�vetkez� j�t�khoz!");
	}
	
	/**
	 * Vissza�ll�tja alaphelyzetbe a j�t�kot.
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
	 * Meghat�rozza, hogy hanyadik n�gyzet felett van az eg�r.
	 * @return Ha n�gyzet felett van vissza adja annak a sz�n�t, ha nem n�gyzet felett van -1-et ad vissza.
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
	 * Beolvassa a ranglist�hoz sz�ks�ges f�jlt �s a lines t�rol�ba teszi azokat.
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
	 * A lines t�rol�t ki�rja az adott f�jlba.
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
	 * Adott n�gyzet poz�ci�j�ra meghat�rozza, hogy h�ny akna szomsz�dja van.
	 * @param pos Integer t�pus� v�ltoz�, ami a n�gyzet poz�ci�jat jelenti.
	 * @return Az akna szomsz�dok sz�m�t hat�rozza meg.
	 */
	public int nearNo(int pos) {
		if(mines.contains(pos)) return 9;
		else if(pos == 0) {
			return getNearNo(new int[] {1,width,width+1},3); //bal fels� n�gyzet
		}
		else if(pos == width-1) {
			return getNearNo(new int[] {width-2,2*width-1,2*width-2},3); //jobb fels� n�gyzet
		}

		else if(pos == (height-1)*width) {
			return getNearNo(new int[] {pos+1,pos-width,pos-width+1},3); //bal als� n�gyzet
		}

		else if(pos == height*width-1) {
			return getNearNo(new int[] {pos-1,pos-width,pos-width-1},3); //jobb als� n�gyzet
		}
		else if(pos%width == 0 && pos != 0 && pos != (height-1)*width) {
			return getNearNo(new int[] {pos-width,pos-width+1,pos+1,pos+width+1,pos+width},5); //bal sz�ls� oszlop
		}
		else if(pos%width == width-1 && pos != width-1 && pos != height*width-1) {
			return getNearNo(new int[] {pos-width,pos-width-1,pos-1,pos+width-1,pos+width},5); //jobb sz�ls� oszlop
		}
		else if(0 < pos && pos < width-1 && pos != 0 && pos != width-1) {
			return getNearNo(new int[] {pos-1,pos+width-1,pos+width,pos+width+1,pos+1},5); //fels� sor
		}
		else if((height-1)*width < pos && pos < width*height-1 && pos != (height-1)*width && pos != width*height-1) {
			return getNearNo(new int[] {pos-1,pos-width-1,pos-width,pos-width+1,pos+1},5);	//als� sor
		}
		else return getNearNo(new int[] {pos-width-1,pos-width,pos-width+1,pos-1,pos+1,pos+width-1,pos+width,pos+width+1},8); //marad�k n�gyzetek
	}
	
	/**
	 * Meghat�rozza, hogy h�ny akna van az adott t�mb n�gyzeteiben.
	 * @param tmb Meghat�rozand� n�gyzetek t�mbje.
	 * @param size T�mb m�rete.
	 * @return Akn�k sz�ma a t�bb n�gyzeteiben.
	 */
	public int getNearNo(int[] tmb, int size) {
		int ret = 0;
		for(int i = 0; i < size; i++) {
			if(mines.contains(tmb[i])) ret++;
		}
		return ret;
	}
}
