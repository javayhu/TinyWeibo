
TinyWeibo

1.导入到AS中，处理以下问题

(1)FeatherSystemReceiver.java 部分内容乱码 （在UTF-8编码情况下）


(2)删除dialog_left_bg.9.png文件

Error:9-patch image /Users/hujiawei/AndroidStudioProjects/TinyWeibo/tinyWeiboV05doc/src/main/res/drawable-hdpi/dialog_left_bg.9.png malformed.


(3)删除title_bar.9.png文件，用titlebar_bg.0.png代替

(4)AndroidFeather项目中的Manifest文件删除下面内容

```
android:debuggable="true"     
android:icon="@drawable/feather_icon"      
android:label="Aviary"    
```

在TinyWeibo项目的Manifest文件中的FeatherActivity中加入

```
tools:replace="android:theme"
```

同时删除它的两项设置

```
android:screenOrientation="unspecified"
android:configChanges="orientation|keyboardHidden|screenSize"
```

2.开发者账号密码

itelite2012@sina.com itelite

App Key：146833241

App Secret：a32c1f950c15ebadac9c001416b454f3



