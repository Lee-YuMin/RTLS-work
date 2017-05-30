#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_hatio_insidepositioning_Odometer_getJNIString(JNIEnv *env, jobject instance) {

    // TODO
    jstring returnValue = "Hello From Jni";

    return (*env)->NewStringUTF(env, returnValue);
}