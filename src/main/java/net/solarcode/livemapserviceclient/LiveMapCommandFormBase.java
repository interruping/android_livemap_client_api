package net.solarcode.livemapserviceclient;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by geonyounglim on 2017. 9. 16..
 */

public class LiveMapCommandFormBase {

    public class SegmentInfo {
        private int _begin;
        private int _size;

        public SegmentInfo() {
            _begin = 0;
            _size  = 0;
        }

        public SegmentInfo(int inputBegin, int inputSize) {
            _begin = inputBegin;
            _size = inputSize;
        }

        public SegmentInfo(SegmentInfo rhs) {
            _begin = rhs._begin;
            _size = rhs._size;
        }

        public int begin() {
            return _begin;
        }

        public int size() {
            return _size;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            SegmentInfo cloned = new SegmentInfo();
            cloned._begin = _begin;
            cloned._size = _size;
            return cloned;
        }
    }

    private int _typeID;

    private int _segmentLastestIndex;
    private ArrayList<Byte>_segmentPool;

    private LiveMapCommandFormBase() {

    }

    protected LiveMapCommandFormBase(int typeID){
        typeID = typeID;
        _segmentPool = new ArrayList<Byte>();
        _segmentLastestIndex = 0;
    }

    protected LiveMapCommandFormBase(LiveMapCommandFormBase rhs) {
        _typeID = rhs._typeID;

        for ( Byte aByte : rhs._segmentPool ){
            _segmentPool.add(aByte);
        }
        _segmentLastestIndex = rhs._segmentLastestIndex;

    }

    protected LiveMapCommandFormBase(int typeID, byte[] inputData) {
        _typeID = typeID;
        _segmentPool = new ArrayList<Byte>();
        _segmentLastestIndex = 0;

        for ( byte abyte : inputData ){
            _segmentPool.add(abyte);
            _segmentLastestIndex++;
        }

    }

    public SegmentInfo addSegment(ByteBuffer segBuffer) {

        int startIndex = _segmentLastestIndex;
        int segSize = segBuffer.array().length;

        for ( byte abyte : segBuffer.array() ){
            _segmentPool.add(abyte);
            _segmentLastestIndex++;
        }

        return new SegmentInfo(startIndex, segSize);
    }

    public ByteBuffer readSegment(SegmentInfo segmentInfo) {

        ByteBuffer readSegBuffer = ByteBuffer.allocate(segmentInfo.size());
        for ( int index = segmentInfo.begin(); index < segmentInfo.begin() + segmentInfo.size() ; index++ ) {
            readSegBuffer.put(_segmentPool.get(index));
        }

        return readSegBuffer;
    }

    public void clear() {
        _segmentLastestIndex = 0;
        _segmentPool.clear();
    }

    public int type() {
        return _typeID;
    }

    public ByteBuffer serialize() {
        ByteBuffer serializeBuffer = ByteBuffer.allocate(_segmentLastestIndex);
        for ( int index = 0; index < _segmentLastestIndex; index++ ){
            serializeBuffer.put(_segmentPool.get(index));
        }

        return serializeBuffer;
    }

}
