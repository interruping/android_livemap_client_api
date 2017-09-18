package net.solarcode.livemapserviceclient;

/**
 * Created by geonyounglim on 2017. 9. 18..
 */

interface LiveMapServiceListener {
    void connectionLostFromLiveMapServer(Exception e);
    void assignNodeFromLiveMapServer(LiveMapClientNode myNode);
    void nearNodesFromLiveMapServer(LiveMapClientNode[] nearNodes);
}
