package net.solarcode.livemapserviceclient;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by geonyounglim on 2017. 9. 16..
 */

final public class ByteUtility {
    private ByteUtility() {}

    public static int byteArrayToInt(byte[] b){
        int MASK = 0xFF;
        int result = 0;
        result = b[0] & MASK;
        result = result + ((b[1] & MASK) << 8);
        result = result + ((b[2] & MASK) << 16);
        result = result + ((b[3] & MASK) << 24);
        return result;
    }

    public static double byteArrayToDouble(byte[] b){

        return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getDouble();

    }

    public static ByteBuffer intTo4Byte ( int intNum ) {

        ByteBuffer intBuffer = ByteBuffer.allocate(4);
        intBuffer.order(ByteOrder.LITTLE_ENDIAN);
        intBuffer.putInt(intNum);

        return intBuffer;
    }

    public static ByteBuffer doubleTo8Byte ( double doubleNum ) {
        ByteBuffer doubleBuffer = ByteBuffer.allocate(8);
        doubleBuffer.order(ByteOrder.LITTLE_ENDIAN);
        doubleBuffer.putDouble(doubleNum);

        return doubleBuffer;
    }
}
