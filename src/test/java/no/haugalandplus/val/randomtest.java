package no.haugalandplus.val;

import org.junit.jupiter.api.Test;

public class randomtest {

    @Test
    public void randomTest() {
        for (int i = 990; i < 1_010; i++) {
            System.out.println(""+i+"\t"+applyTransformation(i));
        }
    }

    @Test
    public void reverse() {
        for (int i = 0; i < 10; i++) {
            System.out.println(reverseUniqueCode(i, 6));
        }
    }

    public String applyTransformation(long id) {
        long largePrime = 1207571;
        long xorMask = 98765;
        long pow = 6;
        if (id >= Math.pow(10, pow)) {
            while (Math.pow(10, pow) <= id) {
                pow += 1;
            }
        }

        long transformedID = id;

        transformedID = transformedID ^ xorMask;

        // Ensure the transformed ID is within the desired range
        transformedID = ((transformedID * largePrime) % (long) Math.pow(10, pow));

        transformedID = reverseUniqueCode(transformedID, pow);


        return String.format("%0"+pow+"d", transformedID);
    }

    public long reverseUniqueCode(long id, long pow) {
        long reversedCode = 0;
        for (int i = 0; i < pow; i++) {
            reversedCode = reversedCode * 10 + (id % 10);
            id /= 10;
        }
        return reversedCode;
    }

}
