package Services;

public class SimbolTable {

    public String lexema;

    public String label;
    public String type;
    public int level;
    public int rot;

    public SimbolTable(String lexema, String type,int level,int rot,String label)
    {
        this.lexema = lexema;
        this.label = label;
        this.type = type;
        this.level = level;
        this.rot = rot;
    }
}
