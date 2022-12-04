package Services;

import javax.swing.*;

public class AdaptedError {
    public static String errorMessage = "";
    public static JTextArea errorInput;

    public static void newError(String message){
        errorMessage += "\n" + message;
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
}
