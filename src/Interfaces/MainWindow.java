package Interfaces;

import Compiler.LexicalAnalyser.LexicalAnalyser;
import Compiler.SyntaxAnalyser.SyntaxAnalyser;
import Services.FileReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class MainWindow {
  Window AppWindow = new Window();
  Container box = null;

  FileChooser FilePicker = new FileChooser();
  File selectedFile = null;
  JTextArea fileInput = null;
  JTextArea codeInput = null;
  JTextArea errorInput = null;

  String program = null;
  FileReader reader = new FileReader();

  CompileWindow compiler = new CompileWindow();

  LexicalAnalyser lexicalAnalyser = null;
  SyntaxAnalyser syntaxAnalyser = null;


  public MainWindow(){

    interfaceBuilder();
  }

  public void interfaceBuilder (){
    AppWindow.createText("Arquivo");
    box = AppWindow.getBox();
    JPanel topPanel = AppWindow.createPanel(990, 40);
    fileInput =  AppWindow.createTextInput(500, 20, 0,0);
    topPanel.add(fileInput);
    JButton chooserButton  = AppWindow.createButton("Abrir Arquivo");
    topPanel.add(chooserButton);
    box.add(topPanel);
    chooserButton.setActionCommand("open_chooser");
    chooserButton.addActionListener(this::actionPerformed);

    JPanel codePanel = AppWindow.createPanel(990, 430);
    codePanel.add(AppWindow.createText("Código"));
    codeInput =  AppWindow.createTextInput(980, 400, 0,0);
    codePanel.add(AppWindow.createScroll(980,400,codeInput));
    box.add(codePanel);

    JPanel errorPanel = AppWindow.createPanel(990, 300);
    errorPanel.add(AppWindow.createText("Erros"));
    errorInput =  AppWindow.createTextInput(980, 200, 0,0);
    errorPanel.add(AppWindow.createScroll(980,200, errorInput));

    JButton button  = AppWindow.createButton("Compilar");
    errorPanel.add(button);
    button.setActionCommand("open_compiler");
    button.addActionListener(this::actionPerformed);
    box.add(errorPanel);

    AppWindow.setWindowStatus(true);
  }

  public boolean analyseCode(){
    lexicalAnalyser = new LexicalAnalyser();
    syntaxAnalyser= new SyntaxAnalyser();
    lexicalAnalyser.setCodeReaded(program);
    try{
      lexicalAnalyser.AnalyseLexemes();
      syntaxAnalyser.setAnalyser(lexicalAnalyser);
      syntaxAnalyser.AnalyzeSyntax();
      return true;
    } catch (IOException error){
      errorInput.setText(error.getMessage());
      return false;
    }
  }

  public void openCompilation () {
    compiler.setVmFilePath(syntaxAnalyser.filePath);
    compiler.setWindowStatus(true);
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
        if(analyseCode()){
          this.openCompilation();
        }
        break;
      default:
        break;
    }
  }
}
