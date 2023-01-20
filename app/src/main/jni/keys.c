#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPIKey_getNewsAPIKey(JNIEnv *env, jobject thiz) {
    // TODO: implement getNewsAPIKey()
    return (*env)->NewStringUTF(env, "ODZkOGU1NGUzYWQ4NGZiZDgxMjM0NWNhYzdkODFkMjk=");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_RSSURL_FeedMultiRSS_getRSS2JSONAPIKey(JNIEnv *env, jobject thiz) {
    // TODO: implement getRSS2JSONAPIKey()
    return (*env)->NewStringUTF(env, "a2RramR2a2pkZmZ5ZmJmdWVxd2t3Mmt0Z2locWthY3kzMHhvd203aQ==");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPPAPI_getNewsAPPKey(JNIEnv *env, jobject thiz) {
    // TODO: implement geNewsAPPHeader()
    return (*env)->NewStringUTF(env, "ZjcxYWVmNmYtMWVjYy00MDIwLWI2YzctMGQ0Nzg4OTA1MDAz");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPPAPI_geNewsAPPHeader(JNIEnv *env, jclass clazz) {
    // TODO: implement geNewsAPPHeader()
    return (*env)->NewStringUTF(env, "ZnJvbS13aXRoLWxvdmUtbm90ZWx5c2lh");
}