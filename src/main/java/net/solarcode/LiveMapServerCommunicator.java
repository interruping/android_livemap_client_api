package net.solarcode;

/**
 * Created by geonyounglim on 2017. 9. 12..
 */

public class LiveMapServerCommunicator extends LiveMapServerCommunicatorImplementor {

    private LiveMapServerCommunicatorImplementor _pimple;

    public LiveMapServerCommunicator() {

    }

    public LiveMapServerCommunicator(String host, int port) {

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
    public long setInterval(long intervalMiliseconds) {
        return _pimple.setInterval(intervalMiliseconds);
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
