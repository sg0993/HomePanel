package com.honeywell.homepanel.common;
/**
 * Created by ellen on 1/24/2017.
 */

public class CommonData {
    /*****************************added by ellen start ************************/
    /***********************left navigation page********************/
    public static final  int LEFT_SELECT_HOME = 0;
    public static final  int LEFT_SELECT_SCENARIOEDIT = 1;
    public static final  int LEFT_SELECT_DEVICEEDIT = 2;
    public static final  int LEFT_SELECT_MESSAGE = 3;
    public static final  int LEFT_SELECT_DIAL = 4;
    public static final  int LEFT_SELECT_SETTING = 5;

    /********************For Top signal ************************/
    public static final String WEATHER_SUNNY = "sunny";
    public static final String TEMPERATURE_DUSTR = "°";
    public static final  String UNHEALTHY = "unhealthy";
    public static final  String ARMSTATUS_ARM = "System Armed";
    public static final  String ARMSTATUS_DISARM = "System Disarmed";

    public static final  int WIFI_CONNECTED = 0;
    public static final  int WIFI_DISCONNECT = 1;
    public static final int SRCURITY_PASSWORD_LENGTH = 6;

    /**********************default scenario******************************/
    public static final  int SCENARIO_HOME = 1;
    public static final  int SCENARIO_AWAY = 2;
    public static final  int SCENARIO_SLEEP = 3;
    public static final  int SCENARIO_WAKEUP = 4;

    /**********************parameter for call******************************/
    public static final  String INTENT_KEY_SCENARIO = "cur_scenario";
    public static final  String INTENT_KEY_CALL_TYPE = "call_type";
    public static final  String INTENT_KEY_UNIT = "unit";

     /******************any call status************************************/
    public static  final int CALL_OUTGOING_NEIGHBOR = 0;/*邻里呼出*/
    public static  final int CALL_INCOMING_NEIGHBOR = 1;/*邻里&PCGuard呼入*/
    public static  final int CALL_CONNECTED_AUDIO_NETGHBOR = 2;/*邻里音频通话*/
    public static  final int CALL_CONNECTED_VIDEO_NETGHBOR = 3;/*邻里视频通话*/
    public static  final int CALL_LOBBY_INCOMMING = 4;/*lobby呼入*/
    public static  final int CALL_LOBBY_CONNECTED = 5;/*lobby接通*/
    /************************added by ellen end**********************************/
    //JSON Key & Valve Define
    public static final String JSON_KEY_MSGID="msgid";

    public static final String JSON_ACTION_KEY="action";
    public static final String JSON_ACTION_VALUE_REQUEST="request";
    public static final String JSON_ACTION_VALUE_RESPONSE="response";
    public static final String JSON_ACTION_VALUE_EVENT="event";

    public static final String JSON_SUBACTION_KEY="subaction";
    public static final String JSON_SUBACTION_VALUE_REGISTER="register";
    public static final String JSON_SUBACTION_VALUE_UNREGISTER="unregister";
    public static final String JSON_SUBACTION_VALUE_HEARTBEAT="heartbeat";
    public static final String JSON_SUBACTION_VALUE_UPGRADE="upgrade";
    public static final String JSON_SUBACTION_VALUE_CALLIN="callin";
    public static final String JSON_SUBACTION_VALUE_CALLOUT="callout";
    public static final String JSON_SUBACTION_VALUE_TAKECALL="talkcall";
    public static final String JSON_SUBACTION_VALUE_ENDCALL="endcall";
    public static final String JSON_SUBACTION_VALUE_RTPCONTOL="rtpctl";
    public static final String JSON_SUBACTION_VALUE_CAPTURE="capture";
    public static final String JSON_SUBACTION_VALUE_DISPATCH="dispatch";

    public static final String JSON_PHONEID_KEY="phoneid";
    public static final String JSON_PHONEID_VALUE_MAIN="0";
    public static final String JSON_PHONEID_VALUE_SUB1="1";
    public static final String JSON_PHONEID_VALUE_SUB2="2";
    public static final String JSON_PHONEID_VALUE_SUB3="3";
    public static final String JSON_PHONEID_VALUE_SUB4="4";
    public static final String JSON_PHONEID_VALUE_SUB5="5";
    public static final String JSON_PHONEID_VALUE_SUB6="6";
    public static final String JSON_PHONEID_VALUE_SUB7="7";
    public static final String JSON_PHONEID_VALUE_SUB8="8";
    public static final String JSON_PHONEID_VALUE_Cloud="C";
	// Standard Call
    public static final  int SENCES_EDIT = 5;
    public static final String CALL_PHONE_MAIN="0";
    public static final String CALL_PHONE_SUB1="1";
    public static final String CALL_PHONE_SUB2="2";
    public static final String CALL_PHONE_SUB3="3";
    public static final String CALL_PHONE_SUB4="4";
    public static final String CALL_PHONE_SUB5="5";
    public static final String CALL_PHONE_SUB6="6";
    public static final String CALL_PHONE_SUB7="7";
    public static final String CALL_PHONE_SUB8="8";
    public static final String CALL_PHONE_Cloud="C";

    public static final int CALL_CHANNEL_MAX=10;// 1 Mainphone, 8 Subphone, 1 Cloud

    public static final int CALL_STATE_UNREGISTER=0;// First State must be zero!!! same as STATE_ANY which defined at StateHandler.java
    public static final int CALL_STATE_IDLE=1;
    public static final int CALL_STATE_CALLIN=2;
    public static final int CALL_STATE_TALKING=3;
    public static final int CALL_STATE_TERMINATED=4;
    public static final int CALL_STATE_PREPARE=5;
    public static final int CALL_STATE_CALLOUT=6;

    public static final String CALL_CMD_INCOMING="incoming";
    public static final String CALL_CMD_TAKECALL="takecall";
    public static final String CALL_CMD_KEEPCALL="keepcall";
    public static final String CALL_CMD_HANGUP="hangup";
    public static final String CALL_CMD_OPENDOOR="opendoor";
    public static final String CALL_CMD_OPENRTP="openrtp";
    public static final String CALL_CMD_CLOSERTP="closertp";
    public static final String CALL_CMD_NETCHANGE="netchange";

    public static final String CALL_TYPE_LOBBY="lobby";
    public static final String CALL_TYPE_CUARD="guard";
    public static final String CALL_TYPE_PANEL="homepanel";
    public static final String CALL_TYPE_DOORCAMERA="ipdoorcamera";
    public static final String CALL_TYPE_IPVDP="ipvdp";

    public static final String CAMERAS_CMD_NEXT="cameras_next";
    public static final String CAMERAS_CMD_FULLSCREEN="cameras_fullscreen";

    public static final int CAMERAS_LIVING = 0;
    public static final int CAMERAS_IPDOOR = 1;
    public static final int CAMERAS_PARKING = 2;

}
