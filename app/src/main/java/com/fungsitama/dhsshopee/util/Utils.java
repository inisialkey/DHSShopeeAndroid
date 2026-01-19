package com.fungsitama.dhsshopee.util;

import android.graphics.Bitmap;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final byte[] UNICODE_TEXT = {35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35};
    private static String[] binaryArray = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    private static String hexStr = "0123456789ABCDEF";

    public static byte[] decodeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        ArrayList arrayList = new ArrayList();
        int i = width / 8;
        int i2 = width % 8;
        String str = "";
        if (i2 > 0) {
            String str2 = str;
            for (int i3 = 0; i3 < 8 - i2; i3++) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append("0");
                str2 = sb.toString();
            }
            str = str2;
        }
        for (int i4 = 0; i4 < height; i4++) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i5 = 0; i5 < width; i5++) {
                int pixel = bitmap.getPixel(i5, i4);
                int i6 = (pixel >> 16) & 255;
                int i7 = (pixel >> 8) & 255;
                int i8 = pixel & 255;
                if (i6 <= 160 || i7 <= 160 || i8 <= 160) {
                    stringBuffer.append("1");
                } else {
                    stringBuffer.append("0");
                }
            }
            if (i2 > 0) {
                stringBuffer.append(str);
            }
            arrayList.add(stringBuffer.toString());
        }
        List binaryListToHexStringList = binaryListToHexStringList(arrayList);
        String str3 = "1D763000";
        if (i2 != 0) {
            i++;
        }
        String hexString = Integer.toHexString(i);
        if (hexString.length() > 2) {
            Log.e("decodeBitmap error", " width is too large");
            return null;
        }
        if (hexString.length() == 1) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("0");
            sb2.append(hexString);
            hexString = sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(hexString);
        sb3.append("00");
        String sb4 = sb3.toString();
        String hexString2 = Integer.toHexString(height);
        if (hexString2.length() > 2) {
            Log.e("decodeBitmap error", " height is too large");
            return null;
        }
        if (hexString2.length() == 1) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("0");
            sb5.append(hexString2);
            hexString2 = sb5.toString();
        }
        StringBuilder sb6 = new StringBuilder();
        sb6.append(hexString2);
        sb6.append("00");
        String sb7 = sb6.toString();
        ArrayList arrayList2 = new ArrayList();
        StringBuilder sb8 = new StringBuilder();
        sb8.append(str3);
        sb8.append(sb4);
        sb8.append(sb7);
        arrayList2.add(sb8.toString());
        arrayList2.addAll(binaryListToHexStringList);
        return hexList2Byte(arrayList2);
    }

    public static List<String> binaryListToHexStringList(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            StringBuffer stringBuffer = new StringBuffer();
            int i = 0;
            while (i < str.length()) {
                int i2 = i + 8;
                stringBuffer.append(myBinaryStrToHexString(str.substring(i, i2)));
                i = i2;
            }
            arrayList.add(stringBuffer.toString());
        }
        return arrayList;
    }

    public static String myBinaryStrToHexString(String str) {
        String substring = str.substring(0, 4);
        String substring2 = str.substring(4, 8);
        String str2 = "";
        for (int i = 0; i < binaryArray.length; i++) {
            if (substring.equals(binaryArray[i])) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(hexStr.substring(i, i + 1));
                str2 = sb.toString();
            }
        }
        for (int i2 = 0; i2 < binaryArray.length; i2++) {
            if (substring2.equals(binaryArray[i2])) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append(hexStr.substring(i2, i2 + 1));
                str2 = sb2.toString();
            }
        }
        return str2;
    }

    public static byte[] hexList2Byte(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String hexStringToBytes : list) {
            arrayList.add(hexStringToBytes(hexStringToBytes));
        }
        return sysCopy(arrayList);
    }

    public static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String upperCase = str.toUpperCase();
        int length = upperCase.length() / 2;
        char[] charArray = upperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(charArray[i2 + 1]) | (charToByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    public static byte[] sysCopy(List<byte[]> list) {
        int i = 0;
        for (byte[] length : list) {
            i += length.length;
        }
        byte[] bArr = new byte[i];
        int i2 = 0;
        for (byte[] bArr2 : list) {
            System.arraycopy(bArr2, 0, bArr, i2, bArr2.length);
            i2 += bArr2.length;
        }
        return bArr;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
