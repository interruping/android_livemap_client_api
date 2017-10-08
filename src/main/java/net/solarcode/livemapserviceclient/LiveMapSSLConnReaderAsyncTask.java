package net.solarcode.livemapserviceclient;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by geonyounglim on 2017. 9. 13..
 */

public class LiveMapSSLConnReaderAsyncTask  extends AsyncTask<InputStream, Void, ByteBuffer> {
    private LiveMapServerCommunicatorListener _targetListener;
    private LiveMapSSLConnReaderAsyncTaskListener _listener;

    private Handler _mainHandler;


    public LiveMapSSLConnReaderAsyncTask(LiveMapServerCommunicatorListener targetListener) {
        super();
        _targetListener = targetListener;
        _mainHandler = new Handler(Looper.getMainLooper());
    }

    public void setTaskListener(LiveMapSSLConnReaderAsyncTaskListener taskListener) {
        _listener = taskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ByteBuffer byteBuffer) {
        super.onPostExecute(byteBuffer);

        _targetListener.readyToReadFromLiveMapServer(byteBuffer);
        _listener.ReaderAsyncTaskComplete();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(ByteBuffer byteBuffer) {
        super.onCancelled(byteBuffer);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected ByteBuffer doInBackground(InputStream... params) {
        InputStream inputStream = params[0];

        ByteBuffer returnByte = null;
        try {

            final int sizeOfInt = 4;
            byte[] header4ByteData = new byte[sizeOfInt];
            int headerDataReadSize = inputStream.read(header4ByteData);

            if ( headerDataReadSize != sizeOfInt ){
                //error handle
            }

            int bodyDataSize = ByteUtility.byteArrayToInt(header4ByteData);

            byte[] bodyBuffer = new byte[bodyDataSize];

            int bodyDataReadSize = inputStream.read(bodyBuffer);

            if ( bodyDataReadSize != bodyDataSize ){
                //error handle
            }

            returnByte = ByteBuffer.allocate(bodyDataSize);
            returnByte.put(bodyBuffer, 0, bodyDataSize);

        } catch (final IOException e) {
             _mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    _listener.IOError(new Error(e.getMessage(), e.getCause()));
                }
            });
            cancel(true);
            return null;
        } catch (final Exception e) {
            _mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    _listener.unknownHostError(new Error(e.getMessage(), e.getCause()));
                }
            });
            cancel(true);
            return null;

        }



        return returnByte;

    }


}
