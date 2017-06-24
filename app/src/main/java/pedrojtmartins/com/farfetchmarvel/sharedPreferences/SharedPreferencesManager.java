package pedrojtmartins.com.farfetchmarvel.sharedPreferences;


import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 22/06/2017
 */

public class SharedPreferencesManager {
    private SharedPreferences sharedPreferences;

    private final String SHARED_PREFS = "sharedPrefs";
    private final String FAVOURITES = "favourites";

    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public ArrayList<Long> getFavourites() {
        ArrayList<Long> favourites = new ArrayList<>();

//        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String sFavourites = sharedPreferences.getString(FAVOURITES, null);
        if (sFavourites == null)
            return favourites;

        String[] arrFavourites = sFavourites.split(";");
        if (arrFavourites.length == 0)
            return favourites;

        // TODO: 22/06/2017 improve
        for (String fav : arrFavourites) {
            favourites.add(Long.parseLong(fav.trim()));
        }

        return favourites;
    }

    public void addFavourites(ArrayList<Long> favourites) {

        String serialized = "";
        for (Long fav : favourites) {
            if (!serialized.isEmpty())
                serialized += ";";

            serialized += fav;
        }

//        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FAVOURITES, serialized);
        editor.apply();
    }
}
