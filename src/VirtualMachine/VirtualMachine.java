package VirtualMachine;

import Services.FileReader;

import java.util.ArrayList;
import java.util.Arrays;

public class VirtualMachine {
    ArrayList<String[]> instructions = new ArrayList<String[]>();
    ArrayList<String> lines = new ArrayList<String>();
    String[] stack = new String[100];
    int s = 50;
    int programCounter = 0;
    String rawProgram = "";

    public VirtualMachine() {
        FileReader reader = new FileReader();
        reader.setFilePath("src/VirtualMachine/test.txt");
        lines = reader.readFile();
        setCodeInstructions();
        runProgram();
    }

    public void setCodeInstructions() {
        for (programCounter =0; programCounter < lines.size(); programCounter ++ ) {
            String label =  lines.get(programCounter).substring(0,4).replaceAll(" ", "");
            String instruction =  lines.get(programCounter).substring(4,12).replaceAll(" ", "");
            String memAddress =  lines.get(programCounter).substring(12,16).replaceAll(" ", "");
            String allocSpace =  lines.get(programCounter).substring(16,20).replaceAll(" ", "");

            String[] inst = { label, instruction, memAddress, allocSpace};

            System.out.println(Arrays.toString(inst));

            instructions.add(inst);
        }
    }

    public void runProgram(){
        for (String[] command : instructions){
            runInstruction(command);
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
                Float addResult = Float.parseFloat(stack[s-1]) + Float.parseFloat(stack[s]);
                stack[s-1] = addResult.toString();
                s--;
                break;
            case "SUB":
                Float subResult = Float.parseFloat(stack[s-1]) - Float.parseFloat(stack[s]);
                stack[s-1] = subResult.toString();
                s--;
                break;
            case "MULT":
                Float multResult = Float.parseFloat(stack[s-1]) * Float.parseFloat(stack[s]);
                stack[s-1] = multResult.toString();
                s--;
                break;
            case "DIVI":
                Float divResult = Float.parseFloat(stack[s-1]) / Float.parseFloat(stack[s]);
                stack[s-1] = divResult.toString();
                s--;
                break;
            case "INV":
                Float invResult = Float.parseFloat(stack[s])*-1;
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
                Float negResult = 1-Float.parseFloat(stack[s]);
                stack[s] =  negResult.toString();
                break;
            case "CME":
                if(Float.parseFloat(stack[s-1]) < Float.parseFloat(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            case "CMA":
                if(Float.parseFloat(stack[s-1]) > Float.parseFloat(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            case "CEQ":
                if(Float.parseFloat(stack[s-1]) == Float.parseFloat(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            case "CDIF":
                if(Float.parseFloat(stack[s-1]) != Float.parseFloat(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            case "CMEQ":
                if(Float.parseFloat(stack[s-1]) <= Float.parseFloat(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            case "CMAQ":
                if(Float.parseFloat(stack[s-1]) >= Float.parseFloat(stack[s])){
                    stack[s-1] = "1";
                } else {
                    stack[s-1] = "0";
                }
                s--;
                break;
            default:
                break;
        }
    }

}
