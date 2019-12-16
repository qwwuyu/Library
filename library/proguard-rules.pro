# 无混淆
# com.scwang.smartrefresh:SmartRefreshLayout:1.1.0-alpha-5
# com.contrarywind:wheelview:4.0.7
# com.jakewharton:butterknife:7.0.1

# ------------------------ rxjava2 rxandroid2 ------------------------
# https://github.com/ReactiveX/RxJava https://github.com/ReactiveX/RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent

# ------------------------ rxlifecycle2 ------------------------
-keep class com.trello.rxlifecycle.** { *; }
-keep interface com.trello.rxlifecycle.** { *; }

# ------------------------ okhttp3 ------------------------
# https://github.com/square/okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# ------------------------ retrofit 2 ------------------------
# https://github.com/square/retrofit
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions

# ------------------------ glide 4.10.0 ------------------------
# https://github.com/bumptech/glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# ------------------------ gson 2.8.5------------------------
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# ------------------------ org.greenrobot:greendao:3.2.2 ------------------------
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

# ------------------------ eventbus ------------------------
#-keepattributes *Annotation*
#-keepclassmembers class * {
#    @org.greenrobot.eventbus.Subscribe <methods>;
#}
#-keep enum org.greenrobot.eventbus.ThreadMode { *; }
##Only required if you use AsyncExecutor
#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#    <init>(java.lang.Throwable);
#}

# ------------------------ zxing ------------------------
#-keep class com.google.zxing.{ *;}
#-dontwarn com.google.zxing.**

# ------------------------ calendarview 3.6.2------------------------
# https://github.com/huanghaibin-dev/CalendarView
#-keep class com.haibin.calendarview.**{*;}
#-keep public class * extends com.haibin.calendarview.MonthView {
#    public <init>(android.content.Context);
#}
#-keep public class * extends com.haibin.calendarview.WeekBar {
#    public <init>(android.content.Context);
#}
#-keep public class * extends com.haibin.calendarview.YearView {
#    public <init>(android.content.Context);
#}
#-keep public class * extends com.haibin.calendarview.WeekView {
#    public <init>(android.content.Context);
#}

# ------------------------ okdownload ------------------------
#-keepnames class com.liulishuo.okdownload.core.connection.DownloadOkHttp3Connection
#-keep class com.liulishuo.okdownload.core.breakpoint.BreakpointStoreOnSQLite {
#        public com.liulishuo.okdownload.core.breakpoint.DownloadStore createRemitSelf();
#        public com.liulishuo.okdownload.core.breakpoint.BreakpointStoreOnSQLite(android.content.Context);
#}

#------------------------ bugly ------------------------
#-dontwarn com.tencent.bugly.**
#-keep public class com.tencent.bugly.**{*;}

# ------------------------ AMap3DMap_6.9.2_AMapNavi_6.9.0_AMapLocation_4.7.0_20190710.jar ------------------------
##AMap3DMap
#-keep class com.amap.api.maps.**{*;}
#-keep class com.autonavi.**{*;}
#-keep class com.amap.api.trace.**{*;}
##AMapNavi
#-keep class com.amap.api.navi.**{*;}
#-keep class com.autonavi.**{*;}
##AMapLocation
#-keep class com.amap.api.location.**{*;}
#-keep class com.amap.api.fence.**{*;}
#-keep class com.autonavi.aps.amapapi.model.**{*;}
##内置语音 V5.6.0之后
#-keep class com.alibaba.idst.nls.** {*;}
#-keep class com.google.**{*;}
#-keep class com.nlspeech.nlscodec.** {*;}

# ------------------------ butterknife ------------------------
#-keep class butterknife.** { *; }
#-dontwarn butterknife.butterknife.**
#-keep class **$$ViewBinder { *; }
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}