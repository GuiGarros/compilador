package Compiler.LexicalAnalyser;

import Services.FileReader;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LexicalAnalyser {
  ArrayList<String> reservedSymbols = new ArrayList<String>();
  ArrayList<String> reservedLexemes = new ArrayList<String>();
  ArrayList<String> codeToAnalyse = new ArrayList<String>();
  ArrayList<String[]> tokenArray = new ArrayList<String[]>();

  public LexicalAnalyser() {
    FileReader reader = new FileReader();
    reader.setFilePath("src/Compiler/LexicalAnalyser/lexemas.txt");
    reservedLexemes = reader.readFile();
    reader.setFilePath("src/Compiler/LexicalAnalyser/simbolos.txt");
    reservedSymbols = reader.readFile();
    System.out.println(reservedLexemes);
  }

  public void setCodeReaded(ArrayList<String> codeToAnalyse) {
    this.codeToAnalyse = codeToAnalyse;
  }

  public void AnalyseLexemes() {
    this.codeToAnalyse = treatText(codeToAnalyse)
  }

  private ArrayList<String> treatText(ArrayList<String> program) {
    for (String line : program){
      line = line.replace(" ", "");
      line = removeComment(line);

    }

    return program;
  }

  private String removeComment(String texto) {
    try {
      boolean aux = false;

      for (int i = 0; i < texto.length(); i++) {
        if (texto.charAt(i) == '{') aux = true;
        if (texto.charAt(i) == '}') {
          texto = texto.substring(0, i) + texto.substring((i + 1));
          aux = false;
        }

        if (aux) {
          texto = texto.substring(0, i) + texto.substring((i + 1));
          i--;
        }
      }
    } catch (Exception e) {
      throw e;
    }

    return texto;
  }
}
