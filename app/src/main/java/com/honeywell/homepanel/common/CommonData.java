package com.honeywell.homepanel.common;

/**
 * Created by ellen on 1/24/2017.
 */

public class CommonData {
    /*****************************added by ellen start ************************/
    /***********************left navigation page********************/
    public static final int LEFT_SELECT_HOME = 0;
    public static final int LEFT_SELECT_SCENARIOEDIT = 1;
    public static final int LEFT_SELECT_DEVICEEDIT = 2;
    public static final int LEFT_SELECT_MESSAGE = 3;
    public static final int LEFT_SELECT_DIAL = 4;
    public static final int LEFT_SELECT_SETTING = 5;

    //added by ellen for message fragment
    public static final int MESSAGE_SELECT_EVENT = 0;
    public static final int MESSAGE_SELECT_ALARMHITORY = 1;
    public static final int MESSAGE_SELECT_NOTIFICATION = 2;
    public static final int MESSAGE_SELECT_VOICEMESSAGE = 3;

    /********************For Top signal ************************/
    public static final String WEATHER_SUNNY = "sunny";
    public static final String WEATHER_CLOUDY = "cloudy";
    public static final String WEATHER_FLURRIES = "flurries";
    public static final String WEATHER_FOG = "fog";
    public static final String WEATHER_MOSTLYCLEAR = "mostlyclear";
    public static final String WEATHER_MOSTLYCLOUDYTHUNDERSTORMS = "mostlycloudythunderstorms";
    public static final String WEATHER_MOSTLYSUNNYFLURRIES = "MOSTLYSUNNYFLURRIES";
    public static final String WEATHER_MOSTLYSUNNYSHOWER = "mostlysunnyshower";
    public static final String WEATHER_MOSTLYSUNNYSNOW = "mostlysunnysnow";
    public static final String WEATHER_MOSTLYSUNNYTHUNDERSTORMS = "mostlysunnythunderstorms";
    public static final String WEATHER_MOSTLYSUNNY = "mostlysunny";
    public static final String WEATHER_PARTLYSUNNY = "partlysunny";
    public static final String WEATHER_RAINSNOWMIXED = "rainsnowmixed";
    public static final String WEATHER_RAIN = "rain";
    public static final String WEATHER_SLEET = "sleet";
    public static final String WEATHER_SNOW = "snow";
    public static final String WEATHER_THUNDERSTORMS = "thunderstorms";
    public static final String WEATHER_WINDY = "windy";

    public static final String TEMPERATURE_DUSTR = "°";
    public static final String UNHEALTHY = "Unhealthy";
    public static final String ARMSTATUS_ARM = "System Armed";
    public static final String ARMSTATUS_DISARM = "System Disarmed";

    public static final int CONNECTED = 1;
    public static final int DISCONNECT = 0;
    public static final int CHANGE_PASSWORD_LENGTH = 4;
    public static final int SRCURITY_PASSWORD_LENGTH = 6;

    /**********************default scenario******************************/
    public static final int SCENARIO_HOME = 1;
    public static final int SCENARIO_AWAY = 2;
    public static final int SCENARIO_SLEEP = 3;
    public static final int SCENARIO_WAKEUP = 4;

    /**********************parameter for call******************************/
    public static final String INTENT_KEY_SCENARIO = "cur_scenario";
    public static final String INTENT_KEY_SCENARIO_UUID = "cur_scenario_uuid";
    public static final String INTENT_KEY_SCENARIO_NAME = "cur_scenario_name";
    public static final String INTENT_KEY_CALL_TYPE = "call_type";
    public static final String INTENT_KEY_UNIT = "unit";
    public static final String INTENT_KEY_TEXTFORMAT = "formattext";

    public static final String INTENT_KEY_DISPLAYNAME = "displayname";// for inner call out
    public static final String INTENT_KEY_VIDEOPATH = "videopath";

    /******************any call status************************************/
    public static final int CALL_OUTGOING_NEIGHBOR = 0;/*邻里呼出*/
    public static final int CALL_INCOMING_NEIGHBOR = 1;/*邻里&PCGuard呼入*/
    public static final int CALL_CONNECTED_AUDIO_NETGHBOR = 2;/*邻里音频通话*/
    public static final int CALL_CONNECTED_VIDEO_NETGHBOR = 3;/*邻里视频通话*/
    public static final int CALL_LOBBY_INCOMMING = 4;/*lobby呼入*/
    public static final int CALL_LOBBY_CONNECTED = 5;/*lobby接通*/
    public static final int CALL_GUARD_INCOMMING = 6;/*guard*/
    public static final int CALL_GUARD_CONNECTED = 7;/*guard*/
    public static final int CALL_GUART_OUTGOING = 8;//guard outging

    public static final int CALL_IPDC_INCOMING = 9;//ipdc incomming
    public static final int CALL_IPDC_CONNECTED = 10;//ipdc  connect

    public static final int CALL_SUBPHONE_INCOMMING = 11;//suhphone incomming
    public static final int CALL_SUBPHONE_CONNECTED = 12;//suhphone  connect
    public static final int CALL_SUBPHONE_OUTGOING = 13;


    public static final int CALL_STATUS_IPDCVIEW = 100;

    /************************added by ellen end**********************************/
    public static final String INTENT_CARD_ALIAS = "card_alias";
    public static final String INTENT_CARD_ID = "card_id";
    public static final String INTENT_CARD_START_DATE = "card_start_date";
    public static final String INTENT_CARD_END_DATE = "card_end_date";
    public static final String INTENT_CARD_START_TIME = "card_start_time";
    public static final String INTENT_CARD_END_TIME = "card_end_time";


    public static final String INTENT_KEY_CARD_TYPE = "card_type";
    public static final String INTENT_KEY_CARD_FRAGMENT = "card_fragment";
    public static final String CARD_PERMANENT = "permanent";
    public static final String CARD_TEMORARY = "temorary";
    /********************paramer for ipc view ******************/
    public static final String INTENT_KEY_IPCINFO = "ipcinfo";
    public static final String INTENT_KEY_CALLINFO = "calldetailinfo";

    public static final int CARD_NO = 8;
    public static final int CARD_LIST = 9;


    // Standard Call
    public static final int SENCES_EDIT = 5;
    public static final String CALL_PHONE_MAIN = "0";
    public static final String CALL_PHONE_SUB1 = "1";
    public static final String CALL_PHONE_SUB2 = "2";
    public static final String CALL_PHONE_SUB3 = "3";
    public static final String CALL_PHONE_SUB4 = "4";
    public static final String CALL_PHONE_SUB5 = "5";
    public static final String CALL_PHONE_SUB6 = "6";
    public static final String CALL_PHONE_SUB7 = "7";
    public static final String CALL_PHONE_SUB8 = "8";
    public static final String CALL_PHONE_Cloud = "cloudphone";

    public static final int CALL_CHANNEL_MAX = 12;// 1 Mainphone, 8 Subphone, 1 Cloud  2,ipdc

    public static final int CALL_STATE_UNREGISTER = 0;// First State must be zero!!! same as STATE_ANY which defined at StateHandler.java
    public static final int CALL_STATE_IDLE = 1;
    public static final int CALL_STATE_CALLIN = 2;
    public static final int CALL_STATE_TALKING = 3;
    public static final int CALL_STATE_TERMINATED = 4;
    public static final int CALL_STATE_PREPARE = 5;
    public static final int CALL_STATE_CALLOUT = 6;
    public static final int CALL_STATE_BUSY = 7;

    public static final String CAMERAS_CMD_NEXT = "cameras_next";
    public static final String CAMERAS_CMD_FULLSCREEN = "cameras_fullscreen";

    public static final int CAMERAS_LIVING = 0;
    public static final int CAMERAS_IPDOOR = 1;
    public static final int CAMERAS_PARKING = 2;


    /***********************ellen 20170313*********************************/
    public static final String APPDATABASEFILE = "homepanel.db";
    public static final String ACTION_SUBPHONE_SERVICE = "com.honeywell.homepanel.subphoneuiservice.SubPhoneUIService";
    public static final String ACTION_CONFIG_SERVICE = "com.honeywell.homepanel.configcenter.ConfigService";
    public static final String ACTION_AVRTP_SERVICE = "com.honeywell.homepanel.avrtp.AvRtp";
    public static final String ACTION_SUBUPGRADE_SERVICE = "com.honeywell.homepanel.upgrade.subupgrader.SubupgraderService";
    public static final String ACTION_WIDGET_SERVICE = "com.honeywell.homepanel.ui.services.WidgetInfoService";

    public static final String ACTION_SYSTEMTIME_CHANGED = "android.intent.action.TIME_SET";

    public static final String WIFIMODULE_DEFAULT_VERSION = "0.0.1";

    public static final int ONLINE = 1;
    public static final int NOTONLINE = 0;

    /**********************engineering mode brusher******************************/
    public static final String ENGIN_MODE_ZONE_ALIAS_NUM = "zoneAliasNum";
    public static final String ENGIN_MODE_ALARM_TYPE_NUM = "alarmTypeNum";
    public static final String ENGIN_MODE_ZONE_TYPE_NUM = "zoneTypeNum";

    /**********************engineering mode******************************/
    public static final String ENGIN_MODE_OPTIONS = "enginneering mode options";
    public static final int ENGIN_MODE_ZONE_TYPE = 13;
    public static final int ENGIN_MODE_ALARM_TYPE = 12;
    public static final int ENGIN_MODE_DEVICE_ROLE = 00;
    public static final int ENGIN_MODE_RELAY_CFG = 20;
    public static final int ENGIN_MODE_ROOM_NUMBER = 31;
    public static final int ENGIN_MODE_ENGIN_CODE = 32;
    public static final int ENGIN_MODE_CONFIRM_ENGIN_CODE = 33;
    public static final int ENGIN_MODE_ZONE_DELAY_TIME = 14;
    public static final int ENGIN_MODE_RELAY_DELAY_TIME = 15;
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

    /**********************IP configuration type******************************/
    public static final String ENGIN_CONFIG_IP_TYPE = "enginneering mode config IP type";
    public static final String ENGIN_CONFIG_IP_CONTENT = "enginneering mode config IP content";
    public static final String ENGIN_CONFIG_NETMASK_CONTENT = "enginneering mode config Netmask content";
    public static final String ENGIN_CONFIG_GATEWAY_CONTENT = "enginneering mode config Gateway content";
    public static final String ENGIN_CONFIG_TEXT_TYPE = "enginneering mode config Text type";
    public static final String ENGIN_CONFIG_TITLE_DESCRIBE = "enginneering title";
    public static final int CONFIG_IP_SERVER = 1;
    public static final int CONFIG_IP_SELF = 2;
    public static final int CONFIG_NETWORK = 3;
    public static final int CONFIG_GATEWAY = 4;
    public static final int CONFIG_SUBPHONE_MASTER = 7;
    public static final int CONFIG_IP_ELEVATOR = 5;
    public static final int CONFIG_PORT_ELEVATOR = 6;
    public static final int CONFIG_LOCAL_FLOOR_ELEVATOR = 17;
    public static final int CONFIG_IP_CAMERA_SETTING_USER_NAME = 11;
    public static final int CONFIG_IP_CAMERA_SETTING_USER_PASSWORD = 12;
    public static final int CONFIG_IP_CAMERA_SETTING_IP_ADDRESS = 13;
    public static final int CONFIG_IP_CAMERA_SETTING_ALIAS = 14;
    public static final int CONFIG_IP_SETTING_ALIAS = 8;
    public static final int CONFIG_SETTING_MASTER_PHONE_IP = 9;
    public static final int CONFIG_SETTING_SUB_IP_ID = 10;
    public static final int CONFIG_DOOR_LOCK_SETTING_BACK = 15;
    public static final int CONFIG_DOOR_LOCK_SETTING_FRONT = 16;


    /**********************Subphone Configuration type******************************/
    public static final String SUBPHONE_CONFIG_TYPE = "enginneering mode config subphone type";
    public static final int CONFIG_SUBPHONE_ID = 1;
    public static final int CONFIG_SUBPHONE_PASSWORD = 2;
    public static final int CONFIG_SUBPHONE_ALIAS = 3;

    /**********************Alias type******************************/
    public static final String ALIAS_CONFIG_TYPE = "enginneering mode device alias type";
    public static final int CONFIG_RELAY_ALIAS = 21;
    public static final int CONFIG_EXTERNAL_ZONE_ALIAS = 22;
    public static final int CONFIG_LOCAL_ZONE_ALIAS = 23;

    /**********************Zone connection type******************************/
    public static final String ZONE_CONNECTION_TYPE = "enginneering mode zone connection type";
    public static final int LOCAL_ZONE_TYPE = 31;
    public static final int EXTENSION_ZONE_TYPE = 32;

    /*******************added by ellen for db *******************************/
    public static final String DATASTATUS_ALL = "all";
    public static final String DATASTATUS_UNREAD = "unread";
    public static final String DATASTATUS_READ = "read";
    public static final String ALARMREPORTSTATUS_REPORTED = "reported";
    public static final String ALARMREPORTSTATUS_UNREPORTED = "unreported";
    public static final String ALARMREPORTSTATUS = "reportstatus";
    public static final String ALARMREPORT_TYPE_AMS = "ams";
    public static final String ALARMREPORT_TYPE_CLOUD = "cloud";


    public static final String COMMONDEVICE_TYPE_LIGHT = "light";
    public static final String COMMONDEVICE_TYPE_RELAY = "relay";
    public static final String COMMONDEVICE_TYPE_SCENARIO = "scenario";
    public static final String COMMONDEVICE_TYPE_CURTAIN = "curtain";
    public static final String COMMONDEVICE_TYPE_AIRCONDITION = "airconditon";
    public static final String COMMONDEVICE_TYPE_VENTILATION = "ventilation";
    public static final String COMMONDEVICE_TYPE_ZONE = "zone";
    public static final String COMMONDEVICE_TYPE_THERMOSTAT = "thermostat";
    public static final String COMMONDEVICE_TYPE_IPC = "ipc";
    public static final String COMMONDEVICE_TYPE_ELEVATOR = "elevator";
    public static final String COMMONDEVICE_TYPE_ELEVATOR_STATUS = "elevatorstatus";
    public static final String COMMONDEVICE_TYPE_LOCK_STATUS = "lockstatus";
    public static final String COMMONDEVICE_TYPE_LOCK = "lock";
    public static final String COMMONDEVICE_TYPE_DOORBELL = "doorbell";
    public static final String COMMONDEVICE_TYPE_IPDC = "ipdoorcamera";

    public static final String JSON_OPERATIONTYPE_VALUE_ADD = "add";
    public static final String JSON_OPERATIONTYPE_VALUE_DELETE = "delete";
    public static final String JSON_OPERATIONTYPE_VALUE_UPDATE = "update";

    public static final String JSON_KEY_NAME = "name";
    public static final String JSON_KEY_ZONETYPE = "zonetype";
    public static final String JSON_KEY_ALARMTYPE = "alarmtype";
    public static final String JSON_KEY_SENSORTYPE = "sensortype";
    public static final String JSON_KEY_ENABLE = "enable";
    public static final String JSON_KEY_DELAYTIME = "delaytime";
    public static final String JSON_KEY_LOOP = "loop";
    public static final String JSON_KEY_MODEL = "model";
    public static final String JSON_KEY_OPERATIONTYPE = "operationtype";
    public static final String JSON_KEY_DATASTATUS = "datastatus";
    public static final String JSON_KEY_READSTATUS = "readstatus";
    public static final String JSON_KEY_UPLOADAMS = "uploadams";
    public static final String JSON_KEY_UPLOADCLOUD = "uploadcloud";
    public static final String JSON_KEY_ROLEID = "roleid";
    public static final String JSON_KEY_START = "start";
    public static final String JSON_KEY_COUNT = "count";
    public static final String JSON_KEY_EVENTTYPE = "eventtype";
    public static final String JSON_KEY_TIME = "datetime";
    public static final String JSON_KEY_IMAGENAME = "imgname";
    public static final String JSON_KEY_VIDEONAME = "videoname";
    public static final String JSON_KEY_CARDID = "cardid";
    public static final String JSON_KEY_CARDTYPE = "cardtype";
    public static final String JSON_KEY_SWIPEACTION = "swipeaction";
    public static final String JSON_KEY_PERMANENTCARD = "permanent";
    public static final String JSON_KEY_TEMORARYCARD = "temorary";
    public static final String JSON_KEY_MESSAGE = "message";
    public static final String JSON_DONGHO_KEY = "dongho";
    public static final String JSON_KEY_FILENAME = "filename";
    public static final String JSON_KEY_DURATION = "duration";
    public static final String JSON_TYPE_KEY = "type";
    public static final String JSON_KEY_DESCRIPTION = "description";
    public static final String JSON_KEY_ADAPTERNAME = "adapteruuid";
    public static final String JSON_KEY_MODULEUUID = "moduleuuid";

    public static final String JSON_KEY_STARTDATE = "startdate";
    public static final String JSON_KEY_ENDDATE = "enddate";

    public static final String JSON_KEY_STARTTIME = "starttime";
    public static final String JSON_KEY_ENDTIME = "endtime";
    public static final String JSON_KEY_SETDATE = "date";
    public static final String JSON_KEY_SETTIME = "time";
    public static final String JSON_KEY_AUTOTIME = "autotime";
    public static final String JSON_KEY_24HOUR = "24hour";
    public static final String JSON_KEY_CITY = "city";
    public static final String JSON_IP_KEY = "ip";
    public static final String JSON_PORT_KEY = "port";
    public static final String JSON_USERNAME_KEY = "username";
    public static final String JSON_ONLINE_KEY = "online";
    public static final String JSON_MAINIP_KEY = "mainip";
    public static final String JSON_PANELALIAS_KEY = "panelalias";
    public static final String JSON_MAINPORT_KEY = "mainport";
    public static final String JSON_SUBPHONEID_KEY = "subphoneid";
    public static final String JSON_SUBPHONENAME_KEY = "subphonename";
    public static final String JSON_UNIT_KEY = "unit";
    public static final String JSON_AMSIP_KEY = "amsip";
    public static final String JSON_AMSPORT_KEY = "amsport";
    public static final String JSON_HOMEPANELTYPE_KEY = "homepaneltype";
    public static final String JSON_CLOUD_ENVIORNMENT_TYPE ="cloudenviornmenttype";
    public static final String JSON_KEY_VERSION = "version";
    public static final String JSON_KEY_VERSIONTYPE = "versiontype";
    public static final String JSON_KEY_MAC = "mac";
    public static final String ZONETYPE_24H = "24h";
    public static final String ZONETYPE_INSTANT = "instant";
    public static final String ZONETYPE_DELAY = "delay";
    public static final String ZONETYPE_TRIGGER = "trigger";
    public static final String ZONETYPE_ENVIRONMENT = "environment";
    public static final String ALARMTYPE_FIRE = "fire";
    public static final String ALARMTYPE_GAS = "gas";
    public static final String ALARMTYPE_INTRUSION = "intrusion";
    public static final String ALARMTYPE_TAMPER = "tamper";
    public static final String ALARMTYPE_EMERGENCY = "emergency";
    public static final String ALARMTYPE_EMERGENCY_SILENCE = "emergencysilence"; // mute
    public static final String ALARMTYPE_EMERGENCY_NODISTURB = "emergencynodisturb"; //hijack
    //    public static final String ALARM_DB_KEY_UPLOAD = "upload";
    public static final String NETMASK_IP_ADDRESS = "netmaskip";
    public static final String NETMASK_IP_NETMASK = "netmasknetmask";
    public static final String NETMASK_IP_GATEWAY = "netmaskgateway";

    public static final String JSON_IPDCTYPE_KEY = "ipdctype";
    public static final String JSON_IPDCTYPE_VALUE_FRONTDOOR = "2001";
    public static final String JSON_IPDCTYPE_VALUE_BACKDOOR = "2002";
    public static final int JSON_IPDCTYPE_VALUEINT_BASE = 2001;

    //added by ellen
    public static final int RELAY_LOOP_NUM = 4;
    public static final int ZONE_LOOP_NUM = 4;

    public static final int ENABLE = 1;
    public static final int DISENABLE = 0;


    /*******************added by xc for notifiction *******************************/
    public static final String FRAGMENT_EVENT = "event";
    public static final String FRAGMENT_ALARM = "alarm";
    public static final String FRAGMENT_NOTIFICATION = "notification";
    public static final String FRAGMENT_VOICEMSG = "voicemsg";
    public static final String JSON_VALUE_VISITOR = "visitor";
    public static final String JSON_VALUE_VIDEO = "video";
    public static final String JSON_VALUE_UNDEF = "undef";
    public static final String JSON_VALUE_SWIPECARD = "swipecard";
    public static final String JSON_VALUE_PERMANENT = "permanent";
    public static final String JSON_VALUE_TEMPORARY = "temporary";
    public static final String JSON_VALUE_DOOROPEN = "dooropen";
    public static final String JSON_KEY_TITLE = "title";
    public static final String JSON_KEY_DATE = "date";
    public static final String COLOR_DARKGREY = "#4A4A4A";
    public static final String COLOR_NORMALGREY = "#C6C6C6";
    public static final String COLOR_SCREENSAVER_TEXT_GREY = "#808080";

    //add by xc for homepanel top status bar
    public final static String JSON_KEY_SERVER = "server";
    public final static String JSON_KEY_TEMPCURRENT = "tempcurrent";
    public final static String JSON_KEY_PM25 = "pm25";
    public final static String JSON_KEY_WEATHER = "weather";
    public final static String JSON_KEY_TEMPHIGH = "temphigh";
    public final static String JSON_KEY_TEMPLOW = "templow";
    public final static String JSON_SUBACTION_VALUE_TIMEGET = "timeget";
    public final static String JSON_VALUE_TIMESYNC = "timesync";
    public final static String JSON_VALUE_OFFLINE = "offline";
    public final static String JSON_VALUE_ONLINE = "online";
    public final static String JSON_SUBACTION_VALUE_WEATHERGET = "weatherget";
    public final static String JSON_SUBACTION_VALUE_AMSWEATHERGET = "amsweatherget";
    public final static String JSON_SUBACTION_VALUE_WEATHERUPDATE = "weatherupdate";

    //add by xc for extended modules
    public final static String JSON_MODULE_NAME_ALARM = "wiredzone";
    public final static String JSON_MODULE_NAME_RELAY = "relay";
    public final static String JSON_MODULE_NAME_UNKNOW = "unknow";

    //add by xc for homepanel scenario
    public final static String JSON_SCENARIO_HOME = "home";
    public final static String JSON_SCENARIO_AWAY = "away";
    public final static String JSON_SCENARIO_SLEEP = "sleep";
    public final static String JSON_SCENARIO_WAKEUP = "wakeup";
    public final static String JSON_KEY_SCENARIO = "scenario";

    public final static String ABNORMAL_INTENT_KEY = "abnormallist";
    //add by ailynn for switcher
    public final static String JSON_SUBACTION_VALUE_SETRELAYLOOPSTATUS = "setrelayloopstatus";
    public final static String JSON_SUBACTION_VALUE_READRELAYLOOPSTATUS = "readrelayloopstatus";
    public final static String JSON_SUBACTION_VALUE_ZONELOOPSTATUSUPDATE = "zoneloopstatusupdate";
    public final static String JSON_SUBACTION_VALUE_DEVICEASSISTALARM = "deviceassitalarm";
    public final static String JSON_SUBACTION_VALUE_LOCKSTATUSUPDATE = "lockstatusupdate";
    public final static String JSON_SUBACTION_VALUE_DOORBELLSTATUSTRIGGER = "doorbellstatustrigger";
    public final static String JSON_SUBACTION_VALUE_LOCALIO = "localio";
    public final static String JSON_KEY_DEVICETYPE_RELAY = "relay";
    public final static String JSON_KEY_DEVICETYPE_WIREDZONE = "wiredzone";
    public final static String JSON_KEY_DEVICETYPE_MOMAS = "momaslock";
    public final static String JSON_KEY_LOCALIO_ID = "localioid";
    public final static String JSON_KEY_LOCALIO_STATUS = "localiostatus";
    public final static String JSON_KEY_STATUS = "status";
    public final static String JSON_KEY_STATUS_ON = "on";
    public final static String JSON_KEY_STATUS_OFF = "off";
    public final static String JSON_KEY_STATUS_TRIGGER = "trigger";
    public final static String JSON_KEY_STATUS_NORMAL = "normal";
    public final static String JSON_KEY_STATUS_OPEN = "open";
    public final static String JSON_KEY_ALARM_PWD = "alarmpwd";
    public final static String JSON_KEY_ALARMMSGID = "alarmmsgid";
    public final static String JSON_KEY_ALARMRECORDID = "alarmrecordid";
    public final static String JSON_KEY_ABNORMALSTATUS = "abnormalstatus";
    public final static String JSON_KEY_ALARMZONEID = "alarmzoneid";
    public final static String JSON_KEY_EVENTCODE = "eventcode";

    //add by ailynn for door open event
    public static final String JSON_VALUE_OPEN_FRONTDOOR = "openfrontdoor";
    public static final String JSON_VALUE_OPEN_BACKDOOR = "openbackdoor";
    public static final String JSON_VALUE_ROLE_HOST = "host";
    public static final String JSON_VALUE_ROLE_HOSTESS = "hostess";
    public static final String JSON_VALUE_ROLE_CHILD = "child";
    public static final String JSON_VALUE_ROLE_HOUSEKEEPER = "housekeeper";
    public static final String JSON_VALUE_ROLE_FRIEND = "friend";
    public static final String JSON_VALUE_ROLE_RELATIVE = "relative";
    public static final String JSON_VALUE_ROLE_ELDERLY = "elderly";

    //add by ailynn for alarm8
    public final static String JSON_COMMAND_ACTION = "action";
    public final static String JSON_COMMAND_SUBACTION = "subaction";
    public final static String JSON_COMMAND_SUBACTION_ALARMINFO = "alarminfo";
    public final static String JSON_COMMAND_SUBACTION_RELAYINFO = "relayinfo";
    public final static String JSON_COMMAND_SUBACTION_SETDEVICE = "setdevice";
    public final static String JSON_COMMAND_SUBACTION_READDEVICE = "readdevice";
    public final static String JSON_COMMAND_TIME = "time";
    public final static String JSON_COMMAND_ACTION_EVENT = "event";
    public static final String JSON_COMMAND_DEVLOOPMAP = "deviceloopmap";
    public static final String JSON_COMMAND_MODULETYPE = "moduletype";
    public static final String JSON_COMMAND_LOOPID = "loopid";
    public static final String JSON_COMMAND_SWITCHSTATUS = "status";
    public static final String JSON_COMMAND_SUBACTION_ECCENC = "ecc256";
    public static final String JSON_COMMAND_SUBACTION_MESSAGE = "message";
    public static final String JSON_COMMAND_SUBACTION_PUBKEY = "pubkey";
    public static final String JSON_COMMAND_SUBACTION_SIGN = "sign";
    public final static String JSON_SUBACTION_VALUE_ALARMINFOTOAMS = "alarminfo2ams";

    //add for bulletin
    public final static String JSON_COMMAND_SUBACTION_BULLETINLST = "bulletinlistget";
    public final static String JSON_COMMAND_SUBACTION_BULLETINCONTENT = "bulletincontentget";
    public final static String JSON_COMMAND_SUBACTION_BULLETINNOTIFICATION = "bulletinnotification";
    public static final String COMMUNITY_DEVLOOPMAP = "deviceloopmap";
    public static final String COMMUNITY_KEY_EQUIP = "equip";
    public static final String COMMUNITY_KEY_TYPE = "type";
    public static final String COMMUNITY_KEY_PAGE = "page";
    public static final String COMMUNITY_KEY_PER = "per";
    public static final String COMMUNITY_KEY_NEXTPAGE = "isnextpage";
    public static final String COMMUNITY_KEY_COUNT = "count";
    public static final String COMMUNITY_KEY_ID = "id";
    public static final String COMMUNITY_KEY_TITLE = "title";
    public static final String COMMUNITY_KEY_DATE = "date";
    public static final String COMMUNITY_KEY_URL = "url";
    public static final String COMMUNITY_KEY_YEAR = "year";
    public static final String COMMUNITY_KEY_MONTH = "month";
    public static final String COMMUNITY_KEY_DAY = "day";
    public static final String COMMUNITY_KEY_HOUR = "hour";
    public static final String COMMUNITY_KEY_MIN = "min";
    public static final String COMMUNITY_KEY_SEC = "sec";
    public static final String COMMUNITY_KEY_ITEM = "item";
    public static final String COMMUNITY_KEY_VALUE = "value";
    public static final String COMMUNITY_KEY_DONGHO = "dongho";
    public static final String COMMUNITY_KEY_CONTENT = "content";
    public static final String COMMUNITY_KEY_LOGIN = "login";
    public static final String COMMUNITY_KEY_CALLTYPE = "calltype";
    public static final String COMMUNITY_KEY_IP = "ip";

    public static final String COMMUNITY_KEY_LOCKID = "lockid";
    public static final String COMMUNITY_KEY_LOWBATTERY = "lowbattery";
    public static final String COMMUNITY_KEY_LOCKCODE = "lockcode";

    public static final String COMMUNITY_KEY_HIJACK = "hijack";

    public static final String COMMUNITY_VALUE_EQUIP_WM = "WM";
    public static final String COMMUNITY_VALUE_EQUIP_GM1 = "GM1";
    public static final String COMMUNITY_VALUE_EQUIP_GM2 = "GM2";
    public static final String COMMUNITY_VALUE_EQUIP_GM3 = "GM3";

    public static final String COMMUNITY_VALUE_TYPE_REMOTE = "remote";
    public static final String COMMUNITY_VALUE_TYPE_MANAGEMENT = "management";
    public static final String COMMUNITY_VALUE_TYPE_ALL = "all";
    public static final String COMMUNITY_VALUE_TYPE_INDIVIDUAL = "individual";
    // scenario
    public static final String JSON_SCENARIO_ACTION_KEY = "action";
    public static final String JSON_ARM_KEY = "arm";
    public static final String JSON_ARM_VALUE_ARM = "arm";
    public static final String JSON_ARM_VALUE_DISARM = "disarm";

    public static final String JSON_IPDC_OPENFRONTDOOR = "openfrontdoor";
    public static final String JSON_IPDC_OPENBACKDOOR = "openbackdoor";
    //Max Voice Message Count supported
    public static final int MAX_VOICE_RECORD_COUNT = 3;
    public static final int MAX_NOTIFICATION_BULLTIN_COUNT = 1000;
    public static final int MAX_ALARM_COUNT = 1000;
    public static final int MAX_EVENT_COUNT = 1000;
    public static final int MAX_DEVICE_COUNT_ALARM = 4; //only 4 devices are supported


    /**
     * log level
     */
    public static final int LOG_LEVEL_DEBUG = 0;
    public static final int LOG_LEVEL_WARNING = 1;
    public static final int LOG_LEVEL_ERROR = 2;

    public static final int MAXALARMCASHCOUNT = 100;
    public static final int MAXALARMHISTROYCOUNT = 100;

    public final static String ALARMLOG_MODULETYPE = "moduletype";// string,
    public final static String ALARMLOG_MODULEADDR = "moduleaddr";//str,ip or mac
    public final static String ALARMLOG_LOOPID = "loopid";// str
    public final static String ALARMLOG_ALARMINFO = "alarminfo";// str
    public final static String ALARMLOG_TIMESTAMP = "timestamp";// long
    public final static String ALARMLOG_ROOMNAME = "roomname";// str
    public final static String ALARMLOG_LOOPNAME = "aliasname";// str
    public final static String ALARMLOG_ISTOCLOUD = "istocloud";// str

    // Full path name for services name

    public final static String ACTION_SERVICE_CALL = "com.honeywell.homepanel.call.CallService";
    public final static String ACTION_SERVICE_CLOUD = "com.honeywell.homepanel.cloud.CloudConnService";
    public final static String ACTION_SERVICE_COMMUNITY = "com.honeywell.homepanel.community.CommunityService";
    public final static String ACTION_SERVICE_CONFIG = "com.honeywell.homepanel.configcenter.ConfigService";
    public final static String ACTION_SERVICE_CONTROL = "com.honeywell.homepanel.control.ControlService";
    public final static String ACTION_SERVICE_DEVICEADAPTER = "com.honeywell.homepanel.deviceadapter.DeviceAdapterService";
    public final static String ACTION_SERVICE_IPDC = "com.honeywell.homepanel.ipdc.IPDCService";
    public final static String ACTION_SERVICE_PBX = "com.honeywell.homepanel.pbx.PBXService";
    public final static String ACTION_SERVICE_SUBCLIENT = "com.honeywell.homepanel.subphoneservice.SubphoneClient.SubPhoneClientService";
    public final static String ACTION_SERVICE_SUBSERVER = "com.honeywell.homepanel.subphoneservice.SubphoneServer.SubPhoneServerService";
    public final static String ACTION_SERVICE_SUBPHONEUI = "com.honeywell.homepanel.subphoneuiservice.SubPhoneUIService";
    public final static String ACTION_SERVICE_UPGRADE = "com.honeywell.homepanel.upgrade.upgrader.UpgraderService";
    public final static String ACTION_SERVICE_SUBUPGRADE = "com.honeywell.homepanel.upgrade.subupgrader.SubupgraderService";
    public final static String ACTION_SERVICE_AVRTP = "com.honeywell.homepanel.avrtp.AvRtp";
    public final static String ACTION_SERVICE_SENSING = "com.honeywell.homepanel.sensingservice.SensingService";
    public final static String ACTION_SERVICE_DISCOVERY = "com.honeywell.homepanel.miscservices.discovery.DiscoveryService";
    public final static String ACTION_SERVICE_LOGCAPTURE = "com.honeywell.homepanel.miscservices.logcat.LogCaptureService";
    public final static String ACTION_SERVICE_WIDGETINFO = "com.honeywell.homepanel.ui.services.WidgetInfoService";
    public final static String ACTION_SERVICE_NOTIFICATION = "com.honeywell.homepanel.ui.services.NotificationService";
    public final static String ACTION_SERVICE_WATCHDOG = "com.honeywell.homepanel.watchdog.WatchDogService";


    public final static String ACTION_ENGINEERING_CHANGE = "com.honeywell.homepanel.engineering.change";

    public final static String SIPAPP_PACKAGE_NAME = "com.honeywell.sipapp";
    public final static String SIPAPP_ACTIVITY_NAME = "com.honeywell.sipapp.MainActivity";
    public final static String SIPAPP_SERVICE_NAME = "com.honeywell.sipapp.SipAppService";

    public static final String PROJECT_CODE = "020106";

    public static final int VIDEO_WIDTH = 1920;
    public static final int VIDEO_HEIGHT = 1200;

    //configservice::::home panel config key
    public static final String KEY_DBVERSIONID = "dbversionid";//int
    public static final String KEY_SECURITY_PASSWORD = "securitypassword";//str
    public static final String KEY_CITY = JSON_KEY_CITY;//str
    public static final String KEY_UNIT = JSON_UNIT_KEY;//str
    public static final String KEY_FLOOR = CommonJson.JSON_FLOORNO_KEY;//int
    public static final String KEY_FRONTLOCKTYPE = "frontlocktype";//str
    public static final String KEY_BACKLOCKTYPE = "backlocktype";//str
    public static final String KEY_TYPE_MOMAS = "momas";//str
    public static final String KEY_TYPE_UNKNOWN = "unknown";//str
    public static final String KEY_TYPE_IPDCRELAY = "ipdcrelay";//str
    public static final String KEY_TYPE_NONE = "none";//str
    public static final String KEY_REGISTERPWD = "registerpwd";//str
    public static final String KEY_AMSIP = JSON_AMSIP_KEY;//str
    public static final String KEY_AMSPORT = JSON_AMSPORT_KEY;//int
    public static final String KEY_ELEVATORIP = CommonJson.JSON_ELEVATORIP_KEY;//str
    public static final String KEY_ELEVATORPORT = CommonJson.JSON_ELEVATORPORT_KEY;//int
    public static final String KEY_MAINIP = JSON_MAINIP_KEY;//str
    public static final String KEY_MAINPORT = JSON_MAINPORT_KEY;//int
    public static final String KEY_SUBPHONEID = JSON_SUBPHONEID_KEY;//int
    public static final String KEY_ENGINEERPWD = "engineerpwd";//str
    public static final String KEY_HOMEPANELTYPE = JSON_HOMEPANELTYPE_KEY;//int
    public static final String KEY_HOMESCREEN = "homescreen";//int
    public static final String KEY_TEMPER = "tamper";//home panel 防拆 int
    public static final String KEY_CURSCENARIO = "curscenario";//cur scenario uuid
    public static final String KEY_IPDC_FRONT_VERSION = "ipdcfrontversion";
    public static final String KEY_IPDC_BACK_VERSION = "ipdcbackversion";

    public static final String KEY_IPDC_FRONT_STATUS = "ipdcfrontstatus";
    public static final String KEY_IPDC_BACK_STATUS = "ipdcbackstatus";

    //add by ellen on 20150525 for ring and call voulme config

    public static final String KEY_VOLUME_PREFIX = "volume";
    public static final String KEY_VOLUME_RING = "volume_ring";
    public static final String KEY_VOLUME_NEIGHBOR = "volume_neighbor";
    public static final String KEY_VOLUME_LOBBY = "volume_lobby";
    public static final String KEY_VOLUME_GUARD = "volume_guard";
    public static final String KEY_VOLUME_INNER = "volume_inner";
    public static final String KEY_VOLUME_IPDC = "volume_ipdc";
    public static final String KEY_CURRENT_SCENARIO_ID = "currentscenarioid";
    public static final String KEY_CURRENT_SYS_ARMSTATE = "currsysarmstate";
    public static final String KEY_CARD_ACTION = "card_action";//str
    public static final int VOLUME_VALUE_DEFAULT = 15;
    //PbxSerice task shecdule
    public static final String TICK_MINIUTE_EVENT = "tickminiuteevent";
    public static final String TICK_GET_WEATHER_EVENT = "getweatherevent";

    // device adapter defines, formatted as "company, module type, device type"
    public static final String DEVADAPTER_ELEVATOR_IP_HON = "honeywell, ethernet, elevator";
    public static final String DEVADAPTER_ELEVATOR_IP_HON_DES = "honeywell homepanel ethernet based ip elevator";

    public static final String DEVADAPTER_LOCK_MOMAS = "honeywell, local uart, momas lock";
    public static final String DEVADAPTER_LOCK_MOMAS_DES = "Honeywell homepanel local uart based momas lock";

    public static final String DEVADAPTER_DOORBELL_LOCAL_HON = "honeywell, local io, door bell";
    public static final String DEVADAPTER_DOORBELL_LOCAL_HON_DES = "Honeywell homePanel local io based door bell";

    public static final String DEVADAPTER_ZONE_LOCAL_HON = "honeywell, local io, zone";
    public static final String DEVADAPTER_ZONE_LOCAL_HON_DES = "Honeywell homePanel local io based zone";

    public static final String DEVADAPTER_ZONE_HEJMODULE_HON = "honeywell, ethernet, hej alarm zone";
    public static final String DEVADAPTER_ZONE_HEJMODULE_HON_DES = "Honeywell ethernet based hej alarm zone";

    public static final String DEVADAPTER_RELAY_HEJMODULE_HON = "honeywell, ethernet, hej relay";
    public static final String DEVADAPTER_RELAY_HEJMODULE_HON_DES = "Honeywell ethernet based hej relay";

    public static final String DEVADAPTER_AC_485_DAKIN = "dakin, local 485, aircondition";
    public static final String DEVADAPTER_AC_485_DAKIN_DES = "Dakin homepanel local 485 based air condition";

    public static final String DEVADAPTER_AC_485_TOSHIBA = "toshiba, local 485, aircondition";
    public static final String DEVADAPTER_AC_485_TOSHIBA_DES = "Toshiba homepanel local 485 based air condition";

    public static final String DEVADAPTER_AC_485_SANLING = "mitsubishi, local 485, aircondition";
    public static final String DEVADAPTER_AC_485_SANLING_DES = "Mitsubishi homepanel local 485 based air condition";

    // scenario ids
    public static final String SCENARIO_ID_HOME = "1";
    public static final String SCENARIO_ID_AWAY = "2";
    public static final String SCENARIO_ID_SLEEP = "3";
    public static final String SCENARIO_ID_WAKEUP = "4";

    /**
     * Zone status detail types
     */
    // general alarms
    public static final String ZONE_ALARM_STATUS_GENERAL_NORMAL = "normal";
    public static final String ZONE_ALARM_STATUS_GENERAL_TRIGGERED = "trigger";
    public static final String ZONE_ALARM_STATUS_GENERAL_TAMPER = "tamper";
    public static final String ZONE_ALARM_STATUS_GENERAL_HIJACK = "hijack";
    public static final String ZONE_ALARM_STATUS_GENERAL_LOWBATTR = "lowbattery";
    public static final String ZONE_ALARM_STATUS_GENERAL_INTRUSION = "intrusion";
    public static final String ZONE_ALARM_STATUS_GENERAL_ZONETROUBLE = "zonetrouble";
    public static final String ZONE_ALARM_STATUS_GENERAL_PM2P5 = "pm2.5limit";
    public static final String ZONE_ALARM_STATUS_GENERAL_MAGNETIC = "door_magnetic";//门磁
    public static final String ZONE_ALARM_STATUS_GENERAL_HEARTHEAT = "heartheat";//
    public static final String ZONE_ALARM_STATUS_GENERAL_RESET = "reset";//sensor has been reset
    public static final String ZONE_ALARM_STATUS_GENERAL_MOTION = "motion";
    public static final String ZONE_ALARM_STATUS_GENERAL_THIEF = "thief";//红外sensor

    // ALARMS from ipvdp system
    public static final String ZONE_ALARM_STATUS_IPVDP_GAS = "gas";
    public static final String ZONE_ALARM_STATUS_IPVDP_FIRE = "fire";
    public static final String ZONE_ALARM_STATUS_IPVDP_HELP = "help";
    public static final String ZONE_ALARM_STATUS_IPVDP_THIEF = "thief";
    public static final String ZONE_ALARM_STATUS_IPVDP_EMERGENCY = "emergency";
    public static final String ZONE_ALARM_STATUS_IPVDP_EMERGENCYSILENCE = "emergencysilence";
    public static final String ZONE_ALARM_STATUS_IPVDP_ARMALL = "armall";
    public static final String ZONE_ALARM_STATUS_IPVDP_DISARMALL = "disarmall";
    public static final String IPVDPZONELOOP = "ipvdpzoneloopmap";
    public static final String ZONE_ALARM_STATUS_IPVDP_MEDICALAID = "medicalaid";
    public static final String ZONE_ALARM_STATUS_IPVDP_LOCKTEMPER = ZONE_ALARM_STATUS_GENERAL_TAMPER;
    public static final String ZONE_ALARM_STATUS_IPVDP_LOCKLOWBATTR = ZONE_ALARM_STATUS_GENERAL_LOWBATTR;
    public static final String ZONE_ALARM_STATUS_IPVDP_LOCK_HIJACK = ZONE_ALARM_STATUS_GENERAL_HIJACK;
    public static final String ZONE_ALARM_STATUS_IPVDP_LOCK_WRONGPWD = "pwderror";
    public static final String ZONE_ALARM_STATUS_IPVDP_LOCK_OPEN = "lockopen";
    public static final String ZONE_ALARM_STATUS_IPVDP_DOOR_OPEN = "dooropen";
    public static final String ZONE_ALARM_STATUS_IPVDP_DOOR_CLOSE = "doorclose";
    public static final String IPVDPLOCKLOOP = "ipvdplockloopmap";

    // Alarms from wired zone
    public static final String ZONE_ALARM_STATUS_WIRED_HIGH = "open";
    public static final String ZONE_ALARM_STATUS_WIRED_LOW = "close";
    public static final String ZONE_ALARM_STATUS_NORMAL = "normal";
    public static final String ZONE_ALARM_STATUS_TRIGGER = "trigger";

    // Alarms from wireless zone
    public static final String ZONE_ALARM_STATUS_WIRELESS_LOWBATTR = ZONE_ALARM_STATUS_GENERAL_LOWBATTR;
    public static final String ZONE_ALARM_STATUS_WIRELESS_TAMPER = ZONE_ALARM_STATUS_GENERAL_TAMPER;

    /********************************** broadcast intent extras ****************************************/
    public static final String INTENT_EXTRA_DATA = "data";
    public static final String INTENT_EXTRA_DATA_LEN = "datalen";
    public static final String INTENT_EXTRA_DATA_JSONCLASS = "jsonclass";

    /********************************** broadcast intent actions ****************************************/
    public final static String INTENT_ACTION_NOTUSE = "com.honeywell.cubebase.broadcast.nouse";
    public static final String INTENT_ACTION_EVENTBUS_MESSAGE = "com.honeywell.homepanel.eventbusmessage";
    public static final String INTENT_ACTION_ALARM_INFO = "com.honeywell.homepanel.broadcast.alarminfo";
    public static final String INTENT_ACTION_ASSISTALARM_INFO = "com.honeywell.homepanel.broadcast.assistalarminfo";
    public final static String INTETN_ACTION_ENVIROMENT_INFO = "com.honeywell.homepanel.broadcast.envirometninfo";
    public final static String INTENT_ACTION_CONFIGINFO_CHANGED = "com.honeywell.homepanel.broadcast.configinfoupdated";
    public static final String INTENT_ACTION_LOCKSTATUS_CHANGED = "com.honeywell.homepanel.broadcast.lockstatusupdated";
    public static final String INTENT_ACTION_DOORBELLSTATUS_CHANGED = "com.honeywell.homepanel.broadcast.doorbellstatusupdated";
    public static final String INTENT_ACTION_CLEARREGISTEREDPHONES = "com.honeywell.homepanel.broadcast.clearregisteredphones";

    public static final String INTENT_ACTION_EXTENSIONMODULE_CLOUD = "com.honeywell.homepanel.broadcast.extensionmodulecloud";

    public static final String INTENT_ACTION_SCENE_CHANGED = "com.honeywell.homepanel.broadcast.scene.changed";
    public static final String INTENT_NOTIFY_ACTIVITY_TIMEOUT = "com.honeywell.lock.NOTIFY_ACTIVITY_TIMEOUT";

    public static final String JSON_CONFIGDATA_CATEGORY_KEY = "category";
    public static final String JSON_CONFIGDATA_CATEGORY_PRIVATE = "privateconfig";
    public static final String JSON_CONFIGDATA_CATEGORY_PUBLIC = "publicconfig";
    public static final String JSON_CONFIGDATA_CATEGORY_SYSSETTINGS = "systemsettings";

    public static final String JSON_CONFIGDATA_CONFIGNAME_KEY = "configname";
    public static final String JSON_CONFIGDATA_CONFIGNAME_DEFAULT = "default";
    public static final String JSON_CONFIGDATA_CONFIGNAME_COMMONDEVLIST = "commondevice";
    public static final String JSON_CONFIGDATA_CONFIGNAME_EXTENTIONMODULE = "extensionmodule";
    public static final String JSON_CONFIGDATA_CONFIGNAME_EXTENSIONRELAY = "extensionrelay";
    public static final String JSON_CONFIGDATA_CONFIGNAME_EXTENSIONZONE = "extensionzone";
    public static final String JSON_CONFIGDATA_CONFIGNAME_LOCALDEVICE = "localdevice";
    public static final String JSON_CONFIGDATA_CONFIGNAME_SCENARIO = "scenario";


    public static final String MQTT_REGISTER_HOME_PANEL = "com.honeywell.homepanel.register";//注册
    public static final String MQTT_BIND_HOME_PANEL = "com.honeywell.homepanel.bind";//绑定
    public static final String MQTT_BIND_USER_ID = "com.honeywell.homepanel.bind_user_id";//绑定
    public static final String MQTT_BIND_PHONE_NUM = "com.honeywell.homepanel.bind.phone.num";//绑定

    public static final String TIME_STR_FORMAT = "yyyyMMdd-HHmmss";

    public static final int HOMEPANEL_TYPE_MAIN = 0;
    public static final int HOMEPANEL_TYPE_SUB = 1;
    public static final int CALL_HISTORY_MAX_COUNT = 32;
    public static final int DEFAULT_IPC_COUNT = 11;
    public static final String CARD_ACTION_DOOROPEN = "dooropen";
    public static final String CARD_ACTION_ARM = "arm";
    public static final String CARD_ACTION_DOOROPEN_ARM = "dooropen & arm";

    public static final String JSON_FRONTDOOR = "2001";
    public static final String JSON_BACKDOOR = "2002";


    public static final int IPDC_FRONTDOOR = 2001;
    public static final int IPDC_BACKDOOR = 2002;


    public static final int MAX_SUPPORT_DB_ALARM_COUNT = 100;
    public static final String LOOP_IP = "127.0.0.1";

    public static final String JSON_MESSAGETYPE = "messageType";
    public static final String JSON_MESSAGEID = "messageId";
    public static final String JSON_CALLERTYPE = "callerType";
    public static final String JSON_DATA = "data";
    public static final String JSON_CALLERDATA = "callerData";
    public static final String JSON_TYPE = "type";
    public static final String JSON_CRYPTOKEY_CLOUD = "cryptoKey";
    public static final String JSON_SESSIONID = "sessionId";
    public static final String JSON_VIDEO_CALL = "videoCall";
    public static final String JSON_FLAGS = "flags";
    public static final String JSON_NEED_RESPONSE = "needResponse";
    public static final String JSON_KEY_COMMANDS = "commands";
    public static final String JSON_RESULT = "result";
    public static final String JSON_REQUEST = "request";
    public static final String WRONG_PWD = "wrongPassword";
    public static final String DOOROPENED = "doorOpened";
    public static final String OPENDOORRESULT = "openDoorResult";

    public static final String JSON_DEVICEID_CLOUD = "deviceId";
    public static final String JSON_CLIENDUUID_CLOUD = "clientUuid";
    public static final String JSON_TIMESTAMP_CLOUD = "timestamp";
    public static final String JSON_SESSIONKEY_CLOUD = "sessionKey";
    public static final String JSON_SECRET_CLOUD = "secret";


    public static final String JSON_SUBACTION_CALLIN_CLOUD = "callIn";
    public static final String JSON_SUBACTION_PICKUPCALL_CLOUD = "pickUpCall";
    public static final String JSON_SUBACTION_OPENDOOR_CLOUD = "openDoor";
    public static final String JSON_SUBACTION_ENDCALL_CLOUD = "endCall";
    public static final String JSON_SUBACTION_CALLENDED_CLOUD = "callEnded";

    public static final String JSON_SUBACTION_CALL_PICKEDUP_CLOUD = "callPickedUp";

    public static final String JSON_SUBACTION_USERID = "userId";

    public static final String ETHERNET_IP = "ethernetip";
    public static final String ETHERNET_NETMASK = "ethernetnetmask";
    public static final String ETHERNET_GATEWAY = "ethernetgateway";
    public static final String SCREENSAVER_QUIT = "screensaverquit";
    public static final String SCREENOFF = "screenoff";
    public static final String DEVICE_ONLINE = "1";
    public static final String DEVICE_OFFLINE = "0";


    public static final int APP_MSG_DEFAULT = 0;
    public static final int APP_MSG_VIDEO = 1;
    public static final int APP_MSG_VIDEO_CRYPT = 2;
    public static final int APP_MSG_AUDIO = 3;
    public static final int APP_MSG_AUDIO_CRYPT = 4;
    public static final int CALLIN_TIMEOUT = 30;// should change to 30 seconds

    public static final String ARMED_STATUS = "0";// 0: system disarmed 1:system armed

    public static final String JSON_KEY_TRIGGERID = "triggerid";

    public static final int SCREEN_DISABLE = 1;
    public static final int SCREEN_ENABLE = 2;
    public static final int SCREEN_WEAKUP = 3;
    public static final int SCREEN_QUIT = 4;
    public static final int SCREEN_WEAKUP_AND_DISABLE = 10;
    public static final int SCREEN_WEAKUP_AND_ENABLE = 11;


    public static final int SCREEN_WAKEUP_SRC_NORMAL = 2;
    public static final int SCREEN_WAKEUP_SRC_STARTUP = 1;
    public static final int SCREEN_WAKEUP_SRC_MANUAL = 3;
    public static final int SCREEN_WAKEUP_SRC_AUTOEVENT = 11;

    public static final int CALLING_TIMEOUT = 30 * 1000;

    public static final int CALLED_TIMEOUT = 3 * 60 * 1000;


    public static final String DEF_PAAS_IP_KIP = "qa.iot1.acscloud.honeywell.com.cn:8883";
    public static final String DEF_PAAS_ADDRESS = "iot1.homecloud.honeywell.com.cn:8883";
    public static final String DEF_PAAS_IP_DEV = "tcp://115.159.152.188:1883";
    public static final String DEF_PAAS_IP_QA = "tcp://115.159.95.88:1883";
    public static final String KPI = "honeywell.homepanel.kpi";
    public static final String PAAS_IP = "honeywell.paas.ip";


    public  static  final  int PWD_TYPE_ALARM = 0;
    public  static  final  int PWD_TYPE_ENGINEER = 1;
    public  static  final  int PWD_TYPE_REGISTER = 2;

    public static String file_() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName();
    }

    public static int line_() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getLineNumber();
    }


}
