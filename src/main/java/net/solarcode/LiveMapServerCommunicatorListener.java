package net.solarcode;

import java.nio.ByteBuffer;

/**
 * Created by geonyounglim on 2017. 9. 12..
 */

public interface LiveMapServerCommunicatorListener {
    public boolean readyToWriteToLiveMapServer(ByteBuffer buffer);
    public void readyTOReadFromLiveMapServer(ByteBuffer buffer);
    public void timedoutRequest(String host, int port);
    public void serverCommunicationError(Error error);
}
