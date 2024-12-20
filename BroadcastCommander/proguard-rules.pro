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

-keep @io.github.airdaydreamers.broadcastcommander.utils.CommandCallbackImplementation class * { *; }
-keep class io.github.airdaydreamers.broadcastcommander.utils.CommandCallbackImplementation
-keep class io.github.airdaydreamers.broadcastcommander.CommandReceiver
-keep class io.github.airdaydreamers.broadcastcommander.CallbackManager

-dontwarn io.github.airdaydreamers.broadcastcommander.data.repositories.**
-dontwarn io.github.airdaydreamers.broadcastcommander.data.executors.**
-dontwarn io.github.airdaydreamers.broadcastcommander.domain.repositories.**
-dontwarn io.github.airdaydreamers.broadcastcommander.domain.services.**
-dontwarn io.github.airdaydreamers.broadcastcommander.domain.usecases.**