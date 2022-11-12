package Services;

public class SimbolTable {

    public String lexema;

    public String label;
    public String type;
    public int level;

    public String line;

    public SimbolTable(String lexema, String type,int level,String line,String label)
    {
        this.lexema = lexema;
        this.label = label;
        this.type = type;
        this.level = level;
        this.line = line;
    }
}
