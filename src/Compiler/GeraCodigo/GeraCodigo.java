package Compiler.GeraCodigo;

import Services.Stack;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class GeraCodigo {
    public String filePath = "";
    public LinkedList<String> codigo_gerado = new LinkedList<>();

    public String formata_codigo(String valor, int tamanho) {
        if (tamanho == 4) {
            while (valor.length() != 4) {
                valor = valor + " ";
            }
        } else {
            while (valor.length() != 8) {
                valor = valor + " ";
            }
        }
        return valor;
    }

    // Quando é passado os 4 parametros, ex: Gera("",START,"","")
    public void criaCodigo(String p1, String p2, String p3, String p4) {
        p1 = formata_codigo(p1, 4);
        p2 = formata_codigo(p2, 8);
        p3 = formata_codigo(p3, 4);
        p4 = formata_codigo(p4, 4);
        codigo_gerado.add(p1);
        codigo_gerado.add(p2);
        codigo_gerado.add(p3);
        codigo_gerado.add(p4);
    }

    // Quando é passado a expressão é o level
    public void criaCodigo(LinkedList<String[]> pilha, Stack value, int level) {
        for (int i = 0; i < pilha.size(); i++) {
            if (pilha.get(i)[1].equals("sidentificador") && (value.findFunction(pilha.get(i)[0]) != 2 || value.findProcedure(pilha.get(i)[0]))) {
                codigo_gerado.add("    ");
                codigo_gerado.add("LDV     ");
                codigo_gerado.add(formata_codigo(String.valueOf(value.getPosicaoMemoria(pilha.get(i), level)), 4));
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("snúmero")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("LDC     ");
                codigo_gerado.add(formata_codigo(pilha.get(i)[0], 4));
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("smais")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("ADD     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("smenos") && pilha.get(i)[0].equals("-")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("SUB     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("smult")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("MULT    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("sdiv")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("DIVI   ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("se")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("AND     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("sou")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("OR      ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("smaior")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CMA     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("smenor")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CME     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("smaiorig")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CMAQ    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("smenorig")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CMEQ    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("sdif")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CDIF    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("sig")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CEQ     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[0].equals("-u") && pilha.get(i)[1].equals("smenos")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("INV     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("snao")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("NEG     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("sverdadeiro")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("LDC     ");
                codigo_gerado.add("1   ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("sfalso")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("LDC     ");
                codigo_gerado.add("0   ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i)[1].equals("satrib")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("STR     ");
                codigo_gerado.add(formata_codigo(String.valueOf(value.getPosicaoMemoriaVariavel(pilha.get(i), level)), 4));
                codigo_gerado.add("    ");
            }
        }
    }

    // Quando é passado um ALLOC ou DALLOC,
    // p1 = ALLOC ou DALLOC
    // p2 = posição na pilha
    // p3 = quantidade que deve ser alocada
    public void criaCodigo(String p1, Integer p2, Integer p3) {
        String aux;
        if (p1 == "ALLOC") {
            codigo_gerado.add("    ");
            codigo_gerado.add("ALLOC   ");
            aux = formata_codigo(String.valueOf(p2), 4);
            codigo_gerado.add(aux);
            aux = formata_codigo(String.valueOf(p3), 4);
            codigo_gerado.add(aux);
        } else {
            codigo_gerado.add("    ");
            codigo_gerado.add("DALLOC  ");
            aux = formata_codigo(String.valueOf(p2), 4);
            codigo_gerado.add(aux);
            aux = formata_codigo(String.valueOf(p3), 4);
            codigo_gerado.add(aux);
        }
    }

    public void geraArquivo() throws IOException {
        int count = 0;
        try {
            String fileName = "src/OutputFiles/assembly.obj";
            FileWriter escreve = new FileWriter(fileName);
            filePath = fileName;
            for (int i = 0; i < codigo_gerado.size(); i++) {
                escreve.write(String.valueOf(codigo_gerado.get(i)));
                count++;
                if (count == 4) {
                    escreve.write("\r");
                    count = 0;
                }
            }
            escreve.close();
        } catch (IOException e) {
            System.out.println("Erro para criar ou escrever no .obj criado.");
            e.printStackTrace();
        }
    }
}
