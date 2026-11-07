package com.github.zhitron.lite_el.operator;

import java.util.*;

/**
 * @author zhitron
 */
public class Operators {

    public static void main(String[] args) {
//        System.out.println(TripleOperatorType.TERNARY.handle(1, 2, 3));
//        System.out.println(TripleOperatorType.TERNARY.handle(true, 4, 5));
//        System.out.println(TripleOperatorType.TERNARY.handle(false, 6, 7));

        Vector firstTryClassesList = new Vector<>();
        firstTryClassesList.add(1);
        firstTryClassesList.addElement(2);
        firstTryClassesList.addAll(Arrays.asList(3,4,5));
        for (Object object : firstTryClassesList) {
            System.out.println(object);
        }
    }

}
