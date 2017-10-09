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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class LiveMapService implements LiveMapServerCommunicatorListener {
    private Queue<LiveMapCommandFormBase> _commandQueue;
    private LiveMapServerCommunicator _serverCommunicator;
    private LiveMapServiceListener _listener;


    public LiveMapService () {
        _commandQueue = new LinkedList<LiveMapCommandFormBase>();

    }


    public void asyncStart() {
       LiveMapServerCommunicator sc = new LiveMapServerCommunicator(ServerType.SSLSOCKETSERVER, "INPUT_YOUR_LIVEMAP_SERVER_URL", 1212);

        sc.setListener(this);
        sc.open();
    }

    public void stop() {
        _commandQueue.clear();
    }

    public void updateUserNode(LiveMapClientNode recentUpdatedNode) {
        System.out.println("update command push");
        _commandQueue.offer(new LiveMapServiceCollection.LiveMapUserNodeUpdate(recentUpdatedNode));
    }

    public void updateUserNodeAndViewPoint(LiveMapClientNode recentUpdateNode, double latitude, double longitude) {
         _commandQueue.offer(new LiveMapServiceCollection.UserViewpointUpdate(recentUpdateNode, LiveMapClientNode.makeCoordinate(latitude, longitude)));
    }

    public void sendMessage(int sendNodeId, int recvNodeId, String msg) {
        _commandQueue.offer(new LiveMapServiceCollection.UTF8MessageSend(sendNodeId, recvNodeId, msg));
    }

    public void setListener(LiveMapServiceListener lisetener) {
        _listener = lisetener;
    }

    public LiveMapServiceListener getListener (){
        return _listener;
    }

    private int getType(byte[] rawByte) {
        byte[] typeByte = new byte[4];

        typeByte = Arrays.copyOfRange(rawByte, 0, 4);

        int type = ByteUtility.byteArrayToInt(typeByte);

        return type;
    }

    private ByteBuffer readContent(byte[] rawByte) {
        byte[] contentBuffer = Arrays.copyOfRange(rawByte, 4, rawByte.length);
        ByteBuffer resultBuffer = ByteBuffer.wrap(contentBuffer);
        return resultBuffer;
    }



    @Override
    public boolean readyToWriteToLiveMapServer(ByteBuffer[] buffer) {
        System.out.println("Current command queue size:" + _commandQueue.size());
        if ( _commandQueue.size() == 0 ) {
            return false;
        }

        LiveMapCommandFormBase toEncodeCommand = _commandQueue.poll();

        ByteBuffer typeBuffer = ByteBuffer.allocate(4);
        typeBuffer.order(ByteOrder.LITTLE_ENDIAN);
        typeBuffer.putInt(toEncodeCommand.type());

        ByteBuffer commandBuffer = toEncodeCommand.serialize();

        ByteBuffer resultBuffer = ByteBuffer.allocate(typeBuffer.capacity() + commandBuffer.capacity());
        resultBuffer.put(typeBuffer.array());

        resultBuffer.put(commandBuffer.array());
        buffer[0] = resultBuffer;

        return true;
    }

    @Override
    public void readyToReadFromLiveMapServer(ByteBuffer buffer) {
        byte[] rawByte = buffer.array();

        if ( rawByte.length == 0 ){
            return;
        }

        int type = getType(rawByte);

        ByteBuffer contentByte = null;

        if ( rawByte.length > 4 )
            contentByte = readContent(rawByte);

        switch (type) {
            case LiveMapServiceType.DEFAULT:
                break;

            case LiveMapServiceType.SETUSERINFO:

                LiveMapServiceCollection.LiveMapSetUserInfo setUserInfo = new LiveMapServiceCollection.LiveMapSetUserInfo(contentByte);
                LiveMapClientNode assignNode = new LiveMapClientNode(setUserInfo.getID());
                _listener.onServiceReady(this, assignNode);
                break;
            case LiveMapServiceType.NEARNODEINFO:

                LiveMapServiceCollection.LiveMapNearNodesInfo nearNodesInfo = new LiveMapServiceCollection.LiveMapNearNodesInfo(contentByte);
                LiveMapClientNode[] liveMapClientNearNodes = nearNodesInfo.getNearNodes();
                _listener.nearNodesFromLiveMapServer(this, liveMapClientNearNodes);
                break;
            case LiveMapServiceType.UTF8MESSAGESEND:
                LiveMapServiceCollection.UTF8MessageSend receivedMsg = new LiveMapServiceCollection.UTF8MessageSend(contentByte);
                _listener.receivedMessage(receivedMsg.senderId(), receivedMsg.getMsg());
                break;


        }
    }

    @Override
    public void illegalArgumentError(Error err) {
        _listener.connectionFailToLiveMapServer(this, err);
    }

    @Override
    public void unknownHostError(Error err) {
        _listener.connectionFailToLiveMapServer(this, err);
    }

    @Override
    public void securityError(Error err) {
        _listener.connectionFailToLiveMapServer(this, err);
    }

    @Override
    public void IOError(Error err) {
        _listener.connectionLostFromLiveMapServer(this, err);
    }

    @Override
    public void unknownError(Error err) {
        _listener.connectionLostFromLiveMapServer(this, err);
    }

    @Override
    public void serverCommunicationStart() {
        requestUserInfo();
    }

    private void requestUserInfo() {
        _commandQueue.offer(new LiveMapServiceCollection.LiveMapRequestUserInfo());
    }

    public int getWaitingCommandCount () {
        return _commandQueue.size();
    }
}
