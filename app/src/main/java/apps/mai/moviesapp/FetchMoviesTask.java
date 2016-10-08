package apps.mai.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import apps.mai.moviesapp.Model.Movie;
import apps.mai.moviesapp.Model.ResultModel;
import apps.mai.moviesapp.data.MovieColumns;
import apps.mai.moviesapp.data.MovieProvider;
import apps.mai.moviesapp.interfaces.FetchMoviesAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mai_ on 23-Sep-16.
 */
public class FetchMoviesTask extends AsyncTask<Void,Void,Void>{
    static int sort_position;
    Context context;

    FetchMoviesTask(int sort_position,Context context)
    {
        this.sort_position=sort_position;
        this.context = context;
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
        String sort;
        if (sort_position == 1){
            sort="top_rated";
        }
        else {
            sort="popular";
        }


        ContentValues contentValues = new ContentValues();
        if (sort_position ==0){
            contentValues.put(MovieColumns.POPULAR_MOVIE,0);

            context.getContentResolver().update(MovieProvider.Movies.CONTENT_URI,
                    contentValues,null,null);
        }
        else if (sort_position ==1){
            contentValues.put(MovieColumns.TOP_RATED,0);
            context.getContentResolver().update(MovieProvider.Movies.CONTENT_URI,
                    contentValues,null,null);

        }
        Call<ResultModel> resultModelCall = fetchMoviesAPI.listResults(sort,
                context.getString(R.string.api_key));
        resultModelCall.enqueue(new Callback<ResultModel>() {
            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                List<Movie> movies = response.body().getResults();
                for (int i = 0; i<movies.size(); i++){
                    Movie movie = movies.get(i);
                    ContentValues movieContentValues = new ContentValues();
                    movieContentValues.put(MovieColumns.DESCRIPTION, movie.getOverview());
                    movieContentValues.put(MovieColumns.TITLE, movie.getTitle());
                    movieContentValues.put(MovieColumns.RELEASE_DATE, movie.getReleaseDate());
                    movieContentValues.put(MovieColumns.VOTE_AVERAGE, movie.getVoteAverage());
                    movieContentValues.put(MovieColumns.MOVIE_ID, movie.getId());
                    if (sort_position == 0){

                        movieContentValues.put(MovieColumns.POPULAR_MOVIE,1);
                    }
                    else if (sort_position == 1){
                        movieContentValues.put(MovieColumns.TOP_RATED,1);
                    }
                    Cursor cursor = context.getContentResolver().query(
                            MovieProvider.Movies.withId(movie.getId()),null,null,null,null);
                    if (cursor == null || cursor.getCount() == 0){
                        context.getContentResolver().insert(MovieProvider.Movies.withId(movie.getId()),
                                movieContentValues);

                    }
                    else {
                        context.getContentResolver().update(MovieProvider.Movies.withId(movie.getId()),
                                movieContentValues,null,null);
                    }
                    String poster_path=context.getString(R.string.poster_path_base_url)+
                            context.getString(R.string.size_poster)+movie.getPosterPath();

                    SaveImageAsBitmap saveImageAsBitmap = new SaveImageAsBitmap(context,movieContentValues,
                            poster_path,movie.getId());
                    saveImageAsBitmap.execute();

                    cursor.close();
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {


            }
        });
        return null;

    }//finish do in background



    class SaveImageAsBitmap extends AsyncTask<Void,Void,byte[]>{
        Bitmap imageBitmap;
        Context context;
        ContentValues movieContentValues;
        String poster_path;
        int movie_id;

        SaveImageAsBitmap(Context context,ContentValues movieContentValues,String poster_path,int movie_id) {
            this.context = context;
            this.movieContentValues = movieContentValues;
            this.poster_path = poster_path;
            this.movie_id = movie_id;
        }
        @Override
        protected byte[] doInBackground(Void... voids) {
            try {
                imageBitmap = Picasso.with(context).load(poster_path).get();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieColumns.IMAGE,byteArray);
                context.getContentResolver().update(MovieProvider.Movies.withId(movie_id),
                        contentValues,null,null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {

        }

    }

}//finsh of async task

