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
 * Created by geonyounglim on 2017. 9. 18..
 */

public interface LiveMapServiceListener {
    void connectionFailToLiveMapServer(LiveMapService livemapService, Error err);
    void connectionLostFromLiveMapServer(LiveMapService livemapService, Error err);
    void onServiceReady(LiveMapService livemapService, LiveMapClientNode livemapClientNode);

    void nearNodesFromLiveMapServer(LiveMapService livemapService, LiveMapClientNode[] nearNodes);
    void receivedMessage(int senderId, String message);
}
