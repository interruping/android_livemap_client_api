package net.solarcode.livemapserviceclient;
/**
 * Created by geonyounglim on 2017. 9. 12..
 */

interface LiveMapServerCommunicatorImplementor {


    void setListener(LiveMapServerCommunicatorListener listener);
    LiveMapServerCommunicatorListener getListener();
    void setInterval(long intervalMiliseconds);
    long getInterval();
    void setHost(String host);String getHost();
    void setPort(int port);int getPort();
    void open();
    void close();

}
