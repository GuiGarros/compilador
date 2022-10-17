import Compiler.LexicalAnalyser.LexicalAnalyser;
import Services.FileReader;
import Services.Stack;

import java.util.ArrayList;
import java.util.Arrays;


public class main {
    static ArrayList<String[]> tokenList = new ArrayList<String[]>();
    static Stack symbolsTable = new Stack();

    public static void main(String[] args) {
        FileReader reader = new FileReader();
        reader.setFilePath("src/Tests/teste_1.txt");
        String program = reader.readProgram();

        LexicalAnalyser analyser = new LexicalAnalyser();

        analyser.setCodeReaded(program);
        analyser.AnalyseLexemes();

        while(analyser.indexStopped < analyser.codeToAnalyse.length()){
            String[] token = analyser.getNextToken();
            if(token[1] == "sidentificador"){
                symbolsTable.push(token[0]);
            }
            tokenList.add(token);
        }

        for(int i=0; i< tokenList.size() ; i++){
            System.out.println(Arrays.toString(tokenList.get(i)));
        }
        System.out.println(symbolsTable.stack);
    }
}
