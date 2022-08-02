


-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class android.content.pm.** { *; }
-keep class com.lubuteam.sellsourcecode.supercleaner.lock.model.** { *; }

