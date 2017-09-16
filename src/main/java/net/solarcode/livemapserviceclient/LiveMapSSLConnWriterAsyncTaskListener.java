package net.solarcode.livemapserviceclient;

/**
 * Created by geonyounglim on 2017. 9. 14..
 */

interface LiveMapSSLConnWriterAsyncTaskListener {
    void WriterAsyncTaskComplete();
    void WriterAsyncTaskFail(Exception e);
}
