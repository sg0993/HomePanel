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
    public static final String JSON_MSGID_KEY ="msgid";

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
    public static final String JSON_SUBACTION_VALUE_CALLMUTE = "mute";
    public static final String JSON_SUBACTION_VALUE_OPENDOOR = "opendoor";
    public static final String JSON_SUBACCTION_VALUE_VIDEOAUTH = "videoauth";
    public static final String JSON_SUBACTION_VALUE_CALLELEVATOR = "callelevator";
    public static final String JSON_SUBACTION_VALUE_ELEVATORAUTH = "elevatorauth";
    public static final String JSON_SUBACTION_VALUE_CALLTERMINATED = "callterminated";
    public static final String JSON_SUBACTION_VALUE_CALLACTIVED = "callactived";
    public static final String JSON_SUBACTION_VALUE_ELEVATORINFO = "elevatorinfo";
    public static final String JSON_SUBACTION_VALUE_IP2ALIAS="ip2alias";
    public static final String JSON_SUBACTION_VALUE_ALIAS2IP="alias2ip";

    public static final String JSON_VIDEORATIO_KEY = "videoratio";
    public static final String JSON_VIDEORATIO_VALUE_DEFAULT = "320*240";

    public static final String JSON_SWICHSTATUS_KEY = "switchstatus";
    public static final String JSON_SWICHSTATUS_VALUE_ON = "on";
    public static final String JSON_SWICHSTATUS_VALUE_OFF= "off";

    public static final String JSON_REGISTERSTATUS_KEY = "registerstatus";
    public static final String JSON_REGISTERSTATUS_VALUE_REGISTERED = "registered";
    public static final String JSON_REGISTERSTATUS_VALUE_UNREGISTERED = "unregistered";

    public static final String JSON_DIRECTION_KEY = "direction";
    public static final String JSON_DIRECTION_VALUE_OUT = "out";
    public static final String JSON_DIRECTION_VALUE_IN = "in";
    public static final String JSON_DIRECTION_VALUE_DOWN = "down";
    public static final String JSON_DIRECTION_VALUE_UP = "up";

    public static final String JSON_ELEVATORID_KEY = "elevatorid";
    public static final String JSON_FLOORNO_KEY = "floorno";

    public static final String JSON_PHONEIP_KEY="phoneip";

    public static final String JSON_PASSWORD_KEY="password";

    public static final String JSON_CALLTYPE_KEY="calltype";
    public static final String JSON_CALLTYPE_VALUE_LOBBY="lobby";
    public static final String JSON_CALLTYPE_VALUE_IPVDP="ipvdp";
    public static final String JSON_CALLTYPE_VALUE_DOORCAMERA="doorcamera";
    public static final String JSON_CALLTYPE_VALUE_GUARD="guard";
    public static final String JSON_CALLTYPE_VALUE_HOMEPANEL="homepanel";
    public static final String JSON_CALLTYPE_VALUE_OFFICE="office";
    public static final String JSON_CALLTYPE_VALUE_NEIGHBOUR="neighbour";
    public static final String JSON_CALLTYPE_VALUE_INNER="inner";

    public static final String JSON_ALIASNAME_KEY="aliasname";
    public static final String JSON_ALIASNAME_VALUE_MAIN="0";
    public static final String JSON_ALIASNAME_VALUE_SUB1="1";
    public static final String JSON_ALIASNAME_VALUE_SUB2="2";
    public static final String JSON_ALIASNAME_VALUE_SUB3="3";
    public static final String JSON_ALIASNAME_VALUE_SUB4="4";
    public static final String JSON_ALIASNAME_VALUE_SUB5="5";
    public static final String JSON_ALIASNAME_VALUE_SUB6="6";
    public static final String JSON_ALIASNAME_VALUE_SUB7="7";
    public static final String JSON_ALIASNAME_VALUE_SUB8="8";
    public static final String JSON_ALIASNAME_VALUE_Cloud="C";
    public static final String JSON_ALIASNAME_VALUE_FRONTDOOR="frontdoor";
    public static final String JSON_ALIASNAME_VALUE_BACKDOOR="backdoor";

    public static final String JSON_DISPLAYNAME_KEY="displayname";
    public static final String JSON_DISPLAYNAME_VALUE_OFFICE="office";
    public static final String JSON_DISPLAYNAME_VALUE_GUARD="guard";
    public static final String JSON_DISPLAYNAME_VALUE_LOBBY="lobby";
    public static final String JSON_DISPLAYNAME_VALUE_FRONTDOOR="frontdoor";
    public static final String JSON_DISPLAYNAME_VALUE_BACKDOOR="backdoor";

    public static final String JSON_DEVICETYPE_KEY="devicetype";
    public static final String JSON_DEVICETYPE_VALUE_HOMEPANEL="homepanel";
    public static final String JSON_DEVICETYPE_VALUE_CLOUD="cloud";

    public static final String JSON_DEVICEVENDOR_KEY="devicevendor";
    public static final String JSON_DEVICEVENDOR_VALUE_HONEYWELL="honeywell";

    public static final String JSON_UUID_KEY="uuid";

    public static final String JSON_ERRORCODE_KEY="errorcode";
    public static final String JSON_ERRORCODE_VALUE_FAIL="-1";
    public static final String JSON_ERRORCODE_VALUE_OK="0";
    public static final String JSON_ERRORCODE_VALUE_KEYMISS="1";
    public static final String JSON_ERRORCODE_VALUE_SUBPHONE_UNREGISTER="2";
    public static final String JSON_ERRORCODE_VALUE_SUBPHONE_BUSY="3";

    public static final String JSON_VIDEOPORT_KEY="videoport";
    public static final String JSON_AUDIOPORT_KEY="audioport";

    public static final String JSON_VIDEOSTATUS_KEY="videostatus";
    public static final String JSON_VIDEOSTATUS_VALUE_ON="on";
    public static final String JSON_VIDEOSTATUS_VALUE_OFF="off";

    public static final String JSON_AUDIOTATUS_KEY="audiostatus";
    public static final String JSON_AUDIOTATUS_VALUE_ON="on";
    public static final String JSON_AUDIOTATUS_VALUE_OFF="off";
    
    public static final String JSON_LOCALIP_KEY="localip";
    
    /* reference android mediacodec defined */
    public static final String JSON_VIDEOCODEC_KEY="videocodec";
    public static final String JSON_VIDEOCODEC_VALUE_H264="video/avc";
    public static final String JSON_VIDEOCODEC_VALUE_MP4V="video/mp4v-es";
    public static final String JSON_VIDEOCODEC_VALUE_VP8="video/x-vnd.on2.vp8";
    public static final String JSON_VIDEOCODEC_VALUE_VP9="video/x-vnd.on2.vp9";
    public static final String JSON_VIDEOCODEC_VALUE_3GPP="video/3gpp";
    
    public static final String JSON_VIDEOWIDTH_KEY="videowidth";
    
    public static final String JSON_VIDEOHEIGHT_KEY="videoheight";
    
    public static final String JSON_VIDEOFRAME_KEY="videoframe";
    public static final String JSON_VIDEOFRAME_VALUE_30="30";
    public static final String JSON_VIDEOFRAME_VALUE_25="25";
    public static final String JSON_VIDEOFRAME_VALUE_20="20";
    /* reference android mediacodec defined */
    public static final String JSON_AUDIOCODEC_KEY="audiocodec";
    public static final String JSON_AUDIOCODEC_VALUE_ALAW="audio/g711-alaw";
    public static final String JSON_AUDIOCODEC_VALUE_ULAW="audio/g711-ulaw";
    
    public static final String JSON_AUDIOFRAME_KEY="audioframe";
    public static final String JSON_AUDIOFRAME_VALUE_8000="8000Hz";
    public static final String JSON_AUDIOFRAME_VALUE_11025="11025Hz";
    public static final String JSON_AUDIOFRAME_VALUE_22050="22050Hz";
    public static final String JSON_AUDIOFRAME_VALUE_44100="44100Hz";
    public static final String JSON_AUDIOFRAME_VALUE_48000="48000Hz";

    public static final String JSON_CALLEEVIDEOPORT_KEY ="calleevideoport";
    public static final String JSON_CALLERVIDEOPORT_KEY ="callervideoport";
    public static final String JSON_VIDEOPORT_VALUE_DEFAULT = "2000";

    public static final String JSON_CALLEEAUDIOPORT_KEY ="calleeaudioport";
    public static final String JSON_CALLERAUDIOPORT_KEY ="calleraudioport";
    public static final String JSON_AUDIOPORT_VALUE_DEFAULT = "3000";

    public static final String JSON_RTPIP_KEY = "rtpip";

    public static final String JSON_LOOPID_KEY="loop";

    public static final String JSON_REASON_KEY="reason";

    public static final String JSON_FROMIP_KEY="fromip";
    public static final String JSON_FROMALIASNAME_KEY="fromalias";
    public static final String JSON_TOIP_KEY="toip";
    public static final String JSON_TOALIASNAME_KEY ="toalias";
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
    public static final int CALL_STATE_BUSY=7;

    public static final String CALL_CMD_INCOMING="incoming";
    public static final String CALL_CMD_TAKECALL="takecall";
    public static final String CALL_CMD_KEEPCALL="keepcall";
    public static final String CALL_CMD_HANGUP="hangup";
    public static final String CALL_CMD_OPENDOOR="opendoor";
    public static final String CALL_CMD_OPENRTP="openrtp";
    public static final String CALL_CMD_CLOSERTP="closertp";
    public static final String CALL_CMD_NETCHANGE="netchange";


    public static final String CAMERAS_CMD_NEXT="cameras_next";
    public static final String CAMERAS_CMD_FULLSCREEN="cameras_fullscreen";

    public static final int CAMERAS_LIVING = 0;
    public static final int CAMERAS_IPDOOR = 1;
    public static final int CAMERAS_PARKING = 2;
    public static final String CALL_DIRECTION_CALLED ="";
    public static final String CALL_DIRECTION_CALLING = "";

}
