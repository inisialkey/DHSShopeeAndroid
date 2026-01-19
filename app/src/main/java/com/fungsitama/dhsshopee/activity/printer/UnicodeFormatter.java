package com.fungsitama.dhsshopee.activity.printer;

public class UnicodeFormatter {
    public static String byteToHex(byte b) {
        char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        return new String(new char[]{hexDigit[(b >> 4) & 15], hexDigit[b & 15]});
    }

    public static String charToHex(char c) {
        return byteToHex((byte) (c >>> 8)) + byteToHex((byte) (c & 255));
    }
}
