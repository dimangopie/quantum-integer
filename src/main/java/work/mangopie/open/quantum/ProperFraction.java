package work.mangopie.open.quantum;

import java.lang.constant.*;
import java.util.Arrays;
import java.util.Optional;

public class ProperFraction extends Number implements Comparable<ProperFraction>, Constable, Cloneable{
    protected final double meanValue;
    protected final int integer;
    protected final int numerator;
    protected final int denominator;
    protected static int maxDenominator;

    public double getMeanValue() {
        return meanValue;
    }
    public int getInteger() {
        return this.integer;
    }
    public int getNumerator() {
        return this.numerator;
    }
    public int getDenominator() {
        return this.denominator;
    }

    @Override
    public int intValue() {
        return this.integer;
    }

    @Override
    public long longValue() {
        return this.integer;
    }

    @Override
    public float floatValue() {
        return (float) this.meanValue;
    }

    @Override
    public double doubleValue() {
        return this.meanValue;
    }

    public static void setMaxDenominator(int maxDenominator) {
        ProperFraction.maxDenominator = maxDenominator;
    }

    private static int getGCD(int a, int b) {
        do {
            int temp = b;
            b = a % b;
            a = temp;
        } while (b != 0);
        return a;
    }

    protected ProperFraction(
        int tmpInteger,
        int tmpNumerator,
        int tmpDenominator
    ) {
        int[] treeParams = toSimplified(
            tmpInteger,
            tmpNumerator,
            tmpDenominator
        );
        this.integer = treeParams[0];
        this.numerator = treeParams[1];
        this.denominator = treeParams[2];
        this.meanValue = this.integer + (double) this.numerator / this.denominator;
    }

    protected ProperFraction(int[] treeParams) {
        this(treeParams[0], treeParams[1], treeParams[2]);
    }

    public static int[] toSimplified(
        int tmpInteger,
        int tmpNumerator,
        int tmpDenominator
    ) {
        if (tmpDenominator < 0) {
            tmpDenominator = - tmpDenominator;
            tmpNumerator = - tmpNumerator;
        }
        if (tmpNumerator < 0) {
            tmpInteger = tmpInteger + tmpNumerator / tmpDenominator - 1;
            tmpNumerator = tmpNumerator % tmpDenominator + tmpDenominator;
        }
        else {
            tmpInteger = tmpInteger + tmpNumerator / tmpDenominator;
            tmpNumerator = tmpNumerator % tmpDenominator;
        }
        if (tmpNumerator != 0) {
            int gcd = getGCD(tmpNumerator, tmpDenominator);
            tmpNumerator /= gcd;
            tmpDenominator /= gcd;
            return new int[]{tmpInteger, tmpNumerator, tmpDenominator};
        }
        else {
            return new int[]{tmpInteger, 0, 1};
        }
    }
    protected  <T extends ProperFraction> int[] toAdd(T properFraction) {
        return new int[] {
            this.integer + properFraction.integer,
            this.denominator * properFraction.denominator,
            this.numerator * properFraction.denominator + properFraction.numerator * this.denominator
        };
    }
    
    public <T extends ProperFraction> ProperFraction add(T properFraction) {
        return new ProperFraction(toAdd(properFraction));
    }
    
    protected <T extends ProperFraction> int[] toSub(T properFraction) {
        return new int[]{
            this.integer - properFraction.integer,
            this.denominator * properFraction.denominator,
            this.numerator * properFraction.denominator - properFraction.numerator * this.denominator
        };
    }
    
    public <T extends ProperFraction> ProperFraction sub(T properFraction) {
        return new ProperFraction(toSub(properFraction));
    }
    
    protected <T extends ProperFraction> int[] toMul(T properFraction) {
        return new int[] {
            this.integer * properFraction.integer,
            this.denominator * properFraction.denominator,
            properFraction.integer * this.numerator * properFraction.denominator + this.integer * properFraction.numerator * this.denominator + this.numerator * properFraction.numerator
        };
    }
    
    public <T extends ProperFraction> ProperFraction mul(T properFraction) {
        return new ProperFraction(toMul(properFraction));
    }
    
    protected <T extends ProperFraction> int[] toDiv(T properFraction) {
        return new int[]{
            0,
            (this.integer * this.denominator + this.numerator) * properFraction.denominator,
            (properFraction.integer * properFraction.denominator + properFraction.numerator) * this.denominator
        };
    }
    
    public <T extends ProperFraction> ProperFraction div(T properFraction) {
        return new ProperFraction(toDiv(properFraction));
    }

    public static int[] toFrac(int numerator, int denominator) {
        return toSimplified(0, numerator, denominator);
    }

    public static ProperFraction frac(int numerator, int denominator) {
        return new ProperFraction(ProperFraction.toFrac(numerator, denominator));
    }

    public static ProperFraction continuedFractionExpansion(double value, int nthOrder) throws ArithmeticException{
        int integerPart = (int) value;
        double decimalPart = value % 1.0;
        if (nthOrder <= 0) {
            nthOrder = 3;
        }
        else if (nthOrder == 1) {
            return new ProperFraction(
                integerPart,
                0,
                1
            );
        }
        if (value < 0) {
            integerPart--;
            decimalPart++;
        }
        var integerNList = new int[nthOrder];
        double valueN = decimalPart;
        integerNList[0] = (int) (valueN % 1.0);
        for (int i = 1; i < integerNList.length; i++) {
            valueN = 1 / (valueN % 1.0);
            integerNList[i] = (int) valueN;
        }
        System.out.println(Arrays.toString(integerNList));
        int numeratorN = 1;
        int denominatorN = 0;
        int numeratorM;
        for (int i = integerNList.length - 1; 0 <= i; i--) {
            numeratorM = integerNList[i] * numeratorN + denominatorN;
            if (numeratorN <= 0) {
                throw new ArithmeticException();
            }
            denominatorN = numeratorN;
            numeratorN = numeratorM;
        }
        return new ProperFraction(
            integerPart,
            numeratorN,
            denominatorN
        );
    }


    public ProperFraction clone(ProperFraction properFraction) {
        return new ProperFraction(properFraction.integer, properFraction.numerator, properFraction.denominator);
    }


    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        return Optional.of(this.integer + "+" + this.numerator + "/" + this.denominator);
    }

    @Override
    public int compareTo(ProperFraction properFraction) {
        int res = this.integer - properFraction.integer;
        if (res == 0) {
            res = this.numerator * properFraction.denominator - properFraction.numerator * this.denominator;
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProperFraction) {
            return (
                this.integer == ((ProperFraction) obj).integer
                && this.numerator == ((ProperFraction) obj).numerator
                && this.denominator == ((ProperFraction) obj).denominator
            );
        } else {
            return false;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return this.toString(false);
    }

    public String toString(boolean debugDetail) {
        if (debugDetail) {
            return
                "ProperFraction " + this.hashCode() + " {\n"
                    + "    meanValue : " + meanValue + "\n"
                    + "    integer : " + integer + "\n"
                    + "    numerator : " + numerator + "\n"
                    + "    denominator : " + denominator + "\n"
                    + "}"
                ;
        }
        else {
            return String.valueOf(meanValue);
        }
    }
}
