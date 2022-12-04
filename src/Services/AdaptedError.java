package Services;

import Compiler.LexicalAnalyser.LexicalAnalyser;

import javax.swing.*;

public class AdaptedError {
    public static String errorMessage = "";

    public static  int line = 0;
    public static JTextArea errorInput;

    public void newError(String message){
        errorMessage += "\n" + "Linha: " + line + " | " + message;
        errorInput.setText(errorMessage);

        throw new Error(message);
    }

    public void setErrorInput(JTextArea input){
        errorInput = input;
    }

    public String getErrors(){
        return errorMessage;
    }

    public void setErrors(String error){
        errorMessage = error;
        errorInput.setText(error);
    }

    public void setLine(int value){
        line = value;
    }
}
