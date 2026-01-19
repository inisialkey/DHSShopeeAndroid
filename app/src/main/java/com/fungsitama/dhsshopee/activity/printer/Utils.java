package com.fungsitama.dhsshopee.activity.printer;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final byte[] UNICODE_TEXT = {35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35};
    private static String[] binaryArray = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    private static String hexStr = "0123456789ABCDEF";

    public static byte[] decodeBitmap(Bitmap bmp) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        List<String> list = new ArrayList<>();
        int i = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;
        String zeroStr = "";
        if (zeroCount > 0) {
            int bitLen = (bmpWidth / 8) + 1;
            for (int i2 = 0; i2 < 8 - zeroCount; i2++) {
                zeroStr = zeroStr + "0";
            }
        }
        for (int i3 = 0; i3 < bmpHeight; i3++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i3);
                int g = (color >> 8) & 255;
                int b = color & 255;
                if (((color >> 16) & 255) <= 160 || g <= 160 || b <= 160) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }
        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D763000";
        String widthHexString = Integer.toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8 : (bmpWidth / 8) + 1);
        if (widthHexString.length() > 2) {
            Log.e("decodeBitmap error", " width is too large");
            return null;
        }
        if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString;
        }
        String widthHexString2 = widthHexString + "00";
        String heightHexString = Integer.toHexString(bmpHeight);
        if (heightHexString.length() > 2) {
            Log.e("decodeBitmap error", " height is too large");
            return null;
        }
        if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString;
        }
        String heightHexString2 = heightHexString + "00";
        List<String> commandList = new ArrayList<>();
        commandList.add(commandHexString + widthHexString2 + heightHexString2);
        commandList.addAll(bmpHexList);
        return hexList2Byte(commandList);
    }

    public static List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList<>();
        for (String binaryStr : list) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                sb.append(myBinaryStrToHexString(binaryStr.substring(i, i + 8)));
            }
            hexList.add(sb.toString());
        }
        return hexList;
    }

    public static String myBinaryStrToHexString(String binaryStr) {
        String hex = "";
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        for (int i = 0; i < binaryArray.length; i++) {
            if (f4.equals(binaryArray[i])) {
                hex = hex + hexStr.substring(i, i + 1);
            }
        }
        for (int i2 = 0; i2 < binaryArray.length; i2++) {
            if (b4.equals(binaryArray[i2])) {
                hex = hex + hexStr.substring(i2, i2 + 1);
            }
        }
        return hex;
    }

    public static byte[] hexList2Byte(List<String> list) {
        List<byte[]> commandList = new ArrayList<>();
        for (String hexStr2 : list) {
            commandList.add(hexStringToBytes(hexStr2));
        }
        return sysCopy(commandList);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        String hexString2 = hexString.toUpperCase();
        int length = hexString2.length() / 2;
        char[] hexChars = hexString2.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray2 : srcArrays) {
            System.arraycopy(srcArray2, 0, destArray, destLen, srcArray2.length);
            destLen += srcArray2.length;
        }
        return destArray;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
