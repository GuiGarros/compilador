package Compiler.SyntaxAnalyser;

import Compiler.LexicalAnalyser.LexicalAnalyser;
import java.util.LinkedList;
import Services.SimbolTable;
import Services.Stack;

public class SyntaxAnalyser {
  private LexicalAnalyser analyser = null;
  private Stack simbolTableStack = new Stack();
  public int level = 0;
  public void setAnalyser(LexicalAnalyser analyser) {
    this.analyser = analyser;
  }
 public void AnalyzeSyntax() {

    while (true) {
      String[] token = analyser.getNextToken();
      if (token[1].equals("sprograma")) {
        token = analyser.getNextToken();
        if (token[1].equals("sidentificador")) {
          token = analyser.getNextToken();
          Insert_table(new SimbolTable(token[0],"nomedeprograma",level,"","")); ///lexema // inicio de programa // tipo null // level 0// linha sei la
          if (token[1].equals("sponto_vírgula")) {
            token = AnalyseBlock();
            if (token[1].equals("sponto")) {
              //validar se acabou arquivo, se não erro
              System.out.println("Fim do arquivo");
              break;
            } else {
              throw new Error("Error: Ausência de um '.' no final do programa ou dois ';' na declaração do programa.");
            }
          } else {
            throw new Error("Error: Ausência de um ';'.");
          }
        } else {
          throw new Error("Error: Ausência de um 'identificador' para o programa.");
        }
      } else {
        throw new Error("Error: Ausência de 'programa' na inicialização.");
      }
    }
  }

  public String[] AnalyseBlock() {
    String[] token = analyser.getNextToken();

    token = AnalyseEtVariables(token);
    token = analyserSubRoutines(token);
    token = AnalyseCommand(token);

    return token;
  }

  public String[] AnalyseEtVariables(String[] originalToken) {
    if (originalToken[1].equals("svar")) {
      String[] token = analyser.getNextToken();
      if (token[1].equals("sidentificador")) {
        while (token[1].equals("sidentificador")) {
          token = AnalyseVariables(token);
          if (token[1].equals("sponto_vírgula")) {
            token = analyser.getNextToken();
          } else {
            throw new Error("Error: Ausência de ';' no final da linha.");
          }
        }
      } else {
        throw new Error("Error: Ausência de um 'identificador' para a variável.");
      }
      return token;
    }
    return originalToken;
  }

  public String[] AnalyseVariables(String[] originalToken) {
    String[] token = originalToken;
    while (true) {
      if (token[1].equals("sidentificador")) {
        if (!Search_duplicatadevar_table(new SimbolTable(token[0],"variável",level,"",""))) {
          Insert_table(new SimbolTable(token[0],"variável",level,"",""));
          token = analyser.getNextToken();
          if (token[1].equals("svírgula") || token[1].equals("sdoispontos")) {
            if (token[1].equals("svírgula")) {
              token = analyser.getNextToken();
              if (token[1].equals("sdoispontos")) {
                throw new Error("Erro: Ausência de ':' na declaração da variável ou uma ',' a mais.");
              }
            }
          } else {
            throw new Error("Erro: Ausência de ',' ou ':' na declaração das variáveis.");
          }
        } else {
          throw new Error("Erro: Variável duplicada");
        }
      }
      if (token[1].equals("sdoispontos")) {
        break;
      }
    }
    token = analyser.getNextToken();
    token = AnalyseType(token);
    return token;
  }

  public String[] AnalyseType(String[] originalToken) {
    if (!originalToken[1].equals("sinteiro") && !originalToken[1].equals("sbooleano")) {
      throw new Error("Error: Ausência do tipo de variável.");
    } else {
      Insert_table(new SimbolTable(originalToken[0],"variavel",0,null,null));
      String[] token = analyser.getNextToken();
      return token;
    }
  }

  public String[] AnalyseCommand(String[] originalToken) {
    if (originalToken[1].equals("sinício")) {
      String[] token = analyser.getNextToken();
      token = AnalyseSimpleCommand(token);
      while (!token[1].equals("sfim")) {
        if (token[1].equals("sponto_vírgula")) {
          token = analyser.getNextToken();
          if (!token[1].equals("sfim")) {
            token = AnalyseSimpleCommand(token);
          } else {
            break;
          }
        } else {
          throw new Error("Error: Ausência de ';' no final da linha.");
        }
      }
      token = analyser.getNextToken();
      return token;
    }
    return originalToken;
  }

  public String[] AnalyseSimpleCommand(String[] originalToken) {
    String[] token;
    switch (originalToken[1]) {
      case "sidentificador":
        token = AnalyserProcedureAtrib(originalToken);
        return token;
      case "sse":
        token = analyserIf();
        return token;
      case "senquanto":
        token = analyserWhile();
        return token;
      case "sleia":
        token = AnalyserRead();
        return token;
      case "sescreva":
        token = AnalyseWrite();
        return token;
      default:
        token = AnalyseCommand(originalToken);
        return token;
    }
  }

  public String[] AnalyserProcedureAtrib(String[] originalToken) {
    String[] token = analyser.getNextToken();
    if (token[1].equals("satribuição")) {
      token = analyser_atrib(token);
    } else {
      analyser_call_procedure();
    }
    return token;
  }

  public String[] analyser_atrib(String[] originalToken) {
    String[] token = analyser.getNextToken();
    LinkedList <String[]> expression = new LinkedList <String[]>();
    expression = analyser_expression(token,expression);
    token = expression.getLast();
    return token;
  }

  public String[] AnalyserRead() {
    String[] token = analyser.getNextToken();
    if (token[1].equals("sabre_parênteses")) {
      token = analyser.getNextToken();
      if (token[1].equals("sidentificador")) {
        if (Search_declarationvar_table(token[0],level)) {
          token = analyser.getNextToken();
          if (token[1].equals("sfecha_parênteses")) {
            token = analyser.getNextToken();
          } else {
            throw new Error("Error: Ausência de ')' na declaração de uma leitura.");
          }
        } else {
          throw new Error("Variavel não declarada");
        }
      } else {
        throw  new Error("sidentificador");
      }
    } else {
      throw  new Error("sabre_parênteses");
    }
    return token;
  }

  public String[] AnalyseWrite() {
    String[] token = analyser.getNextToken();
    if (token[1].equals("sabre_parênteses")) {
      token = analyser.getNextToken();
      if (token[1].equals("sidentificador")) {
        if (Search_declarationvarfunc_table(token[0], level)) {
          token = analyser.getNextToken();
          if (token[1].equals("sfecha_parênteses")) {
            token = analyser.getNextToken();
          } else {
            throw new Error("Erro: Ausência de ')' na declaração de uma escrita.");
          }
        } else {
          throw new Error("Varivael ou função não declarada");
        }
      } else {
        throw new Error("Erro: Ausência de um 'identificador' na declaração de uma escrita.");
      }
    } else {
      throw new Error("Erro: Ausência de '(' na declaração de uma escrita.");
    }
    return token;
  }

  public String[] analyserWhile() {
    String[] token = analyser.getNextToken();
    LinkedList <String[]> expression = new LinkedList <String[]>();
    expression = analyser_expression(token,expression);
    token = expression.getLast();


    if (token[1].equals("sfaca")) {
      token = analyser.getNextToken();
      token = AnalyseSimpleCommand(token);

    } else {
      throw new Error("Error: Ausência de um 'faca' na declaração de um enquanto.");
    }
    return token;
  }

  public String[] analyserIf() {
    String[] token = analyser.getNextToken();
    LinkedList <String[]> expression = new LinkedList <String[]>();
    expression = analyser_expression(token,expression);
    token = expression.getLast();

    if (token[1].equals("sentao")) {
      token = analyser.getNextToken();
      token = AnalyseSimpleCommand(token);

      if (token[1].equals("ssenao")) {
        token = analyser.getNextToken();
        token = AnalyseSimpleCommand(token);
      }
    } else {
      throw new Error("Error: Ausência de 'entao' na declaração de uma operação condicional.");
    }
    return token;
  }

  public String[] analyserSubRoutines(String[] originalToken) {
    String[] token = originalToken;
    while (token[1].equals("sprocedimento") || token[1].equals("sfuncao")) {
      if (token[1].equals("sprocedimento")) {
        token = analyser_procedure_declaration();
      } else {
        token = analyser_function_declaration();
      }
      if (token[1].equals("sponto_vírgula")) {
        token = analyser.getNextToken();
      } else {
        throw new Error("Error: Ausência de ';' na declaração de um procedimento ou função.");
      }
    }
    return token;
  }

  public String[] analyser_procedure_declaration() {
    String[] token = analyser.getNextToken();
    level++;
    if (token[1].equals("sidentificador")) {
      if (!Search_declarationproc_table(token[0])) {
        Insert_table(new SimbolTable(token[0],"procedimento",level,null,null));
        token = analyser.getNextToken();
        if (token[1].equals("sponto_vírgula")) {
          AnalyseBlock();
        } else {
          throw new Error("Error: Ausência de ';' na declaração de  um procedimento.");
        }
      } else {
        throw new Error("Dupla ocorrencia de função");
      }
    } else {
      throw new Error("Error: Ausência de 'identificador' na declaração de um procedimento.");
    }
    level--;
    return token;
  }

  public String[] analyser_function_declaration() {
    String[] token = analyser.getNextToken();
    level++;
    if (token[1].equals("sidentificador")) {
      if (!Search_declarationfunc_table(token[0])) {
        token = analyser.getNextToken();
        if (token[1].equals("sdoispontos")) {
          token = analyser.getNextToken();
          if (token[1].equals("sinteiro") || token[1].equals("sbooleano")) {
            if (token[1].equals("sinteiro")) {
              Insert_table(new SimbolTable(token[0],"funcaointeiro",level+1,null,null)); ///lembrar de colocar o rotulo
            } else {
              Insert_table(new SimbolTable(token[0],"funcaobooleano",level+1,null,null)); //lembrar de colocar o rotulo
            }
            token = analyser.getNextToken();
            if (token[1].equals("sponto_vírgula")) {
              AnalyseBlock();
            }
          } else {
            throw new Error("Error: Ausência de um tipo na declaração de uma função.");
          }
        } else {
          throw new Error("Error: Ausência de ':' na declaração de uma função.");
        }
      } else {
        throw new Error("Dupla ocorrencia de função");
      }
    } else {
      throw new Error("Error: Ausência de 'identificador' na declaração de uma função.");
    }
    level--;
    return token;
  }

  public LinkedList<String[]> analyser_expression(String[] originalToken, LinkedList<String[]> expression) {
    String[] token = originalToken;

    expression = analyser_expression_simple(token,expression);
    token = expression.getLast();


    if (
            token[1].equals("smaior") ||
                    token[1].equals("smaiorig") ||
                    token[1].equals("smenor") ||
                    token[1].equals("smenorig") ||
                    token[1].equals("sdif") ||
                    token[1].equals("sig")) {
      token = analyser.getNextToken();
      expression.addLast(token);
      expression = analyser_expression_simple(token,expression);
      token = expression.getLast();
    }

    return expression;
  }

  public LinkedList<String[]> analyser_expression_simple(String[] originalToken, LinkedList<String[]> expression) {
    String[] token = originalToken;

    if (token[1].equals("smais") || token[1].equals("smenos")) { //unitario ai nesse caso passar o -u
      if(token[1].equals("smenos"))
      {
        token = expression.removeLast();
        token[1] = "-u";
        expression.addLast(token);
      }
      token = analyser.getNextToken();
      expression.addLast(token);
    }

    expression = analyser_term(token,expression);
    token = expression.getLast();

    while (token[1].equals("smais") || token[1].equals("smenos") || token[1].equals("sou")) {
      token = analyser.getNextToken();
      expression.addLast(token);
      expression = analyser_term(token,expression);
      token = expression.getLast();
    }

    return expression;
  }

  public LinkedList<String[]> analyser_term(String[] originalToken, LinkedList<String[]> expression) {
    String[] token = originalToken;
    expression = analyser_factor(token, expression);
    token = expression.getLast();

    while (token[1].equals("smult") || token[1].equals("sdiv") || token[1].equals("se")) {
      token = analyser.getNextToken();
      expression.addLast(token);
      expression = analyser_factor(token,expression);
      token =  expression.getLast();
    }

    return expression;
  }

  public  LinkedList<String[]> analyser_factor(String[] originalToken, LinkedList<String[]> expression) {

    String[] token = originalToken;
    String[] tipo;
    if (token[1].equals("sidentificador")) {
      if (!Search_table(token[0],level)) {
        token = analyser.getNextToken();
        expression.addLast(token);
      }
      token = analyser.getNextToken();
      expression.addLast(token);
      return expression;
    } else if (token[1].equals("snúmero")) {
      token = analyser.getNextToken();
      expression.addLast(token);
      return expression;
    } else if (token[1].equals("snao")) {
      token = analyser.getNextToken();
      expression.addLast(token);
      expression = analyser_factor(token,expression);
    } else if (token[1].equals("sabre_parênteses")) {
      token = analyser.getNextToken();
      expression.addLast(token);
      expression = analyser_expression(token,expression);
      if (token[1].equals("sfecha_parênteses")) {
        token = analyser.getNextToken();
        expression.addLast(token);
        return expression;
      } else {
        throw new Error("Error: Ausência de ')'.");
      }
    } else if (token[0].equals("verdadeiro") || token[0].equals("falso")) {
      token = analyser.getNextToken();
      expression.addLast(token);
      return expression;
    } else {
      throw new Error("Error: Ausência de verdadeiro ou falso.");
    }

    return expression;
  }

  public void analyser_call_procedure() {

  }
 soma:a+b;

  public void analyser_call_function(SimbolTable value) {
    // te.token = getToken(file);
    // te.expression += te.token.lexema + " ";
    if (!Search_declarationfunc_table(value.lexema)) {
      throw new Error("Função não declarada");
    }
    //te.token = getToken(file);
    // return te;
  }

  public void Insert_table(SimbolTable linha) {

    simbolTableStack.push(linha);

  }

  public boolean Search_duplicatadevar_table(SimbolTable value) {///1

     if(!simbolTableStack.findVariable(value.lexema, value.level))
     {
       if(!simbolTableStack.getIdentificador(value))
       {
         return false;
       }
     }

     return true;
  }

  public boolean Search_declarationvar_table(String value, int level) {

    return simbolTableStack.findVariable(value, level);

  }

  public boolean Search_declarationvarfunc_table(String value,int level) {

    if(simbolTableStack.findVariable(value,level))
    {
      if(simbolTableStack.findIdentifier(value)) return true;
    }
    return false;
  }

  public boolean Search_declarationproc_table(String value) {

    if(simbolTableStack.findProcedure(value)) return true;

    return false;
  }

  public boolean Search_declarationfunc_table(String value) {

    if(simbolTableStack.findFunction(value)) return true;

    return false;
  }

  public boolean Search_table(String value, int level) {

    if(!simbolTableStack.findIdentifier(value) && !simbolTableStack.findVariable(value,level))
    {
      throw new Error("ausencia de identificador");
    }

    if (simbolTableStack.findFunction(value)) {
      //analyser_call_function();
      return true;
    }
    return false;
  }

  public LinkedList<String[]> posfixo(LinkedList<String[]> expression) {

    LinkedList<String[]> Pilha = new LinkedList<String[]>();
    LinkedList<String> saida = new LinkedList<String>();

    for(int i = 0; i < expression.size(); i++)
    {
      String[] auxToken = expression.get(i);

      if(auxToken[1].equals("snúmero") || auxToken[1].equals("sidentificador") || auxToken[1].equals("sverdadeiro") || auxToken[1].equals("sfalso"))
      {
        saida.addLast(auxToken[0]);
      }
    }

  }
}

