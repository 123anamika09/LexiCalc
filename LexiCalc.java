import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class LexiCalc {
    private static final Map<String, Integer> wordToDigit = new HashMap<>();
    private static final Set<String> validOperations = new HashSet<>(Arrays.asList("add", "sub", "mul", "rem", "pow"));

    static {
        wordToDigit.put("zero", 0);
        wordToDigit.put("one", 1);
        wordToDigit.put("two", 2);
        wordToDigit.put("three", 3);
        wordToDigit.put("four", 4);
        wordToDigit.put("five", 5);
        wordToDigit.put("six", 6);
        wordToDigit.put("seven", 7);
        wordToDigit.put("eight", 8);
        wordToDigit.put("nine", 9);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine().trim();
        String result = evaluateExpression(expression);
        System.out.println(result);
    }

    private static String evaluateExpression(String expression) {
        String[] tokens = expression.split("\\s+");

        // Split into operations and operands
        List<Integer> operands = new ArrayList<>();
        List<String> operations = new ArrayList<>();

        for (String token : tokens) {
            if (isNumberWord(token)) {
                operands.add(convertWordToNumber(token));
            } else if (validOperations.contains(token)) {
                operations.add(token);
            } else {
                return "expression evaluation stopped invalid words present";
            }
        }

        // Validate if there are enough operands for each operation
        if (operations.isEmpty() || operands.size() < operations.size() + 1) {
            return "expression is not complete or invalid";
        }

        // Evaluate the operations
        try {
            int result = evaluate(operations, operands);
            return String.valueOf(result);
        } catch (Exception e) {
            return "expression is not complete or invalid";
        }
    }

    private static boolean isNumberWord(String word) {
        // Check if it's a valid number word or a combined number word like "onecone"
        if (wordToDigit.containsKey(word)) {
            return true;
        } else if (word.contains("c")) {
            String[] parts = word.split("c");
            for (String part : parts) {
                if (!wordToDigit.containsKey(part)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static int convertWordToNumber(String word) {
        // Handle combined numbers like "onecone" or "oneczerocfive"
        if (word.contains("c")) {
            String[] parts = word.split("c");
            StringBuilder number = new StringBuilder();
            for (String part : parts) {
                number.append(wordToDigit.get(part));
            }
            return Integer.parseInt(number.toString());
        } else {
            return wordToDigit.get(word);
        }
    }

    private static int evaluate(List<String> operations, List<Integer> operands) throws Exception {
        // Evaluate expression by applying the operations in order
        Stack<Integer> operandStack = new Stack<>();
        operandStack.push(operands.get(0));

        int opIndex = 0;
        for (String operation : operations) {
            if (opIndex + 1 >= operands.size()) {
                throw new Exception("Invalid expression");
            }
            int nextOperand = operands.get(++opIndex);

            switch (operation) {
                case "add":
                    operandStack.push(operandStack.pop() + nextOperand);
                    break;
                case "sub":
                    operandStack.push(operandStack.pop() - nextOperand);
                    break;
                case "mul":
                    operandStack.push(operandStack.pop() * nextOperand);
                    break;
                case "rem":
                    operandStack.push(operandStack.pop() % nextOperand);
                    break;
                case "pow":
                    operandStack.push((int) Math.pow(operandStack.pop(), nextOperand));
                    break;
                default:
                    throw new Exception("Invalid operation");
            }
        }

        return operandStack.pop();
    }
}
