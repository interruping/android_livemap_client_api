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

        public LiveMapNodeUpdateBase (Integer typeID, LiveMapClientNode updateInfo, ByteBuffer inputData){
            super(typeID, inputData);
            fillSegmentWithNodeInfo(updateInfo);
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
            super(1, updateInfo);
        }
    }

    public static class LiveMapRequestUserInfo extends LiveMapCommandFormBase
    {
        public LiveMapRequestUserInfo () {
            super(2);
        }

    }

    public static class LiveMapSetUserInfo extends LiveMapCommandFormBase
    {
        SegmentInfo _idIntSegmentInfo = new SegmentInfo(0, 4);;

        public LiveMapSetUserInfo (Integer newID) {
            super(3);

            ByteBuffer idBuffer = ByteBuffer.allocate(4);
            idBuffer.order(ByteOrder.LITTLE_ENDIAN);
            idBuffer.putInt(newID);
            _idIntSegmentInfo = addSegment(idBuffer);
        }

        public LiveMapSetUserInfo (ByteBuffer inputData) {
            super(3, inputData);
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
            super(4);
        }

        public LiveMapNearNodesInfo( ByteBuffer inputData) {
            super(4, inputData);


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

}
