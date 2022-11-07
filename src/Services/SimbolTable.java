package Services;

public class SimbolTable {

    public String lexema;
    public String name;
    public String type;
    public String level;

    public String line;

    public SimbolTable(String lexema,String name, String type,String level,String line)
    {
        this.lexema = lexema;
        this.name = name;
        this.type = type;
        this.level = level;
        this.line = line;
    }
}
