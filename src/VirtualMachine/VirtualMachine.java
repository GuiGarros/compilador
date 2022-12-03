package VirtualMachine;

import Services.FileReader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class VirtualMachine {
    ArrayList<String[]> instructions = new ArrayList<String[]>();
    ArrayList<String> lines = new ArrayList<String>();
    String[] stack = new String[30];
    int s = 1;
    boolean stopFlag = false;
    int programCounter = 0;
    int aaa = 8;

    String rawProgram = "";

    public VirtualMachine(JTable codeTable, JTable stackTable, JTextArea output) {
        FileReader reader = new FileReader();
        reader.setFilePath("src/VirtualMachine/test.txt");
        lines = reader.readFile();
        setCodeInstructions();
        runProgram();
    }

    public void setCodeInstructions() {
        for (String line : lines) {
            String label =  line.substring(0,4).replaceAll(" ", "");
            String instruction =  line.substring(4,12).replaceAll(" ", "");
            String memAddress =  line.substring(12,16).replaceAll(" ", "");
            String allocSpace =  line.substring(16,20).replaceAll(" ", "");

            String[] inst = { label, instruction, memAddress, allocSpace};

            System.out.println(Arrays.toString(inst));

            instructions.add(inst);
        }
    }

    public void runProgram(){
        System.out.println("RUNNING");
        for (programCounter = 0 ; programCounter < instructions.size(); programCounter++){
            if(stopFlag){
                break;
            }
            runInstruction(instructions.get(programCounter));
        }
    }

    public void runInstruction(String[] line){
        String label =  line[0];
        String instruction =  line[1];
        String memAddress =  line[2];
        String allocSpace =  line[3];

        switch (instruction){
            case "LDC":
                s++;
                stack[s] =  memAddress;
                break;
            case "LDV":
                s++;
                stack[s] =  stack[Integer.parseInt(memAddress)];
                break;
            case "ADD":
                Integer addResult = Integer.parseInt(stack[s-1]) + Integer.parseInt(stack[s]);
                stack[s-1] = addResult.toString();
                s--;
                break;
            case "SUB":
                Integer subResult = Integer.parseInt(stack[s-1]) - Integer.parseInt(stack[s]);
                stack[s-1] = subResult.toString();
                s--;
                break;
            case "MULT":
                Integer multResult = Integer.parseInt(stack[s-1]) * Integer.parseInt(stack[s]);
                stack[s-1] = multResult.toString();
                s--;
                break;
            case "DIVI":
                Integer divResult = Integer.parseInt(stack[s-1]) / Integer.parseInt(stack[s]);
                stack[s-1] = divResult.toString();
                s--;
                break;
            case "INV":
                Integer invResult = Integer.parseInt(stack[s])*-1;
                stack[s] =  invResult.toString();
                break;
            case "AND":
                if(Integer.parseInt(stack[s]) == 1 && Integer.parseInt(stack[s-1]) == 1){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            case "OR":
                if(Integer.parseInt(stack[s]) == 1 || Integer.parseInt(stack[s-1]) == 1){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            case "NEG":
                Integer negResult = 1-Integer.parseInt(stack[s]);
                stack[s] =  negResult.toString();
                break;
            
            case "CME":
                if(Integer.parseInt(stack[s-1]) < Integer.parseInt(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            
            case "CMA":
                if(Integer.parseInt(stack[s-1]) > Integer.parseInt(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            
            case "CEQ":
                if(Integer.parseInt(stack[s-1]) == Integer.parseInt(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            
            case "CDIF":
                if(Integer.parseInt(stack[s-1]) != Integer.parseInt(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            
            case "CMEQ":
                if(Integer.parseInt(stack[s-1]) <= Integer.parseInt(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            
            case "CMAQ":
                if(Integer.parseInt(stack[s-1]) >= Integer.parseInt(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            case "JMP":
                programCounter = findAddress(memAddress);
                break;
            case "JMPF":
                if(stack[s] == "0"){
                    programCounter = findAddress(memAddress);
                }
                s--;
                break;
            case "RD":
                s++;
                aaa++;
                stack[s] = String.valueOf(aaa);
                break;
            case "NULL":
                break;
            case "STR":
                stack[Integer.parseInt(memAddress)] = stack[s];
                s--;
                break;
            case "PRN":
                System.out.println(stack[s]);
                s--;
                break;
            case "START":
                s=-1;
                break;
            case "HLT":
                stopFlag = true;
                break;
            case "ALLOC":
                int length = Integer.parseInt(allocSpace);
                for(int i=0; i<length;i++){
                    s++;
                    stack[s] = stack[Integer.parseInt(memAddress) + i];
                }
                break;
            case "DALLOC":
                int dallocLength = Integer.parseInt(allocSpace)-1;
                for(int i=dallocLength; i>=0;i--){
                    stack[Integer.parseInt(memAddress) + i] = stack[s];
                    s--;
                }
                break;
            case "CALL":
                s++;
                stack[s] = String.valueOf(programCounter + 1);
                programCounter = findAddress(memAddress);
                break;
            case "RETURN":
                programCounter = Integer.parseInt(stack[s])-1;
                s--;
                break;
            default:
                break;
        }
//        System.out.println(instruction + " " + memAddress);
//        System.out.println("LINE " + programCounter);
//        System.out.println("STACK " + s);
//        System.out.println(Arrays.toString(stack));
//        System.out.println(" ");
    }

    int findAddress(String address){
        for(int i = 0; i< instructions.size(); i++){
            String[] line = instructions.get(i);
            if(line[0].equals(address)){
                return i;
            }
        }
        return -1;
    }

}
