package net.solarcode.livemapserviceclient;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by geonyounglim on 2017. 9. 14..
 */

public class LiveMapSSLSocketAsyncBuilder extends AsyncTask<SSLSocket[], Void, Void> {

    private LiveMapSSLSocketAsyncBuilderListener _listener;

    private String _host;
    private int _port;

    private OutputStream[] _outputStreamRef;
    private InputStream[] _inputStreamRef;

    private boolean _isAllTrust;

    private TrustManager[] _trustAllCerts;



    public LiveMapSSLSocketAsyncBuilder(String host, int port, OutputStream[] outputStreamRef, InputStream[] inputStreamRef) {
        super();
        _host = host;
        _port = port;

        _outputStreamRef = outputStreamRef;
        _inputStreamRef = inputStreamRef;

    }

    public void trustAllCerts(boolean isAllTrust) {
        _isAllTrust = isAllTrust;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if ( _isAllTrust ){
            _trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        _listener.SSLSocketBuildComplete();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Void doInBackground(SSLSocket[]... params) {
        try {

            SSLSocket[] socketRef = params[0];

            if (_isAllTrust) {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, _trustAllCerts, new java.security.SecureRandom());

                SSLSocketFactory sf = sc.getSocketFactory();
                socketRef[0] = (SSLSocket) sf.createSocket(_host, _port);
            } else {
                SocketFactory sf = SSLSocketFactory.getDefault();
                socketRef[0] = (SSLSocket) sf.createSocket(_host, _port);
            }
//                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
//                    SSLSession s = _socket.getSession();
//
//// Verify that the certicate hostname is for mail.google.com
//// This is due to lack of SNI support in the current SSLSocket.
//                    if (!hv.verify(_host, s)) {
//                        throw new SSLHandshakeException("Expect" + _host + ", " +
//                                "found " + s.getPeerPrincipal());
//                    }
            _inputStreamRef[0] = socketRef[0].getInputStream();
            _outputStreamRef[0] = socketRef[0].getOutputStream();

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void setListener(LiveMapSSLSocketAsyncBuilderListener listener) {
        _listener = listener;
    }

    public LiveMapSSLSocketAsyncBuilderListener getListener() {
        return _listener;
    }
}
