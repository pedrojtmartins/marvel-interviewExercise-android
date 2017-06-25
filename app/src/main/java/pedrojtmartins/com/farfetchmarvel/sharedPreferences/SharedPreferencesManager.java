package pedrojtmartins.com.farfetchmarvel.sharedPreferences;


import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 22/06/2017
 */

public class SharedPreferencesManager {
    private SharedPreferences sharedPreferences;

    public static final String SHARED_PREFS = "sharedPrefs";
    private final String FAVOURITES = "favourites";

    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * @return list of ids of all favourite characters
     */
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

    /**
     * Adds the new favourite id
     *
     * @param favourites character id
     * @return updated list of ids of all favourite characters
     */
    public ArrayList<Long> addFavourites(Long favourites) {
        ArrayList<Long> list = getFavourites();
        if (list == null)
            list = new ArrayList<>();

        if (list.contains(favourites)) {
            list.remove(favourites);
        } else {
            list.add(favourites);
        }

        String serialized = "";
        for (Long fav : list) {
            if (!serialized.isEmpty())
                serialized += ";";

            serialized += fav;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FAVOURITES, serialized);
        editor.apply();

        return getFavourites();
    }
}
