package Services;

import java.util.LinkedList;

public class Stack { //simbol table
    public LinkedList<SimbolTable> stack = new LinkedList<SimbolTable>();

    public SimbolTable pop() {
        return stack.removeFirst();
    }

    public void push(SimbolTable value) {
        stack.addFirst(value);
    }

    public int flagAtribuicaoTipoEsquerda = 0; // 1 = inteiro / 2 = booleano

    public boolean find(SimbolTable value) {
        if (stack.indexOf(value) == -1) {
            return false; //not exists
        }
        return true;
    }

    public boolean findVariable(String token, int level) {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(token) && stack.get(i).type.equals("variável") && stack.get(i).level <= level) {
                return true;
            }
        }
        return false;
    }

    public int findType(String token, int level) {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(token) && stack.get(i).type.equals("variável") && stack.get(i).level <= level) {
                for (int j = i; j >= 0; j--) {
                    if (stack.get(j).lexema.equals("inteiro")) {
                        return 1;
                    }
                    if (stack.get(j).lexema.equals("booleano")) {
                        return 2;
                    }
                }
            }
        }
        return -1;
    }

    public int findTypeFunction(String[] value, int level) {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(value[0]) && (stack.get(i).type.equals("funcao"))) {
                if (stack.get(i-1).type == "funcaointeiro") return 1;
                if (stack.get(i-1).lexema == "funcaobooleano") return 2;
            }
        }
        return -1;
    }

    public boolean findDuplicatedVariable(String token, int level) {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(token) && stack.get(i).type.equals("variável") && stack.get(i).level == level) {
                return true;
            }
        }
        return false;
    }

    public boolean findProcedure(String value) {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(value) && stack.get(i).type.equals("procedimento")) {
                return true;
            }
        }
        return false;
    }

    public int findFunction(String value) //0 não nada, 1 achou o identificador, 2 é função e é tipo booleano ou inteiro
    {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(value) && !stack.get(i).type.equals("procedimento")) {
                if (stack.get(i).type.equals("funcao")) {
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }

    public boolean findIdentifier(String value) {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(value) && !stack.get(i).type.equals("variável")) {
                return true;
            }
        }
        return false;
    }

    public boolean getIdentificador(SimbolTable value) {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(value.lexema) && !stack.get(i).type.equals("variável")) {
                return true;
            }
        }
        return false;
    }

    public int getPosicaoMemoriaVariavel(String[] value, int level) {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(value[0]) && stack.get(i).type.equals("variável")) {
                for (int j = level; j >= 0; j--) {
                    if (stack.get(i).level == j) return (stack.get(i).p_posicao - 1);
                }
            }
        }
        return -1;
    }

//    public int getPosicaoMemoriaFuncao(String[] value) {
//        for (int i = 0; i < stack.size(); i++) {
//            if (stack.get(i).lexema.equals(value[0]) && (stack.get(i).type.equals("funcao"))) {
//                return stack.get(i).rot;
//            }
//        }
//        return -1;
//    }

//    public int getPosicaoMemoriaProcedimento(String[] value) {
//        for (int i = 0; i < stack.size(); i++) {
//            if (stack.get(i).lexema.equals(value[0]) && stack.get(i).type.equals("procedimento")) {
//                return stack.get(i).rot;
//            }
//        }
//        return -1;
//    }

    public int getPosicaoMemoriaVariavelFuncao(String[] value, int level) {
        flagAtribuicaoTipoEsquerda = 0;
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(value[0]) && (stack.get(i).type.equals("funcao"))) {
                if (stack.get(i-1).type == "funcaointeiro") flagAtribuicaoTipoEsquerda = 1;
                if (stack.get(i-1).lexema == "funcaobooleano") flagAtribuicaoTipoEsquerda = 2;
                return 0;
            } else if (stack.get(i).lexema.equals(value[0]) && stack.get(i).type.equals("variável")) {
                for(int j = level; j >= 0; j--) {
                    if (stack.get(i).level == j) return (stack.get(i).p_posicao - 1);
                }
            }
        }
        return -1;
    }

    public int getPosicaoMemoria(String[] value, int level) {
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).lexema.equals(value[0]) && (stack.get(i).type.equals("funcao"))) {
                return stack.get(i).rot;
            } else if (stack.get(i).lexema.equals(value[0]) && (stack.get(i).type.equals("procedimento"))) {
                return stack.get(i).rot;
            } else if (stack.get(i).lexema.equals(value[0]) && stack.get(i).type.equals("variável")) {
                for(int j = level; j >= 0; j--) {
                    if (stack.get(i).level == j) return (stack.get(i).p_posicao - 1);
                }
            }
        }
        return -1;
    }
    public void deletaLevel(int level) {
        printa();
//        System.out.println("Level: " + level);
//        for (int i = stack.size() - 1; i > 0; i --) {
//            if (stack.get(i).level == (level)) {
//                System.out.println(stack.get(i).lexema);
//                stack.removeLast();
//            }
//        }
    }

    public void printa() {
        for (int i = 0; i < stack.size(); i ++) {
            //System.out.println(i + ":" + stack.get(i).lexema + "/" + stack.get(i).level);
        }
    }
}
