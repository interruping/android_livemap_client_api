package net.solarcode.livemapserviceclient;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.net.SocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by geonyounglim on 2017. 9. 13..
 */

public class LiveMapSSLServerCommunicator implements LiveMapServerCommunicatorImplementor, HandshakeCompletedListener {

    private LiveMapServerCommunicatorListener _listener;

    private SSLSocket _socket;

    private BufferedReader _bufferedReader;
    private BufferedWriter _bufferedWriter;

    private long _intervalMiliseconds;

    public LiveMapSSLServerCommunicator(String host, int port) throws IOException, SSLHandshakeException {
        SocketFactory sf = SSLSocketFactory.getDefault();

        try {
            _socket = (SSLSocket) sf.createSocket(host, port);
        } catch (IOException e) {
            throw e;
        }

        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        SSLSession s = _socket.getSession();

// Verify that the certicate hostname is for mail.google.com
// This is due to lack of SNI support in the current SSLSocket.
        if (!hv.verify(host, s)) {
            throw new SSLHandshakeException("Expect" + host + ", " +
                    "found " + s.getPeerPrincipal());
        }
    }

    @Override
    public void setListener(LiveMapServerCommunicatorListener listener) {
        _listener = listener;
    }

    @Override
    public LiveMapServerCommunicatorListener getListener() {
        return _listener;
    }

    @Override
    public void setInterval(long intervalMiliseconds) {
        _intervalMiliseconds = intervalMiliseconds;
    }

    @Override
    public  long getInterval() {
        return 0;
    }

    @Override
    public void setHost(String host) {

    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public void setPort(int port) {

    }

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public void open() {

        try {
            _socket.addHandshakeCompletedListener(this);
            _socket.startHandshake();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
        try {
            _bufferedReader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            _bufferedWriter = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
