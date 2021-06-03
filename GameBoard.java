

import java.awt.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.*;

public class GameBoard extends JPanel {
	
	
	
	private static final long serialVersionUID = 1L;
	public static final int TAMANO = 15;
	public static final int TAMANOC = 35;
	public static final int CENTRO = 7;
	private Square[][] tablero = new Square[TAMANO][TAMANO];
	private Diccionario diccionario;
	private Bolsillo mano;
	
	
	//Constructor en donde se busca el diccionario y el letter 

	public GameBoard(String Diccionario, Bolsillo mano) {
		
		if (Diccionario == null || mano == null) {
			JOptionPane.showMessageDialog(null,"Archivo no encontrado",
					"Diccionario no encontrado", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		this.setLayout(new GridLayout(TAMANO, TAMANO));
		
		//inicializa el tablero
		for (int fila = 0; fila < TAMANO; fila++) {
			for (int columna = 0; columna < TAMANO; columna++) {
				Square cuadrado = new Square(fila, columna);
				tablero[fila][columna] = cuadrado;
				this.add(cuadrado);
			}
		}
		
		//inicializa diccionario
		try {
			this.diccionario = new Diccionario(new TokenScanner(new FileReader(Diccionario)));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Archivo no encontrado",
					"Diccionario no encontrado", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		// inicializa la mano (bolsillo)
		this.mano = mano;
		
	}
	
	
	
	 //tira el puntaje del tablero actual	
	public Square[][] TableroActual() {
		return tablero;
	}

	//copia el tableor actual
	public Square[][] CopiaTablero() {
		Square[][] copia = new Square[tablero.length][tablero[0].length];
		for (int i = 0; i < copia.length; i++) {
		     copia[i] = Arrays.copyOf(tablero[i], tablero[i].length);
		}
			
		return copia;
	}
	
	//fila deseada por el susuario
	//columna deseada por el usuario
	public Square getCuadrado(int fila, int columna) {
		return tablero[fila][columna];
	}
	
	// Anade cuadrado al tablero 

	private void AnadeCuadrado(Square s) {
		tablero[s.getRow()][s.getColumn()] = s;
	}
	

	private void anadeTablero(List<Square> cuadrada) {
		for (Square cuadrado: cuadrada) {
			AnadeCuadrado(cuadrado);
		}
	}
	
	
	// metodo para anadir una palabra al tablero 
	//ArrayList<Square> de cuadrados llenos
	//si es el primero turno para poder añadir 
// Valor del movimiento, siempre debe iniciar en el centro y seguir con letras contiguas o que usen ya puestas
	
	
	public int AnadePalabra(List<Square> cuadrada, boolean TurnoPrimero) {
		for (Square cuadrado: cuadrada) {
			int row = cuadrado.getRow();
			int col = cuadrado.getColumn();
			if (row < 0 || row >= TAMANO 
				|| col < 0 || col >= TAMANO ) return -1;
		}
		
		
		//Organiza de mayor a menos para poder leer la palabra 
		Collections.sort(cuadrada);
		
		//tira el primer cuadrado para mirar si la palabra esta en vertical o en horizontal
		Square primero = cuadrada.get(0);
		int primeraFila = primero.getRow();
		int primeraColumna = primero.getColumn();
		boolean mismaFila = false; 
		
		//colecciona los indices de las coordenadas no contiguas  
		Set<Integer> indices = new TreeSet<Integer>();
		for (int i = 1; i < cuadrada.size(); i++ ) { 
			int fila = cuadrada.get(i).getRow();
			int columna = cuadrada.get(i).getColumn();
			if (tablero[fila][columna].hasContent() || 
					tablero[primeraFila][primeraColumna].hasContent()) {
				return -1; //movimiento invalido
			}
			
			//mira la direccion de la segunda letra
			if (i == 1) {
				if (fila == primeraFila) {
					indices.add(columna);
					mismaFila = true;
					indices.add(primero.getColumn());
				} else if (columna == primeraColumna) {
					indices.add(fila);
					mismaFila = false;
					indices.add(primero.getRow());
					
				} else {
					return -1; //Movimiento invalido, los cuadrados deben ser contiguos
				}
			} else {
			
				if (mismaFila) {
					indices.add(columna);
					if (fila != primeraFila) return -1; //Movimiento invalido, los cuadrados deben ser contiguos
				} else {
					indices.add(columna);
					if (columna != primeraColumna) return -1; //Movimiento invalido, los cuadrados deben ser contiguos
				}
			} 
			
		} 
		
		
		
		if (cuadrada.size()==1) {
			mismaFila = true;
			indices.add(primeraColumna);
		}
		
		//empieza a construir la palabra
		String pal = "";
		int index = -1;
		int PrimerIndex = -1;
		Iterator<Integer> iteraciones = indices.iterator();
		int previa = -1;
		
		//mira si la letra esta en el tablero
		//entonces el jugador pues no recibe puntos porque aja ajajjaja
		
		
		Set<Integer>indicesSinPuntos = new TreeSet<Integer>();
		while (iteraciones.hasNext()) {
			index++;
			int a = iteraciones.next();
			
			if (previa != -1) {
				if (a != previa + 1) {
					//no salen puntos apra palabras fuera del index
					// b/c ya estaban ahi
					indicesSinPuntos.add(previa+1);
					
					if (mismaFila) {
						 //la diferencia invalida el movimiento, o hay una letra que llene la diferencia 
						if (!(tablero[primeraFila][previa+1].hasContent())) return -1;
						pal+= tablero[primeraFila][previa + 1].getContent();
					} else {
						//la diferencia invalida el movimiento, o hay una letra que llene la diferencia 
						if (!(tablero[previa+1][primeraColumna].hasContent())) return -1;
						pal+= tablero[previa + 1][primeraColumna].getContent();
					}
				}
			} else PrimerIndex = a; 
			
			// adjunta a la palabra
			pal += cuadrada.get(index).getContent();
			previa = a;
			
		} 
		
		//convierte a un arreglo de chars 
		char [] palabrachar = pal.toCharArray();
		
		//encuentra la fila y la columna
		int empezarFila = (mismaFila) ? primeraFila : PrimerIndex;	 ///Empezar - fila inicial de palabra
		 ///Empezarcolumna - columna inicial de la palabra
		int empezarColumna = (!mismaFila) ? primeraColumna : PrimerIndex;
		
		
		//llama la ayuda pai ajajaj
		int resultado = palabraAyuda(palabrachar, empezarFila, empezarColumna , (!mismaFila), TurnoPrimero, indicesSinPuntos);
		
		//aï¿½ade si la palabra era valida
		if (resultado > 0) {
			anadeTablero(cuadrada);
			return resultado;
			
		} else return -1;
	}
	 //funcion que ayuda al constructor de anadir palabras
	/// char [] de la palabra creada por los cuadrados de entrada y los espacios en blanco rellenados con letras en el tablero
	 ///Empezar - fila inicial de palabra
	 ///Empezarcolumna - columna inicial de la palabra
	// palabra vertical o palabra horizontal
	// primer turno  ¿es el primer turno?
	//indicesNoPoints: los índices donde la palabra usa letras del tablero,
	//  y no debe recibir puntos por las palabras que aunque estan contiguos no pertenecen a la nueva palabra
	//  puntos anotados, -1 si es movimiento inválido
	// 
	private int palabraAyuda(char[] palabra, int empiezaPalabra, int empiezaColumna, 
			boolean vertical, boolean primerTurno, Set<Integer> indicesSinPuntos) {
		
		int empiezaIndex = (vertical) ? empiezaPalabra : empiezaColumna;
		int otroIndex = (!vertical) ? empiezaPalabra : empiezaColumna;
		
		if (primerTurno) {

			// El centro del tablero debe usarse en el primer movimiento, de lo contrario no es válido
			if (!(CENTRO < empiezaIndex + palabra.length && CENTRO>= empiezaIndex
					&& otroIndex==CENTRO)) return -1;
			if (palabra.length==0) return -1;
			
		} else {
			
			//mira si no es el primer turno, es decir, si empieza en el index del centro 
			
			for (int i = empiezaIndex; i < empiezaIndex + palabra.length; i++) {
				//mira los espacios disponibles . Si estan llenos deben ser la misma letra
				//esta es la forma en que lo escribí, ya que la char []  palabra ya tiene los espacios agregados
				
				if (vertical) {
					if (tablero[i][empiezaColumna].hasContent()) {
						if (palabra[i-empiezaIndex] != tablero[i][empiezaColumna].getContent()) return -1;
					}
				} else {
					if (tablero[empiezaPalabra][i].hasContent()) {
						if (palabra[i-empiezaIndex] != tablero[empiezaPalabra][i].getContent()) {
							return -1;
						}
					}
				}
			}
			
			// se asegura que la palabra tenga una especie de ancla, de letra ya puesto a la que se pueda pegar
			boolean cuadradoEncontrado = false; // hasta que de verdadero ajajja
			
			
			// recorre y rompe cuando se encuentra el cuadrado que ancla
			for (int i = empiezaIndex; i <= empiezaIndex + palabra.length; i++) {
				
				if (vertical) {
					
					if (i==empiezaIndex) {
						if (i - 1 >= 0) {
							Square s = tablero[i-1][empiezaColumna];
							if (s.hasContent()) {
								cuadradoEncontrado = true;
								break;
							}
						}
					}
					
					if (i== empiezaIndex+palabra.length-1) {
						if (i + 1 < TAMANO) {
							Square s = tablero[i+1][empiezaColumna];
							if (s.hasContent()) {
								cuadradoEncontrado = true;
								break;
							}
						}
					}
					
					if (empiezaColumna != 0) {
						Square s = tablero[i][empiezaColumna-1];
						if (s.hasContent()) {
							cuadradoEncontrado = true;
							break;
						}
					}
					
					if (empiezaColumna != TAMANO-1) {
						Square s = tablero[i][empiezaColumna+1];
						if (s.hasContent()) {
							cuadradoEncontrado = true;
							break;
						}
					}

				//termina el ciclo vertical
				} else { //empieza horizontal
					if (i==empiezaIndex) {
						if (i - 1 >= 0) {
							Square s = tablero[empiezaPalabra][i-1];
							if (s.hasContent()) {
								cuadradoEncontrado = true;
								break;
							}
						}
					}
					
					if (i== empiezaIndex+palabra.length-1) {
						if (i + 1 < TAMANO) {
							Square s = tablero[empiezaPalabra][i+1];
							if (s.hasContent()) {
								cuadradoEncontrado = true;
								break;
							}
						}
					}
					
					if (empiezaPalabra != 0) {
						Square s = tablero[empiezaPalabra-1][i];
						if (s.hasContent()) {
							cuadradoEncontrado = true;
							break;
						}
					}
					
					if (empiezaPalabra != TAMANO-1) {
						Square s = tablero[empiezaPalabra+1][i];
						if (s.hasContent()) {
							cuadradoEncontrado = true;
							break;
						}
					}

				} 

			} 
			
			
			if (!(cuadradoEncontrado)) {
				return -1; 
			}
			
		} 
		
		// construye todas las palabras creadas a través de la función ayuda
		List<String> palabrasMirar = EncontrarpalabrasMirar(palabra, empiezaPalabra,
				empiezaColumna, vertical, indicesSinPuntos);
		
		//mira diccionario
		if (palabrasMirar(new TreeSet<String>(palabrasMirar))) {
			return getPoints(palabrasMirar); //retorna los puntos
		} else {
			return -1; //movimiento invalido
		}
	}
	
	//Dado que un movimiento es una ubicación válida, encuentre todas las palabras hechas agregando esa palabra al tablero(no se) 
	  // palabra: palabra agregada
	  // empiezaPalabra: fila de inicio
	  //empiezaColumna: columna de inicio
	  // esvertical: ¿palabra vertical o palabra horizontal?
	 //  indicesSinPuntos: índices en los que el usuario no debería obtener puntos
	  // porque las letras ya estaban ahí//letras que en realidad no son de la palabra pero ya estaban 
	 // Lista <String> de palabras para verificar
	
	
	
	private List<String> EncontrarpalabrasMirar(char[] palabra, int empiezaPalabra,
			int empiezaColumna, boolean esVertical, Set<Integer> indicesSinPuntos) {
		
		// inicializa clista
		LinkedList<String> cuadrada = new LinkedList<String>();
		
		// palabra vertical principal
		if (esVertical) {
			
			for (int i = empiezaPalabra; i < empiezaPalabra + palabra.length; i++) {
				String horizontal = "" + palabra[i-empiezaPalabra];
				

				//hacia la izquierda
				for (int j = empiezaColumna-1; j >= 0; j--) {
					if (indicesSinPuntos.contains(i)) break;
					Square next = tablero[i][j];
					if (next.hasContent()) {
						horizontal = next.getContent() + horizontal;
					} else break;
				}
				

				//hacia la derecha
				for (int j = empiezaColumna+1; j < 15; j++) {
					if (indicesSinPuntos.contains(i)) break;
					Square siguiente = tablero[i][j];
					if (siguiente.hasContent()) {
						horizontal += siguiente.getContent();
					} else break;
				}
				
				if (horizontal.length() > 1) {
					cuadrada.add(horizontal); //añade palabra
				}
			} // filas
			
			
		
			String vertical = new String(palabra);
			//hacia arriba
			for (int up = empiezaPalabra-1; up >= 0; up--) {
				Square siguiente = tablero[up][empiezaColumna];
				if (siguiente.hasContent()) {
					vertical = siguiente.getContent() + vertical;
				} else break;
			}
			
			//hacia abajo
			for (int up = empiezaPalabra+palabra.length; up < 15; up++) {
				Square siguiente = tablero[up][empiezaColumna];
				if (siguiente.hasContent()) {
					vertical += siguiente.getContent();
				} else break;
			}
			cuadrada.add(vertical); 
			
			
			//vertical listo
			//empieza horizontal 
		} else {
			
			
			for (int i = empiezaColumna; i < empiezaColumna + palabra.length; i++) {
				String vertical = "" + palabra[i-empiezaColumna];
				
			//arriba
				for (int j = empiezaPalabra-1; j >= 0; j--) {
					if (indicesSinPuntos.contains(i)) break;
					Square siguiente = tablero[j][i];
					if (siguiente.hasContent()) {
						vertical = siguiente.getContent() + vertical;
					} else break;
				}
				
				//abajo
				for (int j = empiezaPalabra+1; j < 15; j++) {
					if (indicesSinPuntos.contains(i)) break;
					Square siguiente = tablero[j][i];
					if (siguiente.hasContent()) {
						vertical += siguiente.getContent();
					} else break;
				}
				
				if (vertical.length() > 1) {
					cuadrada.add(vertical); 
				}
			} 
			
			
		
			String horizontal = new String(palabra);
			
		//izquierda
			for (int side = empiezaColumna-1; side >= 0; side--) {
				Square siguiente = tablero[empiezaPalabra][side];
				if (siguiente.hasContent()) {
					horizontal = siguiente.getContent() + horizontal;
				} else break;
			}
			
			//derecha
			for (int side = empiezaColumna+palabra.length; side < 15; side++) {
				Square siguiente = tablero[empiezaPalabra][side];
				if (siguiente.hasContent()) {
					
					horizontal += siguiente.getContent();
				} else break;
			}
			
			cuadrada.add(horizontal); 
			

		} 
		
	
		return cuadrada;
		
	}
	 //Compara un conjunto <String> de palabras que se le asignan con el diccionario
	 // palabras: conjunto de Strng para comprobar
	 // boolean: verdadero si todas las palabras están en el diccionario, falso en caso contrario

	
	private boolean palabrasMirar(Set<String> palabras) {
		for (String s: palabras) {
			if (s.length() < 2) continue;
			if (!(diccionario.EsPalabra(s))) return false;
		}
		return true;
	}

	// Obtiene los puntos de las palabras usando this.bagOfTiles.getPointValue ()
	// palabrasMirar - Lista <String> para buscar puntos
	// total de puntos para todas las palabras 

	private int getPoints(List<String> palabrasMirar) {
		int total = 0;
		for (String s: palabrasMirar) {
			if (s.length() < 2) continue; // una palabra de una letra, pues no jaajja
			int suma = 0;
			for (int i = 0; i < s.length(); i++) {
				suma+= mano.getPointValue(s.charAt(i));
			}
			total += suma;
		}
		
		return total;
	}
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(TAMANO*TAMANOC, TAMANO*TAMANOC);
	}
	


	@Override
	public Dimension getMinimumSize() {
		return new Dimension(TAMANO*TAMANOC, TAMANO*TAMANOC);
	}
	
}
