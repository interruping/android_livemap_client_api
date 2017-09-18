package net.solarcode.livemapserviceclient;

import android.widget.ImageView;

/**
 * Created by geonyounglim on 2017. 9. 18..
 */

public class LiveMapMarker {
    private double _latitude = 0.0f;
    private double _longitude = 0.0f;
    private int _uid = 0;
    private ImageView _customImage = null;
    private boolean _isAimate = false;

    public LiveMapMarker (double latitude, double longitude, int uid) {
        _latitude = latitude;
        _longitude = longitude;
        _uid = uid;

    }

    public double latitude() {
        return _latitude;
    }

    public double longitude() {
        return _longitude;
    }

    public void setCustomImage(ImageView imageView, boolean isAnimate) {
        _customImage = imageView;
        _isAimate = isAnimate;
    }

}
