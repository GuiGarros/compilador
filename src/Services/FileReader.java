package Services;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class FileReader {
    private String filePath;
    public void ReadFile() {
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não foi encontrado.");
            e.printStackTrace();
        }
    }

    public void setFilePath (String path) {
        this.filePath = path;
    }

    public String getFilePath () {
        return this.filePath;
    }
}
