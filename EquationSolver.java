import java.util.ArrayDeque;
import java.util.Deque;

public class EquationSolver {

    public static double solve(String expression) {
        return evaluate(parse(expression));
    }

    private static Deque<String> parse(String expression) {
        Deque<String> output = new ArrayDeque<>();
        Deque<Character> operators = new ArrayDeque<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i++));
                }
                i--; 
                output.offer(num.toString());
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.offer(String.valueOf(operators.pop()));
                }
                operators.pop(); 
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                    output.offer(String.valueOf(operators.pop()));
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            output.offer(String.valueOf(operators.pop()));
        }
        return output;
    }

    private static double evaluate(Deque<String> postfix) {
        Deque<Double> stack = new ArrayDeque<>();
        while (!postfix.isEmpty()) {
            String token = postfix.poll();
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                double result = applyOperator(token.charAt(0), operand1, operand2);
                stack.push(result);
            }
        }
        return stack.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedence(char operator) {
        if (operator == '+' || operator == '-') return 1;
        if (operator == '*' || operator == '/') return 2;
        return 0;
    }

    private static double applyOperator(char operator, double operand1, double operand2) {
        switch (operator) {
            case '+': return operand1 + operand2;
            case '-': return operand1 - operand2;
            case '*': return operand1 * operand2;
            case '/': return operand1 / operand2;
            default: return 0;
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String expression = "2 * (10 / (2 + 3)) - 7";
        double result = solve(expression);
        System.out.println(expression + " = " + result);
    }
}