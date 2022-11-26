package Compiler.GeraCodigo;

public class geraCodigo {

    //4___NULL____________
    //____ALLOC___4___4___
    //4 8 4 4
    public LinkedList<String> codigo_gerado = new LinkedList<String>();

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
        String aux = "";
        p1 = formata_codigo(p1,4);
        p2 = formata_codigo(p2,8);
        p3 = formata_codigo(p3,4);
        p4 = formata_codigo(p4,4);
        aux = p1 + "" + p2 + p3 + p4;
        codigo_gerado.add(aux);
    }

    public void criaCodigo(LinkedList<String[]> pilha) {
        for (int i = 0; i < pilha.size(); i++){
            if (pilha[i].equals("+")) {
                codigo_gerado.add("    ADD            ");
            }
        }
    }
}
