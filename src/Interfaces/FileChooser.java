package Interfaces;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileChooser {
  Window ChooserWindow = new Window();
  JFileChooser chooserField = null;
  File selectedFile = null;
  public FileChooser(){
    chooserField = ChooserWindow.createFileInput();
  }

  public File openChooser () {
    Container box = ChooserWindow.getBox();
    int returnVal = chooserField.showOpenDialog(box);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
      setSelectedFile(chooserField.getSelectedFile());
      System.out.println("You chose to open this file: " +
              selectedFile.getName());

      return selectedFile;
    }

    return null;
  }

  public File getSelectedFile() {
    return selectedFile;
  }

  public void setSelectedFile(File selectedFile) {
    this.selectedFile = selectedFile;
  }
}
