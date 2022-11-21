package Interfaces;

import Services.FileReader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class MainWindow {
  Window AppWindow = new Window();
  FileChooser FilePicker = new FileChooser();
  File selectedFile = null;
  JTextArea fileInput = null;
  JTextArea codeInput = null;
  JTextArea errorInput = null;

  String program = null;
  FileReader reader = new FileReader();

  CompileWindow compiler = new CompileWindow();


  public MainWindow(){
    AppWindow.createText("Arquivo");
    fileInput =  AppWindow.createTextInput(300, 20, 0,0, false);

    JButton chooserButton  = AppWindow.createButton("Abrir Arquivo");
    chooserButton.setActionCommand("open_chooser");
    chooserButton.addActionListener(this::actionPerformed);

    AppWindow.createText("Código");


    codeInput =  AppWindow.createTextInput(980, 400, 0,0, true);

    AppWindow.createText("Erros");

    errorInput =  AppWindow.createTextInput(980, 200, 0,0, true);


    JButton button  = AppWindow.createButton("Compilar");
    button.setActionCommand("open_compiler");
    button.addActionListener(this::actionPerformed);

    AppWindow.setWindowStatus(true);
  }

  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "open_chooser":
        selectedFile = FilePicker.openChooser();
        fileInput.setText(selectedFile.getAbsolutePath());
        reader.setFilePath(selectedFile.getAbsolutePath());
        program = reader.readProgram();
        codeInput.setText(reader.spacedCode);
        break;
      case "open_compiler":
        this.openCompilation();
        break;
      default:
        break;
    }
  }

  public void openCompilation () {
    System.out.println("a");
    compiler.setWindowStatus(true);
  }
}
