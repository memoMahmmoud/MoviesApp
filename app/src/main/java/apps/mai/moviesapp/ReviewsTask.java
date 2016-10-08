package apps.mai.moviesapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import apps.mai.moviesapp.Model.Review;
import apps.mai.moviesapp.Model.ReviewResults;
import apps.mai.moviesapp.interfaces.FetchMoviesAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mai_ on 24-Sep-16.
 */
public class ReviewsTask extends AsyncTask<Void,Void,Void> {
    Context context;
    int movie_id;
    RecyclerView review_list_view;
    ArrayList<String> reviewsString;
    ReviewsTask(Context context,int movie_id,RecyclerView review_list_view){
        this.context = context;
        this.movie_id = movie_id;
        this.review_list_view = review_list_view;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(context.getString(R.string.movie_base_url));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(builder.build().toString())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FetchMoviesAPI fetchMoviesAPI = retrofit.create(FetchMoviesAPI.class);
        Call<ReviewResults> reviewResultsCall = fetchMoviesAPI.listResultsReviews(movie_id,
                context.getString(R.string.api_key));
        reviewResultsCall.enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                List<Review> reviews = response.body().getResults();
                reviewsString= new ArrayList<String>();
                for (int i=0; i<reviews.size(); i++){
                    Review review = reviews.get(i);
                    String reviewContent = review.getContent();
                    reviewsString.add(reviewContent);
                }
                ReviewAdapter reviewAdapter = new ReviewAdapter(context,reviewsString);
                review_list_view.setAdapter(reviewAdapter);

            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {

            }
        });

        return null;

    }//finish do in background


}

