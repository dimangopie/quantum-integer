package work.mangopie.open.quantum;

import java.lang.constant.*;
import java.util.Optional;

public class LongProperFraction extends Number implements Comparable<LongProperFraction>, Constable{
    protected final double meanValue;
    protected final long integer;
    protected final long numerator;
    protected final long denominator;
    protected static int maxDenominator;

    public double getMeanValue() {
        return meanValue;
    }
    public long getInteger() {
        return this.integer;
    }
    public long getNumerator() {
        return this.numerator;
    }
    public long getDenominator() {
        return this.denominator;
    }

    @Override
    public int intValue() {
        return (int) this.integer;
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
        LongProperFraction.maxDenominator = maxDenominator;
    }

    private static long getGCD(long a, long b) {
        do {
            var temp = b;
            b = a % b;
            a = temp;
        } while (b != 0);
        return a;
    }

    protected LongProperFraction(
        long tmpInteger,
        long tmpNumerator,
        long tmpDenominator
    ) {
        this.integer = tmpInteger;
        this.numerator = tmpNumerator;
        this.denominator = tmpDenominator;
        this.meanValue = tmpInteger + (double) tmpNumerator / tmpDenominator;
    }

    protected LongProperFraction(
        long[] treeParams
    ) {
        this.integer = treeParams[0];
        this.numerator = treeParams[1];
        this.denominator = treeParams[2];
        this.meanValue = treeParams[0] + (double) treeParams[1] / treeParams[2];
    }

    public static long[] toSimplified(
        long tmpInteger,
        long tmpNumerator,
        long tmpDenominator
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
            long gcd = getGCD(tmpNumerator, tmpDenominator);
            tmpNumerator /= gcd;
            tmpDenominator /= gcd;
            return new long[]{tmpInteger, tmpNumerator, tmpDenominator};
        }
        else {
            return new long[]{tmpInteger, 0, 1};
        }
    }

    public <T extends LongProperFraction> LongProperFraction add(T properFraction) {
        return new LongProperFraction(toSimplified(
            this.integer + properFraction.integer,
            this.denominator * properFraction.denominator,
            this.numerator * properFraction.denominator + properFraction.numerator * this.denominator
        )
        );
    }

    public <T extends LongProperFraction> LongProperFraction sub(T properFraction) {
        return new LongProperFraction(toSimplified(
            this.integer - properFraction.integer,
            this.denominator * properFraction.denominator,
            this.numerator * properFraction.denominator - properFraction.numerator * this.denominator
        )
        );
    }

    public <T extends LongProperFraction> LongProperFraction mul(T properFraction) {
        return new LongProperFraction(toSimplified(
            this.integer * properFraction.integer,
            this.denominator * properFraction.denominator,
            properFraction.integer * this.numerator * properFraction.denominator + this.integer * properFraction.numerator * this.denominator + this.numerator * properFraction.numerator
        )
        );
    }

    public <T extends LongProperFraction> LongProperFraction div(T properFraction) {
        return new LongProperFraction(toSimplified(
            0,
            (this.integer * this.denominator + this.numerator) * properFraction.denominator,
            (properFraction.integer * properFraction.denominator + properFraction.numerator) * this.denominator
        )
        );
    }

    public static LongProperFraction frac(long numerator, long denominator) {
        return new LongProperFraction(toSimplified(0, numerator, denominator));
    }

    public static LongProperFraction continuedFractionExpansion(double value, int nthOrder) {
        long integerPart = (long) value;
        double decimalPart = value % 1.0;
        if (nthOrder <= 0) {
            nthOrder = 3;
        }
        else if (nthOrder == 1) {
            return new LongProperFraction(
                integerPart,
                0,
                1
            );
        }
        if (value < 0) {
            integerPart--;
            decimalPart++;
        }
        var integerNList = new long[nthOrder];
        double valueN = decimalPart;
        integerNList[0] = (long) (valueN % 1.0);
        for (int i = 1; i < integerNList.length; i++) {
            valueN = 1 / (valueN % 1.0);
            integerNList[i] = (int) valueN;
        }
        long numeratorN = 1;
        long denominatorN = 0;
        long numeratorM;
        for (int i = integerNList.length - 1; 0 <= i; i--) {
            numeratorM = integerNList[i] * numeratorN + denominatorN;
            denominatorN = numeratorN;
            numeratorN = numeratorM;
        }
        return new LongProperFraction(
            integerPart,
            numeratorN,
            denominatorN
        );
    }


    public LongProperFraction clone(LongProperFraction properFraction) {
        return new LongProperFraction(properFraction.integer, properFraction.numerator, properFraction.denominator);
    }


    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        return Optional.of(this.integer + "+" + this.numerator + "/" + this.denominator);
    }

    @Override
    public int compareTo(LongProperFraction properFraction) {
        long res = this.integer - properFraction.integer;
        if (res == 0) {
            res = this.numerator * properFraction.denominator - properFraction.numerator * this.denominator;
        }
        return res > 0 ? 1 : (res < 0 ? -1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LongProperFraction) {
            return (
                this.integer == ((LongProperFraction) obj).integer
                    && this.numerator == ((LongProperFraction) obj).numerator
                    && this.denominator == ((LongProperFraction) obj).denominator
            );
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.toString(false);
    }

    public String toString(boolean debugDetail) {
        if (debugDetail) {
            return
                "LongProperFraction " + this.hashCode() + " {\n"
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
