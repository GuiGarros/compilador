package Services;

public class SimbolTable {

    public String lexema;
    public String type;
    public int level;
    public int rot;
    public int p_posicao;

    public SimbolTable(String lexema, String type, int level, int rot, int p_posicao) {
        this.lexema = lexema;
        this.type = type;
        this.level = level;
        this.rot = rot;
        this.p_posicao = p_posicao;
    }
}
