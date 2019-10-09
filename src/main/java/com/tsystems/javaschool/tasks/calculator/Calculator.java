package com.tsystems.javaschool.tasks.calculator;

import java.util.*;

public class Calculator {

    private static final String OPERATORS = "+-*/";
    private static final String DIGITS = "0123456789";
    private static final String SEPARATOR = ".";
    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        if (!isValidStatement(statement))
            return null;

        statement = statement.replaceAll("\\s","");

        List<String> tokens = new ArrayList<>();

        int i = 0;
        String token;

        while (i < statement.length()) {
            token = "";
            while ((i < statement.length()) && (isDigit("" + statement.charAt(i)) || isSeparator("" + statement.charAt(i)))) {
                token += statement.charAt(i);
                ++i;
            }

            if (!token.equals(""))
                tokens.add(token);

            while ((i < statement.length()) && (isOperator("" + statement.charAt(i))
                    || isLeftBracket("" + statement.charAt(i)) || isRightBracket("" + statement.charAt(i)))) {
                tokens.add("" + statement.charAt(i));
                ++i;
            }
        }

        ArrayList<String> result = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String t : tokens) {
            if (isOperator(t)) {
                while (!stack.empty() && isOperator(stack.peek())) {
                    if (comparePrecedence(t, stack.peek()) <= 0) {
                        result.add(stack.pop());
                        continue;
                    }
                    break;
                }
                stack.push(t);
            }
            else if (isLeftBracket(t)) {
                stack.push(t);
            }
            else if (isRightBracket(t)) {
                while (!stack.empty() && !isLeftBracket(stack.peek()))
                {
                    result.add(stack.pop());
                }
                stack.pop();
            }
            else {
                result.add(t);
            }
        }

        while (!stack.empty())
        {
            result.add(stack.pop());
        }

        for (String t : result) {
            if (!isOperator(t))
            {
                stack.push(t);
            }
            else
            {
                Double d2 = Double.valueOf(stack.pop());
                Double d1 = Double.valueOf(stack.pop());

                if (t.equals("/") && (d1 % 1 == 0) && (d2 == 0))
                    return null;

                Double tmp = t.compareTo("+") == 0 ? d1 + d2 : t.compareTo("-") == 0 ? d1 - d2
                        : t.compareTo("*") == 0 ? d1 * d2 : d1 / d2;

                stack.push(String.valueOf(tmp));
            }
        }

        Double d = Double.valueOf(stack.pop());

        return d % 1 == 0 ? String.valueOf(d.intValue()) : String.valueOf(d);
    }

    private int comparePrecedence(String op1, String op2) {
        if ((op1.equals("+") || op1.equals("-")) && (op2.equals("*") || op2.equals("/")))
            return -1;

        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-")))
            return 1;

        return 0;
    }

    private boolean isOperator(String s) {
        return OPERATORS.contains(s);
    }

    private boolean isDigit(String s) {
        return DIGITS.contains(s);
    }

    private boolean isSeparator(String s) {
        return SEPARATOR.equals(s);
    }

    private boolean isLeftBracket(String s) {
        return LEFT_BRACKET.equals(s);
    }

    private boolean isRightBracket(String s) {
        return RIGHT_BRACKET.equals(s);
    }

    private boolean isValidStatement(String statement) {
        if (statement == null)
            return false;

        statement = statement.replaceAll("\\s","");

        int len = statement.length();

        if (len == 0)
            return false;

        if (isOperator("" + statement.charAt(0)) || isSeparator("" + statement.charAt(0)))
            return false;

        if (isOperator("" + statement.charAt(len - 1)) || isSeparator("" + statement.charAt(len - 1)))
            return false;

        int openBracketsCount = 0;
        for(int i = 0; i < len; ++i) {
            switch (statement.charAt(i)) {
                case '(':
                    ++openBracketsCount;
                    break;
                case ')':
                    if (openBracketsCount > 0)
                        --openBracketsCount;
                    else
                        return false;
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                    if (isOperator("" + statement.charAt(i - 1)) || isLeftBracket("" + statement.charAt(i - 1))
                            || isSeparator("" + statement.charAt(i - 1)))
                        return false;

                    if (isOperator("" + statement.charAt(i + 1)) || isRightBracket("" + statement.charAt(i + 1))
                            || isSeparator("" + statement.charAt(i + 1)))
                        return false;
                    break;
                case '.':
                    if (!isDigit("" + statement.charAt(i - 1)))
                        return false;

                    if (!isDigit("" + statement.charAt(i + 1)))
                        return false;
                    break;
                default:
                    if (isDigit("" + statement.charAt(i)))
                        break;

                    return false;
            }
        }
        return openBracketsCount == 0;
    }
}
