package apps.mai.moviesapp;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import apps.mai.moviesapp.rest.Movie;
import apps.mai.moviesapp.rest.MovieCursorAdapter;

/**
 * Created by Mai_ on 23-Sep-16.
 */
public class FetchMoviesTask extends AsyncTask<Void,Void,String> {
    private final String LOG_TAG=FetchMoviesTask.class.getSimpleName();
    HttpURLConnection urlConnection;
    BufferedReader reader;
    String movieJsonStr;
    int sort_position;
    //GridView gridView;
    Context context;
    RecyclerView recyclerView;

    FetchMoviesTask(int sort_position, RecyclerView recyclerView, Context context)
    {
        this.sort_position=sort_position;
        //this.gridView = gridView;
        this.context = context;
        this.recyclerView = recyclerView;
    }


    @Override
    protected String doInBackground(Void... voids) {

        try {
            final String FORECAST_URL_BASE=context.getString(R.string.movie_base_url);
            String sorting="";
            if (sort_position==1){
                //user select top rated movies
                sorting=context.getString(R.string.top_rated_movies_remain_url);
            }
            else {
                //user select popular movies movies
                sorting=context.getString(R.string.popular_movies_remain_url);
            }

            final String api_key=context.getString(R.string.api_key);
            //build uri for movies api
            Uri builtUri=Uri.parse(FORECAST_URL_BASE+sorting).buildUpon()
                    .appendQueryParameter("api_key",api_key)
                    .build();

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
        //Toast.makeText(getActivity(),strings.toString(),Toast.LENGTH_LONG).show();
        Log.e("maiiiiiiiii", strings);
        try {
            fetchJsonString(strings);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void fetchJsonString(String movieJsonString) throws JSONException {
        JSONObject jsonObject=new JSONObject(movieJsonString);
        JSONArray jsonArrayResultsKey=jsonObject.getJSONArray("results");
        ArrayList<Movie> movies=new ArrayList<>();
        Movie movie;

        //iterate for all movies
        for (int i=0;i<jsonArrayResultsKey.length();i++){
            //get each movie object
            JSONObject movieJsonObj=jsonArrayResultsKey.getJSONObject(i);
            String title=movieJsonObj.getString("title");
            String overview=movieJsonObj.getString("overview");
            //because the json object give me relative path not full path, I need full path
            final String poster_path=context.getString(R.string.poster_path_base_url)+
                    context.getString(R.string.size_poster)+movieJsonObj.getString("poster_path");
            String release_date=movieJsonObj.getString("release_date");
            int vote_average=movieJsonObj.getInt("vote_average");
            int image_id = movieJsonObj.getInt("id");


            movie=new Movie(image_id,title,overview,poster_path,release_date,vote_average);
            movies.add(movie);






            //create movie object to add it to array of movies
            //Movie movie=new Movie(title,overview,poster_path,release_date,vote_average,img_bitmap[0]);
            //add this movie to array list of movies
            //recyclerAdapter.add(movie);

        }

        //RecyclerAdapter recyclerAdapter=new RecyclerAdapter(context, movies);
        MovieCursorAdapter movieCursorAdapter = new MovieCursorAdapter(context,movies);
        recyclerView.setAdapter(movieCursorAdapter);

        /*gridView.setAdapter(recyclerAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie1= (Movie) adapterView.getItemAtPosition(i);
                Intent intent=new Intent(context,DetailsActivity.class);
                intent.putExtra("movie",movie1);
                context.startActivity(intent);
            }
        });*/

    }
}//finsh of async task

