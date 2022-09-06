package Services;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Arrays;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;
public class FileReader {
  private String filePath;
  String code = "";

  public String readFile() {
    try {
      File myObj = new File(filePath);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        code += " " + data;
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("Arquivo não foi encontrado.");
      e.printStackTrace();
    }

    return code;
  }

  public void setFilePath(String path) {
    this.filePath = path;
  }

  public String getFilePath() {
    return this.filePath;
  }


}
