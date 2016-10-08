package apps.mai.moviesapp.interfaces;

import apps.mai.moviesapp.Model.ResultModel;
import apps.mai.moviesapp.Model.ReviewResults;
import apps.mai.moviesapp.Model.VideoResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mai_ on 08-Oct-16.
 */

public interface FetchMoviesAPI {
    @GET("3/movie/{sort}")
    Call<ResultModel> listResults(@Path("sort") String sort, @Query("api_key") String key);
    @GET("3/movie/{id}/reviews")
    Call<ReviewResults> listResultsReviews(@Path("id") int id, @Query("api_key") String key);
    @GET("3/movie/{id}/videos")
    Call<VideoResults> listResultsVideos(@Path("id") int id, @Query("api_key") String key);


}
