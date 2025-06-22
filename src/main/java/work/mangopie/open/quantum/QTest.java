package work.mangopie.open.quantum;

import java.lang.constant.ConstantDesc;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class QTest {

    public static void main(String[] args) throws CloneNotSupportedException {
         ProperFraction properFraction = ProperFraction.frac(9, 1);
        System.out.println(properFraction.hashCode());
        System.out.println(properFraction.clone().hashCode());

        System.out.println(ProperFraction.continuedFractionExpansion(3.0000001, 3).toString(true));
        try {
            System.out.println(ProperFraction.continuedFractionExpansion(3.0000001, 10).toString(true));
        }
        catch (Exception e) {
            System.err.println("ERROR");
        }
    }
}
