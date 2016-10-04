package chris.spotifytest;

/**
 * Created by Chris on 2016-09-20.
 */
import chris.spotifytest.dataTypes.SearchResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("/v1/search")
    Call<SearchResult> getSearchResult(@Query("q") String searchString, @Query("type") String type);

}
