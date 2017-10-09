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
