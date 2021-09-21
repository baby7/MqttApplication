const ABILITY_TYPE_EXTERNAL = 0;
const ACTION_SYNC = 0;
const ACTION_MESSAGE_CODE_START_MQTT = 1004;
const ACTION_MESSAGE_CODE_MQTT_MESSAGE = 1005;
const BUNDLE_NAME = 'com.example.mqttapplication';
const ABILITY_NAME = 'com.example.mqttapplication.PlayAbility';

export const playAbility = {
    startMqtt: async function() {
        FeatureAbility.callAbility({
            messageCode: ACTION_MESSAGE_CODE_START_MQTT,
            abilityType: ABILITY_TYPE_EXTERNAL,
            syncOption: ACTION_SYNC,
            bundleName: BUNDLE_NAME,
            abilityName: ABILITY_NAME
        });
    },
    mqttMessage: async function(that) {
        var result = await FeatureAbility.callAbility({
            messageCode: ACTION_MESSAGE_CODE_MQTT_MESSAGE,
            abilityType: ABILITY_TYPE_EXTERNAL,
            syncOption: ACTION_SYNC,
            bundleName: BUNDLE_NAME,
            abilityName: ABILITY_NAME
        });
        var ret = JSON.parse(result);
        if (ret.code == 0) {
            console.info('mqtt is:' + JSON.stringify(ret.abilityResult));
            that.title = 'mqtt is:' + JSON.stringify(ret.abilityResult);
        } else {
            console.error('mqtt error code:' + JSON.stringify(ret.code));
        }
    }
}
export default {
    data: {
        title: "",
        timer: null
    },
    onInit() {
        this.title = this.$t('strings.world');
    },
    task() {
        playAbility.mqttMessage(this);
    },
    mqttMessage() {
        this.title = "开始获取MQTT消息";
        this.task()
        this.timer=setInterval(this.task,200)
    },
    stopMqtt() {
        clearInterval(this.timer)
    }
}
playAbility.startMqtt()