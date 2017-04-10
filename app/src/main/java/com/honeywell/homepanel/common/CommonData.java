package com.honeywell.homepanel.common;

import android.os.Environment;

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
    public static final int CHANGE_PASSWORD_LENGTH = 4;
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
	public static final  String INTENT_CARD_ALIAS = "card_alias";
    public static final  String INTENT_KEY_CARD_TYPE = "card_type";
    public static final  String INTENT_KEY_CARD_FRAGMENT = "card_fragment";
	public static  final String CARD_PERMANENT = "permanent";
	public static  final String CARD_TEMORARY = "temorary";
    public static  final int CARD_NO = 8;
	public static  final int CARD_LIST = 9;
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
    public static final String JSON_SUBACTION_VALUE_AUTOTAKECALL = "autotakecall";
    public static final String JSON_SUBACTION_VALUE_RELAYCALL = "relaycall";
    public static final String JSON_SUBACTION_VALUE_STARTSESSION = "startsession";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONMODULEGET = "extensionmoduleget";
    public static final String JSON_SUBACTION_VALUE_GETSCENARIOLIST = "getscenariolist";
    public static final String JSON_SUBACTION_VALUE_SWITCHSCENARIO = "switchscenario";
    public static final String JSON_SUBACTION_VALUE_GETSCENARIOCONFIG = "getscenarioconfig";
    public static final String JSON_SUBACTION_VALUE_SETSCENARIOCONFIG = "setscenarioconfig";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONEVENTGET = "eventget";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONEVENTCOUNTGET = "eventcountget";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONEVENTADD = "eventadd";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONEVENTDELETE = "eventdelete";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONEVENTUPDATE = "eventupdate";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONALARMGET = "alarmget";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONALARMADD = "alarmadd";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONALARMDELETE = "alarmdelete";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONALARMUPDATE = "alarmupdate";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONVOICEMSGGET = "voicemsgget";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONVOICEMSGUPDATE = "voicemsgupdate";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONVOICEMSGDELETE = "voicemsgdelete";
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONVOICEMSGADD = "voicemsgadd";

    public static final String JSON_SUBACTION_VALUE_EVENTCOUNTGET = "eventcountget";//Add by xc
    public static final String JSON_SUBACTION_VALUE_ALARMCOUNTGET = "alarmcountget";//Add by xc
    public static final String JSON_SUBACTION_VALUE_VOICEMSGCOUNTGET = "voicemsgcountget";//Add by xc
    public static final String JSON_SUBACTION_VALUE_NOTIFICATIONCOUNTGET = "notificationcountget";//Add by xc
    public static final String JSON_SUBACTION_VALUE_BULLETINCOUNTGET = "bulletincountget";//Add by xc
    public static final String JSON_SUBACTION_VALUE_BULLETINLISTGET = "bulletinlistget";//Add by xc
    public static final String JSON_SUBACTION_VALUE_BULLETINUPDATE = "bulletinlistupdate";//Add by xc
    public static final String JSON_SUBACTION_VALUE_BULLETINEVENT = "bulletinevent";//Add by xc
    public static final String JSON_SUBACTION_VALUE_FEELISTGET = "feelistget";//Add by xc
    public static final String JSON_SUBACTION_VALUE_FEEGET = "feeget";//Add by xc
    public static final String JSON_SUBACTION_VALUE_EVENT = "feeevent";//Add by xc
    public static final String JSON_SUBACTION_VALUE_INDIVIDUALLISTGET = "individuallistget";//Add by xc
    public static final String JSON_SUBACTION_VALUE_INDIVIDUALGET = "individualget";
    public static final String JSON_SUBACTION_VALUE_INDIVIDUALLISTEVENT = "individualevent";//Add by xc

    public static final String JSON_SUBACTION_VALUE_SPEEDDIALGET = "speeddialget";
    public static final String JSON_SUBACTION_VALUE_SPEEDDIALADD = "speeddialadd";
    public static final String JSON_SUBACTION_VALUE_SPEEDDIALUPDATE = "speeddialupdate";
    public static final String JSON_SUBACTION_VALUE_SPEEDDIALDELETE = "speeddialdelete";
    public static final String JSON_SUBACTION_VALUE_SUBPHONEGET = "subphoneget";
    public static final String JSON_SUBACTION_VALUE_SUBPHONEADD = "subphoneadd";
    public static final String JSON_SUBACTION_VALUE_SUBPHONEUPDATE = "subphoneupdate";
    public static final String JSON_SUBACTION_VALUE_SUBPHONEDELETE = "subphonedelete";
    public static final String JSON_SUBACTION_VALUE_CARDGET = "cardget";
    public static final String JSON_SUBACTION_VALUE_CARDADD = "cardadd";
    public static final String JSON_SUBACTION_VALUE_CARDDELEETE = "carddelete";
    public static final String JSON_SUBACTION_VALUE_CARDUPDATE = "cardupdate";
    public static final String JSON_SUBACTION_VALUE_IPCGET = "ipcget";
    public static final String JSON_SUBACTION_VALUE_IPCADD = "ipcadd";
    public static final String JSON_SUBACTION_VALUE_IPCDELETE = "ipcdelete";
    public static final String JSON_SUBACTION_VALUE_IPCUPDATE = "ipcupdate";
    public static final String JSON_SUBACTION_VALUE_LOCALZONEGET = "localzoneget";
    public static final String JSON_SUBACTION_VALUE_LOCALZONEUPUDATE = "localzoneupdate";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONZONEGET = "extensionzoneget";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONZONEUPDATE = "extensionzoneupdate";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONZONEDELETE = "extensionzonedelete";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONRELAYGET = "extensionrelayget";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONRELAYUPDATE = "extensionrelayupdate";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONRELAYDELETE = "extensionrelaydelete";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONMODULEADD = "extensionmoduleadd";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONMODULEDELETE = "extensionmoduledelete";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONZONE = "extensionzone";
    public static final String JSON_SUBACTION_VALUE_EXTENSIONRELAY = "extensionrelay";
    public static final String JSON_SUBACTION_VALUE_CONTENTLST0x51 = "contentlst0x51";
    public static final String JSON_SUBACTION_VALUE_CONTENTFEE0x53 = "contentfee0x53";
    public static final String JSON_SUBACTION_VALUE_CONTENTBULLETIN0x54 = "contentbulletin0x54";
    public static final String JSON_SUBACTION_VALUE_CONTENTNOTICE0x55 = "contentnotice0x55";
    public static final String JSON_SUBACTION_VALUE_GETIPBYDONGHO0x57 = "getipbydongho0x57";
    public static final String JSON_SUBACTION_VALUE_GETDONGHOBYIP0x58 = "getdonghobyip0x58";
    public static final String JSON_SUBACTION_VALUE_MGRFACILITY0x5A = "mgrfacility0x5A";
    public static final String JSON_SUBACTION_VALUE_GETDONGHOBYALIAS0xE1 = "getdonghobyalias0xE1";
    public static final String JSON_SUBACTION_VALUE_GETALIASBYDONGHO0xE2 = "getaliasbydongho0xE2";
    public static final String JSON_SUBACTION_VALUE_LEGACYCALLOUT = "legacycallout";





    public static final String JSON_VIDEORATIO_KEY = "videoratio";
    public static final String JSON_VIDEORATIO_VALUE_DEFAULT = "320*240";

    public static final String JSON_REMOTEIP_KEY = "remoteip";

    public static final String JSON_VIDEOFMG_KEY = "videofmg";
    public static final String JSON_VIDEOFMG_VALUE_DEFAULT = "fsfs&**++%$$#";

    public static final String JSON_PHONEMSTYPE_KEY = "phonemstype";
    public static final String JSON_PHONEMSTYPE_VALUE_MASTER = "master";
    public static final String JSON_PHONEMSTYPE_VALUE_SLAVE = "slave";

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
    public static final String JSON_ELEVATORIP_KEY = "elevatorip";
    public static final String JSON_ELEVATORPORT_KEY = "elevatorport";
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
    public static final String JSON_DEVICETYPE_VALUE_LOBBY="lobby";
    public static final String JSON_DEVICETYPE_VALUE_GUARD="guard";
    public static final String JSON_DEVICETYPE_VALUE_IPVDP="ipvdp";

    public static final String JSON_DEVICEVENDOR_KEY="devicevendor";
    public static final String JSON_DEVICEVENDOR_VALUE_HONEYWELL="honeywell";

    public static final String JSON_UUID_KEY="uuid";

    public static final String JSON_ERRORCODE_KEY="errorcode";
    public static final String JSON_ERRORCODE_VALUE_FAIL="-1";
    public static final String JSON_ERRORCODE_VALUE_OK="0";
    public static final String JSON_ERRORCODE_VALUE_KEYMISS="1";
    public static final String JSON_ERRORCODE_VALUE_SUBPHONE_UNREGISTER="2";
    public static final String JSON_ERRORCODE_VALUE_SUBPHONE_BUSY="3";
    public static final String JSON_ERRORCODE_VALUE_PASSWORD="4";
    public static final String JSON_ERRORCODE_VALUE_MSGTO="5";
    public static final String JSON_ERRORCODE_VALUE_TIMEOUT = "6";
    public static final String JSON_ERRORCODE_VALUE_NOCOMMAND="7";
    public static final String JSON_ERRORCODE_VALUE_FSMNOEVENT="8";
    public static final String JSON_ERRORCODE_VALUE_SUBPHONEREPEAT="9";
    public static final String JSON_ERRORCODE_VALUE_EXIST="-100";


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

    public static final String JSON_VIDEOLOCALPORT_KEY ="videoloacalport";
    public static final String JSON_VIDEOLOCALPORT_VALUE_DEFAULT = "2000";

    public static final String JSON_AUDIOLOCALPORT_KEY ="audiolocalport";
    public static final String JSON_AUDIOLOCALPORT_VALUE_DEFAULT = "3000";

    public static final String JSON_VIDEOREMOTEPORT_KEY ="videoremoteport";
    public static final String JSON_VIDEREMOTEPORT_VALUE_DEFAULT = "2000";

    public static final String JSON_AUDIOREMOTEPORT_KEY ="audioremoteport";
    public static final String JSON_AUDIOREMOTEPORT_VALUE_DEFAULT = "3000";

    public static final String JSON_RTPIP_KEY = "rtpip";

    public static final String JSON_LOOPMAP_KEY ="loopmap";

    public static final String JSON_REASON_KEY="reason";

    public static final String JSON_FROMIP_KEY="fromip";
    public static final String JSON_FROMALIASNAME_KEY="fromalias";
    public static final String JSON_TOIP_KEY="toip";
    public static final String JSON_TOALIASNAME_KEY ="toalias";
    public static final String JSON_FROMRTP_KEY="fromrtp";
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
    

    /***********************ellen 20170313*********************************/
    public static final String APPDATABASEFILE = "homepanel.db";
    public static final String ACTION_SUBPHONE_SERVICE = "com.honeywell.homepanel.subphoneuiservice.SubPhoneUIService";
    public static final String ACTION_CONFIG_SERVICE = "com.honeywell.homepanel.configcenter.ConfigService";
    public static final String ACTION_AVRTP_SERVICE = "com.honeywell.homepanel.avrtp.AvRtp";

    public static final String WIFIMODULE_DEFAULT_VERSION ="0.0.1";

    public static final int ONLINE = 1;
    public static final int NOTONLINE = 0;

    /**********************engineering mode******************************/
    public static final String ENGIN_MODE_OPTIONS = "enginneering mode options";
    public static final int ENGIN_MODE_ZONE_TYPE = 13;
    public static final int ENGIN_MODE_ALARM_TYPE = 12;
    public static final int ENGIN_MODE_DEVICE_ROLE = 00;
    public static final int ENGIN_MODE_RELAY_CFG = 20;
    public static final int ENGIN_MODE_ROOM_NUMBER = 31;
    public static final int ENGIN_MODE_ZONE_DELAY_TIME =14;
    public static final String ENGIN_MODE_ZONE = "zone type";
    public static final String ENGIN_MODE_MODULE_UUID = "module_uuid";
    public static final int ENGIN_MODE_LOCAL_ZONE = 10;
    public static final int ENGIN_MODE_EXTENSION_ZONE = 11;
    /**********************Local zone detail******************************/
    public static final String OPTIONS_DETIAL_ZONE = "enginneering mode options";
    public static final int OPTIONS_DETIAL_ZONE1 = 1;
    public static final int OPTIONS_DETIAL_ZONE2 = 2;
    public static final int OPTIONS_DETIAL_RELAY1 = 3;
    public static final int OPTIONS_DETIAL_RELAY2 = 4;

    /**********************************event bus broadcast****************************************/
    public static final String INTENT_ACTION_EVENTBUS_MESSAGE = "com.honeywell.homepanel.eventbusmessage";
    public static final String INTENT_EXTRA_JSONMESSAGECLASS_KEY = "jsonmessageclass";
    public static final String INTENT_EXTRA_JSONMESSAGEBODY_KEY = "jsonmessagebody";


    /*******************added by ellen for db *******************************/
    public static  final  String DATASTATUS_ALL = "all";
    public static  final  String DATASTATUS_UNREAD = "unread";
    public static  final  String DATASTATUS_READ = "read";

    public static  final  String COMMONDEVICE_TYPE_LIGHT = "light";
    public static  final  String COMMONDEVICE_TYPE_RELAY = "relay";
    public static  final  String COMMONDEVICE_TYPE_SCENARIO = "scenario";
    public static  final  String COMMONDEVICE_TYPE_CURTAIN = "curtain";
    public static  final  String COMMONDEVICE_TYPE_AIRCONDITION = "airconditon";
    public static  final  String COMMONDEVICE_TYPE_VENTILATION = "ventilation";
    public static  final  String COMMONDEVICE_TYPE_ZONE = "zone";
    public static  final  String COMMONDEVICE_TYPE_THERMOSTAT = "thermostat";
    public static  final  String COMMONDEVICE_TYPE_IPC = "ipc";

    public static  final  String JSON_OPERATIONTYPE_VALUE_ADD = "add";
    public static  final  String JSON_OPERATIONTYPE_VALUE_DELETE = "delete";
    public static  final  String JSON_OPERATIONTYPE_VALUE_UPDATE = "update";

    public static  final  String JSON_KEY_NAME = "name";
    public static  final  String JSON_KEY_ZONETYPE = "zonetype";
    public static  final  String JSON_KEY_ALARMTYPE = "alarmtype";
    public static  final  String JSON_KEY_ENABLE = "enable";
    public static  final  String JSON_KEY_DELAYTIME = "delaytime";
    public static  final  String JSON_KEY_LOOP = "loop";
    public static  final  String JSON_KEY_OPERATIONTYPE = "operationtype";
    public static  final  String JSON_KEY_DATASTATUS = "datastatus";
    public static  final  String JSON_KEY_START = "start";
    public static  final  String JSON_KEY_COUNT = "count";
    public static  final  String JSON_KEY_EVENTTYPE = "eventtype";
    public static  final  String JSON_KEY_TIME = "time";
    public static  final  String JSON_KEY_IMAGENAME = "imgname";
    public static  final  String JSON_KEY_VIDEONAME = "videoname";
    public static  final  String JSON_KEY_CARDID = "cardid";
    public static  final  String JSON_KEY_CARDTYPE = "cardtype";
    public static  final  String JSON_KEY_SWIPEACTION = "swipeaction";
    public static  final  String JSON_KEY_PERMANENTCARD = "permanentcard";
    public static  final  String JSON_KEY_TEMORARYCARD = "temorarycard";
    public static  final  String JSON_KEY_MESSAGE = "message";
	public static  final  String JSON_DONGHO_KEY = "dongho";
    public static  final  String JSON_KEY_FILENAME = "filename";
    public static  final  String JSON_KEY_DURATION = "duration";
    public static  final  String JSON_TYPE_KEY = "type";


    public static  final  String JSON_KEY_STARTDATE = "startdate";
	public static  final  String JSON_KEY_ENDDATE = "enddate";
 
    public static  final  String JSON_KEY_STARTTIME = "starttime";
	public static  final  String JSON_KEY_ENDTIME = "endtime";
    public static  final  String JSON_KEY_CITY = "city";
    public static  final  String JSON_IP_KEY = "ip";
    public static  final  String JSON_USERNAME_KEY = "username";
    public static  final  String JSON_ONLINE_KEY = "online";
    public static  final  String JSON_MAINIP_KEY = "mainip";
    public static  final  String JSON_MAINPORT_KEY = "mainport";
    public static  final  String JSON_SUBPHONEID_KEY = "subphoneid";
    public static  final  String JSON_UNIT_KEY = "unit";
    public static  final  String JSON_AMSIP_KEY = "amsip";
    public static  final  String JSON_AMSPORT_KEY = "amsport";
    public static  final  String JSON_HOMEPANELTYPE_KEY = "homepaneltype";
    public static  final  String ZONETYPE_24H = "24h";
    public static  final  String JSON_KEY_VERSION = "version";
    public static  final  String JSON_KEY_MAC = "mac";
    public static  final  String ZONETYPE_INSTANT = "instant";
    public static  final  String ZONETYPE_DELAY = "delay";
    public static  final  String ALARMTYPE_EMERGENCY = "emergency";
    public static  final  String ALARMTYPE_HELP = "help";
    public static  final  String ALARMTYPE_FIRE = "fire";
    public static  final  String ALARMTYPE_GAS = "gas";
    public static  final  String ALARMTYPE_INTRUSION = "intrusion";


    //added by ellen
    public  static  final  int RELAY_LOOP_NUM = 4;
    public  static  final  int ZONE_LOOP_NUM = 4;

    public  static  final  int ENABLE = 1;
    public  static  final  int DISENABLE = 0;


    public static final String COMMUNITY_DEVLOOPMAP = "deviceloopmap";
    public static final String COMMUNITY_KEY_EQUIP = "equip";
    public static final String COMMUNITY_KEY_TYPE = "type";
    public static final String COMMUNITY_KEY_PAGE = "page";
    public static final String COMMUNITY_KEY_PER = "per";
    public static final String COMMUNITY_KEY_ID = "id";
    public static final String COMMUNITY_KEY_TITLE = "title";
    public static final String COMMUNITY_KEY_DATE = "date";
    public static final String COMMUNITY_KEY_YEAR = "year";
    public static final String COMMUNITY_KEY_MONTH = "month";
    public static final String COMMUNITY_KEY_DAY= "day";
    public static final String COMMUNITY_KEY_HOUR = "hour";
    public static final String COMMUNITY_KEY_MIN = "min";
    public static final String COMMUNITY_KEY_SEC = "sec";
    public static final String COMMUNITY_KEY_ITEM= "item";
    public static final String COMMUNITY_KEY_VALUE = "value";
    public static final String COMMUNITY_KEY_DONGHO = "dongho";
    public static final String COMMUNITY_KEY_CONTENT = "content";
    public static final String COMMUNITY_KEY_LOGIN = "login";
    public static final String COMMUNITY_KEY_CALLTYPE = "calltype";
    public static final String COMMUNITY_KEY_IP = "ip";
    public static final String COMMUNITY_KEY_GUARDPHONE = "guardphone";
    public static final String COMMUNITY_KEY_OFFICEPHONE = "officephone";
    public static final String COMMUNITY_KEY_LOBBYPHONE = "lobbyphone";
    public static final String COMMUNITY_KEY_ALIAS = "alias";



    public static final String COMMUNITY_VALUE_EQUIP_WM = "WM";
    public static final String COMMUNITY_VALUE_EQUIP_GM1 = "GM1";
    public static final String COMMUNITY_VALUE_EQUIP_GM2 = "GM2";
    public static final String COMMUNITY_VALUE_EQUIP_GM3 = "GM3";

    public static final String COMMUNITY_VALUE_TYPE_REMOTE = "remote";
    public static final String COMMUNITY_VALUE_TYPE_MANAGEMENT = "management";
    public static final String COMMUNITY_VALUE_TYPE_ALL = "all";
    public static final String COMMUNITY_VALUE_TYPE_INDIVIDUAL = "individual";

    /*******************added by xc for notifiction *******************************/
    public static  final  String FRAGMENT_EVENT = "event";
    public static  final  String FRAGMENT_ALARM = "alarm";
    public static  final  String FRAGMENT_NOTIFICATION = "notification";
    public static  final  String FRAGMENT_VOICEMSG = "voicemsg";
    public static  final  String JSON_VALUE_VISITOR = "visitor";
    public static  final  String JSON_VALUE_VIDEO = "video";
    public static  final  String JSON_VALUE_UNDEF = "undef";
    public static  final  String JSON_VALUE_SWIPECARD= "swipecard";
    public static  final  String JSON_VALUE_PERMANENT= "permanent";
    public static  final  String JSON_VALUE_TEMPORARY= "temporary";
    public static  final  String JSON_KEY_TITLE = "title";
    public static  final  String JSON_KEY_DATE = "date";
    public static final String COLOR_DARKGREY = "#4A4A4A";
    public static final String COLOR_NORMALGREY = "#C6C6C6";
    public static final String COLOR_SCREENSAVER_TEXT_GREY = "#808080";

    //Max Voice Message Count supported
    public static final int MAX_VOICE_RECORD_COUNT = 3;
    public static final int MAX_NOTIFICATION_BULLTIN_COUNT = 1000;
    public static final int MAX_ALARM_COUNT = 1000;
    public static final int MAX_EVENT_COUNT = 1000;


/**      log level    */
    public static final int LOG_LEVEL_DEBUG = 0;
    public static final int LOG_LEVEL_WARNING = 1;
    public static final int LOG_LEVEL_ERROR = 2;

    public static final int MAXALARMCASHCOUNT = 100;

    public final static String ALARMLOG_MODULETYPE = "moduletype";// string,
    public final static String ALARMLOG_MODULEADDR = "moduleaddr";//str,ip or mac
    public final static String ALARMLOG_LOOPID = "loopid";// str
    public final static String ALARMLOG_ALARMINFO = "alarminfo";// str
    public final static String ALARMLOG_TIMESTAMP = "timestamp";// long
    public final static String ALARMLOG_ROOMNAME = "roomname";// str
    public final static String ALARMLOG_LOOPNAME = "aliasname";// str
    public final static String ALARMLOG_ISTOCLOUD = "istocloud";// str

    // Full path name for services name
    public final static String ACTION_SERVICE_LOGCENTER = "com.honeywell.homepanel.logserver.LogService";
    public final static String ACTION_SERVICE_CONFIG = "com.honeywell.homepanel.configcenter.ConfigService";
    public final static String ACTION_SERVICE_MONITOR = "com.honeywell.homepanel.watchdog.WatchDogService";


    //added by ellen
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HomePanel/";
    public static final String AUDIO_PATH = FILE_PATH + "audio/";


    public static String file_() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName();
    }

    public static int line_() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getLineNumber();
    }}
