import Compiler.LexicalAnalyser.LexicalAnalyser;
import Compiler.SyntaxAnalyser.SyntaxAnalyser;
import Services.FileReader;
import Services.Stack;

import java.util.ArrayList;
import java.util.Arrays;


public class main {
    static ArrayList<String[]> tokenList = new ArrayList<String[]>();
    static Stack symbolsTable = new Stack();

    public static void main(String[] args) {
        FileReader reader = new FileReader();
        reader.setFilePath("src/Tests/sint1.txt");
        String program = reader.readProgram();

        LexicalAnalyser analyser = new LexicalAnalyser();
        analyser.setCodeReaded(program);
        analyser.AnalyseLexemes();



        System.out.println(symbolsTable.stack);

        SyntaxAnalyser syntax = new SyntaxAnalyser();

        syntax.setAnalyser(analyser);
        syntax.AnalyzeSyntax();
    }
}
