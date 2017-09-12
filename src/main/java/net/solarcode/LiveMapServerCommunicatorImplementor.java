package net.solarcode;

/**
 * Created by geonyounglim on 2017. 9. 12..
 */

abstract class LiveMapServerCommunicatorImplementor {
    private LiveMapServerCommunicatorListener _listener;

    public void setListener(LiveMapServerCommunicatorListener listener) {
        _listener = listener;
    }

    public LiveMapServerCommunicatorListener getListener() {
        return _listener;
    }

    abstract public long setInterval(long intervalMiliseconds);
    abstract public long getInterval();

    abstract public void setHost(String host);
    abstract public String getHost();

    abstract public void setPort(int port);
    abstract public int getPort();

    abstract public void open();
    abstract public void close();

}
