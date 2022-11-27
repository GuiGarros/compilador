package Interfaces;

import Services.FileReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class CompileWindow {
  Window CompilerWindow = new Window();
  JPanel leftPanel = null;
  JPanel rightPanel = null;
  JTable memTable = null;
  JTable codeTable = null;
  JTextArea output = null;
  Container box = null;

  String program = null;
  FileReader reader = new FileReader();


  public CompileWindow(){
    String[] codeCols = {"Linha", "Instrução", "Atributo 1", "Atributo 2","Comentario"};
    String[] memCols = {"Endereço", "Valor"};
    box = CompilerWindow.getBox();
    box.add(CompilerWindow.createText("Código"));

    codeTable = CompilerWindow.createTable(codeCols,300);
    box.add(CompilerWindow.createScroll(980,350,codeTable));

    leftPanel= CompilerWindow.createPanel(490, 350);
    leftPanel.add(CompilerWindow.createText("Memória"));
    memTable = CompilerWindow.createTable(memCols,300);
    leftPanel.add(CompilerWindow.createScroll(480, 300, memTable));
    box.add(leftPanel);

    rightPanel = CompilerWindow.createPanel(490, 350);
    rightPanel.add(CompilerWindow.createText("Saída"));
    output = CompilerWindow.createTextInput(480,300,0,0);
    rightPanel.add(CompilerWindow.createScroll(480, 300, output));
    box.add(rightPanel);

    box.add(CompilerWindow.createButton("Executar"));
    box.add(CompilerWindow.createButton("Proximo Passo"));
  }

  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "open_chooser":

        break;
      default:
        break;
    }
  }

  public void setWindowStatus(Boolean value){
    CompilerWindow.setWindowStatus(value);
  }
}
