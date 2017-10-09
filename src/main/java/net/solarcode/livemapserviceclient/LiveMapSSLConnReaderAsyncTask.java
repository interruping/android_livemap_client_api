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
