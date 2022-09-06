package Compiler.LexicalAnalyser;

import Services.FileReader;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LexicalAnalyser {
  ArrayList<String> reservedSymbols = new ArrayList<String>();
  ArrayList<String> reservedLexemes = new ArrayList<String>();
  ArrayList<String[]> tokenList = new ArrayList<String[]>();

  String codeToAnalyse = "";
  ArrayList<String[]> tokenArray = new ArrayList<String[]>();
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
  }

  private void treatText() {
    removeComment();
    System.out.println(this.codeToAnalyse);
  }

  private void removeComment() {
    try {
      boolean commentFlag = false;
      for (int i = 0, j = 0; i < this.codeToAnalyse.length(); i++) {
        char character = this.codeToAnalyse.charAt(i);
        if (character == '{' && !commentFlag) {
          commentFlag = true;
          j = i;
        }

        if (character == '}') {
          commentFlag = false;
          codeToAnalyse = codeToAnalyse.substring(0, j) + codeToAnalyse.substring((j + 1));
        }
      }
    } catch (Exception e) {
      throw e;
    }
  }

  private void treatToken() {

    try {

      for (int i = 0; i < this.codeToAnalyse.size(); i++) {
        String text = this.codeToAnalyse.get(i);

        for (int j = 0; j < text.length(); j++) {

          if (text.charAt(j) > 47 && text.charAt(j) < 58) {
            treatDigit(text);
          } else if ((text.charAt(j) > 64 && text.charAt(j) < 91) || (text.charAt(j) > 96 && text.charAt(j) < 123)) {
            treatIdentifier(text);
          } else if (text.charAt(j) == ':') {
            treatAttributions(text);
          } else if (text.charAt(j) == '+' || text.charAt(j) == '-' || text.charAt(j) == '*') {
            treatOperations(text);
          } else if (text.charAt(j) == '!' || text.charAt(j) == '<' || text.charAt(j) == '>' || text.charAt(j) == '=') {
            ///não sei qual vai aqui
          } else if (text.charAt(j) == ';' || text.charAt(j) == ',' || text.charAt(j) == '(' || text.charAt(j) == ')' || text.charAt(j) == '.') {
            treatPontuation(text);
          } else {
            new Exception("error");
          }

        }

      }


    } catch (Exception e) {
      throw e;
    }
  }

  public void treatDigit(int index) {
    String word = "";
    for (int i = index; i < codeToAnalyse.length(); i++) {
      char character = codeToAnalyse.charAt(index);
      if (!Character.isDigit(character)) {
        break;
      }
      word += Character.toString(character);
    }

    String[] values = {word, searchReserved(word)};
    tokenList.add(values);
  }

  public void treatIdentifier(int index) {
    String word = "";
    int i = 0;
    for (i = index; i < codeToAnalyse.length(); i++) {
      char character = codeToAnalyse.charAt(index);
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
    //    return values;
  }

  public String searchReserved(String word) {
    int index = reservedLexemes.indexOf(word);
    if (index >= 0) {
      return reservedSymbols.get(index);
    }
    return "sidentificador";
  }


}
