package Compiler.GeraCodigo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class geraCodigo {

    //4___NULL____________
    //____ALLOC___4___4___
    //4 8 4 4
    public LinkedList<String> codigo_gerado = new LinkedList<String>();
    public LinkedList<String> qnt_variaveis_alocadas = new LinkedList<String>();

    public String formata_codigo(String valor, int tamanho){
        if (tamanho == 4) {
            while (valor.length() != 4){
                valor =  valor + " ";
            }
        } else {
            while (valor.length() != 8){
                valor =  valor + " ";
            }
        }
        return valor;
    }

    // Quando é passado os 4 parametros, ex: Gera("",START,"","")
    public void criaCodigo(String p1, String p2, String p3, String p4){
        p1 = formata_codigo(p1,4);
        p2 = formata_codigo(p2,8);
        p3 = formata_codigo(p3,4);
        p4 = formata_codigo(p4,4);
        codigo_gerado.add(p1);
        codigo_gerado.add(p2);
        codigo_gerado.add(p3);
        codigo_gerado.add(p4);
    }

    public void criaCodigo(LinkedList<String[]> pilha) {
        for (int i = 0; i < pilha.size(); i++){
            if (pilha.get(i).equals("sidentificador")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("LDV     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("snúmero")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("LDC     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("smais")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("ADD     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("smenos")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("SUB     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("smult")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("MULT    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("sdiv")){
                codigo_gerado.add("    ");
                codigo_gerado.add("DIVI    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("se")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("AND     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("sou")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("OR      ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("smaior")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CMA     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("smenor")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CME     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("smaiorig")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CMAQ    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("smenorig")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CMEQ    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("sdif")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CDIF    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("sig")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("CEQ     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("-u")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("INV     ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("snao")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("NEG    ");
                codigo_gerado.add("    ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("funcao")) { // CALL

            } else if (pilha.get(i).equals("sverdadeiro")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("LDC    ");
                codigo_gerado.add("1   ");
                codigo_gerado.add("    ");
            } else if (pilha.get(i).equals("sfalso")) {
                codigo_gerado.add("    ");
                codigo_gerado.add("LDC    ");
                codigo_gerado.add("0   ");
                codigo_gerado.add("    ");
            }
        }
    }

    public geraCodigo(String p1, String p2){
        // p1 = ALLOC ou DALLOC
        // p2 = número de variáveis
        // Falta colocar a posição de memória
        if(p1 == "ALLOC") {
            qnt_variaveis_alocadas.addLast(p2);
            codigo_gerado.add("    ");
            codigo_gerado.add("ALLOC   ");
            codigo_gerado.add("    ");
            p2 = formata_codigo(p2, 4);
            codigo_gerado.add(p2);
        } else {
            qnt_variaveis_alocadas.removeLast();
            codigo_gerado.add("    ");
            codigo_gerado.add("DALLOC  ");
            codigo_gerado.add("    ");
            p2 = formata_codigo(p2, 4);
            codigo_gerado.add(p2);
        }

    }


    public void geraArquivo() throws IOException {
        try {
            File arquivo_asembly = new File("D:\\", "assembly.txt");
            arquivo_asembly.createNewFile();
            FileWriter escreve = new FileWriter("D:\\assembly.txt");
            escreve.write(String.valueOf(codigo_gerado));
            escreve.close();
        } catch (IOException e) {
            System.out.println("Erro para criar ou escrever no txt criado.");
            e.printStackTrace();
        }
    }
}
