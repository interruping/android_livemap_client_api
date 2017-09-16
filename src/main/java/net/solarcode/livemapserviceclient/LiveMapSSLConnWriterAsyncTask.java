package net.solarcode.livemapserviceclient;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by geonyounglim on 2017. 9. 13..
 */

public class LiveMapSSLConnWriterAsyncTask extends AsyncTask<OutputStream, Void, Void> {
    private LiveMapServerCommunicatorListener _targetListener;
    private LiveMapSSLConnWriterAsyncTaskListener _listener;

    private ByteBuffer[] _tmpBuffer;

    public LiveMapSSLConnWriterAsyncTask(LiveMapServerCommunicatorListener targetListener) {
        super();
        _tmpBuffer = new ByteBuffer[1];
        _targetListener = targetListener;
    }

    public void setTaskListener(LiveMapSSLConnWriterAsyncTaskListener listener) {
        _listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _targetListener.readyToWriteToLiveMapServer(_tmpBuffer);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        _listener.WriterAsyncTaskComplete();
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
    protected Void doInBackground(OutputStream... params) {
        OutputStream outputStream = params[0];
        String forConvert = new String(_tmpBuffer[0].array());
        try {
            int length_info_for_4byte_header = forConvert.getBytes().length;

            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.putInt(length_info_for_4byte_header);
            byte[] fourByte = byteBuffer.array();

            outputStream.write(byteBuffer.array(), 0, 4);
            outputStream.write(forConvert.getBytes(), 0, forConvert.getBytes().length);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
