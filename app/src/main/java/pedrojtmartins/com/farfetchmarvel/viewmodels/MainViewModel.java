package pedrojtmartins.com.farfetchmarvel.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import pedrojtmartins.com.farfetchmarvel.api.MarvelAPI;
import pedrojtmartins.com.farfetchmarvel.models.MainStatus;
import pedrojtmartins.com.farfetchmarvel.models.MarvelDetails;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;
import pedrojtmartins.com.farfetchmarvel.settings.Settings;
import pedrojtmartins.com.farfetchmarvel.sharedPreferences.SharedPreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pedrojtmartins.com.farfetchmarvel.settings.Settings.DETAILS_COUNT;
import static pedrojtmartins.com.farfetchmarvel.settings.Settings.PAGINATION_ITEMS_COUNT;

/**
 * Pedro Martins
 * 20/06/2017
 */

public class MainViewModel {

    //region Variables
    // Keeps track of the app state.
    public MainStatus mainStatus;

    // Holds the characters that are ready to be displayed.
    private ObservableArrayList<MarvelModel.Character> characters;

    // Holds the filtered characters searched by name that are ready to be displayed.
    private ObservableArrayList<MarvelModel.Character> filteredCharacters;

    // Last filter used
    private String filter;

    // Selected character that will download extra info
    private MarvelModel.Character selectedCharacter;

    // Shared Preferences manager that will handle favourites
    private SharedPreferencesManager spManager;

    // To warn the UI when an api call fails
    public ObservableInt apiCallResult;
    //endregion

    //region Initialization
    public MainViewModel(SharedPreferencesManager spManager) {
        this.spManager = spManager;

        characters = new ObservableArrayList<>();
        filteredCharacters = new ObservableArrayList<>();
        apiCallResult = new ObservableInt(0);

        mainStatus = new MainStatus();
    }

    /**
     * Starts downloading first characters
     */
    public void initialize() {
        if (characters.isEmpty()) {
            // Only needs to download first characters
            // if they are not already cached
            loadCharacters();
        }
    }
    //endregion

    //region Data pagination
    /**
     * Updates to the previous page if possible
     */
    public void previousPage() {
        if (mainStatus.getCurrPage() <= 0)
            return;

        mainStatus.setCurrPage(mainStatus.getCurrPage() - 1);
    }

    /**
     * Updates to the next page if possible.
     * Characters will be downloaded if not cached yet
     */
    public void nextPage() {
        int currTargetPage = mainStatus.getCurrPage() + 1;
        if (currTargetPage >= mainStatus.getAvailablePagesInt()) {
            //No more pages available. Do nothing
            return;
        }

        int firstTargetIndex = currTargetPage * Settings.PAGINATION_ITEMS_COUNT;

        if (mainStatus.currentList == MainStatus.NORMAL_LIST)
            nextNormalPage(firstTargetIndex);
        else
            nextFilteredPage(firstTargetIndex);
    }

    private void nextNormalPage(int firstTargetIndex) {
        if (characters.size() > firstTargetIndex) {
            // We already have the characters cached. Just update the current page
            mainStatus.setCurrPage(mainStatus.getCurrPage() + 1);
        } else {
            // The target characters are not cached. Download them.
            loadCharacters();
        }

    }
    private void nextFilteredPage(int firstTargetIndex) {
        if (filteredCharacters.size() > firstTargetIndex) {
            // We already have the characters cached. Just update the current page
            mainStatus.setCurrPage(mainStatus.getCurrPage() + 1);
        } else {
            // The target characters are not cached. Download them.
            loadFilteredCharacters(filter);
        }
    }
    // endregion

    //region Main lists handling
    /**
     * @return list of characters
     */
    public ObservableArrayList<MarvelModel.Character> getCharacters() {
        return mainStatus.currentList == MainStatus.NORMAL_LIST ? characters : filteredCharacters;
    }

    private void loadCharacters() {
        mainStatus.setLoadingCharacters(true);
        MarvelAPI.getInstance().getCharacters(PAGINATION_ITEMS_COUNT, characters.size()).enqueue(new Callback<MarvelModel>() {
            @Override
            public void onResponse(@NonNull Call<MarvelModel> call, @NonNull Response<MarvelModel> response) {
                mainStatus.setLoadingCharacters(false);
                if (response.isSuccessful()) {
                    MarvelModel apiResponse = response.body();
                    if (apiResponse != null && apiResponse.data != null && apiResponse.data.characters != null) {
                        if (characters.size() == 0) {
                            // If this is the first call, find out how many pages there are.
                            mainStatus.setAvailablePages((int) Math.ceil((double) apiResponse.data.total / Settings.PAGINATION_ITEMS_COUNT));
                        }

                        ArrayList<MarvelModel.Character> updatedCharacters = findFavourites(apiResponse.data.characters);
                        characters.addAll(updatedCharacters);
                        mainStatus.setCurrPage(mainStatus.getCurrPage() + 1);
                    }
                } else {
                    apiCallResult.set(-1);
                }
            }
            @Override
            public void onFailure(@NonNull Call<MarvelModel> call, @NonNull Throwable t) {
                mainStatus.setLoadingCharacters(false);
                apiCallResult.set(-1);
            }
        });
    }

    /**
     * Download (more) characters whose names start with 'query'.
     *
     * @param query Name filter
     */
    public void loadFilteredCharacters(String query) {
        if (query == null || query.isEmpty() || !query.equals(filter)) {
            // The filter is empty or was dismissed OR
            // this is a new search and we need to reset the state
            filteredCharacters.clear();
            mainStatus.resetFilterStatus();

            if (query == null || query.isEmpty())
                return;
        }

        mainStatus.currentList = MainStatus.FILTERED_LIST;
        mainStatus.setLoadingCharacters(true);
        filter = query;

        MarvelAPI.getInstance().getCharactersByName(filter, PAGINATION_ITEMS_COUNT, filteredCharacters.size()).enqueue(new Callback<MarvelModel>() {
            @Override
            public void onResponse(@NonNull Call<MarvelModel> call, @NonNull Response<MarvelModel> response) {
                mainStatus.setLoadingCharacters(false);
                if (response.isSuccessful()) {
                    MarvelModel apiResponse = response.body();
                    if (apiResponse != null && apiResponse.data != null && apiResponse.data.characters != null) {
                        if (filteredCharacters.size() == 0) {
                            // If this is the first call, find out how many pages there are.
                            mainStatus.setAvailablePages((int) Math.ceil((double) apiResponse.data.total / Settings.PAGINATION_ITEMS_COUNT));
                        }

                        filteredCharacters.addAll(findFavourites(apiResponse.data.characters));
                        mainStatus.setCurrPage(mainStatus.getCurrPage() + 1);
                    }
                } else {
                    apiCallResult.set(-1);
                }
            }
            @Override
            public void onFailure(@NonNull Call<MarvelModel> call, @NonNull Throwable t) {
                mainStatus.setLoadingCharacters(false);
                apiCallResult.set(-1);
            }
        });
    }
    //endregion

    //region Favourites helper
    private ArrayList<MarvelModel.Character> findFavourites(ArrayList<MarvelModel.Character> characters) {
        ArrayList<Long> favourites = spManager.getFavourites();

        for (MarvelModel.Character character : characters) {
            character.setFavourite(favourites.contains(character.id));
        }

        return characters;
    }
    //endregion

    //region Item selection
    /**
     * @return Last selected character
     */
    public MarvelModel.Character getSelectedCharacter() {
        return selectedCharacter;
    }

    /**
     * @param character Item that was pressed
     */
    public void onCharacterSelected(MarvelModel.Character character) {
        selectedCharacter = character;
    }


    /**
     * @param character Item that was long pressed
     */
    public void onCharacterLongPressed(MarvelModel.Character character) {
        spManager.addFavourites(character.id);
        character.setFavourite(!character.isFavourite);
    }
    //endregion

    //region Details
    /**
     * Will start the download of required information.
     * Data binding should be used for updates when ready.
     *
     * @param character Item that is selected
     */
    public void getDetails(final MarvelModel.Character character) {
        if (character == null || character.comics == null || character.comics.comicItems == null)
            return;

        downloadComicDetails(character);
        downloadEventDetails(character);
        downloadStoryDetails(character);
        downloadSeriesDetails(character);
    }

    private void downloadComicDetails(MarvelModel.Character character) {
        for (int i = 0; i < DETAILS_COUNT && i < character.comics.comicItems.size(); i++) {
            final MarvelModel.Comic comic = character.comics.comicItems.get(i);
            comic.loaded = new ObservableBoolean(false);

            MarvelAPI.getInstance().getDetails(comic.resourceURI).enqueue(new Callback<MarvelDetails>() {
                @Override
                public void onResponse(@NonNull Call<MarvelDetails> call, @NonNull Response<MarvelDetails> response) {
                    if (response.isSuccessful()) {
                        MarvelDetails details = response.body();
                        if (details != null && details.data != null &&
                                details.data.results != null && details.data.results.size() > 0) {

                            MarvelDetails.Result result = details.data.results.get(0);
                            comic.setDescription(result.description);

                            if (result.thumbnail != null)
                                comic.setThumbnail(result.thumbnail.path, result.thumbnail.extension);

                            comic.setLoaded(true);
                        }
                    } else {
                        apiCallResult.set(-1);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<MarvelDetails> call, @NonNull Throwable t) {
                    apiCallResult.set(-1);
                }
            });
        }
    }
    private void downloadEventDetails(MarvelModel.Character character) {
        for (int i = 0; i < DETAILS_COUNT && i < character.events.eventItems.size(); i++) {
            final MarvelModel.Event event = character.events.eventItems.get(i);
            event.loaded = new ObservableBoolean(false);

            MarvelAPI.getInstance().getDetails(event.resourceURI).enqueue(new Callback<MarvelDetails>() {
                @Override
                public void onResponse(@NonNull Call<MarvelDetails> call, @NonNull Response<MarvelDetails> response) {
                    if (response.isSuccessful()) {
                        MarvelDetails details = response.body();
                        if (details != null && details.data != null &&
                                details.data.results != null && details.data.results.size() > 0) {

                            MarvelDetails.Result result = details.data.results.get(0);
                            event.setDescription(result.description);

                            if (result.thumbnail != null)
                                event.setThumbnail(result.thumbnail.path, result.thumbnail.extension);

                            event.setLoaded(true);
                        }
                    } else {
                        apiCallResult.set(-1);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<MarvelDetails> call, @NonNull Throwable t) {
                    apiCallResult.set(-1);
                }
            });
        }
    }
    private void downloadStoryDetails(MarvelModel.Character character) {
        for (int i = 0; i < DETAILS_COUNT && i < character.stories.storyItems.size(); i++) {
            final MarvelModel.Story story = character.stories.storyItems.get(i);
            story.loaded = new ObservableBoolean(false);

            MarvelAPI.getInstance().getDetails(story.resourceURI).enqueue(new Callback<MarvelDetails>() {
                @Override
                public void onResponse(@NonNull Call<MarvelDetails> call, @NonNull Response<MarvelDetails> response) {
                    if (response.isSuccessful()) {
                        MarvelDetails details = response.body();
                        if (details != null && details.data != null &&
                                details.data.results != null && details.data.results.size() > 0) {

                            MarvelDetails.Result result = details.data.results.get(0);
                            story.setDescription(result.description);

                            if (result.thumbnail != null)
                                story.setThumbnail(result.thumbnail.path, result.thumbnail.extension);

                            story.setLoaded(true);
                        }
                    } else {
                        apiCallResult.set(-1);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<MarvelDetails> call, @NonNull Throwable t) {
                    apiCallResult.set(-1);
                }
            });
        }
    }
    private void downloadSeriesDetails(MarvelModel.Character character) {
        for (int i = 0; i < DETAILS_COUNT && i < character.series.seriesItems.size(); i++) {
            final MarvelModel.SeriesItem series = character.series.seriesItems.get(i);
            series.loaded = new ObservableBoolean(false);

            MarvelAPI.getInstance().getDetails(series.resourceURI).enqueue(new Callback<MarvelDetails>() {
                @Override
                public void onResponse(@NonNull Call<MarvelDetails> call, @NonNull Response<MarvelDetails> response) {
                    if (response.isSuccessful()) {
                        MarvelDetails details = response.body();
                        if (details != null && details.data != null &&
                                details.data.results != null && details.data.results.size() > 0) {

                            MarvelDetails.Result result = details.data.results.get(0);
                            series.setDescription(result.description);

                            if (result.thumbnail != null)
                                series.setThumbnail(result.thumbnail.path, result.thumbnail.extension);

                            series.setLoaded(true);
                        }
                    } else {
                        apiCallResult.set(-1);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<MarvelDetails> call, @NonNull Throwable t) {
                    apiCallResult.set(-1);
                }
            });
        }
    }
    //endregion
}
