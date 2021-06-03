

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;


public class Square extends JComponent implements Comparable<Square>  {
	private char content;
	private int fila;
	private int col;
	private final int Tamano = GameBoard.TAMANOC;
	

	public Square(int fila, int col) {
		this(fila, col, (char)-1);
	}
	

	public Square(int fila, int col, char content) {
		this.fila = fila;
		this.col = col;
		this.content = content;

	}
	

	public void setContent(char content) {
		this.content = content;
	}


	public boolean hasContent() {
		return content!=((char)-1);
	}
	


	public char getContent() {
		return content;
	}
	

	public int getRow() {
		return fila;
	}
	

	public int getColumn() {
		return col;
	}
	

	@Override
	public int compareTo(Square o) {
		if (this.fila!=o.getRow()) {
			if (this.fila > o.getRow()) return 1;
			return -1;
		} else if (this.col != o.getColumn()) {
			if (this.col > o.getColumn()) return 1;
			return -1;
		} else { return 0;}
	}
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (fila==7 && col==7) {
			g.setColor(Color.red);
			g.drawRect(0, 0, Tamano+1, Tamano-2);
		} else {
			g.setColor(Color.black);
			g.drawRect(0, 0, Tamano+1, Tamano-2);
		}
		if (this.hasContent()) {
			g.setColor(Color.black);
			g.setFont(new Font("Verdana", Font.PLAIN, 25)); 
			g.drawString(Character.toString(content), Tamano/3, Tamano-(Tamano/3));
		}
	}
	

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Tamano, Tamano);
	}
	

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(Tamano, Tamano);
	}
}
