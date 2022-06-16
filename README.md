# ipsdk
Step 1. Add the JitPack repository to your build file
allprojects {
  repositories { maven { url 'https://jitpack.io' }
}

Step 2. Add the dependency
dependencies { implementation 'com.github.1611196687:ipsdk:v1.0.3' }

在BaseApplication初始化
IpUtil.getInstance().init(baseApp);
在获取定位权限之后调用方法
IpUtil.getInstance().loadData(this);
IpUtil.getInstance().channel("");(当前app的渠道标识)

添加res/xml/network_security_config.xml文件名可自拟
在AndroidManifest.xml中
 <application android:networkSecurityConfig="@xml/network_security_config"></application>
      
network_security_config.xml内容  <base-config cleartextTrafficPermitted="true" />
