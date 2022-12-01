package com.example.pr7.ui.filmdetail.models.videomodel;

import java.util.ArrayList;

public class FilmVideos {
    private int total;
    private ArrayList<Item> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
