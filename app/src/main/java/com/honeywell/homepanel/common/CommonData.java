package com.honeywell.homepanel.common;
/**
 * Created by H135901 on 1/24/2017.
 */

public class CommonData {
    public static final  int LEFT_SELECT_HOME = 0;
    public static final  int LEFT_SELECT_SCENARIOEDIT = 1;
    public static final  int LEFT_SELECT_DEVICEEDIT = 2;
    public static final  int LEFT_SELECT_MESSAGE = 3;
    public static final  int LEFT_SELECT_DIAL = 4;
    public static final  int LEFT_SELECT_SETTING = 5;

    public static final String WEATHER_SUNNY = "sunny";

    public static final String TEMPERATURE_DUSTR = "°";

    public static final  String UNHEALTHY = "unhealthy";
    public static final  String ARMSTATUS_ARM = "System Armed";
    public static final  String ARMSTATUS_DISARM = "System Disarmed";

    public static final  int WIFI_CONNECTED = 0;
    public static final  int WIFI_DISCONNECT = 1;

    public static final int SRCURITY_PASSWORD_LENGTH = 6;


    public static final  int SCENARIO_HOME = 1;
    public static final  int SCENARIO_AWAY = 2;
    public static final  int SCENARIO_SLEEP = 3;
    public static final  int SCENARIO_WAKEUP = 4;
	public static final  int SENCES_EDIT = 5;

    public static final  String INTENT_KEY_SCENARIO = "cur_scenario";
    public static final  String INTENT_KEY_CALL_TYPE = "call_type";
    public static final  String INTENT_KEY_UNIT = "unit";

    public static  final int CALL_OUTGOING_NEIGHBOR = 0;
    public static  final int CALL_INCOMING_NEIGHBOR = 1;
    public static  final int CALL_CONNECTED_AUDIO_NETGHBOR = 2;
    public static  final int CALL_CONNECTED_VIDEO_NETGHBOR = 3;
    public static  final int CALL_LOBBY_INCOMMING = 4;
    public static  final int CALL_LOBBY_CONNECTED = 5;
	
	// Standard Call
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
