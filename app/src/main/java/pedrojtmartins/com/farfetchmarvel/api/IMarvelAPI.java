package pedrojtmartins.com.farfetchmarvel.api;

import pedrojtmartins.com.farfetchmarvel.models.MarvelDetails;
import pedrojtmartins.com.farfetchmarvel.models.MarvelModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Pedro Martins
 * 20/06/2017
 */

public interface IMarvelAPI {
    @GET("v1/public/characters")
    Call<MarvelModel> getCharacters(@Query("limit") int limit, @Query("offset") int offset);

    @GET("v1/public/characters")
    Call<MarvelModel> getCharactersByName(@Query("nameStartsWith") String name, @Query("limit") int limit, @Query("offset") int offset);

    @GET
    Call<MarvelDetails> getDetails(@Url String url);
}
