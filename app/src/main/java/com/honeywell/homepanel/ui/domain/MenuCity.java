package com.honeywell.homepanel.ui.domain;

import java.util.ArrayList;

/**
 * Created by H135901 on 3/10/2017.
 */

public class MenuCity {
    public String provinceStr = "";
    public ArrayList<String> citiesStr = null;

    public MenuCity() {
    }

    public String getProvinceStr() {
        return provinceStr;
    }

    public void setProvinceStr(String provinceStr) {
        this.provinceStr = provinceStr;
    }

    public ArrayList<String> getCitiesStr() {
        return citiesStr;
    }

    public void setCitiesStr(ArrayList<String> citiesStr) {
        this.citiesStr = citiesStr;
    }
}
