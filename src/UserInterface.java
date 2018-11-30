import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInterface {


    ArrayList<String> tokens = new ArrayList<>();
    Map<String, String> variables = new LinkedHashMap();
    ReversePolishNotation reversePolishNotation = new ReversePolishNotation(variables);

    Parser parser = new Parser();

    void start() {

        String input;
        Scanner scanner = new Scanner(System.in);
        String operation;
        String variableName;

        String result;

        while (true) {
            tokens.clear();
            input = scanner.nextLine();
            generateTokens(input);
            if (!isLexycalyOkey(input) || input.length() == 0) {
                System.out.println("ERROR");
                continue;
            }
            operation = checkOperation(input);
            if (operation.equals("set")) {
                try {
                    variableName = removeVariableNameAndComa();
                    result = calculate();
                    variables.put(variableName, result);
                } catch (SyntaxException e) {
                    System.out.println(e.value);
                }
            } else if (operation.equals("print")) {
                try {
                    result = calculate();
                    System.out.println(calculate());
                } catch (SyntaxException e) {
                    System.out.println(e.value);
                }
            } else {
                System.out.println("ERROR");
            }
        }
    }

    private boolean isLexycalyOkey(String input) {
        String tokensTogether = "";
        for (int i = 0; i < tokens.size(); i++) {
            tokensTogether += tokens.get(i);
        }
        String temp = input.replaceAll("\\s", "");
        return (tokensTogether.length() == temp.length());
    }

    private void generateTokens(String input) {
        String temp;
        String tokensPattern = "[a-zA-Z]+|,|[0-9]+|\\+|-|\\*|\\(|\\)";
        Pattern regex = Pattern.compile(tokensPattern);
        Matcher matcher = regex.matcher(input);

        while (matcher.find()) {
            temp = matcher.group(0);
            tokens.add(temp);
        }
    }

    private String checkOperation(String input) {
        String temp = tokens.remove(0);
        if (temp.equals("print")) {
            return temp;
        } else if (temp.equals("set")) {
            return temp;
        } else {
            return "ERROR";
        }
    }

    private String removeVariableNameAndComa() throws SyntaxException {
        String variableName;
        String coma;
        variableName = tokens.remove(0);
        coma = tokens.remove(0);
        if (!coma.equals(",")) {
            throw new SyntaxException("ERROR");
        }
        return variableName;
    }

    String calculate() throws SyntaxException {
        Queue<String> queueTokens = new LinkedList<>(tokens);
        reversePolishNotation.setTokens(queueTokens);
        reversePolishNotation.convert();
        try {
            if (!parser.checkSyntax(reversePolishNotation.makeTree())) {
                throw new SyntaxException("ERROR");
            }
            return reversePolishNotation.calculate();
        } catch (Exception e) {
            throw new SyntaxException("ERROR");
        }
    }

}
