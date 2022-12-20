#include <jni.h>
#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_global_AzureSQLServer_getServerName(JNIEnv *env, jobject instance) {

    return (*env)-> NewStringUTF(env, "bmV3c2FwcGFuZHJvaWQuZGF0YWJhc2Uud2luZG93cy5uZXQ=");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_global_AzureSQLServer_getDatabaseName(JNIEnv *env, jobject thiz) {
    // TODO: implement getDatabaseName()
    return (*env)-> NewStringUTF(env, "bmV3c2FwcC1hbmRyb2lk");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_global_AzureSQLServer_getUserName(JNIEnv *env, jobject thiz) {
    // TODO: implement getUserName()
    return (*env)-> NewStringUTF(env, "c3Fsc2VydmVyQG5ld3NhcHBhbmRyb2lk");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_global_AzureSQLServer_getPassword(JNIEnv *env, jobject thiz) {
    // TODO: implement getPassword()
    return (*env)-> NewStringUTF(env, "TG9uZ0AyOTg0ODI5");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_NewsAPI_NewsAPIKey_getNewsAPIKey(JNIEnv *env, jobject thiz) {
    // TODO: implement getNewsAPIKey()
    return (*env)-> NewStringUTF(env, "ODZkOGU1NGUzYWQ4NGZiZDgxMjM0NWNhYzdkODFkMjk=");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_RSSURL_FeedMultiRSS_getRSS2JSONAPIKey(JNIEnv *env, jobject thiz) {
    // TODO: implement getRSS2JSONAPIKey()
    return (*env) -> NewStringUTF(env, "a2RramR2a2pkZmZ5ZmJmdWVxd2t3Mmt0Z2locWthY3kzMHhvd203aQ==");
}