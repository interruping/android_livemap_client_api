package net.solarcode.livemapserviceclient;

/**
 * Created by geonyounglim on 2017. 9. 18..
 */

public interface LiveMapServiceListener {
    void connectionFailToLiveMapServer(LiveMapService livemapService, Error err);
    void connectionLostFromLiveMapServer(LiveMapService livemapService, Error err);
    void onServiceReady(LiveMapService livemapService, LiveMapClientNode livemapClientNode);

    void nearNodesFromLiveMapServer(LiveMapService livemapService, LiveMapClientNode[] nearNodes);
    void receivedMessage(int senderId, String message);
}
