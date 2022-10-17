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
            //Chamar Analisa Bloco
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

//    Analisa_et_variáveis
//    Analisa_subrotinas
//    Analisa_comandos
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
//        Pesquisa_duplicvar_ tabela(token.lexema)
//        se não encontrou duplicidade
//        então início
//        insere_tabela(token.lexema, “variável” ,””,””)
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
    //Analisa Tipo
  }

  public void AnalyseType(String[] originalToken){
    if(originalToken[1] != "sinteiro" && originalToken[1] != "sbooleano"){
      throw new Error("BLOCO SPONTOVIRGULA");
    } else {
      //coloca na tabela
      String[] token = analyser.getNextToken();
    }
//    se (token.símbolo  sinteiro e token.símbolo   sbooleano))
//    então ERRO
//    senão coloca_tipo_tabela(token.lexema)
//            Léxico(token)
  }

  public void AnalyseCommand(String[] originalToken) {
    if (originalToken[1] != "sinicio") {
      String[] token = analyser.getNextToken();
      //Analisa Comandos Simples
      while(token[1] != "sfim"){
        if(token[1] == "spontovirgula"){
          token = analyser.getNextToken();
          if(token[1] != "sfim"){
            //Analisa Comandos Simples
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
        //Analisa_atrib_chprocedimento
        break;
      case "sse":
        //Analisa_se
        break;
      case "senquanto":
        //Analisa_enquanto
        break;
      case "sleia":
        //Analisa_leia
        break;
      case "sescreva":
        //Analisa_escreva
        break;
      default:
        //Analisa_comandos
    }
  }

  public void AnalyseProcedureAtrib(String[] originalToken){
    if(originalToken[1] == "satribuição"){
      //Analisa_atribuicao
    } else {
      //Chamada_procedimento
    }
  }

  public void AnalyseRead(){
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
      }
    }
  }

}
