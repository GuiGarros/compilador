import Compiler.LexicalAnalyser.LexicalAnalyser;
import Compiler.SyntaxAnalyser.SyntaxAnalyser;
import Services.FileReader;
import Services.Stack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class main {
    static ArrayList<String[]> tokenList = new ArrayList<String[]>();
    static Stack symbolsTable = new Stack();

    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader();
        reader.setFilePath("src/Tests/gera1.txt");
        String program = reader.readProgram();
        LexicalAnalyser analyser = new LexicalAnalyser();
        analyser.setCodeReaded(program);
        analyser.AnalyseLexemes();
        SyntaxAnalyser syntax = new SyntaxAnalyser();
        syntax.setAnalyser(analyser);
        syntax.AnalyzeSyntax();
    }
}
