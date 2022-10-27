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
                        AnalyseBlock();
                        token = analyser.getNextToken();
                        System.out.println(token[1]);
                        if (token[1].equals("sponto")) {
                            //validar se acabou arquivo, se não erro
                            System.out.println("Fim do arquivo");
                            break;
                        } else {
                            throw new Error("Error: Falta '.' no final ");
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

    public void AnalyseBlock() {
        String[] token = analyser.getNextToken();

        AnalyseEtVariables(token);
        analyserSubRoutines(token);
        AnalyseCommand(token);
    }

    public void AnalyseEtVariables(String[] originalToken) {
        if (originalToken[1] == "svar") {
            String[] token = analyser.getNextToken();

            if (token[1].equals("sidentificador")) {
                while (token[1].equals("sidentificador")) {
                    AnalyseVariables(token);
                    if (token[1] == "spontvirgula") {
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
            if (token[1].equals("sidentificador")) {
                token = analyser.getNextToken();
                if (token[1].equals("svírgula") || token[1].equals("sdoispontos")) {
                    if (token[1].equals("svirgula")) {
                        token = analyser.getNextToken();
                        if (token[1].equals("sdoispontos")) {
                            throw new Error("BLOCO SPONTOVIRGULA");
                        }
                    }
                } else {
                    throw new Error("BLOCO SPONTOVIRGULA");
                }
            }
            if (token[1].equals("sdoispontos")) {
                break;
            }
        }
        token = analyser.getNextToken();
        AnalyseType(token);
    }

    public void AnalyseType(String[] originalToken) {
        if (originalToken[1] != "sinteiro" && originalToken[1] != "sbooleano") {
            throw new Error("BLOCO SPONTOVIRGULA");
        } else {
            String[] token = analyser.getNextToken();
        }
    }

    public void AnalyseCommand(String[] originalToken) {
        if (originalToken[1] != "sinicio") {
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
                    throw new Error("BLOCO SPONTOVIRGULA");
                }
                token = analyser.getNextToken();
            }
        }
    }

    public String[] AnalyseSimpleCommand(String[] originalToken) {


        switch (originalToken[1]) {
            case "sidentificador":
                String[] token = AnalyserProcedureAtrib(originalToken);
                return token;
            case "sse":
                token = analyserIf();
                return token;
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

        return originalToken;
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

    public void AnalyserRead() {
        String[] token = analyser.getNextToken();
        if (token[1].equals("sabre_parênteses")) {
            token = analyser.getNextToken();
            if (token[1].equals("sidentificador")) {
                token = analyser.getNextToken();
                if (token[1].equals("sfechaparenteses")) {
                    token = analyser.getNextToken();
                } else {
                    throw new Error("BLOCO SPONTOVIRGULA");
                }
            }
        }
    }

    public void AnalyseWrite() {
        String[] token = analyser.getNextToken();
        if (token[1].equals("sabreparenteses")) {
            token = analyser.getNextToken();
            if (token[1].equals("sidentificador")) {
                token = analyser.getNextToken();
                if (token[1].equals("sfecha_parênteses")) {
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

    public void analyserWhile() {
        String[] token = analyser.getNextToken();
        analyser_expression(token);

        if (token[1].equals("sfaca")) {
            token = analyser.getNextToken();
            token = AnalyseSimpleCommand(token);

        } else {
            throw new Error("Error sfaca");
        }

    }

    public String[] analyserIf() {
        String[] token = analyser.getNextToken();
        token = analyser_expression(token);

        if (token[1].equals("sentao")) {
            token = analyser.getNextToken();
            token = AnalyseSimpleCommand(token);

            if (token[1] == "ssenao") {
                token = analyser.getNextToken();
               token =  AnalyseSimpleCommand(token);
            }
        } else {
            throw new Error("Error sentao");
        }
        return token;
    }

    public void analyserSubRoutines(String[] originalToken) {
        while (originalToken[1].equals("sprocedimento") || originalToken[1].equals("sfuncao")) {
            if (originalToken[1].equals("sprocedimento")) {
                analyser_procedure_declaration();
            } else {
                analyser_function_declaration();
            }
            if (originalToken[1].equals("sponto_vírgula")) {
                String[] token = analyser.getNextToken();
            } else {
                throw new Error("Error sponto_virgula");
            }
        }
    }

    public void analyser_procedure_declaration() {
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
    }

    public void analyser_function_declaration() {
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

        while (token[1].equals("smulti") || token[1].equals("sdiv") || token[1].equals("se")) {
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
        } else if (token[1].equals("snumero")) {
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
