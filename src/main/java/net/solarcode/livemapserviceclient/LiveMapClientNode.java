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
 @file LiveMapClientNode.java
 @author GeunYoung Lim, interruping@naver.com
 @date 2017. 9. 16.
 */
package net.solarcode.livemapserviceclient;

public class LiveMapClientNode implements LiveMapIDHolder<Integer> {
    /*!
     @brief GPS 좌표 객체 생성 메서드
     @details 좌표값 위도, 경도를 받아 좌표 객체를 생성하여 반환한다.
     @param latitude 위도.
     @param longitude 경도.
     @return Coordinate 생성된 좌표 객체.
     */
    public static Coordinate makeCoordinate(double latitude, double longitude) {
        Coordinate coordinate = new Coordinate();
        coordinate.latitude = latitude;
        coordinate.longitude = longitude;
        return coordinate;
    }
    
    /*!
     @brief GPS 좌표 클래스.
     @details 위도 경도를 갖는 클래스
     */
    public static class Coordinate {
        public double latitude = 0.0f;
        public double longitude = 0.0f;
    }


    private Integer _id;

    private double _latitude;
    private double _longitude;

    /*!
     @brief 생성자
     @details id 값을 인자로 받는 생성자
     @param id LiveMapServer로부터 발급받은 유일한 id 값.
     */
    public LiveMapClientNode(Integer id) {
        setID(id);
    }

    /*!
     @breif id 값 setter
     @param id LiveMapServer로부터 발급받은 유일한 id 값.
     */
    @Override
    public void setID(Integer id) {
        _id = id;
    }

    /*!
     @brief id getter.
     @return Integer LiveMapServer로부터 발급받은 유일한 id 값
     */
    @Override
    public Integer getID() {
        return _id;
    }

    /*!
     @brief Coordinate setter.
     @param coordinate 클라이언트의 GPS좌표 정보.
     */
    public void setCoordinate(Coordinate coordinate) {
        _latitude = coordinate.latitude;
        _longitude = coordinate.longitude;
    }

    /*!
     @brief Coordinate getter.
     @return coordinate 클라이언트의 GPS좌표 정보.
     */
    public Coordinate getCoordinate() {
        Coordinate coordinate = new Coordinate();
        coordinate.latitude = _latitude;
        coordinate.longitude = _longitude;
        return coordinate;
    }
}
