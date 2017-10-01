package net.solarcode.livemapserviceclient;

/**
 * Created by geonyounglim on 2017. 9. 12..
 */
enum ServerType {
    TCPSERVER,
    SSLSOCKETSERVER,
}
public class LiveMapServerCommunicator implements LiveMapServerCommunicatorImplementor {

    private LiveMapServerCommunicatorImplementor _pimple;

    private LiveMapServerCommunicator() {
    }

    public LiveMapServerCommunicator(ServerType serverType, String host, int port) {
        switch ( serverType ){
            case TCPSERVER:
                break;
            case SSLSOCKETSERVER:
                try {
                    _pimple = new LiveMapSSLServerCommunicator(host, port);

                } catch (Exception e) {
                    _pimple = null;
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void setListener(LiveMapServerCommunicatorListener listener) {
        _pimple.setListener(listener);
    }

    @Override
    public LiveMapServerCommunicatorListener getListener() {
        return _pimple.getListener();
    }

    @Override
    public void setInterval(long intervalMiliseconds) {
        _pimple.setInterval(intervalMiliseconds);
    }

    @Override
    public long getInterval() {
        return _pimple.getInterval();
    }

    @Override
    public void setHost(String host) {
        _pimple.setHost(host);
    }

    @Override
    public String getHost() {
        return _pimple.getHost();
    }

    @Override
    public void setPort(int port) {
        _pimple.setPort(port);
    }

    @Override
    public int getPort() {
        return _pimple.getPort();
    }

    @Override
    public void open() {
        _pimple.open();
    }

    @Override
    public void close() {
        _pimple.close();
    }
}
