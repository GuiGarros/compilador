package Compiler.LexicalAnalyser;

import Services.FileReader;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LexicalAnalyser {
  ArrayList<String> reservedSymbols = new ArrayList<String>();
  ArrayList<String> reservedLexemes = new ArrayList<String>();
  ArrayList<String> codeToAnalyse = new ArrayList<String>();
  ArrayList<String[]> tokenArray = new ArrayList<String[]>();
  public int indexStopped = 0;

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

  public String[] treatDigit (String wordToTreat) {
    String digit = "";
    for(int i=0; i< wordToTreat.length(); i++){
      if(Character.isDigit(wordToTreat.charAt(i))){
        digit += wordToTreat.charAt(i);
      } else  {
        this.indexStopped = i;
        break;
      }
    }

    String[] values = {digit, "snumero"};

    return values;
  }

  public String[] treatIdentifier (String wordToTreat) {
    String word = "";
    for(int i=0; i< wordToTreat.length(); i++){
      char character = wordToTreat.charAt(i);
      if(Character.isDigit(character) || Character.isAlphabetic(character) || character == '_'){
        word += wordToTreat.charAt(i);
      } else {
        this.indexStopped = i;
        break;
      }
    }

    String lexeme = searchReserved(word);
    String[] values = {word, lexeme};

    return values;
  }

  public String[] treatAttributions (String wordToTreat) {
    String word = "";
    char character = wordToTreat.charAt(1);
    if(character == '='){
      String[] values = {":=", "satribuição"};

      return values;
    }

    return null;
  }

  public String[] treatCondicionalOperations (String wordToTreat) {
    String word = "";
    char character = wordToTreat.charAt(0);
    if(character == '>' || character == '<' || character == '='){
      String[] values = {Character.toString(character), searchReserved(Character.toString(character))};

      return values;
    }

    return null;
  }

  public String[] treatOperations (String wordToTreat) {
    String word = "";
    char character = wordToTreat.charAt(0);
    if(character == '+' || character == '-' || character == '*'){
      String[] values = {Character.toString(character), searchReserved(Character.toString(character))};

      return values;
    }

    return null;
  }

  public String[] treatPontuation (String wordToTreat) {
    String word = "";
    char character = wordToTreat.charAt(0);
    if(character == ';' || character == '(' || character == ')' || character == ')'){
      String[] values = {Character.toString(character), searchReserved(Character.toString(character))};

      return values;
    }

    return null;
  }

  public String searchReserved(String word) {
    int index = reservedLexemes.indexOf(word);
    if(index >= 0){
      return reservedSymbols.get(index);
    }

    return "sidentificador";
  }


}
