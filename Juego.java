
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;


public class Juego implements Runnable{


	final String MensajeInicio =
			"Bienvenido a mi Scrabble \n"
			+ "A continuacion encontraras las reglas de mi juego. \n"
			+ "Deshacer, Pasar, Combinar, Restantes, evaluar. \n"
			+ "Deshacer: Resetea el tablero para que quede como lo tenias al inicio \n"
			+ "Pasar: Ceder el turno \n"
			+ "Combinar: pues revolver las fichas, pero debes pasar de turno \n"
			+ "Restantes: Te muestra cuantas fichas te quedan en el juego \n"
			+ "Evaluar:Mira si la palabra que tienes en el tablero en realidad esta buena"
			+ "\n"
			+ "Arriba tienes los puntos \n"
			+ "Si hay dudas mira el boton de instrucciones \n"
			+"Disfrutalo, aunque sea Scrabble";


//Empieza el juego invocando un invokeLeter sobre la clase Juego
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Juego());
	}



//Resetea el tablero para cuando le damos en Deshacer
	private void resetearTablero(GameBoard tempTablero, GameBoard Tableroactual) {
		Square[][]  currTablero = tempTablero.TableroActual();
		Square[][] tableroViejo = Tableroactual.TableroActual();
		for (int fila = 0; fila < currTablero.length; fila++) {
			for (int col = 0; col < currTablero[fila].length; col++) {
				Square sq = currTablero[fila][col];
				Square SqV = tableroViejo[fila][col];
				sq.setContent(SqV.getContent());
				sq.repaint();
			}
		}
	}

	
	//cuadro para pedir y devolver el nombre del jugador
	private String getUsername(String jugador) {
		String tgt = JOptionPane.showInputDialog(null, jugador + ", Ingrese el nombre:");
		if (tgt==null) return getUsername(jugador);
		else return tgt;
	}

	
	//Corre todo el juego, es la vida del codigo basicamente 
	public void run() {
		//Nombre del cuadro inicial
		JOptionPane.showMessageDialog(null,MensajeInicio,
				"Informacion", JOptionPane.INFORMATION_MESSAGE);

		String nombre1 = getUsername("Jugador 1");
		String nombre2 = getUsername("Jugador 2");


		final JFrame frame = new JFrame("Scrabble Por Grupo JJS");


		 ImageIcon logo = null;
		 // Intento fallido de cogido

	      java.net.URL imgURL = Juego.class.getResource("LogoFinal.png");
	      if (imgURL != null) {
	         logo = new ImageIcon(imgURL);
	         frame.setIconImage(logo.getImage());
	      } else {
	         JOptionPane.showMessageDialog(frame, "Ignoren esto, no me funciona");
	      }



        ///////////////

	      
	      
	      
	      
	      
	      
	      //Organizar tamanio y todo el asunto

		frame.setLocation(500, 500);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
 



		try {
			int scaledWidth = 5;
            int scaledHeight = 2;
            String inputImagePath = "C:\\Users\\Usuario\\IdeaProjects\\ScrabbleFIN\\src\\LogoFinal.png";
            Juego.resize(inputImagePath, scaledWidth, scaledHeight);// me salvó la vida encontrar esta funcion

			BufferedImage myPicture = ImageIO.read(new File(inputImagePath));

			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			JPanel picPanel = new JPanel();

			picPanel.add(picLabel);
			frame.add(picPanel);
		} catch (Exception e) {

		}


       //leer el letter para el asunto de los puntajes

		final Bolsillo letterBag = new Bolsillo("C:\\Users\\Usuario\\IdeaProjects\\ScrabbleFIN\\src\\letters.txt");

		final Jugador p1 = new Jugador(nombre1, letterBag.drawTiles(7), true);
		final Jugador p2 = new Jugador(nombre2, letterBag.drawTiles(7), false);


		//El lugar del puntaje en el tablero
		final JPanel PuntajeTablero = new JPanel();
		 PuntajeTablero.setLayout(new GridLayout(1, 3));
		final JLabel puntos1 = new JLabel("\t\t\t\t"+ p1.getName() + " Tiene un puntaje de " + p1.getScore() + " puntos");
		final JLabel puntos2 = new JLabel(p2.getName() +  " Tiene un puntaje de " + p2.getScore() + " puntos");
		final JLabel turn = new JLabel("\t\t\t\t\t  Es el turno de " + p1.getName());

// anade puntos 
		 PuntajeTablero.add(puntos1);
		 PuntajeTablero.add(turn);
		 PuntajeTablero.add(puntos2);


		//variables que ayudan al input del jugador
		final Square selectedLetter = new Square(-1, -1);
		final List<Square> squaresToSubmit = new LinkedList<Square>();


		//panel de letras interactivas, con mouse y action events
		//coloca las letras que selecciona en los cuadros que selecciona
		final JPanel tileBenchPanel = new JPanel();
		Jugador currPlayer = (p1.isMyTurn() ? p1 : p2);
		for (int i = 0; i < currPlayer.getBenchSize(); i++) {

			char c = currPlayer.getLetter(i);
			final JButton b = new JButton(Character.toString(c));
			tileBenchPanel.add(b);

			b.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean blank = b.getText().equals("");
					boolean selected = selectedLetter.hasContent();
					if (!blank && !selected) {
						selectedLetter.setContent(b.getText().charAt(0));
						b.setText("");
					}
				}
			});
		}


// crea el tablero inicial con las palabras, y el tablero temporal a traves de los turnos mediante los mouse events
		final GameBoard tablero = new GameBoard("C:\\Users\\Usuario\\IdeaProjects\\ScrabbleFIN\\src\\diccionario.txt", letterBag);
		final GameBoard tempTablero = new GameBoard("C:\\Users\\Usuario\\IdeaProjects\\ScrabbleFIN\\src\\diccionario.txt", letterBag);
		Square[][]  tableroAct = tempTablero.TableroActual();
		for (int fila = 0; fila < tableroAct.length; fila++) {
			for (int col = 0; col < tableroAct[fila].length; col++) {
				final Square cuadrado = tableroAct[fila][col];
				cuadrado.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if ((!cuadrado.hasContent()) && (selectedLetter.hasContent())) {
							cuadrado.setContent(selectedLetter.getContent());
							cuadrado.repaint();
							squaresToSubmit.add(cuadrado);
							selectedLetter.setContent((char)-1);
						}
					}
				});
			}
		}



		//inicializa los botones ////////////////////////////////////////
		final JPanel gameButtonPanel = new JPanel();
//boton deshacer
		final JButton deshacer = new JButton("Deshacer");
		deshacer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetearTablero(tempTablero, tablero);
				selectedLetter.setContent((char)-1);
				tileBenchPanel.removeAll();
				squaresToSubmit.clear();
				Jugador currPlayer = (p1.isMyTurn() ? p1 : p2);
				for (int i = 0; i < currPlayer.getBenchSize(); i++) {
					char c = currPlayer.getLetter(i);
					final JButton b = new JButton(Character.toString(c));
					tileBenchPanel.add(b);

					b.addActionListener( new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (!(b.getText().equals("")) &&
									(!selectedLetter.hasContent())) {
								selectedLetter.setContent(b.getText().charAt(0));
								b.setText("");
							}
						}
					});
				}
				frame.getContentPane().validate();
				frame.getContentPane().repaint();

			}
		});

//boton pasar turno
		final JButton Pasar = new JButton("Pasar turno");
		Pasar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetearTablero(tempTablero, tablero);

				selectedLetter.setContent((char)(-1));
				Jugador currPlayer = (p1.isMyTurn()) ? p2 : p1;
				p1.setMyTurn(!p1.isMyTurn());
				p2.setMyTurn(!p2.isMyTurn());
				turn.setText("Es el turno de " + currPlayer.getName());

				tileBenchPanel.removeAll();
				for (int i = 0; i < currPlayer.getBenchSize(); i++) {
					char c = currPlayer.getLetter(i);
					final JButton b = new JButton(Character.toString(c));
					tileBenchPanel.add(b);

					b.addActionListener( new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (!(b.getText().equals("")) &&
									(!selectedLetter.hasContent())) {
								selectedLetter.setContent(b.getText().charAt(0));
								b.setText("");
							}
						}
					});
				}

				frame.getContentPane().validate();
				frame.getContentPane().repaint();



			}
		});

//boton revolver
		final JButton revolver = new JButton("Revolver");
		revolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetearTablero(tempTablero, tablero);
				selectedLetter.setContent((char)(-1));

				Jugador currPlayer = (p1.isMyTurn()) ? p1 : p2;
				List<Character> newLetters = letterBag.swapTiles(currPlayer.getAll());
				currPlayer.clear();
				currPlayer.addLetters(newLetters);

				currPlayer = (p1.isMyTurn()) ? p2 : p1; //opuesto
				turn.setText("Es el turno de " + currPlayer.getName());
				p1.setMyTurn(!p1.isMyTurn());
				p2.setMyTurn(!p2.isMyTurn());
				tileBenchPanel.removeAll();
				selectedLetter.setContent((char)-1);
				for (int i = 0; i < currPlayer.getBenchSize(); i++) {
					char c = currPlayer.getLetter(i);
					final JButton b = new JButton(Character.toString(c));
					tileBenchPanel.add(b);

					b.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (!(b.getText().equals("")) &&
									(!selectedLetter.hasContent())) {
								selectedLetter.setContent(b.getText().charAt(0));
								b.setText("");
							}
						}
					});
				}
				frame.getContentPane().validate();
				frame.getContentPane().repaint();


			}
		});


//boton evaluar
		final JButton evaluar = new JButton("Evaluar");
		evaluar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (squaresToSubmit.isEmpty()) {
					JOptionPane.showMessageDialog(null,"Mueva uno, tu puedes "
							+ "Antes de",
							"Asi no ess", JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean primerTurno = ((p1.getScore()==0 && p2.getScore()==0));
				int puntosAnotados = tablero.AnadePalabra(squaresToSubmit, primerTurno);

				if (puntosAnotados > 0) {

					Jugador currPlayer = (p1.isMyTurn()) ? p1 : p2;
// muestra los puntos arriba
					currPlayer.addToScore(puntosAnotados);
					puntos1.setText("\t" + p1.getName() + "Tiene un puntaje de " + p1.getScore() + " puntos");
					puntos2.setText(p2.getName() + "Tiene un puntaje de " + p2.getScore() + " puntos");

					List<Character> lettersUsed = new ArrayList<Character>();
					for (Square s: squaresToSubmit) {
						lettersUsed.add(s.getContent());
					}

					squaresToSubmit.clear();
					selectedLetter.setContent((char)(-1));
					currPlayer.useLetters(lettersUsed);
					currPlayer.addLetters(letterBag.drawTiles(lettersUsed.size()));

//si ya no hay letras etnonces paganor 
					if (currPlayer.getBenchSize()==0) {
						boolean pGanador = (p1.getScore()>p2.getScore());
						String Ganador = (pGanador) ? p1.getName() : p2.getName();
						Ganador = "El ganador es " + Ganador + "!\n";
						Ganador+= p1.getName() + " tiene " + p1.getScore() + " puntos\n"
								+ p2.getName() + " tiene " + p2.getScore() + " puntos";

						JOptionPane.showMessageDialog(null, Ganador, "Se acabó :,(",
								JOptionPane.INFORMATION_MESSAGE);
						System.exit(1);
					}

					lettersUsed.clear();


					currPlayer = (p1.isMyTurn()) ? p2 : p1;
					turn.setText("Es el turno de " + currPlayer.getName());
					p1.setMyTurn(!p1.isMyTurn());
					p2.setMyTurn(!p2.isMyTurn());

					tileBenchPanel.removeAll();
					for (int i = 0; i < currPlayer.getBenchSize(); i++) {
						char c = currPlayer.getLetter(i);
						final JButton b = new JButton(Character.toString(c));
						tileBenchPanel.add(b);

						b.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (!(b.getText().equals(""))&&
										(!selectedLetter.hasContent())) {
									selectedLetter.setContent(b.getText().charAt(0));
									b.setText("");
								}
							}
						});
					}


					frame.getContentPane().validate();
					frame.getContentPane().repaint();


				} else {
					JOptionPane.showMessageDialog(null,"Asi no esss. Intentalo de nuevo",
							"Asi no esss", JOptionPane.ERROR_MESSAGE);

					deshacer.doClick();
				}




			}
		});

//boton de info que aparece al inicio y el que aparece de penultima
		
		final JButton instrucciones = new JButton("Informacion");
		instrucciones.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,MensajeInicio,
						"Informacion", JOptionPane.INFORMATION_MESSAGE);
			}


		});

		//boton de las fichas que quedan 
		final JButton checkTilesLeft = new JButton("Cuantas quedan");
		checkTilesLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Las fichas que quedan son " + letterBag.getTilesLeft(),
						"Fichas restantes", JOptionPane.INFORMATION_MESSAGE);
			}
		});


		
		// crea todo la imagen como tal
		frame.add(PuntajeTablero);

		frame.add(tempTablero);
		frame.add(tileBenchPanel);


		gameButtonPanel.add(deshacer);
		gameButtonPanel.add(evaluar);
		gameButtonPanel.add(Pasar);
		gameButtonPanel.add(revolver);
		gameButtonPanel.add(instrucciones);
		gameButtonPanel.add(checkTilesLeft);



		frame.add(gameButtonPanel);


//tampoco me sirvio por alguna razon que no acabe de entender ajajajja
		try {
			BufferedImage letrass = ImageIO.read(new File("C:\\Users\\Usuario\\IdeaProjects\\ScrabbleFIN\\src\\valoresletras.png"));
			JLabel picLabel = new JLabel(new ImageIcon(letrass));
			JPanel picPanel = new JPanel();
			picPanel.add(picLabel);
			frame.add(picPanel);
		} catch (Exception e) {

		}



		frame.validate();
		frame.setResizable(true);
		frame.setSize(670, 1090);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}



	private static void resize(String inputImagePath, int scaledWidth,
			int scaledHeight) throws IOException {
		File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);


        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());


        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

     }


	}


 