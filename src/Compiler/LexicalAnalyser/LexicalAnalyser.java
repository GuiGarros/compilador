package Compiler.LexicalAnalyser;

import Services.FileReader;

import java.util.ArrayList;

public class LexicalAnalyser {
    ArrayList<String> reservedSymbols = new ArrayList<String>();
    ArrayList<String> reservedLexemes = new ArrayList<String>();
    public String codeToAnalyse = "";
    public int indexStopped = 0;


    public LexicalAnalyser() {
        FileReader reader = new FileReader();
        reader.setFilePath("src/Compiler/LexicalAnalyser/lexemas.txt");
        reservedLexemes = reader.readFile();
        reader.setFilePath("src/Compiler/LexicalAnalyser/simbolos.txt");
        reservedSymbols = reader.readFile();
    }

    public void setCodeReaded(String codeToAnalyse) {
        this.codeToAnalyse = codeToAnalyse;
    }

    public void AnalyseLexemes() {
        treatText();
    }

    private void treatText() {
        removeComment();
    }

    private void removeComment() {
        boolean commentFlag = false;
        String newCode = "";
        for (int i = 0, j = 0; i < this.codeToAnalyse.length(); i++) {
            char character = this.codeToAnalyse.charAt(i);
            if (character == '{' && !commentFlag) {
                commentFlag = true;
                j = i;
            }

            if (character == '}') {
                commentFlag = false;
                codeToAnalyse = codeToAnalyse.substring(0, j) + codeToAnalyse.substring(i + 1, codeToAnalyse.length());
                i = j;
            }
        }

        if (commentFlag) {
            throw new Error("Erro: Comentário não foi encerrado.");
        }
    }

    public String[] getNextToken() {
        char character = codeToAnalyse.charAt(indexStopped);
        try {
            while (character == ' ') {
                indexStopped++;
                character = codeToAnalyse.charAt(indexStopped);
            }
            if (Character.isDigit(character)) {
                return treatDigit(indexStopped);
            } else if (Character.isAlphabetic(character)) {
                return treatIdentifier(indexStopped);
            } else if (character == ':') {
                return treatAttributions(indexStopped);
            } else if (character == '+' || character == '-' || character == '*') {
                return treatOperations(indexStopped);
            } else if (character == '!' || character == '<' || character == '>' || character == '=') {
                return treatCondicionalOperations(indexStopped);
            } else if (character == ';' || character == ',' || character == '(' || character == ')' || character == '.') {
                return treatPontuation(indexStopped);
            } else {
                String[] values = {Character.toString(character), "serror"};
                indexStopped++;
                return values;
            }
        } catch (Error error) {
            String[] values = {Character.toString(character), "serror"};
            indexStopped++;
            return values;
        }
    }

    public String[] treatDigit(int index) {
        String word = "";
        int i = 0;
        for (i = index; i < codeToAnalyse.length(); i++) {
            char character = codeToAnalyse.charAt(i);
            if (!Character.isDigit(character)) {
                break;
            }
            word += Character.toString(character);
        }
        indexStopped = i;
        String[] values = {word, "snúmero"};
        return values;
    }

    public String[] treatIdentifier(int index) {
        String word = "";
        int i = 0;
        for (i = index; i < codeToAnalyse.length(); i++) {
            char character = codeToAnalyse.charAt(i);
            if (!Character.isDigit(character) && !Character.isAlphabetic(character) && character != '_') {
                break;
            }
            word += Character.toString(character);
        }
        indexStopped = i;
        String[] values = {word, searchReserved(word)};
        return values;
    }

    public String[] treatAttributions(int index) {
        char character = codeToAnalyse.charAt(index);
        String word = "";
        word = Character.toString(character);
        indexStopped = index + 1;

        character = codeToAnalyse.charAt(index + 1);
        if (character == '=') {
            indexStopped++;
            word += Character.toString(character);
        }
        String[] values = {word, searchReserved(word)};
        return values;
    }

    public String[] treatCondicionalOperations(int index) {
        char character = codeToAnalyse.charAt(index);
        String word = "";
        word = Character.toString(character);
        indexStopped = index + 1;

        if (character != '=') {
            character = codeToAnalyse.charAt(index + 1);
            if (character == '=') {
                indexStopped++;
                word += Character.toString(character);
            }
        }
        String[] values = {word, searchReserved(word)};
        return values;
    }

    public String[] treatOperations(int index) {
        char character = codeToAnalyse.charAt(index);
        indexStopped = index + 1;
        String[] values = {Character.toString(character), searchReserved(Character.toString(character))};
        return values;
    }

    public String[] treatPontuation(int index) {
        char character = codeToAnalyse.charAt(index);
        indexStopped = index + 1;
        String[] values = {Character.toString(character), searchReserved(Character.toString(character))};
        return values;
    }

    public String searchReserved(String word) {
        int index = reservedLexemes.indexOf(word);
        if (index >= 0) {
            return reservedSymbols.get(index);
        }
        return "sidentificador";
    }
}
