package net.solarcode.livemapserviceclient;

/**
 * Created by geonyounglim on 2017. 9. 14..
 */

interface LiveMapSSLConnReaderAsyncTaskListener {
    void ReaderAsyncTaskComplete();
    void ReaderAsyncTaskFail(Exception e);
}
