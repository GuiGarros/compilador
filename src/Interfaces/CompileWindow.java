package Interfaces;

import Services.FileReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class CompileWindow {
  Window CompilerWindow = new Window();
  FileChooser FilePicker = new FileChooser();
  File selectedFile = null;
  JTextArea fileInput = null;
  JTextArea codeInput = null;
  JTextArea errorInput = null;

  String program = null;
  FileReader reader = new FileReader();


  public CompileWindow(){
    String[] codeCols = {"Linha", "Instrução", "Atributo 1", "Atributo 2","Comentario"};
    String[] memCols = {"Endereço", "Valor"};

    CompilerWindow.createText("Código");
    CompilerWindow.createTable(codeCols,300,980, 400);
    CompilerWindow.createText("Memória", 400, 20);
    CompilerWindow.createText("Saída", 400, 20);
    CompilerWindow.createTable(memCols,300,480, 300);
    CompilerWindow.createTextInput(480,300,0,0,true);
    CompilerWindow.createButton("Executar");
    CompilerWindow.createButton("Proximo Passo");
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
      default:
        break;
    }
  }

  public void setWindowStatus(Boolean value){
    CompilerWindow.setWindowStatus(value);
  }
}
