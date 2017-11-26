# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\ovi\android_all\android-sdk\android-sdk/tools/proguard/proguard-android.txt
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
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
-dontwarn com.android.volley.toolbox.**
-dontwarn roboguice.**
-dontwarn com.github.mikephil.**
-keep public class android.util.FloatMath
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-dontwarn javax.xml.stream.events.**
-dontwarn org.simpleframework.xml.stream.**
-libraryjars <java.home>/lib/rt.jar(java/**,javax/**)
-keepattributes Signature
-ignorewarnings
-keepattributes *Annotation*


## Glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#    **[] $VALUES;
#    public *;
#}
## support-v7-appcompat
#-keep public class android.support.v7.widget.** { *; }
#-keep public class android.support.v7.internal.widget.** { *; }
#-keep public class android.support.v7.internal.view.menu.** { *; }
#-keep public class * extends android.support.v4.view.ActionProvider {
#    public <init>(android.content.Context);
#}
## support-design
#-dontwarn android.support.design.**
#-keep class android.support.design.** { *; }
#-keep interface android.support.design.** { *; }
#-keep public class android.support.design.R$* { *; }