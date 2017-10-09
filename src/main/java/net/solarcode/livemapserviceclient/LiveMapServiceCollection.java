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

import net.solarcode.livemapserviceclient.LiveMapClientNode.Coordinate;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by geonyounglim on 2017. 9. 16..
 */

public final class LiveMapServiceCollection {
    public static class LiveMapNodeUpdateBase  extends LiveMapCommandFormBase {

        private SegmentInfo _idSegmentInfo = new SegmentInfo(0,4);
        private SegmentInfo _latSegmentInfo = new SegmentInfo(4,8);
        private SegmentInfo _lonSegmentInfo = new SegmentInfo(12, 8);

        public LiveMapNodeUpdateBase  (Integer typeID, LiveMapClientNode updateInfo){
            super(typeID);
            fillSegmentWithNodeInfo(updateInfo);
        }

        public LiveMapNodeUpdateBase (Integer typeID, ByteBuffer inputData){
            super(typeID, inputData);
        }

        public void fillSegmentWithNodeInfo( LiveMapClientNode updateInfo){
            int id = updateInfo.getID();

            _idSegmentInfo = addSegment(ByteUtility.intTo4Byte(id));


            Coordinate coordinate = updateInfo.getCoordinate();

            double lat = coordinate.latitude;
            _latSegmentInfo = addSegment(ByteUtility.doubleTo8Byte(lat));

            double lon = coordinate.longitude;
            _latSegmentInfo = addSegment(ByteUtility.doubleTo8Byte(lon));

        }

    }

    public static class LiveMapUserNodeUpdate extends LiveMapNodeUpdateBase {
        public LiveMapUserNodeUpdate (LiveMapClientNode updateInfo) {
            super(LiveMapServiceType.USERNODEUPDATE, updateInfo);
        }
    }

    public static class LiveMapRequestUserInfo extends LiveMapCommandFormBase
    {
        public LiveMapRequestUserInfo () {
            super(LiveMapServiceType.REQUESTUSERINFO);
        }

    }

    public static class LiveMapSetUserInfo extends LiveMapCommandFormBase
    {
        SegmentInfo _idIntSegmentInfo = new SegmentInfo(0, 4);;

        public LiveMapSetUserInfo (Integer newID) {
            super(LiveMapServiceType.SETUSERINFO);

            ByteBuffer idBuffer = ByteBuffer.allocate(4);
            idBuffer.order(ByteOrder.LITTLE_ENDIAN);
            idBuffer.putInt(newID);
            _idIntSegmentInfo = addSegment(idBuffer);
        }

        public LiveMapSetUserInfo (ByteBuffer inputData) {
            super(LiveMapServiceType.SETUSERINFO, inputData);
        }

        Integer getID() {
            ByteBuffer _idData = readSegment(_idIntSegmentInfo);
            int id = ByteUtility.byteArrayToInt(_idData.array());
            return id;
        }
    }

    public static class LiveMapNearNodesInfo extends LiveMapCommandFormBase {
        SegmentInfo _numOfNearNodeInfo = new SegmentInfo(0, 4);

        public LiveMapNearNodesInfo() {
            super(LiveMapServiceType.NEARNODEINFO);
        }

        public LiveMapNearNodesInfo( ByteBuffer inputData) {
            super(LiveMapServiceType.NEARNODEINFO, inputData);


        }

        public void setNearNodes(LiveMapClientNode[] nearNodes) {

            if ( getEntireSize() != 0 ){
                return;
            }

            int countNumOfNearNode = nearNodes.length;

            addSegment(ByteUtility.intTo4Byte(countNumOfNearNode));

            for ( LiveMapClientNode nearNode : nearNodes) {
                int id = nearNode.getID();
                addSegment(ByteUtility.intTo4Byte(id));

                final Coordinate coordinate = nearNode.getCoordinate();
                addSegment(ByteUtility.doubleTo8Byte(coordinate.latitude));
                addSegment(ByteUtility.doubleTo8Byte(coordinate.longitude));
            }

        }

        public LiveMapClientNode[] getNearNodes() {

            int readNumOfNearNode  = 0;
            ByteBuffer numOfNearNodeBuffer = readSegment(_numOfNearNodeInfo);
            readNumOfNearNode = ByteUtility.byteArrayToInt(numOfNearNodeBuffer.array());

            LiveMapClientNode[] readNearNodes = new LiveMapClientNode[readNumOfNearNode];

            final int sizeofID = 4;
            final int sizeofLat = 8;
            final int sizeofLon = 8;

            int startBegin = 4;

            for (int count = 0; count < readNumOfNearNode; count++ ) {

                SegmentInfo idSegmentInfo = new SegmentInfo(startBegin, sizeofID);
                SegmentInfo latSegmentInfo = new SegmentInfo(startBegin + sizeofID, sizeofLat);
                SegmentInfo lonSegmentInfo = new SegmentInfo(startBegin + sizeofID + sizeofLat, sizeofLon);

                startBegin += sizeofID + sizeofLat + sizeofLon;

                int readId = ByteUtility.byteArrayToInt(readSegment(idSegmentInfo).array());
                double readLat = ByteUtility.byteArrayToDouble(readSegment(latSegmentInfo).array());
                double readLon = ByteUtility.byteArrayToDouble(readSegment(lonSegmentInfo).array());

                readNearNodes[count] = new LiveMapClientNode(readId);
                readNearNodes[count].setCoordinate(LiveMapClientNode.makeCoordinate(readLat, readLon));
            }

            return readNearNodes;

        }



    }

    public static class UserViewpointUpdate extends LiveMapNodeUpdateBase {
        private SegmentInfo _viewPointLat = new SegmentInfo(20, 8);
        private SegmentInfo _viewPointLon = new SegmentInfo(28, 8);

        public UserViewpointUpdate(LiveMapClientNode updateInfo, Coordinate viewPoint) {
            super(LiveMapServiceType.USERVIEWPOINTUPDATE, updateInfo);

            addSegment(ByteUtility.doubleTo8Byte(viewPoint.latitude));
            addSegment(ByteUtility.doubleTo8Byte(viewPoint.longitude));

        }
    }

    public static class UTF8MessageSend extends LiveMapCommandFormBase {
        SegmentInfo _senderIdInfo = new SegmentInfo(0, 4);
        SegmentInfo _recvIdInfo = new SegmentInfo(4, 4);
        SegmentInfo _msgLengthInfo = new SegmentInfo(8, 4);

        public UTF8MessageSend(int senderId, int receiverId, String msg) {
            super(LiveMapServiceType.UTF8MESSAGESEND);

            addSegment(ByteUtility.intTo4Byte(senderId));
            addSegment(ByteUtility.intTo4Byte(receiverId));
            addSegment(ByteUtility.intTo4Byte(msg.length()));

            addSegment(ByteBuffer.wrap(msg.getBytes()));

        }

        public UTF8MessageSend(ByteBuffer inputData) {
            super(LiveMapServiceType.UTF8MESSAGESEND, inputData);
        }

        public int senderId() {
            int id = 0;
            id = ByteUtility.byteArrayToInt(readSegment(_senderIdInfo).array());

            return id;
        }

        public int receiverId() {
            int id = 0;
            id = ByteUtility.byteArrayToInt(readSegment(_recvIdInfo).array());

            return id;
        }

        public String getMsg() {
            int length = 0;
            length = ByteUtility.byteArrayToInt(readSegment(_msgLengthInfo).array());

            SegmentInfo msgSegmentInfo = new SegmentInfo(12, length);
            return new String(readSegment(msgSegmentInfo).array());
        }
    }

}
