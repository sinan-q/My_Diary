# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# ==========================================
# Minify but do NOT obfuscate (keeps RB-friendly)
# ==========================================
-dontobfuscate

# Keep line number info for readable crash stack traces
-keepattributes SourceFile,LineNumberTable

# ==========================================
# Gson
# ==========================================
# Gson uses generic type information stored in a class file when working with fields.
# R8 by default removes this. Keep it.
-keepattributes Signature

# Keep Gson-serialized classes (adjust package if needed)
-keep class com.sinxn.mydiary.data.** { *; }

# For Gson's TypeToken
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

# ==========================================
# Room
# ==========================================
# Room generates code at compile time via KSP; standard rules are bundled.
# Keep Room-related classes just in case.
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# ==========================================
# Hilt / Dagger
# ==========================================
# Hilt and Dagger bundle their own ProGuard rules.
# These additional rules prevent edge-case issues.
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep class * extends androidx.lifecycle.ViewModel

# ==========================================
# AndroidX / Compose / Navigation
# ==========================================
# Compose and Navigation bundle their own rules.
# Keep Parcelable/Serializable for navigation args.
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ==========================================
# Biometric
# ==========================================
-keep class androidx.biometric.** { *; }

# ==========================================
# DataStore
# ==========================================
# DataStore Preferences bundle their own rules. No extra rules needed.

# ==========================================
# General
# ==========================================
# Keep annotation info
-keepattributes *Annotation*,InnerClasses,EnclosingMethod