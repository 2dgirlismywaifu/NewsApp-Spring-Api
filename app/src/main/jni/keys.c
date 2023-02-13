/*Copyright By @2dgirlismywaifu (2023) .
*
*Licensed under the Apache License, Version 2.0 (the "License");
*you may not use this file except in compliance with the License.
*You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing, software
*distributed under the License is distributed on an "AS IS" BASIS,
*WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*See the License for the specific language governing permissions and
*limitations under the License.
*/
#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPIKey_getNewsAPIKey(JNIEnv *env, jobject thiz) {
    // TODO: implement getNewsAPIKey()
    //your NewsAPI key
    return (*env)->NewStringUTF(env, "ODZkOGU1NGUzYWQ4NGZiZDgxMjM0NWNhYzdkODFkMjk=");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPPAPI_getNewsAPPKey(JNIEnv *env, jobject thiz) {
    // TODO: implement geNewsAPPHeader()
    //your API key Spring Boot Services
    return (*env)->NewStringUTF(env, "ZjcxYWVmNmYtMWVjYy00MDIwLWI2YzctMGQ0Nzg4OTA1MDAz");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_Retrofit_NewsAPPAPI_geNewsAPPHeader(JNIEnv *env, jclass clazz) {
    // TODO: implement geNewsAPPHeader()
    //your API Header Spring Boot Services
    return (*env)->NewStringUTF(env, "ZnJvbS13aXRoLWxvdmUtbm90ZWx5c2lh");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_RSS2JSON_FeedMultiRSS_getRSS2JSONAPIKey(JNIEnv *env,
                                                                         jobject thiz) {
    //Your RSS2JSON Key
    return (*env)->NewStringUTF(env, "a2RramR2a2pkZmZ5ZmJmdWVxd2t3Mmt0Z2locWthY3kzMHhvd203aQ==");
}

JNIEXPORT jstring JNICALL
Java_com_notmiyouji_newsapp_java_RecycleViewAdapter_ListRSSAdapter_getSecretKey(JNIEnv *env,jobject thiz) {
    //Your secret key to access image save from Azure Blob Storage
    return (*env)->NewStringUTF(env,
                                "YzNBOWNpWnpkRDB5TURJekxUQXlMVEE1VkRFMk9qTTFPak0zV2laelpUMHlNREkwTFRBeUxURXdWREF3T2pNMU9qTTNXaVp6YVhBOU1DNHdMakF1TUMweU5UVXVNalUxTGpJMU5TNHlOVFVtYzNCeVBXaDBkSEJ6Sm5OMlBUSXdNakV0TURZdE1EZ21jM0k5WXlaemFXYzlNVUkxY2tGQ05UZDRXa1kwYjBGTVEwTjJUMlV3Y2tsVWRFVnVWVGgxVjA0eE5XcHVPWEVsTWtaSWN6YzRKVE5F");
}