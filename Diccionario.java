import java.io.*;
import java.util.Set;
import java.util.TreeSet;

public class Diccionario {
	
	private Set<String> TodasLasPalabras = new TreeSet<String>(); //Palabras en minusculas
	
	
	
    public Diccionario(TokenScanner diccionarioS) throws IOException {
    	if (diccionarioS == null) throw new IllegalArgumentException();
    	while(diccionarioS.hasNext()) {
    		String s = diccionarioS.next();
    		if (TokenScanner.isWord(s)) {
    			TodasLasPalabras.add(s.toLowerCase());
    		}
    	}
    }

    //construye  un diccionario  a partir de las palabras del archivo
    // Ruta de donde se lee el archivo
   // FileNotFoundException en el caso de que el documento no exista
 
 
   public static Diccionario make(String archivo) throws IOException { // me hace el diccionario a partir del lector de archivos
	  Reader lector = new FileReader(archivo);
	  Diccionario d = new Diccionario(new TokenScanner(lector));
	  lector.close();
  	  return d;
   }
   
   

  
   // retorna el numero de palabras unicas 
  public int getNumWords() {
     return TodasLasPalabras.size();
  }
  //mira si la palabra si esta en el diccionario, si si esta en el diccionario, 
  //pues me retorna true, de lo contrario, si no esta, tiene espacios, o es un valor null, pues me retorna false
  public boolean EsPalabra(String palabra) {
	  if (palabra==null) return false;
	  if (TokenScanner.isWord(palabra)) {
		  return TodasLasPalabras.contains(palabra.toLowerCase());
	  } else return false;
  }
}

 