package com.example.mqttapplication;

import com.example.mqttapplication.mqtt.MqttThread;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.*;
import ohos.utils.zson.ZSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayAbility extends Ability {

    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 1, "MY_TAG");

    private static final int ERROR = -1;
    private static final int SUCCESS = 0;
    private static final int START_MQTT = 1004;
    private static final int MQTT_MESSAGE = 1005;

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    protected IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);
        PlayRemote remote = new PlayRemote();
        return remote.asObject();
    }

    static class PlayRemote extends RemoteObject implements IRemoteBroker {

        private List<String> message;

        private Thread thread;

        public PlayRemote() {
            super("PlayRemote");
        }

        @Override
        public boolean onRemoteRequest(int code, MessageParcel data, MessageParcel reply, MessageOption option) {
            // 开始mqtt
            if (code == START_MQTT) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", SUCCESS);
                result.put("abilityResult", "成功开始mqtt");
                try {
                    message = new ArrayList<>();
                    MqttThread mqttThread = new MqttThread(message);
                    thread = new Thread(mqttThread);
                    thread.start();
                    System.out.println("mqtt启动成功");
                }
                catch (Exception e) {
                    result.put("code", ERROR);
                    result.put("abilityResult", "启动失败");
                }
                reply.writeString(ZSONObject.toZSONString(result));
            }
            // 获取mqtt消息
            else if (code == MQTT_MESSAGE) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", SUCCESS);
                if (message.isEmpty()) {
                    result.put("abilityResult", "未接收到MQTT消息");
                }
                else {
                    ZSONObject zsonObject = ZSONObject.stringToZSON(message.get(0));
                    result.put("abilityResult", zsonObject.getString("message"));
                }
                reply.writeString(ZSONObject.toZSONString(result));
            }
            else {
                Map<String, Object> result = new HashMap<>();
                result.put("abilityError", ERROR);
                reply.writeString(ZSONObject.toZSONString(result));
                return false;
            }
            return true;
        }

        @Override
        public IRemoteObject asObject() {
            return this;
        }
    }
}