#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPIKey_getNewsAPIKey(JNIEnv *env, jobject thiz) {
    // TODO: implement getNewsAPIKey()
    return (*env)->NewStringUTF(env, "ODZkOGU1NGUzYWQ4NGZiZDgxMjM0NWNhYzdkODFkMjk=");
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

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_RSS2JSON_FeedMultiRSS_getRSS2JSONAPIKey(JNIEnv *env,
                                                                         jobject thiz) {
    return (*env)->NewStringUTF(env, "a2RramR2a2pkZmZ5ZmJmdWVxd2t3Mmt0Z2locWthY3kzMHhvd203aQ==");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_RecycleViewAdapter_ListRSSAdapter_getSecretKey(JNIEnv *env,
                                                                                jobject thiz) {
    return (*env)->NewStringUTF(env,
                                "YzNBOWNpWnpkRDB5TURJekxUQXlMVEF4VkRFMU9qSXhPalUzV2laelpUMHlNREl6TFRBeUxUQXhWREl6T2pJeE9qVTNXaVp6YVhBOU1DNHdMakF1TUMweU5UVXVNalUxTGpJMU5TNHlOVFVtYzNCeVBXaDBkSEJ6Sm5OMlBUSXdNakV0TURZdE1EZ21jM0k5WXlaemFXYzllVlI1Y2xKQ1NVdE5URWt4YWpsaGRsSTFVekJTZURGVWFYZzFlVXAwUTJ0MFRFVnJZU1V5UWxWTVFUbFJKVE5FCg==");
}