#include <jni.h>

#ifndef DEFPGEN_H
#define DEFPGEN_H
#endif

typedef  enum _pwdtype{
	PWD_TYPE_ALARM,
	PWD_TYPE_ENGINEER,
	PWD_TYPE_REGISTER
}pwdtype;

#define  PWD_LEN_ALARM  (6)
#define  PWD_LEN_ENGINEER  (6)
#define  PWD_LEN_REGISTER  (4)

JNIEXPORT jstring JNICALL Java_com_honeywell_homepanel_nativeapi_DefPGen_get
		(JNIEnv * pEnv, jclass jClass, jint pType);
				  void getAlarmDPwd(char *pwd);
				  void getEngineerDPwd(char *pwd);
				  void getRegisterDPwd(char *pwd);