package net.solarcode.livemapserviceclient;

import java.nio.ByteBuffer;

/**
 * Created by geonyounglim on 2017. 9. 16..
 */

final public class ByteUtility {
    private ByteUtility() {}

    public static int convertirOctetEnEntier(byte[] b){
        int MASK = 0xFF;
        int result = 0;
        result = b[0] & MASK;
        result = result + ((b[1] & MASK) << 8);
        result = result + ((b[2] & MASK) << 16);
        result = result + ((b[3] & MASK) << 24);
        return result;
    }
}
