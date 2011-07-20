package com.objectica.bbot.rumblepad.utils;

import org.apache.commons.codec.binary.BinaryCodec;

public class HexUtils {

    public static String toHexString(byte[] data){
        StringBuffer hexBuffer = new StringBuffer();
        for (byte aData : data) {
            hexBuffer.append(aData).append(", ");
        }
        return hexBuffer.toString();
    }

    public static String toByteString(byte[] data) {
        StringBuffer buffer = new StringBuffer();

        for (byte byteData : data) {
            buffer.append(BinaryCodec.toAsciiString(new byte[]{byteData})).append(" ");
        }
        return buffer.toString();
    }
}
