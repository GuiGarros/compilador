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
            AnalyseSimpleCommand(token);
            while (token[1] != "sfim") {
                if (token[1].equals("spontovirgula")) {
                    token = analyser.getNextToken();
                    if (token[1] != "sfim") {
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

    public void AnalyseSimpleCommand(String[] originalToken) {
        switch (originalToken[1]) {
            case "sidentificador":
                AnalyserProcedureAtrib(originalToken);
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

    public void AnalyserProcedureAtrib(String[] originalToken) {
        if (originalToken[1].equals("satribuição")) {
            analyser_atrib(originalToken);
        } else {
            analyser_call_procedure();
        }
    }

    public void analyser_atrib(String[] originalToken) {
        analyser_expression(originalToken);
    }

    public void AnalyserRead() {
        String[] token = analyser.getNextToken();
        if (token[1].equals("sabreparenteses")) {
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
                if (token[1].equals("sfecha_parenteses")) {
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
            AnalyseSimpleCommand(token);

        } else {
            throw new Error("Error sfaca");
        }

    }

    public void analyserIf() {
        String[] token = analyser.getNextToken();
        analyser_expression(token);

        if (token[1].equals("sentao")) {
            token = analyser.getNextToken();
            AnalyseSimpleCommand(token);

            if (token[1] == "ssenao") {
                token = analyser.getNextToken();
                AnalyseSimpleCommand(token);
            }
        } else {
            throw new Error("Error sentao");
        }
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

    public void analyser_expression(String[] originalToken) {
        analyser_expression_simple(originalToken);

        if (originalToken[1].equals("smaior") || originalToken[1].equals("smaiorig") || originalToken[1].equals("smenor") || originalToken[1].equals("smenorig")) {
            String[] token = analyser.getNextToken();
            analyser_expression_simple(token);
        }
    }

    public void analyser_expression_simple(String[] originalToken) {
        if (originalToken[1].equals("smais") || originalToken[1].equals("smenos")) {
            String[] token = analyser.getNextToken();
            analyser_term(token);

            while (token[1].equals("smais") || token[1].equals("smenos") || token[1].equals("sou")) {
                token = analyser.getNextToken();
                analyser_term(token);
            }
        }

    }

    public void analyser_term(String[] originalToken) {
        analyser_factor(originalToken);

        while (originalToken[1].equals("smulti") || originalToken[1].equals("sdiv") || originalToken[1].equals("sse")) {

            String[] token = analyser.getNextToken();
            analyser_factor(token);

        }
    }

    public void analyser_factor(String[] originalToken) {
        if (originalToken[1].equals("sidentificador")) {
            // chama analisa função vai fazer no semantico
        } else if (originalToken[1].equals("snumero")) {
            String[] token = analyser.getNextToken();
        } else if (originalToken[1].equals("snao")) {
            String[] token = analyser.getNextToken();
            analyser_factor(token);
        } else if (originalToken[1].equals("sabre_parenteses")) {
            String[] token = analyser.getNextToken();
            analyser_expression(token);
            if (token[1].equals("sfecha_parenteses")) {
                token = analyser.getNextToken();
            } else {
                throw new Error("Error ausencia de fecha parenteses )");
            }
        } else if (originalToken[0].equals("verdadeiro") || originalToken[0].equals("falso")) {
            String[] token = analyser.getNextToken();
        } else {

            throw new Error("Error");
        }
    }

    public void analyser_call_procedure() {

    }

    public void analyser_call_function() {

    }
}
