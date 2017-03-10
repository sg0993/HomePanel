package com.honeywell.homepanel;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.honeywell.homepanel.common.CommonData;
import com.honeywell.homepanel.pbx.PBXService;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {
    private IPBXService service = null;
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.honeywell.homepanel", appContext.getPackageName());
    }

    @Rule
    public final ServiceTestRule RepeatRegisterRule = ServiceTestRule.withTimeout(1000,TimeUnit.SECONDS);

    @Before
    public void setUp() throws Exception {
        IBinder binder=null;
        Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), PBXService.class);
        do {
            binder = RepeatRegisterRule.bindService(intent);
            Thread.sleep(1000);
        }while (binder == null);

        service = IPBXService.Stub.asInterface(binder);
    }

    @Test
    public void RepeatRegisterTest() throws Exception{
        //{    "msgid": "2387r958273894",    "action": "request",    "subaction": "register",    "aliasname": "2",    "password": "1111",    "displayname": "厨房分机",    "devicetype": "HOMEPANEL",    "devicevendor": "honeywell"}
        JSONObject json = new JSONObject();
        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ACTION_KEY,CommonData.JSON_ACTION_VALUE_REQUEST);
        json.put(CommonData.JSON_SUBACTION_KEY,CommonData.JSON_SUBACTION_VALUE_REGISTER);
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_MAIN);
        json.put(CommonData.JSON_PASSWORD_KEY,"1111");
        json.put(CommonData.JSON_DISPLAYNAME_KEY,"厨房分机");
        json.put(CommonData.JSON_DEVICETYPE_KEY,CommonData.JSON_DEVICETYPE_VALUE_HOMEPANEL);
        json.put(CommonData.JSON_DEVICEVENDOR_KEY, CommonData.JSON_DEVICEVENDOR_VALUE_HONEYWELL);

        assertNotNull(service);

        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_MAIN);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB1);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB2);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB3);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB4);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB5);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB6);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB7);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB8);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_Cloud);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_SUBACTION_KEY,CommonData.JSON_SUBACTION_VALUE_UNREGISTER);
        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_MAIN);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB1);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB2);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB3);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB4);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB5);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB6);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB7);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_SUB8);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_Cloud);
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);
    }

    @Test
    public void UnrepeatRegisterTest() throws Exception{
        //{    "msgid": "2387r958273894",    "action": "request",    "subaction": "register",    "aliasname": "2",    "password": "1111",    "displayname": "厨房分机",    "devicetype": "HOMEPANEL",    "devicevendor": "honeywell"}
        JSONObject json = new JSONObject();
        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        json.put(CommonData.JSON_ACTION_KEY,CommonData.JSON_ACTION_VALUE_REQUEST);
        json.put(CommonData.JSON_SUBACTION_KEY,CommonData.JSON_SUBACTION_VALUE_UNREGISTER);
        json.put(CommonData.JSON_ALIASNAME_KEY, CommonData.JSON_ALIASNAME_VALUE_MAIN);
        json.put(CommonData.JSON_PASSWORD_KEY,"1111");
        json.put(CommonData.JSON_DISPLAYNAME_KEY,"厨房分机");
        json.put(CommonData.JSON_DEVICETYPE_KEY,CommonData.JSON_DEVICETYPE_VALUE_HOMEPANEL);
        json.put(CommonData.JSON_DEVICEVENDOR_KEY, CommonData.JSON_DEVICEVENDOR_VALUE_HONEYWELL);

        assertNotNull(service);

        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);

        json.put(CommonData.JSON_MSGID_KEY, java.util.UUID.randomUUID().toString());
        service.JsonSend(":subphoneserver",json.toString());
        Thread.sleep(1000);;
    }


}
