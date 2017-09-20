# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\fcj\softwaretools\adt-bundle-windows-x86_64-20130917\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-optimizationpasses 5                   #指定代码的压缩级别
-dontusemixedcaseclassnames             #包名不混淆大小写
-dontskipnonpubliclibraryclasses        #不去忽略非公共的库类
-dontoptimize                           #优化  不优化输入的类文件
-dontpreverify                          #预校验
-verbose                                #混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*     # 混淆时所采用的算法
-keepattributes *Annotation*    #保护注解


#v4包不混淆
-keep public class * extends android.support.v4.app.Fragment
#eventbus包不混淆
-keep class org.greenrobot.eventbus.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#忽略警告
-ignorewarning

##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
#未混淆的类和成员
-printseeds proguard/seeds.txt
#列出从apk中删除的代码
-printusage proguard/unused.txt
#混淆前后的映射
-printmapping proguard/mapping.txt
########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

#如果引用了v4或者v7包
-dontwarn android.support.**

####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#不混淆反射用到的类
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod,*SerializedName*,*StateHandler*,*ParserMessage*

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#这个主要是在layout 中写的onclick方法android:onclick="onClick"，不进行混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉
#-keepattributes Signature

#移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用，另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
#-assumenosideeffects class android.util.Log {
#    public static *** v(...);
#    public static *** i(...);
#    public static *** d(...);
#    public static *** w(...);
#    public static *** e(...);
#}

# volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* {*;}
-keep class com.android.volley.Request$* {*;}
-keep class com.android.volley.RequestQueue$* {*;}
-keep class com.android.volley.toolbox.HurlStack$* {*;}
-keep class com.android.volley.toolbox.ImageLoader$* {*;}
-keep class org.apache.http.** {*;}

-keep interface com.honeywell.homepanel.TinyMachine.** {*;}
-keep class com.honeywell.homepanel.TinyMachine.** {*;}
#############################################################################################
########################                 以上通用           ##################################
#############################################################################################

#######################     常用第三方模块的混淆选项         ###################################
#gson
#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
-keepattributes Signature
-keepattributes StateHandler
-keepattributes Retention
-keepattributes Target
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keepattributes *StateHandler*,InnerClasses  # 保留Annotation不混淆
#mob
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class m.framework.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

######引用的其他Module可以直接在app的这个混淆文件里配置

# 如果使用了Gson之类的工具要使被它解析的JavaBean类即实体类不被混淆。
-keep class com.matrix.app.entity.json.** { *; }
-keep class com.matrix.appsdk.network.model.** { *; }

#####混淆保护自己项目的部分代码以及引用的第三方jar包library#######
#如果在当前的application module或者依赖的library module中使用了第三方的库，并不需要显式添加规则
#-libraryjars xxx
#添加了反而有可能在打包的时候遭遇同一个jar多次被指定的错误，一般只需要添加忽略警告和保持某些class不被混淆的声明。
#以libaray的形式引用了开源项目,如果不想混淆 keep 掉，在引入的module的build.gradle中设置minifyEnabled=false
-keep class com.nineoldandroids.** { *; }
-keep interface com.nineoldandroids.** { *; }
-dontwarn com.nineoldandroids.**

# observablescrollview：tab fragment
-keep class com.github.ksoichiro.** { *; }
-keep interface com.github.ksoichiro.** { *; }
-dontwarn com.github.ksoichiro.**
-keep class com.honeywell.homepanel.Utils.** { *; }
-keep class com.honeywell.homepanel.nativeapi.** { *; }
-keep class com.honeywell.homepanel.ipdc.IPDoorCameraInterface { *; }
-keep class com.honeywell.homepanel.cloud.** { *; }
-keep class android.net.ethernet.** { *; }
-keep class com.a64.a64board.** { *; }
-keep class com.honeywell.homepanel.ui.uicomponent.** { *; }
-keep class com.honeywell.homepanel.community.** { *; }
-keep class com.honeywell.homepanel.common.** { *; }
-keep class com.sosea.xmeeting.** { *; }
-keep class com.honeywell.homepanel.call.CallService.** { *; }
-keep class com.honeywell.homepanel.call.PBXCallManager.** { *;}

-keep class com.honeywell.homepanel.call.constant.** { *; }
-keep class com.honeywell.homepanel.call.legacy.** { *; }
-keep class com.honeywell.homepanel.call.login.** { *; }
-keep class com.honeywell.homepanel.call.message.** { *; }

#-keep class com.honeywell.homepanel.call.talk.** { *;}
-keep class com.honeywell.homepanel.call.util.** { *; }
-keep class com.honeywell.homepanel.cloud.** { *; }
-keep class com.honeywell.homepanel.control.** { *; }
-keep class com.honeywell.homepanel.deviceadapter.** { *; }
-keep class com.honeywell.homepanel.ipdc.** { *; }
-keep class com.honeywell.homepanel.miscservices.** { *; }
-keep class com.honeywell.homepanel.pbx.** { *; }
-keep class com.honeywell.homepanel.sensingservices.** { *; }
-keep class com.honeywell.homepanel.subphoneservice.** { *; }
-keep class com.honeywell.homepanel.subphoneuiservice.** { *; }
#-keep class com.honeywell.homepanel.TinyMachine.** { *; }
-keep class com.honeywell.homepanel.ui.** { *; }
-keep class com.honeywell.homepanel.pbx.** { *; }
-keep class com.honeywell.homepanel.upgrade.** { *; }
-keep class com.honeywell.homepanel.watchdog.** { *; }