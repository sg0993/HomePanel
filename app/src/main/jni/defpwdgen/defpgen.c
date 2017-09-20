#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include "defpgen.h"
#include <android/log.h>
#define LOG_TAG "defpgen"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

JNIEXPORT jstring JNICALL Java_com_honeywell_homepanel_nativeapi_DefPGen_get
		(JNIEnv * pEnv, jclass jClass, jint pType){
	char *pPwd = NULL;
	int len = 0;
	LOGD("Java_com_honeywell_homepanel_nativeapi_DefPGen_get() 111111111111\n");
	switch(pType){
		case PWD_TYPE_ALARM:
			len = PWD_LEN_ALARM;
			break;
		case PWD_TYPE_ENGINEER:
			len = PWD_LEN_ENGINEER;
			break;
		case PWD_TYPE_REGISTER:
			len = PWD_LEN_REGISTER;
			break;
		default:
			break;
	}
	if(len == 0){
		return NULL;
	}
	len += 1;
	pPwd = (char*)malloc(len);
	if(NULL == pPwd){
		return NULL;
	}
	memset(pPwd,'\0',len);
	switch(pType){
		case PWD_TYPE_ALARM:
			getAlarmDPwd(pPwd);
			break;
		case PWD_TYPE_ENGINEER:
			getEngineerDPwd(pPwd);
			break;
		case PWD_TYPE_REGISTER:
			getRegisterDPwd(pPwd);
			break;
		default:
			break;
	}
	jstring retStr = (*pEnv)->NewStringUTF(pEnv,pPwd);
	LOGD("Java_com_honeywell_homepanel_nativeapi_DefPGen_get() pPwd:%s222222222\n",pPwd);
	free((void*)pPwd);
	pPwd = NULL;
    return retStr;
}

void getAlarmDPwd(char *pwd){
	if(!pwd){
        LOGD("getAlarmDPwd() pwd:%s aaaaa\n", pwd);
		return;
	}
	for (int i = 0; i < PWD_LEN_ALARM; ++i) {
		char str[2]={'\0'};
        LOGD("getAlarmDPwd() 1111111\n");
        sprintf(str, "%d",i+1);
        LOGD("getAlarmDPwd()str：%s 22222222\n",str);
		if(i == 0){
			strcpy(pwd,str);
            LOGD("getAlarmDPwd()str：%s 33333\n",str);
		}
		else{
			strcat(pwd,str);
            LOGD("getAlarmDPwd()str：%s 4444444\n",str);
		}
	}
	LOGD("getAlarmDPwd() pwd:%s 5555555555\n", pwd);
}

void getEngineerDPwd(char *pwd){
	char str[PWD_LEN_ENGINEER]={'\0'};
	int a = 85213;
	if(!pwd){
		return;
	}
	strcpy(pwd,"0");
    sprintf(str, "%d",a);
	strcat(pwd,str);
	LOGD("getEngineerDPwd() pwd:%s 111111111111\n", pwd);
}

void getRegisterDPwd(char *pwd){
	if(!pwd){
		return;
	}
	for (int i = 0; i < PWD_LEN_REGISTER; ++i) {
		char str[2]={'\0','\0'};
        sprintf(str,"%d",i-i);
		if(i == 0){
			strcpy(pwd,str);
		}
		else{
			strcat(pwd,str);
		}
	}
	LOGD("getRegisterDPwd() pwd:%s 111111111111\n", pwd);
}