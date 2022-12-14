package Compiler.SyntaxAnalyser;

import Compiler.GeraCodigo.GeraCodigo;
import Compiler.LexicalAnalyser.LexicalAnalyser;

import java.io.IOException;
import java.util.LinkedList;

import Services.AdaptedError;
import Services.SimbolTable;
import Services.Stack;

public class SyntaxAnalyser {
    AdaptedError Errors = new AdaptedError();
    private LexicalAnalyser analyser = null;
    private Stack simbolTableStack = new Stack();
    public int level = 0;
    public int rotulo = 1;

    public int inicios = 0;
    public int p = 0; // posição na pilha
    public int contador_variaveis = 0;
    public LinkedList<Integer> flagAtribuicaoTipoDireita = new LinkedList<>(); // 1 = inteiro / 2 = booleano
    public LinkedList<Integer> lista_alloc = new LinkedList<>();
    private GeraCodigo gera = new GeraCodigo();
    public String filePath = "";

    public void setAnalyser(LexicalAnalyser analyser) {
        this.analyser = analyser;
    }

    public void AnalyzeSyntax() throws IOException {
        gera.criaCodigo("", "START", "", "");
        gera.criaCodigo("", "ALLOC", String.valueOf(p), "1");
        lista_alloc.addLast(1); // posição de retorno de função
        p = p + 1;
        while (true) {
            String[] token = analyser.getNextToken();
            if (token[1].equals("sprograma")) {
                token = analyser.getNextToken();
                if (token[1].equals("sidentificador")) {
                    token = analyser.getNextToken();
                    Insert_table(new SimbolTable(token[0], "nomedeprograma", level, -1, p)); ///lexema // inicio de programa // tipo null // level 0// linha sei la
                    if (token[1].equals("sponto_vírgula")) {
                        token = AnalyseBlock();
                        if (token[1].equals("sponto")) {
                            gera.criaCodigo("DALLOC", 0, lista_alloc.removeLast()); // retorna de função
                            gera.criaCodigo("", "HLT", "", "");
                            gera.geraArquivo();
                            filePath = gera.filePath;
                            break;
                        } else {
                            Errors.newError("Error: Ausência de um '.' no final do programa.");
                        }
                    } else {
                        Errors.newError("Error: Ausência de um ';'.");
                    }
                } else {
                    Errors.newError("Error: Ausência de um 'identificador' para o programa.");
                }
            } else {
                Errors.newError("Error: Ausência de 'programa' na inicialização.");
            }
        }
    }

    public String[] AnalyseBlock() {
        String[] token = analyser.getNextToken();
        token = AnalyseEtVariables(token);
        token = analyserSubRoutines(token);
        token = AnalyseCommand(token);

        if (lista_alloc.size() > 1 && level == 0) {
            p = p - lista_alloc.getLast();
            gera.criaCodigo("DALLOC", p, lista_alloc.removeLast());
        }
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
                        Errors.newError("Error: Ausência de ';' no final da linha.");
                    }
                }
            } else {
                Errors.newError("Error: Ausência de um 'identificador' para a variável.");
            }
            gera.criaCodigo("ALLOC", (p - contador_variaveis), contador_variaveis);
            lista_alloc.addLast(contador_variaveis);
            contador_variaveis = 0;
            return token;
        }
        return originalToken;
    }

    public String[] AnalyseVariables(String[] originalToken) {
        String[] token = originalToken;
        while (true) {
            if (token[1].equals("sidentificador")) {
                contador_variaveis++;
                p++;
                if (!Search_duplicatadevar_table(new SimbolTable(token[0], "variável", level, -1, p))) {
                    Insert_table(new SimbolTable(token[0], "variável", level, -1, p));
                    token = analyser.getNextToken();
                    if (token[1].equals("svírgula") || token[1].equals("sdoispontos")) {
                        if (token[1].equals("svírgula")) {
                            token = analyser.getNextToken();
                            if (token[1].equals("sdoispontos")) {
                                Errors.newError("Erro: Ausência de ':' na declaração da variável ou uma ',' a mais.");
                            }
                        }
                    } else {
                        Errors.newError("Erro: Ausência de ',' ou ':' na declaração das variáveis.");
                    }
                } else {
                    Errors.newError("Erro: Variável duplicada");
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
            Errors.newError("Error: Ausência de 'inteiro' ou 'booleano'.");
            return null;
        } else {
            Insert_table(new SimbolTable(originalToken[0], "variavel", level, -1, p));
            String[] token = analyser.getNextToken();
            return token;
        }
    }

    public String[] AnalyseCommand(String[] originalToken) {
        if (originalToken[1].equals("sinício") || inicios > 0) {
            inicios++;
            String[] token = analyser.getNextToken();
            token = AnalyseSimpleCommand(token);
            while (!token[1].equals("sfim")) {
                if (token[1].equals("sponto_vírgula")) {
                    token = analyser.getNextToken();
                    if (!token[1].equals("sfim")) {
                        token = AnalyseSimpleCommand(token);
                    }
                    else
                    {
                        inicios--;
                        break;
                    }
                } else {
                    Errors.newError("Error: Ausência de ';' no final da linha.");
                }
            }

            token = analyser.getNextToken();
            return token;
        } else {
            Errors.newError("Error: Ausência de 'inicio'.");
            return null;
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
            analyser_call_procedure(originalToken);
        }
        return token;
    }

    public String[] analyser_atrib(String[] originalToken, LinkedList<String[]> expression) {
        flagAtribuicaoTipoDireita.clear();
        LinkedList<String[]> auxiliar = new LinkedList<String[]>();
        String[] token = analyser.getNextToken();
        expression.addLast(originalToken);
        expression = analyser_expression(token, expression);
        token = expression.getLast();
        LinkedList<String[]> posExpression = new LinkedList<String[]>();
        auxiliar.add(expression.removeFirst()); // Removendo o primeiro token para colocar no STR
        posExpression = posfixo(expression);
        gera.criaCodigo(posExpression, simbolTableStack, level);
        int aux_p = simbolTableStack.getPosicaoMemoriaVariavelFuncao(auxiliar.getFirst(), level);
        if (simbolTableStack.findType(auxiliar.getFirst()[0], level) != -1) {
            simbolTableStack.flagAtribuicaoTipoEsquerda = simbolTableStack.findType(auxiliar.getFirst()[0], level);
        }


        if (verificaValidaExpressão(simbolTableStack.flagAtribuicaoTipoEsquerda, flagAtribuicaoTipoDireita)) {
            gera.criaCodigo("", "STR", String.valueOf(aux_p), "");
        } else {
            Errors.newError("Error: Compatibilidade de tipos na expressão é inválida.");
        }
        return token;
    }

    public String[] AnalyserRead() {
        gera.criaCodigo("", "RD", "", "");
        String[] token = analyser.getNextToken();
        if (token[1].equals("sabre_parênteses")) {
            token = analyser.getNextToken();
            if (token[1].equals("sidentificador")) {
                int busca = Search_declarationvar_table(token[0], level);
                if (busca != -1) { // -1 Não declarada / 1 variável inteira / 2 variável booleana
                    if (busca == 2) Errors.newError("Error: Variável no 'leia' não pode ser booleana.");
                    gera.criaCodigo("", "STR", String.valueOf(simbolTableStack.getPosicaoMemoria(token, level)), "");
                    token = analyser.getNextToken();
                    if (token[1].equals("sfecha_parênteses")) {
                        token = analyser.getNextToken();
                    } else {
                        Errors.newError("Error: Ausência de ')' na declaração de uma leitura.");
                    }
                } else {
                    Errors.newError("Error: Variável não declarada.");
                }
            } else {
                Errors.newError("Error: Ausência de 'sidentificador'.");
            }
        } else {
            Errors.newError("Error: Ausência de '(' na declaração de uma leitura.");
        }
        return token;
    }

    public String[] AnalyseWrite() {
        String[] token = analyser.getNextToken();
        if (token[1].equals("sabre_parênteses")) {
            token = analyser.getNextToken();
            if (token[1].equals("sidentificador")) {
                if (Search_declarationvarfunc_table(token[0], level) != -1) { // -1 não declarada / 1 é variável / 2 é função
                    if (Search_declarationvarfunc_table(token[0], level) == 1) {
                        if (Search_declarationvarType(token[0], level)) { // True = Variável inteira / False = Variável booleana
                            gera.criaCodigo("", "LDV", String.valueOf(simbolTableStack.getPosicaoMemoria(token, level)), "");
                        } else {
                            Errors.newError("Error: Variável no 'escreva' não pode ser booleana.");
                        }
                    } else {
                        gera.criaCodigo("", "CALL", String.valueOf(simbolTableStack.getPosicaoMemoria(token, level)), "");
                        gera.criaCodigo("", "LDV", String.valueOf(simbolTableStack.getPosicaoMemoria(token, level)), "");
                    }
                    token = analyser.getNextToken();
                    if (token[1].equals("sfecha_parênteses")) {
                        gera.criaCodigo("", "PRN", "", "");
                        token = analyser.getNextToken();
                    } else {
                        Errors.newError("Error: Ausência de ')' na declaração de uma escrita.");
                    }
                } else {
                    Errors.newError("Error: Variável ou função não declarada.");
                }
            } else {
                Errors.newError("Error: Ausência de um 'identificador' na declaração de uma escrita.");
            }
        } else {
            Errors.newError("Error: Ausência de '(' na declaração de uma escrita.");
        }
        return token;
    }

    public String[] analyserWhile() {
        int auxrot1, auxrot2;
        auxrot1 = rotulo;
        rotulo++;
        gera.criaCodigo(String.valueOf(auxrot1), "NULL", "", "");
        String[] token = analyser.getNextToken();
        LinkedList<String[]> expression = new LinkedList<String[]>();
        expression = analyser_expression(token, expression);
        token = expression.getLast();
        LinkedList<String[]> posExpression = new LinkedList<String[]>();
        posExpression = posfixo(expression);
        gera.criaCodigo(posExpression, simbolTableStack, level);
        if (token[1].equals("sfaca")) {
            auxrot2 = rotulo;
            gera.criaCodigo("", "JMPF", String.valueOf(rotulo), "");
            token = analyser.getNextToken();
            token = AnalyseSimpleCommand(token);
            gera.criaCodigo("", "JMP", String.valueOf(auxrot1), "");
            gera.criaCodigo(String.valueOf(auxrot2), "NULL", "", "");
        } else {
            Errors.newError("Error: Ausência de um 'faca' na declaração de um enquanto.");
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
        LinkedList<String[]> posExpression = new LinkedList<String[]>();
        posExpression = posfixo(expression);
        gera.criaCodigo(posExpression, simbolTableStack, level);
        gera.criaCodigo("", "JMPF", String.valueOf(auxrot1), "");
        auxrot2 = rotulo;
        if (token[1].equals("sentao")) {
            token = analyser.getNextToken();
            token = AnalyseSimpleCommand(token);
            if (token[1].equals("ssenao")) {
                gera.criaCodigo("", "JMP", String.valueOf(auxrot2), "");
                gera.criaCodigo(String.valueOf(auxrot1), "NULL", "", "");
                token = analyser.getNextToken();
                token = AnalyseSimpleCommand(token);
                gera.criaCodigo(String.valueOf(auxrot2), "NULL", "", "");
            } else {
                gera.criaCodigo(String.valueOf(auxrot1), "NULL", "", "");
            }
        } else {
            Errors.newError("Error: Ausência de 'entao' na declaração de uma operação condicional.");
        }
        return token;
    }

    public String[] analyserSubRoutines(String[] originalToken) {
        int auxrot1 = 0, flag = 0;
        String[] token = originalToken;
        if (token[1].equals("sprocedimento") || token[1].equals("sfuncao")) {
            auxrot1 = rotulo;
            rotulo++;
            gera.criaCodigo("", "JMP", String.valueOf(auxrot1), "");
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
                Errors.newError("Error: Ausência de ';' na declaração de um procedimento ou função.");
            }
        }
        if (flag == 1) gera.criaCodigo(String.valueOf(auxrot1), "NULL", "", "");
        return token;
    }

    public String[] analyser_procedure_declaration() {
        String[] token = analyser.getNextToken();
        level++;
        if (token[1].equals("sidentificador")) {
            if (!Search_declarationproc_table(token[0])) {
                Insert_table(new SimbolTable(token[0], "procedimento", level, rotulo, p));
                gera.criaCodigo(String.valueOf(rotulo), "NULL", "", "");
                rotulo++;
                token = analyser.getNextToken();
                if (token[1].equals("sponto_vírgula")) {
                    token = AnalyseBlock();
                } else {
                    Errors.newError("Error: Ausência de ';' na declaração de um procedimento.");
                }
            } else {
                Errors.newError("Error: Dupla ocorrência de procedimento");
            }
        } else {
            Errors.newError("Error: Ausência de 'identificador' na declaração de um procedimento.");
        }
        if ((lista_alloc.size() - 1) != level) {
            p = p - lista_alloc.getLast();
            gera.criaCodigo("DALLOC", p, lista_alloc.removeLast());
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
                Insert_table(new SimbolTable(token[0], "funcao", level, rotulo, p));
                token = analyser.getNextToken();
                if (token[1].equals("sdoispontos")) {
                    token = analyser.getNextToken();
                    if (token[1].equals("sinteiro") || token[1].equals("sbooleano")) {
                        if (token[1].equals("sinteiro")) {
                            Insert_table(new SimbolTable(token[0], "funcaointeiro", level, rotulo, p));
                        } else {
                            Insert_table(new SimbolTable(token[0], "funcaobooleano", level, rotulo, p));
                        }
                        gera.criaCodigo(String.valueOf(rotulo), "NULL", "", "");
                        rotulo++;
                        token = analyser.getNextToken();
                        if (token[1].equals("sponto_vírgula")) {
                           token = AnalyseBlock();
                        }
                    } else {
                        Errors.newError("Error: Ausência de um tipo na declaração de uma função.");
                    }
                } else {
                    Errors.newError("Error: Ausência de ':' na declaração de uma função.");
                }
            } else {
                Errors.newError("Error: Dupla ocorrencia de função");
            }
        } else {
            Errors.newError("Error: Ausência de 'identificador' na declaração de uma função.");
        }
        if ((lista_alloc.size() - 1) != level) {
            p = p - lista_alloc.getLast();
            gera.criaCodigo("DALLOC", p, lista_alloc.removeLast());
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
            expression = analyser_expression_simple(token, expression);
        }
        return expression;
    }

    public LinkedList<String[]> analyser_expression_simple(String[] originalToken, LinkedList<String[]> expression) {
        String[] token = originalToken;
        if (token[1].equals("smais") || token[1].equals("smenos")) {
            if (token[1].equals("smenos")) {
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
            expression = analyser_factor(token, expression);
            token = expression.getLast();
        }
        return expression;
    }

    public LinkedList<String[]> analyser_factor(String[] originalToken, LinkedList<String[]> expression) {
        String[] token = originalToken;
        if (token[1].equals("sidentificador")) {
            int busca = simbolTableStack.findFunction(token[0]);
            if (busca == 0)
                Errors.newError("identificador não encontrado"); // 0 não encontrou / 1 é variável / 2 é função
            else if (busca == 1) {
                flagAtribuicaoTipoDireita.addLast(simbolTableStack.findType(token[0], level));
                token = analyser.getNextToken();
                expression.addLast(token);
            } else if (busca == 2) {
                flagAtribuicaoTipoDireita.addLast(simbolTableStack.findTypeFunction(token, level));
                analyser_call_function(token);
                token = analyser.getNextToken();
                expression.addLast(token);
            }
            return expression;
        } else if (token[1].equals("snúmero")) {
            flagAtribuicaoTipoDireita.addLast(1);
            token = analyser.getNextToken();
            expression.addLast(token);
            return expression;
        } else if (token[1].equals("snao")) {
            token = analyser.getNextToken();
            expression.addLast(token);
            expression = analyser_factor(token, expression);
        } else if (token[1].equals("sabre_parênteses")) {
            token = analyser.getNextToken();
            expression = analyser_expression(token, expression);
            token = expression.getLast();
            if (token[1].equals("sfecha_parênteses")) {
                token = analyser.getNextToken();
                expression.addLast(token);
                return expression;
            } else {
                Errors.newError("Error: Ausência de ')'.");
            }
        } else if (token[0].equals("verdadeiro") || token[0].equals("falso")) {
            token = analyser.getNextToken();
            expression.addLast(token);
            return expression;
        } else {
            Errors.newError("Error: Ausência de verdadeiro ou falso.");
        }
        return expression;
    }

    public void analyser_call_procedure(String[] originalToken) {
        Search_declarationproc_table(originalToken[0]);
        gera.criaCodigo("", "CALL", String.valueOf(simbolTableStack.getPosicaoMemoria(originalToken, level)), "");
    }

    public void analyser_call_function(String[] originalToken) {
        if (!Search_declarationfunc_table(originalToken[0])) {
            Errors.newError("Error: Função não declarada");
        } else {
            gera.criaCodigo("", "CALL", String.valueOf(simbolTableStack.getPosicaoMemoria(originalToken, level)), "");
            gera.criaCodigo("", "LDV", String.valueOf(0), "");
        }
    }

    public void Insert_table(SimbolTable linha) {
        simbolTableStack.push(linha);
    }

    public boolean Search_duplicatadevar_table(SimbolTable value) {///1
        if (!simbolTableStack.findDuplicatedVariable(value.lexema, value.level)) {
            if (!simbolTableStack.getIdentificador(value)) {
                return false;
            }
        }
        return true;
    }

    public int Search_declarationvar_table(String value, int level) {
        if (!simbolTableStack.findVariable(value, level)) return -1;
        return simbolTableStack.findType(value, level);
    }

    public int Search_declarationvarfunc_table(String value, int level) {
        if (simbolTableStack.findVariable(value, level)) return 1;
        else if (simbolTableStack.findIdentifier(value)) return 2;
        return -1;
    }

    public boolean Search_declarationvarType(String value, int level) {
        if (simbolTableStack.findType(value, level) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Search_declarationproc_table(String value) {
        if (simbolTableStack.findProcedure(value)) return true;
        return false;
    }

    public boolean Search_declarationfunc_table(String value) {
        if (simbolTableStack.findFunction(value) == 2) return true;
        return false;
    }

    public LinkedList<String[]> posfixo(LinkedList<String[]> expression) {
        LinkedList<String[]> pilha = new LinkedList<String[]>();
        LinkedList<String[]> saida = new LinkedList<String[]>();
        for (int i = 0; i < expression.size(); i++) {
            String[] auxToken = expression.get(i);
            if (auxToken[1].equals("snúmero") || auxToken[1].equals("sidentificador") || auxToken[1].equals("sverdadeiro") || auxToken[1].equals("sfalso") || auxToken[1].equals("-u")) {
                saida.addLast(auxToken);
            } else if (auxToken[1].equals("sabre_parênteses")) {
                pilha.addLast(auxToken);
            } else if (auxToken[1].equals("sfecha_parênteses")) {
                int topo = (pilha.size() - 1);
                while (!(pilha.get(topo)[1].equals("sabre_parênteses"))) {
                    saida.addLast(pilha.get(topo));
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
                            saida.addLast(pilha.get(aux_topo_pilha));
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
                saida.addLast(pilha.get(i));
                pilha.remove(i);
            }
        }
        return saida;
    }

    public int operartionPriority(String[] token) {
        if (null != token[0]) {
            if (token[0].equals("-u") || token[0].equals("nao")) {
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

    public boolean verificaValidaExpressão(int flagAtribuicaoTipoEsquerda, LinkedList<Integer> flagAtribuicaoTipoDireita) {
        for (int i = 0; i < flagAtribuicaoTipoDireita.size(); i++) {
            if (flagAtribuicaoTipoEsquerda != flagAtribuicaoTipoDireita.get(i)) return false;
        }
        return true;
    }

//    public void printExpression(LinkedList<String[]> expression) {
//        for (int i = 0; i < expression.size(); i++) System.out.print(" " + expression.get(i)[0] + "(" + expression.get(i)[1] + ")");
//        System.out.println("\n");
//    }

//    public void printExpression2(LinkedList<String[]> expression) {
//        for (int i = 0; i < expression.size(); i++) System.out.print(" " + expression.get(i)[0]);
//        System.out.println("\n");
//    }
}

