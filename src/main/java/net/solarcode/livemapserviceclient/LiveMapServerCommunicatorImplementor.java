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
 * Created by geonyounglim on 2017. 9. 12..
 */

interface LiveMapServerCommunicatorImplementor {


    void setListener(LiveMapServerCommunicatorListener listener);
    LiveMapServerCommunicatorListener getListener();
    void setInterval(long intervalMiliseconds);
    long getInterval();
    void setHost(String host);String getHost();
    void setPort(int port);int getPort();
    void open();
    void close();

}
