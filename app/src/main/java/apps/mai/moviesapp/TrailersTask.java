package apps.mai.moviesapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import apps.mai.moviesapp.Model.Video;
import apps.mai.moviesapp.Model.VideoResults;
import apps.mai.moviesapp.interfaces.FetchMoviesAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mai_ on 23-Sep-16.
 */
public class TrailersTask extends AsyncTask<Void,Void,Void> {

    Context context;
    int movie_id;
    RecyclerView trailer_list_view;
    TrailersTask(Context context,int movie_id,RecyclerView trailer_list_view){
        this.context = context;
        this.movie_id = movie_id;
        this.trailer_list_view = trailer_list_view;

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
        Call<VideoResults> videoResultsCall = fetchMoviesAPI.listResultsVideos(movie_id,
                context.getString(R.string.api_key));

        videoResultsCall.enqueue(new Callback<VideoResults>() {
            @Override
            public void onResponse(Call<VideoResults> call, Response<VideoResults> response) {
                List<Video> videos = response.body().getResults();
                ArrayList<String> trailers = new ArrayList<>();
                for (int i=0;i<videos.size();i++){
                    Video video = videos.get(i);
                    String trailerKey = video.getKey();
                    trailers.add(trailerKey);

                }
                App.firstTrailerLink = null;
                TrailerAdapter trailerAdapter = new TrailerAdapter(context,trailers);
                trailer_list_view.setAdapter(trailerAdapter);
                if (trailers.size()>0){
                    String firstTrailer = trailers.get(0);
                    App.firstTrailerLink = context.getString(R.string.youtube_base_url).concat(firstTrailer);
                }


            }

            @Override
            public void onFailure(Call<VideoResults> call, Throwable t) {

            }
        });

        return null;

    }//finish do in background

}
