package com.example.pr7.utils;

import com.example.pr7.ui.filmdetail.models.videomodel.Item;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String getIdFromUrl(String url)  {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url); //url is youtube url for which you want to extract the id.
        if (matcher.find()) {
            return matcher.group();
        }
        else
        {
            return null;
        }
    }

    public static Item getYoutubeItemFromArray(ArrayList<Item> items){
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            String site = item.getSite();
            if (site.matches("YOUTUBE"))
            {
                return item;
            }
        }
        return null;
    }
}

