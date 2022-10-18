package Compiler.SyntaxAnalyser;

import Compiler.LexicalAnalyser.LexicalAnalyser;

import java.util.ArrayList;

public class SyntaxAnalyser {
  private LexicalAnalyser analyser = null;

  public void setAnalyser(LexicalAnalyser analyser) {
    this.analyser = analyser;
  }

  public void AnalyzeSyntax() {
    while (true){
      String [] token = analyser.getNextToken();
      if(token[1] == "sprograma"){
        token = analyser.getNextToken();

        if(token[1] == "sindentificador"){
          token = analyser.getNextToken();

          if(token[1] == "spontovirgula"){
            AnalyseBlock();
            token = analyser.getNextToken();

            if(token[1] == "sponto"){
              //validar se acabou arquivo, se não erro
              System.out.println("Fim do arquivo");
              break;
            } else {
              throw new Error("SPONTO");
            }
          } else {
            throw new Error("SPONTOVIRGULA");
          }
        } else {
          throw new Error("SIDENTIFICADOR");
        }
      } else {
        throw new Error("SPROGRAMA");
      }
    }
  }

  public void AnalyseBlock(){
    String[] token = analyser.getNextToken();

    AnalyseVariables(token);
    analyserSubRoutines(token);
    AnalyseCommand(token);
  }

  public void AnalyseDeclarations(String[] originalToken){

    if(originalToken[1] == "svar"){
      String[] token = analyser.getNextToken();

      if(token[1] == "sidentificador"){
        while (token[1] == "sidentificador"){
          //Analisa Variavel
          if(token[1] == "spontvirgula") {
            token = analyser.getNextToken();
          } else {
            throw new Error("BLOCO SPONTOVIRGULA");
          }

          token = analyser.getNextToken();
        }
      } else {
        throw new Error("BLOCO SIDENTIFICADOR");
      }
    }
  }

  public void AnalyseVariables(String[] originalToken) {
    String[] token = originalToken;
    while (true) {
      if (token[1] == "sidentificador") {
        token = analyser.getNextToken();
        if (token[1] == "svirgula" || token[1] == "sdoispontos") {
          if (token[1] == "svirgula") {
            token = analyser.getNextToken();
            if (token[1] == "sdoispontos") {
              throw new Error("BLOCO SPONTOVIRGULA");
            }
          }
        } else {
          throw new Error("BLOCO SPONTOVIRGULA");
        }
      }
      if(token[1] == "sdoispontos"){
        break;
      }
    }
    token = analyser.getNextToken();
    AnalyseType(token);
  }

  public void AnalyseType(String[] originalToken){
    if(originalToken[1] != "sinteiro" && originalToken[1] != "sbooleano"){
      throw new Error("BLOCO SPONTOVIRGULA");
    } else {
      String[] token = analyser.getNextToken();
    }
  }

  public void AnalyseCommand(String[] originalToken) {
    if (originalToken[1] != "sinicio") {
      String[] token = analyser.getNextToken();
      AnalyseSimpleCommand(token);
      while(token[1] != "sfim"){
        if(token[1] == "spontovirgula"){
          token = analyser.getNextToken();
          if(token[1] != "sfim"){
            AnalyseSimpleCommand(token);
          } else {
            break;
          }
        } else {
          throw new Error("BLOCO SPONTOVIRGULA");
        }
        token = analyser.getNextToken();
      }
    }
  }

  public void AnalyseSimpleCommand(String[] originalToken){
    switch (originalToken[1]){
      case "sidentificador":
        AnalyseProcedureAtrib(originalToken);
        break;
      case "sse":
        analyserIf();
        break;
      case "senquanto":
        analyserWhile();
        break;
      case "sleia":
        AnalyserRead();
        break;
      case "sescreva":
        AnalyseWrite();
        break;
      default:
        AnalyseCommand(originalToken);
    }
  }

  public void AnalyseProcedureAtrib(String[] originalToken){
    if(originalToken[1] == "satribuição"){
      //Analisa_atribuicao
    } else {
      //Chamada_procedimento
    }
  }

  public void AnalyserRead(){
    String[] token = analyser.getNextToken();
    if(token[1] == "sabreparenteses"){
      token = analyser.getNextToken();
      if(token[1] == "sidentificador"){
        token = analyser.getNextToken();
        if(token[1] == "sfechaparenteses") {
          token = analyser.getNextToken();
        } else {
          throw new Error("BLOCO SPONTOVIRGULA");
        }
      }
    }
  }

  public void AnalyseWrite(){
    String[] token = analyser.getNextToken();
    if(token[1] == "sabre_parenteses "){
      token = analyser.getNextToken();
      if(token[1] == "sidentificador"){
        token = analyser.getNextToken();
        if(token[1] == "sfecha_parenteses"){
          token = analyser.getNextToken();
        } else {
          throw new Error("BLOCO SPONTOVIRGULA");
        }
      } else {
        throw new Error("BLOCO SPONTOVIRGULA");
      }
    } else {
      throw new Error("BLOCO SPONTOVIRGULA");
    }
  }

  public void analyserWhile()
  {
    String[] token = analyser.getNextToken();
    analyser_expression(token);

    if(token[1] == "sfaca")
    {
      token = analyser.getNextToken();
      AnalyseSimpleCommand(token);

    } else {
      throw new Error("Error sfaca");
    }

  }

  public void analyserIf()
  {
    String[] token = analyser.getNextToken();
    analyser_expression(token);

    if(token[1] == "sentao")
    {
      token = analyser.getNextToken();
      AnalyseSimpleCommand(token);

      if(token[1] == "ssenao") {
        token = analyser.getNextToken();
        AnalyseSimpleCommand(token);
      }
    } else {
      throw new Error("Error sentao");
    }
  }

  public void analyserSubRoutines(String[] originalToken)
  {
    while(originalToken[1] == "sprocedimento" || originalToken[1] == "sfuncao")
    {
      if(originalToken[1] == "sprocedimento")
      {
        analyser_procedure_declaration();
      } else {
        analyser_function_declaration();
      }
      if(originalToken[1] == "sponto_vírgula")
      {
        String[] token = analyser.getNextToken();
      } else {
        throw new Error ("Error sponto_virgula");
      }
    }
  }

  public void analyser_procedure_declaration()
  {
    String [] token = analyser.getNextToken();
    if(token[1] == "sidentificador")
    {
      token = analyser.getNextToken();
      if(token[1] == "sponto_vírgula") {
        AnalyseBlock();
      } else {
        throw new Error("Error sponto_vírgula");
      }
    } else {
      throw new Error("Error sidentificador");
    }
  }

  public void analyser_function_declaration()
  {
    String[] token = analyser.getNextToken();

    if(token[1] == "sidentificador")
    {
      token = analyser.getNextToken();

      if(token[1] == "sdoispontos")
      {
        token = analyser.getNextToken();

        if(token[1] == "sinteiro" || token[1] == "sbooleano")
        {
          token = analyser.getNextToken();
          if(token[1] == "sponto_vírgula")
          {
            AnalyseBlock();
          }
        } else {
          throw new Error("Error sinteiro || booleano");
        }
      } else {
        throw new Error("Error sdoipontos");
      }
    } else
    {
      throw new Error("Error sidentificador");
    }
  }

  public void analyser_expression(String[] originalToken)
  {
    analyser_expression_simple(originalToken);

    if(originalToken[1] == "smaior" || originalToken[1] == "smaiorig" || originalToken[1] == "smenor" || originalToken[1] == "smenorig")
    {
      String[] token = analyser.getNextToken();
      analyser_expression_simple(token);
    }
  }

  public void analyser_expression_simple(String[] originalToken)
  {
    if(originalToken[1] == "smais" || originalToken[1] == "smenos")
    {
      String[] token = analyser.getNextToken();
      analyser_term(token);

      while(token[1] == "smais" || token[1] == "smenos" || token[1] == "sou")
      {
        token = analyser.getNextToken();
        analyser_term(token);
      }
    }

  }

  public void analyser_term(String[] originalToken)
  {
    analyser_factor(originalToken);

    while(originalToken[1] == "smulti" || originalToken[1] == "sdiv" || originalToken[1] == "sse")
    {

      String[] token = analyser.getNextToken();
      analyser_factor(token);

    }
  }

  public void analyser_factor(String[] originalToken)
  {
    if(originalToken[1] == "sidentificador")
    {
      // chama analisa função
    } else if(originalToken[1] == "snumero"){
        String[] token = analyser.getNextToken();
    } else if(originalToken[1] == "snao"){
      String[] token = analyser.getNextToken();
      analyser_factor(token);
    } else if(originalToken[1] == "sabre_parenteses"){
      String[] token = analyser.getNextToken();
      analyser_expression(token);
      if(token[1] == "sfecha_parenteses"){
        token = analyser.getNextToken();
      } else {
        throw new Error ("Error ausencia de fecha parenteses )");
      }
    } else if(originalToken[0] == "verdadeiro" || originalToken[0] == "falso"){
      String[] token = analyser.getNextToken();
    } else {

      throw new Error("Error");
    }
  }

  public void analyser_call_procedure()
  {

  }

  public void analyser_call_function()
  {

  }
}
