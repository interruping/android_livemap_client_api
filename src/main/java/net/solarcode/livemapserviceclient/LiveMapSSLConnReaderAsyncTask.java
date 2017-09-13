package net.solarcode.livemapserviceclient;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.nio.ByteBuffer;

/**
 * Created by geonyounglim on 2017. 9. 13..
 */

public class LiveMapSSLConnReaderAsyncTask  extends AsyncTask<BufferedReader, Void, ByteBuffer> {
    LiveMapServerCommunicatorListener _targetListener;

    public LiveMapSSLConnReaderAsyncTask(LiveMapServerCommunicatorListener targetListener) {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ByteBuffer byteBuffer) {
        super.onPostExecute(byteBuffer);

        _targetListener.readyToReadFromLiveMapServer(byteBuffer);
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
    protected ByteBuffer doInBackground(BufferedReader... params) {
        BufferedReader bufferedReader = params[0];

        StringBuffer tmpStringBuffer = new StringBuffer();
        String tmpStr = null;

        try {
            while ( (tmpStr = bufferedReader.readLine()) != null ) {
                tmpStringBuffer.append(tmpStr);
            }
        } catch (Exception e) {

        }


        return ByteBuffer.wrap(tmpStringBuffer.toString().getBytes());

    }
}
