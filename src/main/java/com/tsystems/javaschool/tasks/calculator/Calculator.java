package com.tsystems.javaschool.tasks.calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        if ((statement ==null) || (!validate(statement))) return null;

        int firstBracketIndex = statement.indexOf("(");
        while(firstBracketIndex != -1) {
            statement = bracketsCompress(statement.substring(firstBracketIndex + 1));
            if(statement == null) return null;
            firstBracketIndex = statement.indexOf("(");
        }

        return calcSimple(statement);
    }

    private boolean validate(String statement) {
        Character[] ligal = {'0','1','2','3','4','5','6','7','8','9','(',')','.','+','-','*','/'};
        String[] checkedResult = statement.split("[0123456789()\\-+*/]");
        return checkedResult.length == 0;
    }

    private String bracketsCompress(String statement) {
        int closedBracketIndex = statement.indexOf(")");
        int newOpenBracketIndex = statement.indexOf("(");
        if(closedBracketIndex == -1) return null;
        if((newOpenBracketIndex != -1) && (closedBracketIndex > newOpenBracketIndex)) {
            statement = bracketsCompress(statement.substring(newOpenBracketIndex + 1));
        }
        closedBracketIndex = statement.indexOf(")");
        if(closedBracketIndex == -1) return null;
        if(closedBracketIndex != statement.length())
            return calcSimple(statement.substring(0, closedBracketIndex)) + statement.substring(closedBracketIndex + 1);
        else
            return calcSimple(statement.substring(0, closedBracketIndex));
    }

    private String calcSimple(String statement) {
        ArrayList<String> operands = new ArrayList<>(Arrays.asList(statement.split("[+\\-/*]")));
        ArrayList<String> operations = new ArrayList<>(Arrays.asList(statement.split("[0123456789.,]")));
        operations.removeIf(n -> n.equals(""));
        operands.removeIf(n -> n.equals(""));
        if(statement.charAt(0) == '-') {
            operands.add(0, '-'+operands.get(0));
            operands.remove(1);
            operations.remove(0);
        }
        //Calculate multiply and division
        int count = operations.size();
        int index = 0;
        for(int i =0; i < count; i++) {
            if((operations.get(index).equals("/"))||(operations.get(index).equals("*")))
            {
                double res = 0;
                switch (operations.get(index)) {
                    case "/":
                            res = Double.parseDouble(operands.get(index))/Double.parseDouble(operands.get(index+1));
                        break;
                    case "*":
                            res = Double.parseDouble(operands.get(index))*Double.parseDouble(operands.get(index+1));
                        break;
                }
                operands.remove(index+1);
                operands.remove(index);
                operands.add(index, String.valueOf(res));
                operations.remove(index);
            } else index++;
        }

        //Calculate summarize and subtraction
        index = 0;
        count = operations.size();
        for(int i = 0; i < count; i++) {
            if((operations.get(index).equals("+"))||(operations.get(index).equals("-")))
            {
                double res = 0;
                switch (operations.get(index)) {
                    case "+":
                        res = Double.parseDouble(operands.get(index))+Double.parseDouble(operands.get(index+1));
                        break;
                    case "-":
                        res = Double.parseDouble(operands.get(index))-Double.parseDouble(operands.get(index+1));
                        break;
                }
                operands.remove(index+1);
                operands.remove(index);
                operands.add(index, String.valueOf(res));
                operations.remove(index);
            } else index++;
        }
        double res = Double.parseDouble(operands.get(0));
        if (res == (long)res)
            return String.format(Locale.ENGLISH,"%d", (long)res);
            else
                return  String.format(Locale.ENGLISH,"%.4f", res);
    }
}
