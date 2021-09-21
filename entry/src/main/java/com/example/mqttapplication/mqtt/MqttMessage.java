package com.example.mqttapplication.mqtt;

public class MqttMessage {

    private String message = "";

    private static MqttMessage instance;

    private MqttMessage() {
    }

    public static synchronized MqttMessage getInstance() {
        if (instance == null) {
            instance = new MqttMessage();
        }
        return instance;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String showMessage() {
        return this.message;
    }
}
