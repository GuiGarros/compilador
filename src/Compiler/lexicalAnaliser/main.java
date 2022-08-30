package lexicalAnaliser;
import lexicalAnaliser.lexicalAnaliser;
public class main {

    public static void main(String[]args){
        lexicalAnaliser lexical = new lexicalAnaliser();
        lexical.codigo = lexical.tratarEspaco("Guilherme {Marques} Brait Garros");
        lexical.removeComentario(lexical.codigo);
    }
}