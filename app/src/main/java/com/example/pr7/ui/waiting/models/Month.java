package com.example.pr7.ui.waiting.models;

import java.util.ArrayList;

public class Month {
    ArrayList<String> monthList = new ArrayList<>();

    public Month(){
        monthList.add("JANUARY");
        monthList.add("FEBRUARY");
        monthList.add("MARCH");
        monthList.add("APRIL");
        monthList.add("MAY");
        monthList.add("JUNE");
        monthList.add("JULY");
        monthList.add("AUGUST");
        monthList.add("SEPTEMBER");
        monthList.add("OCTOBER");
        monthList.add("NOVEMBER");
        monthList.add("DECEMBER");
    }

    public ArrayList<String> getMonthList() {
        return monthList;
    }
}
