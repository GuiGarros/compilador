//import Compiler.LexicalAnalyser.LexicalAnalyser;
//import Compiler.SyntaxAnalyser.SyntaxAnalyser;
//import Services.FileReader;
import Interfaces.FileChooser;
import Interfaces.MainWindow;
import Services.FileReader;
import Services.Stack;

import Interfaces.Window;

import java.util.ArrayList;
import java.util.Arrays;


public class main {
    static ArrayList<String[]> tokenList = new ArrayList<String[]>();
    static Stack symbolsTable = new Stack();
    FileReader reader = new FileReader();
    String program = "";

    public static void main(String[] args) {
//        reader.setFilePath("src/Tests/sint19.txt");
//        String program = reader.readProgram();
//
//        LexicalAnalyser analyser = new LexicalAnalyser();
//        analyser.setCodeReaded(program);
//        analyser.AnalyseLexemes();
//
//
//
//        System.out.println(symbolsTable.stack);
//
//        SyntaxAnalyser syntax = new SyntaxAnalyser();
//
//        syntax.setAnalyser(analyser);
//        syntax.AnalyzeSyntax();
//        Window appWindow = new Window();
//
//        appWindow.createButton("Teste 1");
//        appWindow.createButton("Teste 2");
//        appWindow.createTextInput(10);
//        appWindow.createFileInput();
//
//        appWindow.setWindowStatus(true);

//        FileChooser chooser = new FileChooser();
//        chooser.openChooser();
        MainWindow wind = new MainWindow();

    }
}
