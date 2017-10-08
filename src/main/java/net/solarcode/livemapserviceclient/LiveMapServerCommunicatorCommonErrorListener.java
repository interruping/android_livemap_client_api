package net.solarcode.livemapserviceclient;

/**
 * Created by geonyounglim on 2017. 9. 21..
 */

interface LiveMapServerCommunicatorCommonErrorListener {

    void illegalArgumentError(Error err);
    void unknownHostError(Error err);
    void securityError(Error err);
    void IOError(Error err);
    void unknownError(Error err);
}
