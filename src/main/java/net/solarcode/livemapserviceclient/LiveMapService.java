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
/*!
 @file LiveMapService.java
 @author Geun Young Lim, interruping@naver.com
 @date 2017. 9. 11.
 */
package net.solarcode.livemapserviceclient;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/*!
 @brief 라이브맵 서비스 클래스
 @details LiveMapServer와 통신하고 사용가능한 서비스를 사용할 수 있는 클래스
 */
public class LiveMapService implements LiveMapServerCommunicatorListener {
    private Queue<LiveMapCommandFormBase> _commandQueue;
    private LiveMapServerCommunicator _serverCommunicator;
    private LiveMapServiceListener _listener;

    /*!
     @brief 생성자 매서드
     */
    public LiveMapService () {
        _commandQueue = new LinkedList<LiveMapCommandFormBase>();

    }

    /*!
     @brief 비동기 서비스 시작 매서드.
     @details LiveMapServer와 연결을 요청하고 연결 완료 후 ID발급을 요청을 한다.
              이 초기화 매서드를 호출한 다음 이 매서드를 호출하고 이 객체가 참조하고 있는 LiveMapServiceListener의
              onServiceReady() 가 호출되었을 때 이 클래스 객체가 사용 준비상태가 된다.
              따라서 onServiceReady() 호출 이전에 setListener(), getListener() 매서드 이외의 매서드는 사용할 수 없다.
     */
    public void asyncStart() {
       LiveMapServerCommunicator sc = new LiveMapServerCommunicator(ServerType.SSLSOCKETSERVER, "INPUT_YOUR_LIVEMAP_SERVER_URL", 1212);

        sc.setListener(this);
        sc.open();
    }

    /*!
     @brief 라이브맵 서비스 종료 매서드.
     @details LiveMapServer와 연결을 종료하고 서비스를 종료합니다.
     */
    public void stop() {
        _commandQueue.clear();
    }

    /*!
     @brief 라이브맵 사용자의 위치를 업데이트 한다.
     @details LiveMapServer가 사용자의 위치를 이 메서드로 받고 실시간으로 업데이트 한다.
              LiveMapServer가 업데이트된 위치를 보고 주변 클라이언트의 정보를 사용자에게 전송한다.
     @param recentUpdatedNode 사용자의 최신 위치 정보가 담긴 LiveMapClientNode 객체
     */
    public void updateUserNode(LiveMapClientNode recentUpdatedNode) {
        _commandQueue.offer(new LiveMapServiceCollection.LiveMapUserNodeUpdate(recentUpdatedNode));
    }

    /*!
     @brief 라이브맵 사용자의 위치와 사용자의  시점를 업데이트 한다.
     @details LiveMapServer가 사용자의 위치를 이 메서드로 받고 실시간으로 업데이트 한다.
              LiveMapServer가 사용자의 시점 위치를 보고 시점 주변 클라이언트의 정보를 사용자에게 전송한다.
     @param recentUpdateNode 사용자의 최신 위치 정보가 담긴 LiveMapClientNode 객체
     @param latitude 시점 위도
     @param longitude 시점 경도
     */
    public void updateUserNodeAndViewPoint(LiveMapClientNode recentUpdateNode, double latitude, double longitude) {
         _commandQueue.offer(new LiveMapServiceCollection.UserViewpointUpdate(recentUpdateNode, LiveMapClientNode.makeCoordinate(latitude, longitude)));
    }

    /*!
     @brief LiveMapServer에 연결되어있는 다른 클라이언트에게 메시지 전송
     @details 다른 클라이언트에게 메시지를 전송한다. 메시지를 전송하기 위해선 수신자 클라이언트의 id를 알아야 한다.
     @param sendNodeId 송신자 Id.
     @param recvNodeId 수신자 Id.
     @param msg 메시지.
     */
    public void sendMessage(int sendNodeId, int recvNodeId, String msg) {
        _commandQueue.offer(new LiveMapServiceCollection.UTF8MessageSend(sendNodeId, recvNodeId, msg));
    }
    
    /*!
     @brief LiveMapServiceListener 클래스 객체 setter.
     @details LiveMapService
     @param listener 리스너 객체
     */
    public void setListener(LiveMapServiceListener lisetener) {
        _listener = lisetener;
    }
    /*!
     @brief LiveMapServiceListener 클래스 객체 getter.
     @param listener 리스너 객체
     */
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
    /*!
     @brief 대기중인 서비스 요청 갯수.
     @details 사용자가 서비스 요청을 할 경우 요청이 네트워크 큐에서 전송될때까지 대기한다.
             대기하고 있는 서비스 요청의 수를 반환하는 매서드.
     @return int 대기중인 서비스 요청의 수
     */
    public int getWaitingCommandCount () {
        return _commandQueue.size();
    }
}
