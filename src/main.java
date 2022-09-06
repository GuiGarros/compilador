import Compiler.LexicalAnalyser.LexicalAnalyser;
import Services.FileReader;

import java.util.ArrayList;
import java.util.Arrays;

public class main {
    public static void main(String[] args) {
        FileReader reader = new FileReader();
        reader.setFilePath("src/Services/inicio.txt");
        String program = reader.readProgram();

        LexicalAnalyser analyser = new LexicalAnalyser();

        analyser.setCodeReaded(program);
        analyser.AnalyseLexemes();

        for(int i=0; i< analyser.tokenList.size() ; i++){
            System.out.println(Arrays.toString(analyser.tokenList.get(i)));
        }
    }
}
