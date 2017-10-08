package net.solarcode.livemapserviceclient;

/**
 * Created by geonyounglim on 2017. 9. 16..
 */

public class LiveMapClientNode implements LiveMapIDHolder<Integer> {
    public static Coordinate makeCoordinate(double latitude, double longitude) {
        Coordinate coordinate = new Coordinate();
        coordinate.latitude = latitude;
        coordinate.longitude = longitude;
        return coordinate;
    }
    public static class Coordinate {
        public double latitude = 0.0f;
        public double longitude = 0.0f;
    }

    private Integer _id;

    private double _latitude;
    private double _longitude;

    public LiveMapClientNode(Integer id) {
        setID(id);
    }

    @Override
    public void setID(Integer id) {
        _id = id;
    }

    @Override
    public Integer getID() {
        return _id;
    }

    public void setCoordinate(Coordinate coordinate) {
        _latitude = coordinate.latitude;
        _longitude = coordinate.longitude;
    }

    public Coordinate getCoordinate() {

        Coordinate coordinate = new Coordinate();
        coordinate.latitude = _latitude;
        coordinate.longitude = _longitude;
        return coordinate;
    }
}
