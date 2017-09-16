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
            ByteBuffer idBuffer = ByteBuffer.allocate(4);
            idBuffer.order(ByteOrder.LITTLE_ENDIAN);
            idBuffer.putInt(id);
            _idSegmentInfo = addSegment(idBuffer);
            Coordinate coordinate = updateInfo.getCoordinate();

            double lat = coordinate.latitude;
            ByteBuffer latBuffer = ByteBuffer.allocate(8);
            latBuffer.order(ByteOrder.LITTLE_ENDIAN);
            latBuffer.putDouble(lat);
            _latSegmentInfo = addSegment(latBuffer);

            double lon = coordinate.longitude;
            ByteBuffer lonBuffer = ByteBuffer.allocate(8);
            lonBuffer.order(ByteOrder.LITTLE_ENDIAN);
            lonBuffer.putDouble(lon);
            _latSegmentInfo = addSegment(lonBuffer);

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
            int id = ByteUtility.convertirOctetEnEntier(_idData.array());
            return id;
        }
    }

}
