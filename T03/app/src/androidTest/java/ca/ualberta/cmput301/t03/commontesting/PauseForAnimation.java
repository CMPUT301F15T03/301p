package ca.ualberta.cmput301.t03.commontesting;

/**
 * Copyright 2015 John Slevinsky
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class PauseForAnimation {

    private static Integer TIME_TO_WAIT = 500; // 5 seconds

    public static void pause(){
        customPause(TIME_TO_WAIT);
    }

    public static void customPause(Integer time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
