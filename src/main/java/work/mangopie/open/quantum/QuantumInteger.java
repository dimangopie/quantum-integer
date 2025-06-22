package work.mangopie.open.quantum;

import java.util.Random;

public class QuantumInteger extends ProperFraction implements Quantizable<Integer> {
    private int index = 0;
    protected int[] imaginaryIntegerList;
    private static Long seed;
//    private boolean entanglement;

    @Override
    public Integer Observe() {
        this.index++;
        this.index %= this.denominator;
        return integer + this.getImaginaryInteger();
    }

    @Override
    public int intValue() {
        return this.Observe();
    }

    @Override
    public long longValue() {
        return this.Observe();
    }

    @Override
    public float floatValue() {
        return this.Observe();
    }

    @Override
    public double doubleValue() {
        return this.Observe();
    }

    public static void setSeed(long seed) {
        QuantumInteger.seed = seed;
    }

    public <T extends ProperFraction> QuantumInteger(T properFraction) {
        this(properFraction.integer, properFraction.numerator, properFraction.denominator);
    }

    public QuantumInteger(int[] treeParams) {
        this(treeParams[0], treeParams[1], treeParams[2]);
    }

    private QuantumInteger(
        int tmpInteger,
        int tmpNumerator,
        int tmpDenominator
    ) {
        super(tmpInteger, tmpNumerator, tmpDenominator);
        if (seed == null) {
            this.setImaginaryIntegerArray();
        }
//        this.entanglement = false;
    }

    private void setImaginaryIntegerArray() {
        this.index = 0;
        if (imaginaryIntegerList == null || imaginaryIntegerList.length != this.denominator) {
            imaginaryIntegerList = new int[this.denominator];
        }
        int tmpNumerator0 = 0;
        int tmpNumerator1 = 1;
        int tmpDenominator = 1;
        int mainDiagonal0;
        int mainDiagonal1;
        int antiDiagonal;
        int differenceValue0;
        int differenceValue1;
        for (int i = 0; i < this.denominator; i++) {
            mainDiagonal0 = tmpNumerator0 * this.denominator;
            mainDiagonal1 = tmpNumerator1 * this.denominator;
            antiDiagonal = this.numerator * tmpDenominator;
            differenceValue0 = mainDiagonal0 - antiDiagonal;
            differenceValue1 = mainDiagonal1 - antiDiagonal;
            differenceValue0 = differenceValue0 > 0 ? differenceValue0 : - differenceValue0;
            differenceValue1 = differenceValue1 > 0 ? differenceValue1 : - differenceValue1;
            if (differenceValue0 <= differenceValue1) {
                imaginaryIntegerList[i] = 0;
            }
            else {
                imaginaryIntegerList[i] = 1;
                tmpNumerator0++;
                tmpNumerator1++;
            }
            tmpDenominator++;
        }
    }

    private int getImaginaryInteger() {
        if (seed == null) {
            return this.imaginaryIntegerList[this.index];
        }
        else {
            Random random = new Random(seed);
            int randomNumber = random.nextInt(denominator) + 1;
            return randomNumber <= numerator ? 1 : 0;
        }
    }

    @Override
    public <T extends ProperFraction> QuantumInteger add(T quantumInteger) {
        return new QuantumInteger(super.toAdd(quantumInteger));
    }

    @Override
    public <T extends ProperFraction> QuantumInteger sub(T quantumInteger) {
        return new QuantumInteger(super.toSub(quantumInteger));
    }

    @Override
    public <T extends ProperFraction> QuantumInteger mul(T quantumInteger) {
        return new QuantumInteger(super.toMul(quantumInteger));
    }

    @Override
    public <T extends ProperFraction> QuantumInteger div(T quantumInteger) {
         return new QuantumInteger(super.toDiv(quantumInteger));
    }

    public static QuantumInteger frac(int numerator, int denominator) {
        return new QuantumInteger(ProperFraction.toFrac(numerator, denominator));
    }

    public static QuantumInteger continuedFractionExpansion(double value, int nthOrder) throws ArithmeticException{
        return new QuantumInteger(ProperFraction.continuedFractionExpansion(value, nthOrder));
    }

    @Override
    public String toString(boolean debugDetail) {
        if (debugDetail) {
            return
                "QuantumInteger " + this.hashCode() + " {\n"
                    + "    meanValue : " + meanValue + "\n"
                    + "    integer : " + integer + "\n"
                    + "    numerator : " + numerator + "\n"
                    + "    denominator : " + denominator + "\n"
                    + "}"
                ;
        }
        else {
            return String.valueOf(Observe());
        }
    }
}
