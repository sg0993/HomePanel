package com.honeywell.homepanel.Utils;

import android.content.Context;

import com.honeywell.homepanel.Utils.plist_parser.xml.plist.PListXMLHandler;
import com.honeywell.homepanel.Utils.plist_parser.xml.plist.PListXMLParser;
import com.honeywell.homepanel.Utils.plist_parser.xml.plist.domain.Array;
import com.honeywell.homepanel.Utils.plist_parser.xml.plist.domain.Dict;
import com.honeywell.homepanel.Utils.plist_parser.xml.plist.domain.PList;
import com.honeywell.homepanel.Utils.plist_parser.xml.plist.domain.PListObject;
import com.honeywell.homepanel.ui.domain.MenuCity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by H157925 on 16/5/12. 14:59
 * Email:Shodong.Sun@honeywell.com
 * 用于对应的Plist文件解析
 */
public class PlistUtil {
    private static String TAG = PlistUtil.class.getSimpleName();
    private Context mContext;
    private static PListXMLParser parser = new PListXMLParser();
    private static PListXMLHandler handler = new PListXMLHandler();
    private static PlistUtil plistUtil = null;

    public static PlistUtil newInstance(Context context) {
        if (plistUtil == null) {
            plistUtil = new PlistUtil(context);
        }
        return plistUtil;
    }

    private PlistUtil(Context context) {
        this.mContext = context;
        parser.setHandler(handler);
    }

    /**
     * 解析数组型Plist文件
     *
     * @param fileName
     * @return
     */
    public static synchronized ArrayList<Object> parseArrayPlistWithName(String fileName) {
        return plistUtil.parceArrayPlist(fileName);
    }

    /**
     * 解析复合型plist 里面既有数组 又有字符串
     *
     * @param fileName
     * @return
     */
    public static synchronized ArrayList<Object> parceMultiArrayWithName(String fileName) {
        return plistUtil.parceMultiArrayPlist(fileName);
    }


    /**
     * 解析字典型Plist文件
     *
     * @param fileName
     * @return
     */
    public static Map<String, PListObject> parseDictPlistWithName(String fileName) {
        return plistUtil.parcePlist(fileName);
    }


    /**
     * 私有方法，解析plist文件,标准
     *
     * @param fileName
     */
    private synchronized Map<String, PListObject> parcePlist(String fileName) {
        try {
            parser.parse(plistUtil.mContext.getAssets().open(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PList actualPlist = ((PListXMLHandler) parser.getHandler()).getPlist();
        Dict root = (Dict) actualPlist.getRootElement();

        Map<String, PListObject> items = root.getConfigMap();
        return items;

    }

    /**
     * 私有方法，解析plist文件，非标准
     *
     * @param fileName
     * @return
     */
    private synchronized ArrayList<Object> parceArrayPlist(String fileName) {
        try {
            parser.parse(mContext.getAssets().open(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PList actualPlist = ((PListXMLHandler) parser.getHandler()).getPlist();
        Array array = (Array) actualPlist.getRootElement();
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (int i = 0; i < array.size(); i++) {
            Dict dic = (Dict) array.get(i);
            Map<java.lang.String, PListObject> temp = dic.getConfigMap();
            Map<java.lang.String, java.lang.String> item = new HashMap<String, String>();

            for (java.lang.String key : temp.keySet()) {
                java.lang.String str = ((com.honeywell.homepanel.Utils.plist_parser.xml.plist.domain.String) temp.get(key)).getValue();
                item.put(key, str);
            }
            arrayList.add(item);
        }
        return arrayList;
    }

    /**
     * 解析复合型数据
     *
     * @param fileName
     * @return
     */
    private synchronized ArrayList<Object> parceMultiArrayPlist(String fileName) {
        try {
            parser.parse(mContext.getAssets().open(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PList actualPlist = ((PListXMLHandler) parser.getHandler()).getPlist();
        PListObject plistObject = actualPlist.getRootElement();
        Array array = null;
        if (plistObject instanceof Array) {
            array = (Array) plistObject;
        } else {
            return null;
        }
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (int i = 0; i < array.size(); i++) {
            Dict dic = (Dict) array.get(i);
            Map<java.lang.String, PListObject> temp = dic.getConfigMap();
            Map<java.lang.String, Object> item = new HashMap<String, Object>();

            for (java.lang.String key : temp.keySet()) {
                PListObject object = temp.get(key);
                if (object instanceof com.honeywell.homepanel.Utils.plist_parser.xml.plist.domain.String) {
                    java.lang.String str = ((com.honeywell.homepanel.Utils.plist_parser.xml.plist.domain.String) temp.get(key)).getValue();
                    item.put(key, str);
                } else if (object instanceof Array) {
                    Array arrayObject = (Array) temp.get(key);
                    ArrayList<PListObject> plistObjects = arrayObject.data;
                    ArrayList<Object> itemArray = new ArrayList<Object>();
                    if (plistObjects.size() == 0) {
                        item.put(key, itemArray);
                    } else {
                        for (PListObject object1 : plistObjects) {
                            java.lang.String str = ((com.honeywell.homepanel.Utils.plist_parser.xml.plist.domain.String) object1).getValue();
                            itemArray.add(str);
                        }
                        item.put(key, itemArray);
                    }
                }
            }
            arrayList.add(item);
        }
        return arrayList;
    }


    public static ArrayList<MenuCity> getDefaultCities(Context context) {
        PlistUtil.newInstance(context);
        ArrayList<Object> items = PlistUtil.parceMultiArrayWithName("Citys.plist");
        if (items == null) {
            return null;
        }
        ArrayList<MenuCity> returnArray = new ArrayList<MenuCity>();
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> item = (Map<String, Object>) items.get(i);
            ArrayList<Object> cityArr = (ArrayList<Object>) item.get("Cities");
            String province = (String) item.get("ProvinceName");
            MenuCity menuCity = new MenuCity();
            menuCity.citiesStr = cityArr;
            menuCity.provinceStr = province;
            returnArray.add(menuCity);
        }
        return returnArray;
    }

}
