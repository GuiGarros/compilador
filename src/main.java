import Services.FileReader;

public class main {
    public static void main(String[] args) {
        FileReader reader = new FileReader();
        reader.setFilePath("a");
        reader.ReadFile();
    }
}
