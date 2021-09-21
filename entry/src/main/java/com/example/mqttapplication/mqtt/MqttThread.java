package com.example.mqttapplication.mqtt;

import ohos.utils.zson.ZSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;


public class MqttThread implements Runnable {

    /**地址*/
    public static final String MQTT_BROKER_HOST = "tcp://47.98.117.2:1883";
    /**客户端唯一标识*/
    public static final String MQTT_CLIENT_ID = "client";
    /**订阅标识*/
    public static final String MQTT_TOPIC = "HarmonyTest";
    /**客户端*/
    private volatile static MqttClient mqttClient;
    /**连接选项*/
    private static MqttConnectOptions options;
    /**消息*/
    private final List<String> message;

    public MqttThread(List<String> message) {
        this.message = message;
    }

    public void run() {
        try {
            mqttClient = new MqttClient(MQTT_BROKER_HOST, MQTT_CLIENT_ID, new MemoryPersistence());
            options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(20);
            options.setKeepAliveInterval(20);
            mqttClient.connect(options);
            mqttClient.subscribe(MQTT_TOPIC);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) { }
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) {
                    message.clear();
                    message.add(mqttMessage.toString());
                    System.out.println("接收到mqtt消息：" + mqttMessage.toString());
                    ZSONObject json = ZSONObject.stringToZSON(mqttMessage.toString());
                    String message = json.getString("message");
                    if (null != message && !"".equals(message)) {
                        com.example.mqttapplication.mqtt.MqttMessage.getInstance().setMessage(message.toString());
                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) { }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}