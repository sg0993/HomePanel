LOCAL_PATH := $(call my-dir)

SRC_FILES = defpgen.c
include $(CLEAR_VARS)

LOCAL_MODULE    := defpgen
LOCAL_SRC_FILES := $(SRC_FILES)
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)