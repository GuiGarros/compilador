import Compiler.LexicalAnalyser.LexicalAnalyser;
import Compiler.SyntaxAnalyser.SyntaxAnalyser;
import Interfaces.MainWindow;
import Services.FileReader;
import Services.Stack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class main {
    static ArrayList<String[]> tokenList = new ArrayList<String[]>();
    static Stack symbolsTable = new Stack();

    public static void main(String[] args) throws IOException {
        MainWindow window = new MainWindow();
    }
}
