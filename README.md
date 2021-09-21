# HarmonyOS实现MQTT消息监听展示

## 流程

因为harmonyOS暂时没有发现现成的mqtt的js包，所以使用Java进行Mqtt消息的接收，使用JS去定时调用Java接收到消息并展示

首先是JS调用Java，JS FA(Feature Ability)调用Java PA(Particle Ability)有两种方式，Ability和Internal Ability，这里使用的是第一种Ability

然后是Java端的Mqtt消息接收，使用paho的第三方库进行消息接收，页面启动时JS端调用Java端实现Mqtt消息接收开始，使用异步挂起，接收消息并缓存，随后JS端每次调用Java端拿到的都是最新缓存的信息

（详细流程可以参见我的博客：[https://www.baby7blog.com/myBlog?id=101](https://www.baby7blog.com/myBlog?id=101)）

# 运行

首先需要搭建一个EMQ的服务端，这里就不赘述了，网上可以找到很多的帖子

然后将此代码导入Deveco即可运行

另外启动后需要发送消息可以使用python代码简单实现，这里写一下

```python
import paho.mqtt.publish as publish
publish.single('HarmonyTest', '{"message":"BongShakalaka"}', hostname='xxx.xxx.xxx.xxx')
```
