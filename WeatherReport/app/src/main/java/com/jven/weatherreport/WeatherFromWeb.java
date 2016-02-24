package com.jven.weatherreport;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Jven on 24/2/2016.
 */
public class WeatherFromWeb{
    private static final String TAG = "WeatherReport.Weather";
    private static final String
            SERVER_URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
    private static final String NSPACE = "http://WebXml.com.cn/";
    private static String methods[] = {"getSupportCity"};

    public static void Test(){
        final HttpTransportSE httpTransportSE = new HttpTransportSE(SERVER_URL);
        SoapObject soapObject = new SoapObject(NSPACE,methods[0]);
        soapObject.addPropertyIfValue("byProvinceName", "");
        final SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        soapSerializationEnvelope.bodyOut = soapObject;
        //设置与.NET提供的Web Service保持兼容，如果服务端没用rpc则设为true，否则为false
        soapSerializationEnvelope.dotNet = true;
        Log.d(TAG, "begin task");

        //java中提供了FutureTask供异步任务使用，可以将一个任务提交给后台线程执行，当需要执行结果的时候调用task.get函数
        //如果后台线程还没有执行完，那么task.get将会阻塞，直到后台线程返回，如果后台线程已经执行完，则直接返回结果
        //还可用于查看执行的状态
        FutureTask<String> futureTask = new FutureTask<String>(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String str = "";
                        Log.d(TAG,"before call");
                        try {
                            httpTransportSE.call(NSPACE + methods[0], soapSerializationEnvelope);
                        }catch (Exception e){
                            Log.d(TAG,"exception"+e.toString());
                        }
                        try {
                            if (soapSerializationEnvelope.getResponse() != null) {
                                Log.d(TAG, "is response not null");
                                //获取服务器返回的soap信息
                                SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                                SoapObject detail =
                                        (SoapObject) result.getProperty("getSupportCityResult");
                                for (int i = 0; i < detail.getPropertyCount(); i++) {
                                    str = detail.getPropertyAsString(i);
                                    Log.d(TAG, str);
                                    Log.d(TAG, "enter");
                                }
                            }
                        }catch (Exception e){
                            Log.d(TAG,"exception"+e.toString());
                        }
                        Log.d(TAG,"is response null");
                        return str;
                    }
                }
        );
        new Thread(futureTask).start();

        try{
            futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
