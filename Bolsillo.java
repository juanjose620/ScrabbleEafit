

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JOptionPane;



public class Bolsillo {
	private Map<Character, Integer> letterToAmountLeft = new TreeMap<Character, Integer>();
	private Map<Character, Integer> letterToPointValue = new TreeMap<Character, Integer>();
	
	//INICIALIZA UN BOLISLLO EN BASE AL archivo LETTERS, (LETRA VALOR CANTIDAD)
//metodo par leer los valores y la frecuencia 
	public Bolsillo(String filename) {
		try {
		readFile(filename);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,"Archivo no encontrado",
					"Archivo no encontrado letras", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Error",
					"Archivo no encontrado letras", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
	}

	///funcion que ayuda al constructor a leer el archivo

	private void readFile(String filename) throws IOException {
		BufferedReader lector = new BufferedReader(new FileReader(filename));
		String linea;
		while ((linea = lector.readLine()) != null) {

			String[] list = linea.split("/");
			char letter = list[0].toCharArray()[0];//letra
			int pointValue = Integer.parseInt(list[1]);//valpr
			int amount = Integer.parseInt(list[2]);//frecuencia

			letterToAmountLeft.put(letter, amount); //letras que quedan
			letterToPointValue.put(letter, pointValue);// letras puntaje
		}
		lector.close();// se cierra cuando ya no queden
	}
	


	public int getPointValue(char c) { 
		return letterToPointValue.get(c);// me convierte la letra en el valor de la misma, y me lo devuelve
	}
	

	
	public int getTilesLeft() {
		int suma = 0;
		for (int i : letterToAmountLeft.values()) {
			suma += i;
		}
		return suma;
		//me devuelve las letras que faltan  
		
	}
	
	// me retorna una letra random del bolsillo
	// lo que en realidad es un mapeo de los que quedan 

	
	private char drawTile() {
		Random r = new Random();
		int tama = this.getTilesLeft();
		if (tama < 1) throw new RuntimeException("No quedan letras");
		
		int pick = r.nextInt(tama);
		char toDraw = (char)(-1);
		for (char c: letterToAmountLeft.keySet()) {
			int amountLeft = letterToAmountLeft.get(c);
			pick -= letterToAmountLeft.get(c);
			if (pick <= 0) {
				toDraw = c;
				letterToAmountLeft.put(c, amountLeft-1);
				break;
			}
			
		}
		return toDraw;
	}


	 // Lista de letras a dibujar en el swing
	

	
	public List<Character> drawTiles(int amount) {
		int amountLeft = this.getTilesLeft();
		if (amount > amountLeft) {
			amount = amountLeft;
		}
		
		List<Character> tgt = new ArrayList<Character>();
		for (int i = 0; i < amount; i++) {
			
			tgt.add(this.drawTile());
		}
		return tgt;
	}
	
	//Letras que no se usaron y se deben poner de nuevo en el bolsillo
	//misma cantidad que las originales 7
	
	public List<Character> swapTiles(List<Character> oldLetters) {
		for (char c: oldLetters) {
			int oldValue = letterToAmountLeft.get(c);
			letterToAmountLeft.put(c, oldValue+1);
		}
		return this.drawTiles(oldLetters.size());
		
	}

}
