# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/idyll/Library/Android/sdk/tools/proguard/proguard-android.txt
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
-dontpreverify #混淆前认证，可去掉加快混淆速度
-repackageclasses ''
-allowaccessmodification
-verbose #混淆的log 帮助排错

#-optimizations !code/simplification/arithmetic
#-dontshrink    不压缩输入的类文件
#-dontoptimize  不优化输入的类文件
#-dontobfuscate 不混淆输入的类文件
-ignorewarnings
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable, *Annotation*
-keepattributes Exceptions, Signature, InnerClasses

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.app.Activity { public void *(android.view.View); }
-keepclassmembers class android.support.v4.app.Fragment { *** getActivity(); public *** onCreate(); public *** onCreateOptionsMenu(...); }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep class android.support.v4.widget.** { *; }
-keep class android.support.v4.view.** { *; }
-keep class android.widget.** { *; }
-keep class android.view.** { *; }

-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep class android.support.v7.widget.** { *; }
-keep class android.support.v7.view.** { *; }
-keep public class * extends android.support.v7.**

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#-keep class class com.sponia.foundationmoudle.net.** { *; }
-keep class class com.sponia.foundationmoudle.bean.** { *; }
-keep public class * implements java.io.Serializable {*;}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends com.google.android.maps.MapActivity

-dontwarn com.squareup.okhttp.**

-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

-keep class com.alibaba.fastjson.** { *; }

-keep class org.xmlpull.v1.** { *; }
-keep class org.kxml2.io.** {*;}

-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-dontwarn com.baidu.**


# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
# nineoldandroids
-keep interface com.nineoldandroids.view.** { *; }
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *; }
# support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
# support-design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }