package VirtualMachine;

import Services.FileReader;

import java.util.ArrayList;
import java.util.Arrays;

public class VirtualMachine {

    ArrayList<String[]> instructions = new ArrayList<String[]>();
    ArrayList<String> lines = new ArrayList<String>();
    String[] stack = new String[100];
    int s = 50;
    String rawProgram = "";

    public VirtualMachine() {
        FileReader reader = new FileReader();
        reader.setFilePath("src/VirtualMachine/test.txt");
        lines = reader.readFile();
        setCodeInstructions();
        runProgram();
    }

    public void setCodeInstructions() {
        for (String line: lines) {
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
                break;
            case "OR":
                break;
            case "NEG":
                break;
            case "CME":
                break;
            case "CMA":
                break;
            case "CEQ":
                break;
            case "CDIF":
                break;
            case "CMEQ":
                break;
            case "CMAQ":
                break;
            default:
                break;
        }
    }

}
