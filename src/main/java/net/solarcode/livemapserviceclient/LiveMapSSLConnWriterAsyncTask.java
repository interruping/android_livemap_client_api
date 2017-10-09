// Copyright 2017 GeunYoung Lim <interruping4dev@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package net.solarcode.livemapserviceclient;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

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

    Handler _mainHandler;

    public LiveMapSSLConnWriterAsyncTask(LiveMapServerCommunicatorListener targetListener) {
        super();
        _tmpBuffer = new ByteBuffer[1];
        _targetListener = targetListener;
        _mainHandler = new Handler(Looper.getMainLooper());
    }

    public void setTaskListener(LiveMapSSLConnWriterAsyncTaskListener listener) {
        _listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();



        boolean isReady = _targetListener.readyToWriteToLiveMapServer(_tmpBuffer);



        if ( isReady == false ){

           LiveMapCommandDefault toEncodeCommand = new LiveMapCommandDefault();
                    ByteBuffer typeBuffer = ByteBuffer.allocate(4);
            typeBuffer.order(ByteOrder.LITTLE_ENDIAN);
            typeBuffer.putInt(toEncodeCommand.type());

            ByteBuffer commandBuffer = toEncodeCommand.serialize();

            ByteBuffer resultBuffer = ByteBuffer.allocate(typeBuffer.capacity() + commandBuffer.capacity());
            resultBuffer.put(typeBuffer.array());

            resultBuffer.put(commandBuffer.array());
            _tmpBuffer[0] = resultBuffer;

        }

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
        //String forConvert = new String(_tmpBuffer[0].array());
        try {
            int length_info_for_4byte_header = _tmpBuffer[0].array().length;

            ByteBuffer byteBuffer = ByteBuffer.allocate(4);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byteBuffer.putInt(length_info_for_4byte_header);
            byte[] fourByte = byteBuffer.array();

            outputStream.write(byteBuffer.array(), 0, 4);
            outputStream.write(_tmpBuffer[0].array(), 0, length_info_for_4byte_header);

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
                    _listener.unknownError(new Error(e.getMessage(), e.getCause()));
                }
            });
            cancel(true);
            return null;
        }
        return null;
    }
}
