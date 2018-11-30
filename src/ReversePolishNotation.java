
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReversePolishNotation {

    Queue<String> tokens = new LinkedList<String>();
    Queue<String> tokensONP = new LinkedList<String>();

    Stack<String> convertingStack = new Stack<String>();
    Stack<String> calculationStack = new Stack<String>();

    Map<String, String> variables = new LinkedHashMap();


    Stack<Node> treeMakingStack = new Stack<Node>();

    ReversePolishNotation(Map<String, String> variables){
        this.variables = variables;
    }

    void setTokens(Queue<String> tokens){
        this.tokens = tokens;
        tokensONP.clear();
        convertingStack.clear();
        calculationStack.clear();
    }

    void convert() throws SyntaxException {
        String temp;

        while (tokens.size() != 0) {
            temp = tokens.poll();
            if (temp.equals("(")) {
                convertingStack.push(temp);
            } else if (isNumber(temp)) {
                tokensONP.add(temp);
            } else if (isVariable(temp)) {
                if(!isVariableSaved(temp)){
                    throw new SyntaxException("???");
                }
                String variableValue = variables.get(temp);
                tokensONP.add(variableValue);
            } else if (temp.equals(")")) {
                moveOperatorsFromStackTillLeftBracket();
            } else if (isOperator(temp)) {
                checkOperatorsOnStack(temp);
            } else {
                throw new SyntaxException("ERROR");
            }
        }
        moveOperatorsFromStack();

    }

    String calculate() throws Exception {
        String temp;
        String a;
        String b;
        int numberA;
        int numberB;
        int numberResult;
        String result;
        while (tokensONP.size() != 0) {
            temp = tokensONP.poll();
            if (isNumber(temp)) {
                calculationStack.push(temp);
            } else {
                a = calculationStack.pop();
                b = calculationStack.pop();
                numberA = Integer.parseInt(a);
                numberB = Integer.parseInt(b);
                if (temp.equals("+")) {
                    numberResult = numberA + numberB;
                    result = Integer.toString(numberResult);
                    calculationStack.push(result);
                } else if (temp.equals("-")) {
                    numberResult = numberB - numberA;
                    result = Integer.toString(numberResult);
                    calculationStack.push(result);
                } else if (temp.equals("*")) {
                    numberResult = numberA * numberB;
                    result = Integer.toString(numberResult);
                    calculationStack.push(result);
                }
            }
        }
        return calculationStack.pop();
    }

    private boolean isNumber(String temp) {
        try {
            Integer.parseInt(temp);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    private boolean isVariable(String temp) {
        String tokensPattern = "[a-zA-Z]+";
        Pattern regex = Pattern.compile(tokensPattern);
        Matcher matcher = regex.matcher(temp);
        if(matcher.find()){
            return (matcher.group(0).equals(temp));
        } else return false;
    }

    private boolean isVariableSaved(String temp){
        return variables.containsKey(temp);
    }

    private void moveOperatorsFromStackTillLeftBracket() {
        String temp;
        while (true) {
            try {
                temp = convertingStack.pop();
                if (!temp.equals("(")) {
                    tokensONP.add(temp);
                } else {
                    break;
                }
            } catch (EmptyStackException e) {
                return;
            }
        }
    }

    private void moveOperatorsFromStack() {
        while (!convertingStack.isEmpty()) {
            tokensONP.add(convertingStack.pop());
        }
    }

    private void checkOperatorsOnStack(String newOperator) {
        String operatorFromStack;
        if (!convertingStack.isEmpty()) {
            operatorFromStack = convertingStack.pop();
        } else {
            convertingStack.push(newOperator);
            return;
        }
        while (true) {
            if (newOperator.equals("*") && operatorFromStack.equals("*")) {
                tokensONP.add(operatorFromStack);
                if (!convertingStack.isEmpty()) {
                    operatorFromStack = convertingStack.pop();
                } else {
                    break;
                }

            } else if ((newOperator.equals("-") || newOperator.equals("+")) && (operatorFromStack.equals("+") || operatorFromStack.equals("-") || operatorFromStack.equals("*"))) {
                tokensONP.add(operatorFromStack);
                if (!convertingStack.isEmpty()) {
                    operatorFromStack = convertingStack.pop();
                } else {
                    break;
                }
            } else {
                convertingStack.push(operatorFromStack);
                break;
            }
        }
        convertingStack.push(newOperator);
    }

    boolean isOperator(String temp) {
        if (temp.equals("+") || temp.equals("-") || temp.equals("*")) {
            return true;
        } else {
            return false;
        }
    }

    BinaryTree makeTree() throws Exception{

        Queue<String> tokensONPTree = new LinkedList<String>();

        String temp;
        Node tempNode;
        Node tempLeft;
        Node tempRight;

        ((LinkedList<String>) tokensONPTree).addAll(0, tokensONP);

        while(!tokensONPTree.isEmpty()){
            temp = tokensONPTree.poll();
            if(isNumber(temp)){
                treeMakingStack.push(new Node(temp));
            }
            else if(isOperator(temp)){
                tempRight = treeMakingStack.pop();
                tempLeft = treeMakingStack.pop();
                treeMakingStack.push(new Node(temp, tempLeft, tempRight));
            }
        }
        return new BinaryTree(treeMakingStack.pop());
    }
}
