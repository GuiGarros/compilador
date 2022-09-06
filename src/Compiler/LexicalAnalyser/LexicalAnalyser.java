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

  }

  public void setCodeReaded(ArrayList<String> codeToAnalyse) {
    this.codeToAnalyse = codeToAnalyse;
  }

  public void AnalyseLexemes() {
   treatText();
  }

  private void treatText() {
    for (int i = 0; i < this.codeToAnalyse.size();i++){
      String line = this.codeToAnalyse.get(i);

      ////
      ////
      //// remove os espaços da linha
      //// line = line.replace(" ", "");
      ////
      ////

      if(line.isEmpty())
      {
        this.codeToAnalyse.remove(i);
      }
      else
      {
        this.codeToAnalyse.set(i,line);
      }


    }

    removeComment();

    System.out.println(this.codeToAnalyse);

  }

  private void removeComment() {
    try {
      boolean aux = false;

      for (int i = 0; i < this.codeToAnalyse.size();i++) {

        if(!this.codeToAnalyse.get(i).isEmpty()){

          String texto = this.codeToAnalyse.get(i);

          for(int j = 0; j < texto.length(); j++)
          {
            if (texto.charAt(j) == '{') aux = true;

            if (texto.charAt(j) == '}') {
              texto = texto.substring(0, j) + texto.substring((j + 1));
              aux = false;
            }

            if (aux) {
              texto = texto.substring(0, j) + texto.substring((j + 1));
              if(!texto.isEmpty())
              {
                j--;
              }
            }

            if(texto.isEmpty())
            {
              this.codeToAnalyse.remove(i);
              i--;
            }
            else
            {
              this.codeToAnalyse.set(i,texto);
            }

          }

        }

      }
    } catch (Exception e) {
      throw e;
    }
  }

  private void treatToken(){

    try{

      for (int i = 0; i < this.codeToAnalyse.size();i++) {
        String text = this.codeToAnalyse.get(i);

        for(int j = 0; j < text.length(); j++)
        {

          if(Character.isDigit(text.charAt(j)))
          {
            treatDigit(text);
          }
          else if(Character.isAlphabetic(text.charAt(j)))
          {
            treatIdentifier(text);
          }
          else if(text.charAt(j) == ':')
          {
            treatAttributions(text);
          }
          else if(text.charAt(j) == '+' || text.charAt(j) == '-' || text.charAt(j) == '*')
          {
            treatOperations(text);
          }
          else if(text.charAt(j) == '!' || text.charAt(j) == '<' || text.charAt(j) == '>' || text.charAt(j) == '=')
          {
            ///não sei qual vai aqui
          }
          else if(text.charAt(j) == ';' || text.charAt(j) == ',' || text.charAt(j) == '(' || text.charAt(j) == ')' || text.charAt(j) == '.')
          {
            treatPontuation(text);
          }
          else
          {
            new Exception("error");
          }

        }

      }


    }
    catch(Exception e)
    {
      throw e;
    }
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
