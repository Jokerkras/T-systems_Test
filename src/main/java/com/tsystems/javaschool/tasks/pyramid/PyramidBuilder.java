package com.tsystems.javaschool.tasks.pyramid;

import java.text.CollationElementIterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        if(inputNumbers.contains(null)) throw new CannotBuildPyramidException();
        int size =check(inputNumbers.size());
        if (size == -1) throw new CannotBuildPyramidException();
        try {
            Collections.sort(inputNumbers);
        } catch (OutOfMemoryError e) {
            throw new CannotBuildPyramidException();
        }
        int[][] pyramid = new int[size][size + size -1];
        int startForNumber = (pyramid[0].length - 1) / 2;
        int numbersInRow = 1;
        int inputIndex = 0;
        for (int firstIndex = 0; firstIndex < size; firstIndex++) {
            for(int secondIndex = 0; secondIndex < numbersInRow; secondIndex++) {
                pyramid[firstIndex][startForNumber + 2*secondIndex] = inputNumbers.get(inputIndex);
                inputIndex++;
            }
            numbersInRow++;
            startForNumber--;
        }

        return pyramid;
    }

    private int check(int count) {
        int j = 0;
        int i = 0;
        while (j < count) {
            i++;
            j+=i;
        }
        return j==count? i: -1;
    }
}
