package apps.mai.moviesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import apps.mai.moviesapp.data.MovieColumns;
import apps.mai.moviesapp.data.MovieProvider;

/**
 * Created by Mai_ on 23-Sep-16.
 */
public class FetchMoviesTask extends AsyncTask<Void,Void,String> {
    private final String LOG_TAG=FetchMoviesTask.class.getSimpleName();
    HttpURLConnection urlConnection;
    BufferedReader reader;
    String movieJsonStr;
    static int sort_position;
    Context context;

    FetchMoviesTask(int sort_position,Context context)
    {
        this.sort_position=sort_position;
        this.context = context;
    }


    @Override
    protected String doInBackground(Void... voids) {

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https").
                    authority(context.getString(R.string.movie_base_url)).
                    appendPath("3").appendPath("movie");

            if (sort_position==1){
                //user select top rated movies
                builder.appendPath("top_rated");
            }
            else {
                //user select popular movies movies
                builder.appendPath("popular");
            }

            builder.appendQueryParameter("api_key",context.getString(R.string.api_key));

            Uri builtUri = builder.build();
            URL urlForMovieApi=new URL(builtUri.toString());

            // Create the request to OpenMoviesApi, and open the connection
            urlConnection= (HttpURLConnection) urlForMovieApi.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStreamReader inputStreamReader=new InputStreamReader(urlConnection.getInputStream());
            reader=new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer=new StringBuffer();
            String line;
            while ((line=reader.readLine())!=null){
                stringBuffer.append(line+"\n");
            }
            if (stringBuffer.length()!=0){
                movieJsonStr=stringBuffer.toString();
                //Log.v(LOG_TAG,"foresacast String :"+movieJsonStr);
                return movieJsonStr;

            }
            else {
                return null;
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }//finally in try-catch

    }//finish do in background



    @Override
    protected void onPostExecute(String strings) {
        try {
            fetchJsonString(strings);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void fetchJsonString(String movieJsonString) throws JSONException, IOException {
        JSONObject jsonObject=new JSONObject(movieJsonString);
        JSONArray jsonArrayResultsKey=jsonObject.getJSONArray("results");
        //Movie movie;

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


        for (int i=0;i<jsonArrayResultsKey.length();i++){
            //get each movie object
            JSONObject movieJsonObj=jsonArrayResultsKey.getJSONObject(i);
            String title=movieJsonObj.getString("title");
            String overview=movieJsonObj.getString("overview");
            //because the json object give me relative path not full path, I need full path
            final String poster_path=context.getString(R.string.poster_path_base_url)+
                    context.getString(R.string.size_poster)+movieJsonObj.getString("poster_path");
            String release_date=movieJsonObj.getString("release_date");
            double vote_average=movieJsonObj.getDouble("vote_average");
            int movie_id = movieJsonObj.getInt("id");

            ContentValues movieContentValues = new ContentValues();
            movieContentValues.put(MovieColumns.DESCRIPTION, overview);
            movieContentValues.put(MovieColumns.TITLE, title);
            movieContentValues.put(MovieColumns.RELEASE_DATE, release_date);
            movieContentValues.put(MovieColumns.VOTE_AVERAGE, vote_average);
            movieContentValues.put(MovieColumns.MOVIE_ID, movie_id);
            //if sort_position = 0, this movie is from popular movies
            if (sort_position == 0){

                movieContentValues.put(MovieColumns.POPULAR_MOVIE,1);
            }
            else if (sort_position == 1){
                movieContentValues.put(MovieColumns.TOP_RATED,1);
            }
            Cursor cursor = context.getContentResolver().query(
                    MovieProvider.Movies.withId(movie_id),null,null,null,null);
            if (cursor == null || cursor.getCount() == 0){
                context.getContentResolver().insert(MovieProvider.Movies.withId(movie_id),
                        movieContentValues);

            }
            else {
                context.getContentResolver().update(MovieProvider.Movies.withId(movie_id),
                        movieContentValues,null,null);
            }
            SaveImageAsBitmap saveImageAsBitmap = new SaveImageAsBitmap(context,movieContentValues,
                    poster_path,movie_id);
            saveImageAsBitmap.execute();

            cursor.close();



        }

    }
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

