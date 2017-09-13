package net.solarcode.livemapserviceclient;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by geonyounglim on 2017. 9. 13..
 */

public class LiveMapSSLConnWriterAsyncTask extends AsyncTask<BufferedWriter, Void, Void> {
    private LiveMapServerCommunicatorListener _targetListener;
    private ByteBuffer[] _tmpBuffer;

    public LiveMapSSLConnWriterAsyncTask(LiveMapServerCommunicatorListener targetListener) {
        super();
        _tmpBuffer = new ByteBuffer[1];
        _targetListener = targetListener;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _targetListener.readyToWriteToLiveMapServer(_tmpBuffer);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
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
    protected Void doInBackground(BufferedWriter... params) {
        BufferedWriter bufferedWriter = params[0];
        String forConvert = new String(_tmpBuffer[0].array());
        try {
            bufferedWriter.write(forConvert, 0, forConvert.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
