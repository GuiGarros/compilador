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
      line = line.replace(" ", "");

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

  private String treatToken(String texto){

    try{
      return "guilherme";

    }
    catch(Exception e)
    {
      throw e;
    }
  }
}
