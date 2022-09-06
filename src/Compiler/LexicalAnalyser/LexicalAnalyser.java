package Compiler.LexicalAnalyser;

import Services.FileReader;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LexicalAnalyser {
  ArrayList<String> reservedSymbols = new ArrayList<String>();
  ArrayList<String> reservedLexemes = new ArrayList<String>();
  public ArrayList<String[]> tokenList = new ArrayList<String[]>();

  String codeToAnalyse = "";
  public int indexStopped = 0;


  public LexicalAnalyser() {
    FileReader reader = new FileReader();
    reader.setFilePath("src/Compiler/LexicalAnalyser/lexemas.txt");
    reservedLexemes = reader.readFile();
    reader.setFilePath("src/Compiler/LexicalAnalyser/simbolos.txt");
    reservedSymbols = reader.readFile();
  }

  public void setCodeReaded(String codeToAnalyse) {
    this.codeToAnalyse = codeToAnalyse;
  }

  public void AnalyseLexemes() {
    treatText();
    treatToken();
  }

  private void treatText() {
    removeComment();
    System.out.println(this.codeToAnalyse);
  }

  private void removeComment() {
      boolean commentFlag = false;
      String newCode = "";
      for (int i = 0, j = 0; i < this.codeToAnalyse.length(); i++) {
        char character = this.codeToAnalyse.charAt(i);
        if (character == '{' && !commentFlag) {
          commentFlag = true;
          j = i;
        }

        if (character == '}') {
          commentFlag = false;
          codeToAnalyse = codeToAnalyse.substring(0, j) + codeToAnalyse.substring(i+1, codeToAnalyse.length());
          i = j;
        }
      }
  }

  private void treatToken() {
    try {
      for (int i = 0; i < codeToAnalyse.length()-1; i++) {
        i = indexStopped;
        if(i >= codeToAnalyse.length()){
          break;
        }
        char character = codeToAnalyse.charAt(i);
        if(character == ' '){
          indexStopped ++;
        } else if (Character.isDigit(character)) {
          treatDigit(i);
        } else if (Character.isAlphabetic(character)) {
          treatIdentifier(i);
        } else if (character == ':') {
          treatAttributions(i);
        } else if (character == '+' || character == '-' || character == '*') {
          treatOperations(i);
        } else if (character == '!' || character == '<' || character == '>' || character == '=') {
          treatCondicionalOperations(i);
        } else if (character == ';' || character == ',' || character == '(' || character == ')' || character == '.') {
          treatPontuation(i);
        } else {
          new Exception("error");
        }
      }


    } catch (Exception e) {
      throw e;
    }
  }

  public void treatDigit(int index) {
    String word = "";
    int i =0;
    for (i = index; i < codeToAnalyse.length(); i++) {
      char character = codeToAnalyse.charAt(i);
      if (!Character.isDigit(character)) {
        break;
      }
      word += Character.toString(character);
    }

    indexStopped = i;
    String[] values = {word, searchReserved(word)};
    tokenList.add(values);
  }

  public void treatIdentifier(int index) {
    String word = "";
    int i = 0;
    for (i = index; i < codeToAnalyse.length(); i++) {
      char character = codeToAnalyse.charAt(i);
      if (!Character.isDigit(character) && !Character.isAlphabetic(character) && character != '_') {
        break;
      }
      word += Character.toString(character);
    }
    indexStopped = i;
    String[] values = {word, searchReserved(word)};
    tokenList.add(values);
  }

  public void treatAttributions(int index) {
    char character = codeToAnalyse.charAt(index);
    String word = "";
    word = Character.toString(character);
    indexStopped = index + 1;

    character = codeToAnalyse.charAt(index + 1);
    if (character == '=') {
      indexStopped++;
      word += Character.toString(character);
    }

    String[] values = {word, searchReserved(word)};
    tokenList.add(values);
  }

  public void treatCondicionalOperations(int index) {
    char character = codeToAnalyse.charAt(index);
    String word = "";
    word = Character.toString(character);
    indexStopped = index + 1;

    if (character != '=') {
      character = codeToAnalyse.charAt(index + 1);
      if (character == '=') {
        indexStopped++;
        word += Character.toString(character);
      }
    }

    String[] values = {word, searchReserved(word)};
    tokenList.add(values);
  }

  public void treatOperations(int index) {
    char character = codeToAnalyse.charAt(index);
    indexStopped = index + 1;
    String[] values = {Character.toString(character), searchReserved(Character.toString(character))};
    tokenList.add(values);
  }

  public void treatPontuation(int index) {
    char character = codeToAnalyse.charAt(index);
    indexStopped = index + 1;
    String[] values = {Character.toString(character), searchReserved(Character.toString(character))};
    tokenList.add(values);
  }

  public String searchReserved(String word) {
    int index = reservedLexemes.indexOf(word);
    if (index >= 0) {
      return reservedSymbols.get(index);
    }
      return "sidentificador";
  }
}
