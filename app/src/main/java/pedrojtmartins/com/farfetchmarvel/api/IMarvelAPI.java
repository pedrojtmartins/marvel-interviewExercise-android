package pedrojtmartins.com.farfetchmarvel.api;

import pedrojtmartins.com.farfetchmarvel.models.MarvelDetails;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Pedro Martins
 * 20/06/2017
 */

public interface IMarvelAPI {
    /**
     * Downloads new characters without any filtering
     *
     * @param limit  max amount of characters to download
     * @param offset amount of characters already cached
     * @return list of new characters
     */
    @GET("v1/public/characters")
    Call<MarvelModel> getCharacters(@Query("limit") int limit,
                                    @Query("offset") int offset);

    /**
     * Downloads new characters with name filtering.
     * All returned characters will have a name
     * that starts with 'name'
     *
     * @param name   filter to be applied.
     * @param limit  max amount of characters to download
     * @param offset amount of characters already cached
     * @return list of new filtered characters
     */
    @GET("v1/public/characters")
    Call<MarvelModel> getCharactersByName(@Query("nameStartsWith") String name,
                                          @Query("limit") int limit,
                                          @Query("offset") int offset);

    /**
     * @param url determines what type of detail is returned
     * @return requested details
     */
    @GET
    Call<MarvelDetails> getDetails(@Url String url);
}
