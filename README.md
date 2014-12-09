TinyWeibo  微微博  
=========

An Android application for Sina Weibo 

一个强大的新浪微博 Android 客户端，采用TX的微信界面来开发Sina的微博客户端

注：高仿的是微信5.0版本的微信界面

#### 最新更新 (2014-12-9)

今天我将原来的代码导入到最新发布的Android Studio 1.0中，修改编辑了某些地方之后，微微博最终可以再次运行啦！

进入[http://fir.im/tinywb/](http://fir.im/tinywb/)下载或者直接扫码下载

![qr](http://hujiaweibujidao.qiniudn.com/qr_tinywb.png)


#### 运行效果

更加详细的界面运行效果看`app运行截图`文件夹或者看[运行视频](http://hujiaweibujidao.qiniudn.com/tinyweibo.mp4)

![intro](http://hujiaweibujidao.qiniudn.com/intro.jpg)

![main](http://hujiaweibujidao.qiniudn.com/main.jpg)


#### 测试账号

`testtinywb@sina.com`   `testtinywb`  (请不要去修改他的密码，谢谢)


#### 主要功能

1.采用OAuth2.0认证登陆；

2.微博功能（发，转发，评论，查看微博列表）；

3.语音微博（科大讯飞接口）；

4.图片处理（Aviary接口）；

5.查找附近玩微博的人！亲，它还是可以摇的哟！

#### Repository中的几个文件夹介绍：

0.TinyWeibo：这个是最新的全部代码，直接导入到Android Studio（1.0版本哟）中应该是没有问题的。

![as](http://hujiaweibujidao.qiniudn.com/tinyweibo_as.png)

你如果要使用的话，需要修改的地方

(1)文件`cn.edu.csu.iteliter.util.ConstantUtil`

`CONSUMER_KEY`的值改为你在微博开发平台中的应用的`APP KEY`

`CONSUMER_SECRET`的值改为你在微博开发平台中的应用的`APP SECRET`

`REDIRECT_URL`的值改为你在微博开发平台中的应用的授权之后的回调地址

`AUTHOR_UID`的值改为你的新浪微博开发者账号的用户ID

其他的常量属性字段你可以自行决定是否修改，比如你有自己的讯飞语音或者Aviary的APP KEY，你可以将里面的KEY改成自己的KEY

(2)文件`weibo4j.util.WeiboConfig`

在`static`静态代码块中修改下某些值

`client_ID`对应你在微博开发平台中的应用的`APP KEY`

`client_SERCRET`对应你在微博开发平台中的应用的`APP SECRET`

`redirect_URI`对应你在微博开发平台中的应用的授权之后的回调地址

```
static {
    props = new Properties();
    props.setProperty("client_ID","146833241");
    props.setProperty("client_SERCRET","a32c1f950c15ebadac9c001416b454f3");
    props.setProperty("redirect_URI","http://www.sina.com");
    props.setProperty("baseURL","https://api.weibo.com/2/");
    props.setProperty("accessTokenURL","https://api.weibo.com/oauth2/access_token");
    props.setProperty("authorizeURL","https://api.weibo.com/oauth2/authorize");
    props.setProperty("rmURL","https://rm.api.weibo.com/2/");
}
```

================ some old stuff ====================

1. Android-Feather：这个是Aviary图片处理工具的核心代码，也是TinyWeibo引用了的library，所以必须要备着<br>
   Aviary 官网：[http://www.aviary.com](http://www.aviary.com)

2. MscDemo：这个是科大讯飞语音提供的demo代码，TinyWeibo只是参考，没有引用<br>
   科大讯飞语音云开放平台：[http://open.voicecloud.cn/](http://open.voicecloud.cn/)

3. TinyWeiboV0.5Doc：这个是最终给评委的包含了注释的全部源代码，也可以直接看TinyWeibo中的，里面的注释依然在

4. weibo---：下面的都是新浪微博的SDK，这个都是2012年12月的最新的SDK，这个项目中我使用两个SDK，包括新浪微博的Android SDK和Java SDK。

##### 需要注意的是两个SDK都不是最新版本的！因为我只是修改了下以前开发的应用，并没有更新SDK，而且以前开发的应用对SDK做过一些改动，因为时间缘故已经忘了改了哪些。


   新浪微博接口文档：[http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2](http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2)

   新浪微博API测试工具：[http://open.weibo.com/tools/console](http://open.weibo.com/tools/console)


#### 作者

2014, Hujiawei, Tsinghua University

email: [hujiawei090807@gmail.com](mailto:hujiawei090807@gmail.com)

blog: [http://hujiaweibujidao.github.io/](http://hujiaweibujidao.github.io/)

========= 帮忙投下票，真的很快的，三次点击就完了 ==========

[点击进入投票网址，给第一排第二个和第三个投下票](https://campaign.gitcafe.com/pingpp-hackathon2014/candidates?page=4)


