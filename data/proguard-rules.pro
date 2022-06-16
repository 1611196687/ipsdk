# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 工具集
-keep class com.blankj.utilcode.** { *; }
-keepclassmembers class com.blankj.utilcode.** { *; }
-dontwarn com.blankj.utilcode.**

#fastjson
-keepattributes Signature
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

##------------------------------百度地图--------------------------------------------------------
#
#-dontskipnonpubliclibraryclassmembers
#-useuniqueclassmembernames
#-keeppackagenames
#-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,Synthetic,EnclosingMethod
#-keepparameternames
#
#-keep class com.baidu.lbsapi.panoramaview.ImageMarker{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.tools.CoordinateConverter{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.tools.Point{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.model.BaiduPanoData{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.model.BaiduPoiPanoData{
#    public protected <fields>;
#    public protected <methods>;
#}
#-keep class com.baidu.lbsapi.model.StatisticsEvent{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.panoramaview.TextMarker{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.panoramaview.StatisticsCallback{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#
#-keep class com.baidu.lbsapi.panoramaview.OnTabMarkListener{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.panoramaview.PanoramaRequest{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.panoramaview.PanoramaView{
#                                  public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.panoramaview.PanoramaViewListener{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.BMapManager{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.lbsapi.MKGeneralListener{
#    public protected <fields>;
#    public protected <methods>;
#}
#
#-keep class com.baidu.pano.platform.plugin.PluginManager{
#	public static *;
#	public *;
#}
#
#-keep class com.baidu.pano.platform.plugin.PluginHttpExecutor{*;}
#
#-keep interface com.baidu.pano.platform.plugin.indooralbum.**{ *; }
#-keep class com.baidu.pano.platform.plugin.indooralbum.**{ *; }
#
#-keep class com.baidu.pano.platform.comjni.** { *; }
#-keep class com.baidu.lbsapi.panoramaview.PanoramaView$* { *; }
#-keep class com.baidu.lbsapi.panoramaview.PanoramaRequest$* { *; }
#-keep class com.baidu.lbsapi.tools.CoordinateConverter$* { *; }
#
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#-keepclasseswithmembernames class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembernames class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#-dontwarn org.apache.commons.logging.**
#-ignorewarnings
#-keep class * {
#public private *;
#}


-keep class repeackage.com.uodis.opendevice.aidl.** { *; }
-keep interface repeackage.com.uodis.opendevice.aidl.** { *; }
-keep class repeackage.com.asus.msa.SupplementaryDID.** { *; }
-keep interface repeackage.com.asus.msa.SupplementaryDID.** { *; }
-keep class repeackage.com.bun.lib.** { *; }
-keep interface repeackage.com.bun.lib.** { *; }
-keep class repeackage.com.heytap.openid.** { *; }
-keep interface repeackage.com.heytap.openid.** { *; }
-keep class repeackage.com.samsung.android.deviceidservice.** { *; }
-keep interface repeackage.com.samsung.android.deviceidservice.** { *; }
-keep class repeackage.com.zui.deviceidservice.** { *; }
-keep interface repeackage.com.zui.deviceidservice.** { *; }
-keep class repeackage.com.coolpad.deviceidsupport.** { *; }
-keep interface repeackage.com.coolpad.deviceidsupport.** { *; }
-keep class repeackage.com.android.creator.** { *; }
-keep interface repeackage.com.android.creator.** { *; }
-keep class repeackage.com.google.android.gms.ads.identifier.internal.** { *; }
-keep interface repeackage.com.google.android.gms.ads.identifier.internal.* { *; }
