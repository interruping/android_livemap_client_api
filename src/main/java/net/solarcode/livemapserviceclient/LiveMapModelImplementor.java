package net.solarcode.livemapserviceclient;

import android.app.Fragment;
import android.view.View;

/**
 * Created by geonyounglim on 2017. 9. 18..
 */

interface LiveMapModelImplementor {

    void setMapDrawView(View v);
    void setMapDrawView(View v, double viewPointLat, double viewPointLon);
    void setMapDrawFragment(Fragment mapFragment);
    void setMapDrawFragment(Fragment mapFragment, double viewPointLat, double viewPointLon);

    void setViewPoint(double latitude, double longitude);
    void setViewPoint(double latitude, double longitude, float zoom);
    void setViewPoint(double latitude, double longitude, float zoom, boolean isAnimate);

    void addAndDrawMarkerAtMap();
    void removedMarkerFormMap();
    boolean updataMarkerPositionWithIdentifier();

}
