package Compiler.SyntaxAnalyser;

import Compiler.LexicalAnalyser.LexicalAnalyser;

import java.util.ArrayList;
import java.util.Arrays;

public class SyntaxAnalyser {
  private LexicalAnalyser analyser = null;

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

          if (token[1].equals("sponto_vírgula")) {
            token = AnalyseBlock();
            if (token[1].equals("sponto")) {
              //validar se acabou arquivo, se não erro
              System.out.println("Fim do arquivo");
              break;
            } else {
              throw new Error("Error: Ausência de um '.' no final do programa.");
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
        token = analyser.getNextToken();
        if (token[1].equals("svírgula") || token[1].equals("sdoispontos")) {
          if (token[1].equals("svírgula")) {
            token = analyser.getNextToken();
            if (token[1].equals("sdoispontos")) {
              throw new Error("Error: Ausência de ':' na declaração da variável.");
            }
          }
        } else {
          throw new Error("Error: Ausência de ',' ou ':' na declaração das variáveis.");
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
    token = analyser_expression(token);
    return token;
  }

  public String[] AnalyserRead() {
    String[] token = analyser.getNextToken();
    if (token[1].equals("sabre_parênteses")) {
      token = analyser.getNextToken();
      if (token[1].equals("sidentificador")) {
        token = analyser.getNextToken();
        if (token[1].equals("sfecha_parênteses")) {
          token = analyser.getNextToken();
        } else {
          throw new Error("Error: Ausência de ')' na declaração de uma leitura.");
        }
      }
    }
    return token;
  }

  public String[] AnalyseWrite() {
    String[] token = analyser.getNextToken();
    if (token[1].equals("sabre_parênteses")) {
      token = analyser.getNextToken();
      if (token[1].equals("sidentificador")) {
        token = analyser.getNextToken();
        if (token[1].equals("sfecha_parênteses")) {
          token = analyser.getNextToken();
        } else {
          throw new Error("Error: Ausência de ')' na declaração de uma escrita.");
        }
      } else {
        throw new Error("Error: Ausência de um 'identificador' na declaração de uma escrita.");
      }
    } else {
      throw new Error("Error: Ausência de '(' na declaração de uma escrita.");
    }
    return token;
  }

  public String[] analyserWhile() {
    String[] token = analyser.getNextToken();
    token = analyser_expression(token);

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
    token = analyser_expression(token);

    if (token[1].equals("sentao")) {
      token = analyser.getNextToken();
      token = AnalyseSimpleCommand(token);

      if (token[1].equals("ssenao")) {
        token = analyser.getNextToken();
        token =  AnalyseSimpleCommand(token);
      }
    } else {
      throw new Error("Error sentao");
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
          throw new Error("Error sponto_vírgula");
      }
    }
    return token;
  }

  public String[] analyser_procedure_declaration() {
    String[] token = analyser.getNextToken();
    if (token[1].equals("sidentificador")) {
      token = analyser.getNextToken();
      if (token[1].equals("sponto_vírgula")) {
        AnalyseBlock();
      } else {
        throw new Error("Error sponto_vírgula");
      }
    } else {
      throw new Error("Error sidentificador");
    }
    return token;
  }

  public String[] analyser_function_declaration() {
    String[] token = analyser.getNextToken();

    if (token[1].equals("sidentificador")) {
      token = analyser.getNextToken();

      if (token[1].equals("sdoispontos")) {
        token = analyser.getNextToken();

        if (token[1].equals("sinteiro") || token[1].equals("sbooleano")) {
          token = analyser.getNextToken();
          if (token[1].equals("sponto_vírgula")) {
            AnalyseBlock();
          }
        } else {
          throw new Error("Error sinteiro || booleano");
        }
      } else {
        throw new Error("Error sdoipontos");
      }
    } else {
      throw new Error("Error sidentificador");
    }
    return token;
  }

  public String[] analyser_expression(String[] originalToken) {
    String[] token = originalToken;
    token = analyser_expression_simple(token);

    if (
            token[1].equals("smaior") ||
                    token[1].equals("smaiorig") ||
                    token[1].equals("smenor") ||
                    token[1].equals("smenorig") ||
                    token[1].equals("sdif") ||
                    token[1].equals("sig")) {
      token = analyser.getNextToken();
      token = analyser_expression_simple(token);
    }

    return token;
  }

  public String[] analyser_expression_simple(String[] originalToken) {
    String[] token = originalToken;
    if (token[1].equals("smais") || token[1].equals("smenos")) {
      token = analyser.getNextToken();
    }
    token = analyser_term(token);
    while (token[1].equals("smais") || token[1].equals("smenos") || token[1].equals("sou")) {
      token = analyser.getNextToken();
      token = analyser_term(token);
    }

    return token;
  }

  public String[] analyser_term(String[] originalToken) {
    String[] token = originalToken;
    token = analyser_factor(token);

    while (token[1].equals("smult") || token[1].equals("sdiv") || token[1].equals("se")) {
      token = analyser.getNextToken();
      token = analyser_factor(token);
    }

    return token;
  }

  public String[] analyser_factor(String[] originalToken) {

    String [] token = originalToken;
    System.out.println(token[1]);
    if (token[1].equals("sidentificador")) {
      analyser_call_function();
      return analyser.getNextToken();
    } else if (token[1].equals("snúmero")) {
      return analyser.getNextToken();
    } else if (token[1].equals("snao")) {
      token = analyser.getNextToken();
      analyser_factor(token);
    } else if (token[1].equals("sabre_parênteses")) {
      token = analyser.getNextToken();
      token = analyser_expression(token);
      if (token[1].equals("sfecha_parênteses")) {
        return analyser.getNextToken();
      } else {
        throw new Error("Error ausencia de fecha parenteses )");
      }
    } else if (token[0].equals("verdadeiro") || token[0].equals("falso")) {
      return analyser.getNextToken();
    } else {

      throw new Error("Error");
    }

    return token;
  }

  public void analyser_call_procedure() {

  }

  public void analyser_call_function() {

  }
}
