package Interfaces;

import VirtualMachine.VirtualMachine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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
    VirtualMachine vm = null;


    public CompileWindow() {
        interfaceBuilder();
    }

    public void setVmFilePath(String filepath) {
        vm = new VirtualMachine(filepath, codeTable, memTable, output);
    }

    public void interfaceBuilder() {
        String[] codeCols = {"Endereço", "Instrução", "Atributo 1", "Atributo 2"};
        String[] memCols = {"Endereço", "Valor"};
        box = CompilerWindow.getBox();

        JPanel codePanel = CompilerWindow.createPanel(990, 370);
        codePanel.add(CompilerWindow.createText("Código"));
        codeTable = CompilerWindow.createTable(codeCols, 300);
        codePanel.add(CompilerWindow.createScroll(980, 350, codeTable));
        box.add(codePanel);

        leftPanel = CompilerWindow.createPanel(490, 330);
        leftPanel.add(CompilerWindow.createText("Memória"));
        memTable = CompilerWindow.createTable(memCols, 300);
        leftPanel.add(CompilerWindow.createScroll(480, 300, memTable));
        box.add(leftPanel);

        rightPanel = CompilerWindow.createPanel(490, 330);
        rightPanel.add(CompilerWindow.createText("Saída"));
        output = CompilerWindow.createTextInput(480, 300, 0, 0);
        rightPanel.add(CompilerWindow.createScroll(480, 300, output));
        box.add(rightPanel);

        JPanel buttonPannel = CompilerWindow.createPanel(990, 50);
        runButton = CompilerWindow.createButton("Executar");
        runButton.setActionCommand("run");
        stepButton = CompilerWindow.createButton("Proximo Passo");
        stepButton.setActionCommand("next_step");
        runButton.addActionListener(this::actionPerformed);
        stepButton.addActionListener(this::actionPerformed);
        buttonPannel.add(runButton);
        buttonPannel.add(stepButton);
        box.add(buttonPannel);
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

    public void setWindowStatus(Boolean value) {
        CompilerWindow.setWindowStatus(value);
    }
}
