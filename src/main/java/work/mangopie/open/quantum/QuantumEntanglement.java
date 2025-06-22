package work.mangopie.open.quantum;

import java.util.ArrayList;
import java.util.Arrays;

public class QuantumEntanglement extends ArrayList<QuantumInteger> implements Quantizable<int[]>{
    public int index = 0;
    public int[][] imaginaryIntegerMap;
    public int[] integerList;
    private float meanValue;
    QuantumInteger quantumIntegerSum;

    public float getMeanValue() {
        return this.meanValue;
    }

    public void add(QuantumInteger... quantumIntegers) {
        this.addAll(Arrays.asList(quantumIntegers));
    }

    private void setMean() {
        this.meanValue = quantumIntegerSum.getInteger() + (float) quantumIntegerSum.getDenominator() / quantumIntegerSum.getDenominator();
    }

    public void setIntegerList() {
        final int[] tmpIndex = {0};
        this.integerList = new int[this.size()];
        this.forEach(
            quantumInteger -> {
                this.integerList[tmpIndex[0]++] = quantumInteger.getInteger();
            }
        );
    }

    private void setImaginaryIntegerTwoDimensionalArray() {
        this.imaginaryIntegerMap = new int[this.quantumIntegerSum.getDenominator()][this.size()];
        for (int quantumIntegerIndex = 0; quantumIntegerIndex < this.size(); quantumIntegerIndex++) {
            QuantumInteger quantumInteger = this.get(quantumIntegerIndex);
            for (int imaginaryIntegerListIndex = 0; imaginaryIntegerListIndex < this.quantumIntegerSum.getDenominator(); imaginaryIntegerListIndex++) {
                if (this.quantumIntegerSum.getDenominator() != 1) {
                    this.imaginaryIntegerMap[imaginaryIntegerListIndex][quantumIntegerIndex] = quantumInteger.imaginaryIntegerList[imaginaryIntegerListIndex % quantumInteger.getDenominator()];
                }
            }
        }
    }

    public QuantumEntanglement(QuantumInteger... quantumIntegers) {
        this.quantumIntegerSum = QuantumInteger.frac(0, 1);
        Arrays.asList(quantumIntegers).forEach(
            quantumInteger -> {
                this.add(quantumInteger);
                this.quantumIntegerSum = this.quantumIntegerSum.add(quantumInteger);
            }
        );
        this.setImaginaryIntegerTwoDimensionalArray();
        this.setIntegerList();
    }

    @Override
    public int[] Observe() {
        this.index++;
        this.index %= this.quantumIntegerSum.getDenominator();
        int[] tmpObservedArrays = new int[this.integerList.length];
        for (int i = 0; i < this.integerList.length; i++) {
            tmpObservedArrays[i] = this.integerList[i] + imaginaryIntegerMap[this.index][i];
        }
        return tmpObservedArrays;
    }

    @Override
    public String toString() {
        return Arrays.toString(Observe());
    }
}
