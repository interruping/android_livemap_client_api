package net.solarcode.livemapserviceclient;

import java.nio.ByteBuffer;

/**
 * Created by geonyounglim on 2017. 9. 12..
 */

public interface LiveMapServerCommunicatorListener extends LiveMapServerCommunicatorCommonErrorListener {
    boolean readyToWriteToLiveMapServer(ByteBuffer[] buffer);
    void readyToReadFromLiveMapServer(ByteBuffer buffer);
    void serverCommunicationStart();

}
