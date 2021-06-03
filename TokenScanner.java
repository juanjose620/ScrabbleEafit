
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.IOException;
import java.io.Reader;



public class TokenScanner implements Iterator<String> {
	private int c;
	private Reader r;
	/*
	* Crea un TokenScanner para un lector determinado
	* Como iterador, TokenScanner solo debe leer del Reader tanto
	* como es necesario para mirar si sigue con getNext () y next ()
	* IOException si hay un error en la lectura
	*  IllegalArgumentException cuando el Reader proporcionado es nulo
	*/

  public TokenScanner(java.io.Reader in) throws IOException {
	  if (in==null) throw new IllegalArgumentException();
	  this.r = in;
	  c = r.read();
	  
  }


  public static boolean isWordCharacter(int c) {
    return (Character.isLetter(c) || ((char)c)=='\'');
  }
  /**
   * Mira si el string es una palabar valida 
   * todas son diferentes de null si son palabras 
 */


  public static boolean isWord(String s) {
		if (s==null || s.length()<=0) return false;
		for (int i = 0; i < s.length(); i++) {
			if (!(isWordCharacter(s.codePointAt(i)))) {
				return false;
			}
		}
		return true;
  }

  //Determina si hay otro token disponible

  public boolean hasNext() {
    return c != -1;
  }

  /*
  * Devuelve el siguiente token o lanza un NoSuchElementException si no queda ninguno
  *
  * throws NoSuchElementException cuando se alcanza el final 
  */
  public String next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		String respuesta = "";
		boolean isWordChar = isWordCharacter(c);
		
		try {
			if (isWordChar) {
				while(isWordCharacter(c) && c != -1) {
					respuesta  += (char)c;
					c = r.read();
				}	
			} else {
				while(!isWordCharacter(c) && c != -1) {
					respuesta  += (char)c;
					c = r.read();
				}
			}	
		} catch (IOException e) {
			throw new NoSuchElementException();
		}
		return respuesta ;
  }



  public void remove() {
    throw new UnsupportedOperationException();
  }
}
