package pedrojtmartins.com.farfetchmarvel.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import pedrojtmartins.com.farfetchmarvel.api.MarvelAPI;
import pedrojtmartins.com.farfetchmarvel.models.MarvelDetails;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;
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

    // Main variable that holds the characters that are ready to be displayed.
    private ObservableArrayList<MarvelModel.Character> characters;

    // This temp variable will be used when a search is done.
    // The cached characters will be "moved" to this variable
    // while the "characters" variable is used to present the filtered characters.
    // This makes the maintenance easier since only one observable is used.
    private ObservableArrayList<MarvelModel.Character> tempCharacters;
    private String filter;

    private MarvelModel.Character selectedCharacter;

    // selectedCharacterDetails will be used to control the loading animations
    private ObservableArrayList<Integer> selectedCharacterDetails;


    public MainViewModel() {
        characters = new ObservableArrayList<>();
        tempCharacters = new ObservableArrayList<>();

        selectedCharacterDetails = new ObservableArrayList<>();
    }

    /**
     * Starts downloading first characters
     */
    public void initialize() {
        if (characters.isEmpty()) {
            // Only needs to download first characters
            // if they are not already cached
            downloadCharacters();
        }
    }

    private void downloadCharacters() {
        MarvelAPI.getInstance().getCharacters(PAGINATION_ITEMS_COUNT, characters.size()).enqueue(new Callback<MarvelModel>() {
            @Override
            public void onResponse(@NonNull Call<MarvelModel> call, @NonNull Response<MarvelModel> response) {
                if (response.isSuccessful()) {
                    MarvelModel apiResponse = response.body();
                    if (apiResponse != null && apiResponse.data != null && apiResponse.data.characters != null) {
                        characters.addAll(apiResponse.data.characters);
                        return;
                    }
                } else {
                    // TODO: 20/06/2017 display error msg
                }
            }
            @Override
            public void onFailure(Call<MarvelModel> call, Throwable t) {
                // TODO: 20/06/2017 display error msg
            }
        });
    }
    public ObservableArrayList<MarvelModel.Character> getCharacters() {
        return characters;
    }

    public void loadMoreCharacters() {
        if (tempCharacters.isEmpty())
            downloadCharacters();
        else
            loadFilteredCharacters(filter);

        // TODO: 22/06/2017 don't like this
    }

    public void loadFilteredCharacters(String query) {
        if (query == null || query.isEmpty()) {
            // The filter is empty or was dismissed.
            // Recover all the cached characters
            characters.clear();
            characters.addAll(tempCharacters);
            tempCharacters.clear();
            return;
        }

        filter = query;

        int offset = tempCharacters.isEmpty() ? 0 : characters.size();
        MarvelAPI.getInstance().getCharactersByName(filter, PAGINATION_ITEMS_COUNT, offset).enqueue(new Callback<MarvelModel>() {
            @Override
            public void onResponse(@NonNull Call<MarvelModel> call, @NonNull Response<MarvelModel> response) {
                if (response.isSuccessful()) {
                    MarvelModel apiResponse = response.body();
                    if (apiResponse != null && apiResponse.data != null && apiResponse.data.characters != null) {
                        if (tempCharacters.isEmpty()) {
                            tempCharacters.addAll(characters);
                            characters.clear();
                        }

                        characters.addAll(apiResponse.data.characters);
                    }
                } else {
                    // TODO: 20/06/2017 display error msg
                }
            }
            @Override
            public void onFailure(Call<MarvelModel> call, Throwable t) {
                // TODO: 20/06/2017 display error msg
            }
        });
    }

    public MarvelModel.Character getSelectedCharacter() {
        return selectedCharacter;
    }

    public void onCharacterSelected(MarvelModel.Character character) {
        selectedCharacter = character;
    }

    public ObservableArrayList<Integer> getDetails(final MarvelModel.Character character) {
        selectedCharacterDetails.clear();

        if (character == null || character.comics == null || character.comics.comicItems == null)
            return null;

        downloadComicDetails(character);
        downloadEventDetails(character);
        downloadStoryDetails(character);
        downloadSeriesDetails(character);

        return selectedCharacterDetails;
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
                        // TODO: 20/06/2017 display error msg
                    }
                }
                @Override
                public void onFailure(Call<MarvelDetails> call, Throwable t) {
                    // TODO: 20/06/2017 display error msg
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
                        // TODO: 20/06/2017 display error msg
                    }
                }
                @Override
                public void onFailure(Call<MarvelDetails> call, Throwable t) {
                    // TODO: 20/06/2017 display error msg
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
                        // TODO: 20/06/2017 display error msg
                    }
                }
                @Override
                public void onFailure(Call<MarvelDetails> call, Throwable t) {
                    // TODO: 20/06/2017 display error msg
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
                        // TODO: 20/06/2017 display error msg
                    }
                }
                @Override
                public void onFailure(Call<MarvelDetails> call, Throwable t) {
                    // TODO: 20/06/2017 display error msg
                }
            });
        }
    }

}
