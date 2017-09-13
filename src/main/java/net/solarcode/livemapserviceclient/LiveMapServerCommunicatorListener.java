package net.solarcode.livemapserviceclient;

import java.nio.ByteBuffer;

/**
 * Created by geonyounglim on 2017. 9. 12..
 */

public interface LiveMapServerCommunicatorListener {
    boolean readyToWriteToLiveMapServer(ByteBuffer buffer);
    void readyTOReadFromLiveMapServer(ByteBuffer buffer);
    void timedoutRequest(String host, int port);
    void serverCommunicationError(Error error);
}
