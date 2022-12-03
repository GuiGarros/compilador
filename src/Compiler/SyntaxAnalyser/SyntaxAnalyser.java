package Compiler.SyntaxAnalyser;

import Compiler.GeraCodigo.GeraCodigo;
import Compiler.LexicalAnalyser.LexicalAnalyser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.*;

import Services.SimbolTable;
import Services.Stack;

public class SyntaxAnalyser {
    private LexicalAnalyser analyser = null;
    private Stack simbolTableStack = new Stack();
    public int level = 0;
    public int rotulo = 1;
    private GeraCodigo gera = new GeraCodigo();
    public void setAnalyser(LexicalAnalyser analyser) {
        this.analyser = analyser;
    }

    public void AnalyzeSyntax() throws IOException {
        gera.criaCodigo("", "START", "", "");
        while (true) {
            String[] token = analyser.getNextToken();
            if (token[1].equals("sprograma")) {
                token = analyser.getNextToken();
                if (token[1].equals("sidentificador")) {
                    token = analyser.getNextToken();
                    Insert_table(new SimbolTable(token[0], "nomedeprograma", level, -1, "")); ///lexema // inicio de programa // tipo null // level 0// linha sei la
                    if (token[1].equals("sponto_vírgula")) {
                        token = AnalyseBlock();
                        if (token[1].equals("sponto")) {
                            //validar se acabou arquivo, se não erro
                            //gera.criaCodigo("DALLOC", "1"); // retorna de função
                            gera.criaCodigo("", "HLT", "", "");
                            System.out.println("Fim do arquivo");
                            gera.geraArquivo();
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
                if (!Search_duplicatadevar_table(new SimbolTable(token[0], "variável", level, -1, ""))) {
                    Insert_table(new SimbolTable(token[0], "variável", level, -1, ""));
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
            throw new Error("Error: Ausência de 'inteiro' ou 'booleano'.");
        } else {
            Insert_table(new SimbolTable(originalToken[0], "variavel", 0, -1, null));
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
        } else {
            throw new Error("Error: Ausência de 'inicio'.");
        }
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
        LinkedList<String[]> expression = new LinkedList<String[]>();
        expression.addLast(originalToken);
        if (token[1].equals("satribuição")) {
            token = analyser_atrib(token, expression);
        } else {
            analyser_call_procedure(token);
        }
        return token;
    }

    public String[] analyser_atrib(String[] originalToken, LinkedList<String[]> expression) {
        //LinkedList<String[]> expression = new LinkedList<String[]>();
        String[] token = analyser.getNextToken();
        expression.addLast(originalToken);
        //System.out.println("aqui ::: " + originalToken[0]);
        expression = analyser_expression(token, expression);
        token = expression.getLast();
        LinkedList<String> posExpression = new LinkedList<String>();
        System.out.println("expressão");
        printExpression(expression);
        posExpression = posfixo(expression);
        gera.criaCodigo(posExpression);
        gera.criaCodigo("", "STR", String.valueOf(posExpression.getFirst()), "");
        System.out.println("inicio");
        printExpression2(posExpression);
        System.out.println("fim");
        return token;
    }

    public String[] AnalyserRead() {
        gera.criaCodigo("", "RD", "", "");
        String[] token = analyser.getNextToken();
        if (token[1].equals("sabre_parênteses")) {
            token = analyser.getNextToken();
            if (token[1].equals("sidentificador")) {
                if (Search_declarationvar_table(token[0], level)) {
                    gera.criaCodigo("", "STR", String.valueOf(token[0]), "");
                    token = analyser.getNextToken();
                    if (token[1].equals("sfecha_parênteses")) {
                        token = analyser.getNextToken();
                    } else {
                        throw new Error("Error: Ausência de ')' na declaração de uma leitura.");
                    }
                } else {
                    throw new Error("Error: Variável não declarada.");
                }
            } else {
                throw new Error("Error: Ausência de 'sidentificador'.");
            }
        } else {
            throw new Error("Error: Ausência de '(' na declaração de uma leitura.");
        }
        return token;
    }

    public String[] AnalyseWrite() {
        String[] token = analyser.getNextToken();
        if (token[1].equals("sabre_parênteses")) {
            token = analyser.getNextToken();
            if (token[1].equals("sidentificador")) {
                if (Search_declarationvarfunc_table(token[0], level) != -1) {
                    if (Search_declarationvarfunc_table(token[0], level) == 1) { // É variável
                        gera.criaCodigo("", "LDV", String.valueOf(token[0]), "");
                    } else { // É função
                        gera.criaCodigo("", "CALL", "L" + level, "");
                        gera.criaCodigo("","LDV","","");
                    }
                    token = analyser.getNextToken();
                    if (token[1].equals("sfecha_parênteses")) {
                        gera.criaCodigo("", "PRN", "", "");
                        token = analyser.getNextToken();
                    } else {
                        throw new Error("Error: Ausência de ')' na declaração de uma escrita.");
                    }
                } else {
                    throw new Error("Error: Variável ou função não declarada.");
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
        int auxrot1, auxrot2;
        auxrot1 = rotulo;
        rotulo++;
        gera.criaCodigo("L" + auxrot1, "NULL", "", "");
        String[] token = analyser.getNextToken();
        LinkedList<String[]> expression = new LinkedList<String[]>();
        expression = analyser_expression(token, expression);
        token = expression.getLast();
        LinkedList<String> posExpression = new LinkedList<String>();
        posExpression = posfixo(expression);
        gera.criaCodigo(posExpression);
        // printExpression2(posExpression);
        if (token[1].equals("sfaca")) {
            auxrot2 = rotulo;
            auxrot2++;
            gera.criaCodigo("", "JMPF", "L" + String.valueOf(rotulo), "");
            token = analyser.getNextToken();
            token = AnalyseSimpleCommand(token);
            gera.criaCodigo("", "JMP", "L" + String.valueOf(auxrot1), "");
            gera.criaCodigo("L" + String.valueOf(auxrot2), "NULL", "", "");
        } else {
            throw new Error("Error: Ausência de um 'faca' na declaração de um enquanto.");
        }
        return token;
    }

    public String[] analyserIf() {
        int auxrot1, auxrot2;
        auxrot1 = rotulo;
        rotulo++;
        String[] token = analyser.getNextToken();
        LinkedList<String[]> expression = new LinkedList<String[]>();
        expression = analyser_expression(token, expression);
        token = expression.getLast();
        LinkedList<String> posExpression = new LinkedList<String>();
        posExpression = posfixo(expression);
        gera.criaCodigo(posExpression);
        gera.criaCodigo("", "JMPF", "L" + String.valueOf(auxrot1), "");
        auxrot2 = level;
        // printExpression2(posExpression);
        // geracodigoPosfixo;
        if (token[1].equals("sentao")) {
            token = analyser.getNextToken();
            token = AnalyseSimpleCommand(token);
            if (token[1].equals("ssenao")) {
                gera.criaCodigo("", "JMP", String.valueOf(auxrot2), "");
                gera.criaCodigo("L" + String.valueOf(auxrot1), "NULL", "", "");
                token = analyser.getNextToken();
                token = AnalyseSimpleCommand(token);
                gera.criaCodigo("L" + String.valueOf(auxrot2), "NULL", "", "");
            } else { // se não tiver o senão
                gera.criaCodigo("L" + String.valueOf(auxrot1), "NULL", "", "");
            }
        } else {
            throw new Error("Error: Ausência de 'entao' na declaração de uma operação condicional.");
        }
        return token;
    }

    public String[] analyserSubRoutines(String[] originalToken) {
        int auxrot1 = 0, flag = 0;
        String[] token = originalToken;
        if (token[1].equals("sprocedimento") || token[1].equals("sfuncao")) {
            auxrot1 = rotulo;
            rotulo++;
            gera.criaCodigo("", "JMP", "L" + String.valueOf(auxrot1), "");
            flag = 1;
        }
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
        if (flag == 1) gera.criaCodigo("L" + String.valueOf(auxrot1), "NULL", "", "");
        return token;
    }

    public String[] analyser_procedure_declaration() {
        String[] token = analyser.getNextToken();
        level++;
        if (token[1].equals("sidentificador")) {
            if (!Search_declarationproc_table(token[0])) {
                Insert_table(new SimbolTable(token[0], "procedimento", level, rotulo, null));
                gera.criaCodigo("L" + String.valueOf(rotulo), "NULL", "", "");
                rotulo++;
                token = analyser.getNextToken();
                if (token[1].equals("sponto_vírgula")) {
                    AnalyseBlock();
                } else {
                    throw new Error("Error: Ausência de ';' na declaração de um procedimento.");
                }
            } else {
                throw new Error("Error: Dupla ocorrência de procedimento");
            }
        } else {
            throw new Error("Error: Ausência de 'identificador' na declaração de um procedimento.");
        }
        gera.criaCodigo("", "RETURN", "", "");
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
                            Insert_table(new SimbolTable(token[0], "funcaointeiro", level + 1, rotulo, null));
                        } else {
                            Insert_table(new SimbolTable(token[0], "funcaobooleano", level + 1, rotulo, null));
                        }
                        gera.criaCodigo("L" + String.valueOf(rotulo), "NULL", "", "");
                        rotulo++;
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
                throw new Error("Error: Dupla ocorrencia de função");
            }
        } else {
            throw new Error("Error: Ausência de 'identificador' na declaração de uma função.");
        }
        gera.criaCodigo("", "RETURN", "", "");
        level--;
        return token;
    }

    public LinkedList<String[]> analyser_expression(String[] originalToken, LinkedList<String[]> expression) {
        String[] token = originalToken;
        expression = analyser_expression_simple(token, expression);
        token = expression.getLast();
        if (
                token[1].equals("smaior") ||
                        token[1].equals("smaiorig") ||
                        token[1].equals("smenor") ||
                        token[1].equals("smenorig") ||
                        token[1].equals("sdif") ||
                        token[1].equals("sig")) {
            token = analyser.getNextToken();
            //expression.addLast(token);
            expression = analyser_expression_simple(token, expression);
            token = expression.getLast();
        }
        return expression;
    }

    public LinkedList<String[]> analyser_expression_simple(String[] originalToken, LinkedList<String[]> expression) {
        String[] token = originalToken;
        if (token[1].equals("smais") || token[1].equals("smenos")) { //unitario ai nesse caso passar o -u
            if (token[1].equals("smenos")) {
                token = expression.removeLast();
                token[0] = "-u";
                expression.addLast(token);
            }
            token = analyser.getNextToken();
            expression.addLast(token);
            expression = analyser_term(token, expression);
            token = expression.getLast();
        } else {
            expression.addLast(token);
            expression = analyser_term(token, expression);
            token = expression.getLast();
        }
        while (token[1].equals("smais") || token[1].equals("smenos") || token[1].equals("sou")) {
            token = analyser.getNextToken();
            expression.addLast(token);
            expression = analyser_term(token, expression);
            //printExpression(expression);
            token = expression.getLast();
        }
        return expression;
    }

    public LinkedList<String[]> analyser_term(String[] originalToken, LinkedList<String[]> expression) {
        String[] token = originalToken;
        //printExpression(expression);
        expression = analyser_factor(token, expression);
        token = expression.getLast();
        while (token[1].equals("smult") || token[1].equals("sdiv") || token[1].equals("se")) {
            token = analyser.getNextToken();
            expression.addLast(token);
            expression = analyser_factor(token, expression);
            token = expression.getLast();
        }
        return expression;
    }

    public LinkedList<String[]> analyser_factor(String[] originalToken, LinkedList<String[]> expression) {
        String[] token = originalToken;
        String[] tipo;
        if (token[1].equals("sidentificador")) {
            int busca = simbolTableStack.findFunction(token[0]);
            if (busca == 0) throw new Error("identificador não encontrado");
            else if (busca == 1) {
                //expression.addLast(token);
                token = analyser.getNextToken();
                expression.addLast(token);
            } else if (busca == 2) {
                analyser_call_function(token);
                expression.addLast(token);
                token = analyser.getNextToken();
                expression.addLast(token);
            }
            return expression;
        } else if (token[1].equals("snúmero")) {
            token = analyser.getNextToken();
            expression.addLast(token);
            return expression;
        } else if (token[1].equals("snao")) {
            token = analyser.getNextToken();
            expression.addLast(token);
            expression = analyser_factor(token, expression);
        } else if (token[1].equals("sabre_parênteses")) {
            token = analyser.getNextToken();
            //expression.addLast(token);
            expression = analyser_expression(token, expression);
            token = expression.getLast();
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

    public void analyser_call_procedure(String[] originalToken) {
        Search_declarationproc_table(originalToken[0]);
        gera.criaCodigo("", "CALL", "L" + String.valueOf(rotulo), "");
    }

    public void analyser_call_function(String[] originalToken) {
        if (!Search_declarationfunc_table(originalToken[0])) {
            throw new Error("Função não declarada");
        } else {
            gera.criaCodigo("", "CALL", "L" + String.valueOf(rotulo), "");
            gera.criaCodigo("", "LDV", String.valueOf(0), "");
        }
    }

    public void Insert_table(SimbolTable linha) {
        simbolTableStack.push(linha);
    }

    public boolean Search_duplicatadevar_table(SimbolTable value) {///1
        if (!simbolTableStack.findVariable(value.lexema, value.level)) {
            if (!simbolTableStack.getIdentificador(value)) {
                return false;
            }
        }
        return true;
    }

    public boolean Search_declarationvar_table(String value, int level) {
        return simbolTableStack.findVariable(value, level);
    }

    public Integer Search_declarationvarfunc_table(String value, int level) {
        if (simbolTableStack.findVariable(value, level)) return 1;
        else if (simbolTableStack.findIdentifier(value)) return 2;
        return -1;
    }

    public boolean search_declartion_function(String[] value) {
        if (simbolTableStack.findIdentifier(value[0])) {
            return true;
        }
        return false;
    }

    public boolean Search_declarationproc_table(String value) {
        if (simbolTableStack.findProcedure(value)) return true;
        return false;
    }

    public boolean Search_declarationfunc_table(String value) {
        if (simbolTableStack.findFunction(value) == 2) return true;
        return false;
    }

    public boolean Search_table(String value, int level) {
        if (!simbolTableStack.findIdentifier(value) && !simbolTableStack.findVariable(value, level)) {
            throw new Error("ausencia de identificador");
        }
        if (simbolTableStack.findFunction(value) == 2) {
            //analyser_call_function();
            return true;
        }
        return false;
    }

    public LinkedList<String> posfixo(LinkedList<String[]> expression) {
        LinkedList<String[]> pilha = new LinkedList<String[]>();
        LinkedList<String> saida = new LinkedList<String>();
        for (int i = 0; i < expression.size(); i++) {
            String[] auxToken = expression.get(i);
            if (auxToken[1].equals("snúmero") || auxToken[1].equals("sidentificador") || auxToken[1].equals("sverdadeiro") || auxToken[1].equals("sfalso")) {
                saida.addLast(auxToken[0]);
            } else if (auxToken[1].equals("sabre_parênteses")) {
                pilha.addLast(auxToken);
            } else if (auxToken[1].equals("sfecha_parênteses")) {
                int topo = (pilha.size() - 1);
                while (!(pilha.get(topo)[1].equals("sabre_parênteses"))) {
                    saida.addLast(pilha.get(topo)[1]);
                    pilha.remove(topo);
                    topo--;
                }
                pilha.remove(topo);
            } else {
                if (pilha.size() == 0) {
                    pilha.addLast(auxToken);
                } else {
                    int operador = 0, operador_pilha = 0;
                    int aux_topo_pilha = (pilha.size() - 1);
                    do {
                        operador = operartionPriority(auxToken);
                        operador_pilha = operartionPriority(pilha.get(aux_topo_pilha));
                        if (operador_pilha >= operador) {
                            saida.addLast(pilha.get(aux_topo_pilha)[1]);
                            pilha.removeLast();
                            aux_topo_pilha--;
                        }
                    } while (operador_pilha >= operador && pilha.size() != 0);
                    if (operador_pilha < operador || pilha.size() != 0) {
                        pilha.addLast(auxToken);
                    }
                }
            }
        }
        int aux_topo_pilha = (pilha.size() - 1);
        if (pilha.size() != 0) {
            for (int i = aux_topo_pilha; i >= 0; i--) {
                saida.addLast(pilha.get(i)[1]);
                pilha.remove(i);
            }
        }
        return saida;
    }

    public int operartionPriority(String[] token) {
        if (null != token[0]) {
            if (token[0].equals("-u") || token[0].equals("nao")) { //Não sei se precisa ver o "+u"
                return 5;
            } else if (token[0].equals("*") || token[0].equals("div")) {
                return 4;
            } else if (token[0].equals("+") || token[0].equals("-")) {
                return 3;
            } else if (token[0].equals(">") || token[0].equals("<") || token[0].equals(">=") || token[0].equals("<=") || token[0].equals("!=") || token[0].equals("=")) {
                return 2;
            } else if (token[0].equals("e")) {
                return 1;
            } else if (token[0].equals("ou")) {
                return 0;
            }
        }
        return -1;
    }

    public void printExpression(LinkedList<String[]> expression) {
        System.out.println("\n");
        for (int i = 0; i < expression.size(); i++) System.out.print(" " + expression.get(i)[0]);
        System.out.println("\n");
    }

    public void printExpression2(LinkedList<String> expression) {
        System.out.println("\n");
        for (int i = 0; i < expression.size(); i++) System.out.print(" " + expression.get(i));
        System.out.println("\n");
    }
}

