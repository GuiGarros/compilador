package Interfaces;

import Services.FileReader;
import VirtualMachine.VirtualMachine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class CompileWindow {
  JButton runButton = null;
  JButton stepButton = null;
  Window CompilerWindow = new Window();
  JPanel leftPanel = null;
  JPanel rightPanel = null;
  JTable memTable = null;
  JTable codeTable = null;
  JTextArea output = null;
  Container box = null;

  String program = null;
  FileReader reader = new FileReader();
  int step = 0;
  boolean stepFlag = false;

  VirtualMachine vm = null;


  public CompileWindow(){
    interfaceBuilder();
  }

  public void setVmFilePath(String filepath){
    vm = new VirtualMachine(filepath,codeTable, memTable, output);
  }

  public void interfaceBuilder(){
    String[] codeCols = {"Endereço", "Instrução", "Atributo 1","Atributo 2"};
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

    runButton = CompilerWindow.createButton("Executar");
    runButton.setActionCommand("run");
    stepButton = CompilerWindow.createButton("Proximo Passo");
    stepButton.setActionCommand("next_step");
    runButton.addActionListener(this::actionPerformed);
    stepButton.addActionListener(this::actionPerformed);
    box.add(runButton);
    box.add(stepButton);
  }

  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "run":
        vm.runProgram();
        break;
      case "next_step":
        vm.runStep();
        break;
      default:
        break;
    }
  }

  public void setWindowStatus(Boolean value){
    CompilerWindow.setWindowStatus(value);
  }
}
