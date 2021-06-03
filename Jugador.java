
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

public class Jugador extends JComponent {
	
	private List<Character> letterBench;
	private String nombre;
	private int puntaje;
	private boolean isMyTurn;
	
	public Jugador(String nombre, List<Character> empezarMano, boolean isMyTurn) {
		this.nombre = nombre;
		letterBench = new ArrayList<Character>(empezarMano);
		puntaje = 0;
		this.isMyTurn = isMyTurn;
	
		
	}
	
	

	public boolean isMyTurn() {
		return isMyTurn;
	}




	public void setMyTurn(boolean isMyTurn) {
		this.isMyTurn = isMyTurn;
	}
	

	public char getLetter(int index) {
		return letterBench.get(index);
	}
	//todas las letras que hay 

	public List<Character> getAll() {
		return new ArrayList<Character>(letterBench);
	}
	
//limpia el bolsillo 
	public void clear() {
		letterBench.clear();
	}
	
	//tamaño del bolsillo

	public void addToScore(int points) {
		puntaje += points;
	}
	

	public int getBenchSize() {
		return letterBench.size();
	}

	
	public void useLetters(List<Character> toUse) {
		for (char c : toUse) {
			this.useLetter(c);
		}
		
	}

	
	private void useLetter(Character c) {
		letterBench.remove(c);
	}
	

	
	public void addLetters(List<Character> toAdd) {
		letterBench.addAll(toAdd);
	}
	

	public String getName() {
		return nombre;
	}

	public int getScore() {
		return puntaje;
	}
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println("Aqui");
		g.fillRect(0, 0, 100, 100000);
		g.setFont(new Font("TimesRoman", Font.PLAIN, GameBoard.TAMANOC-(GameBoard.TAMANOC/2))); 
		int index = 0;
		for (char c: letterBench) {
			g.drawString(Character.toString(c), index*GameBoard.TAMANOC, GameBoard.TAMANOC/2);
		}
		
		

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(GameBoard.TAMANOC*7, GameBoard.TAMANOC);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(GameBoard.TAMANOC*7, GameBoard.TAMANOC);
	}
}
