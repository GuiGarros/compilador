import Compiler.LexicalAnalyser.LexicalAnalyser;
import Services.FileReader;

import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        FileReader reader = new FileReader();
        reader.setFilePath("src/Services/inicio.txt");
        ArrayList<String> program = reader.readFile();

        LexicalAnalyser analyser = new LexicalAnalyser();

        analyser.setCodeReaded(program);
        analyser.AnalyseLexemes();


    }
}
