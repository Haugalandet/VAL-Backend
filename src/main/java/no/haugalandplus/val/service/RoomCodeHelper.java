package no.haugalandplus.val.service;

import java.math.BigInteger;

public class RoomCodeHelper {
    /**
     * Algorithm that generates unique room codes for
     * every id.
     * @param id poll Id
     * @return roomCode
     */
    public static String generateRoomCode(Long id) {
        BigInteger largePrime = BigInteger.valueOf(1207571);
        long xorMask = 56876;
        long pow = 6;
        if (id*2 >= Math.pow(10, pow)) {
            while (Math.pow(10, pow) <= id*2) {
                pow += 1;
            }
        }
        long code = id;
        code = code ^ xorMask;
        code = BigInteger.valueOf(code).multiply(largePrime).mod(BigInteger.valueOf((long) Math.pow(10, pow))).longValue();
        return String.format("%0"+pow+"d", code);
    }
}
