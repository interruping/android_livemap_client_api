package net.solarcode.livemapserviceclient;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.SocketFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by geonyounglim on 2017. 9. 13..
 */

public class LiveMapSSLServerCommunicator implements LiveMapServerCommunicatorImplementor, LiveMapSSLSocketAsyncBuilderListener, LiveMapSSLConnWriterAsyncTaskListener, LiveMapSSLConnReaderAsyncTaskListener {

    private LiveMapServerCommunicatorListener _listener;

    private String _host;
    private int _port;

    private SSLSocket[] _socketRef;

    private InputStream[] _inputStreamRef;
    private OutputStream[] _outputStreamRef;

    private long _intervalMiliseconds;
    private Timer _timer;

    public LiveMapSSLServerCommunicator(String host, int port) throws IOException, SSLHandshakeException {
        _host = host;
        _port = port;
        _socketRef = new SSLSocket[1];
        _inputStreamRef = new InputStream[1];
        _outputStreamRef = new OutputStream[1];
        _intervalMiliseconds = 100/* miliseconds */;


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
        return _intervalMiliseconds;
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

      LiveMapSSLSocketAsyncBuilder socketBuilder = new LiveMapSSLSocketAsyncBuilder(_host, _port, _outputStreamRef, _inputStreamRef);
        socketBuilder.setListener(this);
        socketBuilder.trustAllCerts(true);
        socketBuilder.execute(_socketRef);

    }

    @Override
    public void close() {

    }

    @Override
    public void SSLSocketBuildComplete() {
        LiveMapSSLConnWriterAsyncTask writerAsyncTask = new LiveMapSSLConnWriterAsyncTask(_listener);
        writerAsyncTask.setTaskListener(this);
        writerAsyncTask.execute(_outputStreamRef[0]);
    }

    @Override
    public void SSLSocketBuildFail(Exception e) {

    }

    @Override
    public void WriterAsyncTaskComplete() {
//        TimerTask readerTaskWillRun = new TimerTask() {
//            @Override
//            public void run() {
//                LiveMapSSLConnReaderAsyncTask readerAsyncTask = new LiveMapSSLConnReaderAsyncTask(_listener);
//                readerAsyncTask.setTaskListener(LiveMapSSLServerCommunicator.this);
//                readerAsyncTask.execute(_bufferedReaderRef[0]);
//            }
//        };
//        _timer = new Timer();
//        _timer.schedule(readerTaskWillRun, _intervalMiliseconds);
                LiveMapSSLConnReaderAsyncTask readerAsyncTask = new LiveMapSSLConnReaderAsyncTask(_listener);
                readerAsyncTask.setTaskListener(LiveMapSSLServerCommunicator.this);
                readerAsyncTask.execute(_inputStreamRef[0]);
    }

    @Override
    public void ReaderAsyncTaskComplete() {
        TimerTask writerTaskWillRun = new TimerTask() {
            @Override
            public void run() {
                LiveMapSSLConnWriterAsyncTask writerAsyncTask = new LiveMapSSLConnWriterAsyncTask(_listener);
                writerAsyncTask.setTaskListener(LiveMapSSLServerCommunicator.this);
                writerAsyncTask.execute(_outputStreamRef[0]);
            }
        };
        _timer = new Timer();
        _timer.schedule(writerTaskWillRun, _intervalMiliseconds);
    }

    @Override
    public void WriterAsyncTaskFail(Exception e) {

    }

    @Override
    public void ReaderAsyncTaskFail(Exception e) {

    }
}
