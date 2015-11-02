package ca.ualberta.cmput301.t03.common.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
public class HttpClient extends HttpClientAsync {
    public HttpClient(String root) throws MalformedURLException {
        super(root);
    }

    public HttpClient(URL root) {
        super(root);
    }

    private String mSuffix;
    private byte[] mDataToBeSent;
    private HttpMethods mSendMethod;
    private HttpResponse mResponse;



    protected HttpResponse makeAsyncSendDataRequest(String suffix, byte[] dataToBeSent, HttpMethods sendMethod) throws IOException {
        return super.makeSendDataRequest(suffix, dataToBeSent, sendMethod);
    }

    protected HttpResponse makeAsyncDataLessRequest(String suffix, HttpMethods method) throws IOException {
        return super.makeDataLessRequest(suffix, method);
    }

    @Override
    protected HttpResponse makeSendDataRequest(String suffix, byte[] dataToBeSent, HttpMethods sendMethod){
        mDataToBeSent = dataToBeSent;
        mSuffix = suffix;
        mSendMethod = sendMethod;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mResponse = makeAsyncSendDataRequest(mSuffix, mDataToBeSent, mSendMethod);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mResponse;

    }

    @Override
    protected HttpResponse makeDataLessRequest(String suffix, HttpMethods method){
        mSuffix = suffix;
        mSendMethod = method;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mResponse =  makeAsyncDataLessRequest(mSuffix, mSendMethod);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mResponse;
    }

}
