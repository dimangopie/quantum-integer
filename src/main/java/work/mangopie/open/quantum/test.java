package work.mangopie.open.quantum;

import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        var x0 = 3.000001;
        var nthOrder = 4;
        var iNList = new int[nthOrder];

        double xN = x0;
        iNList[0] = (int) xN;
        for (int i = 1; i < iNList.length; i++) {
            xN = 1 / (xN % 1.0);
            iNList[i] = (int) xN;
        }
        long nN = 1;
        long dN = 0;
        long nM;
        for (int i = iNList.length - 1; 0 <= i; i--) {
            nM = iNList[i] * nN + dN;
            dN = nN;
            nN = nM;
            System.out.println(nN + " / "+ dN);
        }

        System.out.println(Arrays.toString(iNList));
        System.out.println(nN);
        System.out.println(dN);
        System.out.println((double) nN / dN);
//        System.out.println((double) 355 / 113);

        System.out.println();
    }
}
