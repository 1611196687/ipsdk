# ipsdk
Step 1. Add the JitPack repository to your build file
allprojects {
  repositories { maven { url 'https://jitpack.io' }
}

Step 2. Add the dependency
dependencies { implementation 'com.github.1611196687:ipsdk:v1.0.4' }

在BaseApplication初始化
IpUtil.getInstance().init(baseApp);

填写当前app的渠道标识
IpUtil.getInstance().channel("");
填写密钥内容
IpUtil.getInstance().secretKey("");
在获取定位权限之后调用方法
IpUtil.getInstance().loadData(this);

Android P以后网络访问安全策略升级，限制了非加密的流量请求
添加res/xml/network_security_config.xml文件名可自拟
在AndroidManifest.xml中引用
