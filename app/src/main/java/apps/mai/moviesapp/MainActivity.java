package apps.mai.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable{
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;
    GridView gridView;
    RecyclerAdapter recyclerAdapter;
    Spinner sortSpinner;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int sort_position;
    LinearLayout sort_layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = (GridView) findViewById(R.id.grid);
        sort_layout= (LinearLayout) findViewById(R.id.layout_sort);

        sortSpinner= (Spinner) findViewById(R.id.spinner_sort);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        sort_position=sharedPreferences.getInt("sort",0);
        //if there is internet on device
        if (isOnline()){
            sortSpinner.setSelection(sort_position);

            sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (isOnline()){
                            editor.putInt("sort", i);
                            editor.commit();
                            sort_position=sharedPreferences.getInt("sort",0);
                            FetchMoviesTask fetchMoviesTask=new FetchMoviesTask(sort_position);
                            fetchMoviesTask.execute();
                        }
                        else {
                            Toast.makeText(MainActivity.this,"No network to sort",Toast.LENGTH_SHORT).show();
                        }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        else {
            sort_layout.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,"No network to fetch online data",Toast.LENGTH_SHORT).show();
        }






    }
    //check for connecting interner or not
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void fetchJsonString(String movieJsonString) throws JSONException {
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
            final String poster_path=getString(R.string.poster_path_base_url)+
                    getString(R.string.size_poster)+movieJsonObj.getString("poster_path");
            String release_date=movieJsonObj.getString("release_date");
            int vote_average=movieJsonObj.getInt("vote_average");


            movie=new Movie(title,overview,poster_path,release_date,vote_average);
            movies.add(movie);






            //create movie object to add it to array of movies
            //Movie movie=new Movie(title,overview,poster_path,release_date,vote_average,img_bitmap[0]);
            //add this movie to array list of movies
            //recyclerAdapter.add(movie);

        }

        recyclerAdapter=new RecyclerAdapter(MainActivity.this, movies);

        gridView.setAdapter(recyclerAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie1= (Movie) adapterView.getItemAtPosition(i);
                Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("movie",movie1);
                startActivity(intent);
            }
        });

    }

    private class FetchMoviesTask extends AsyncTask<Void,Void,String> {
        private final String LOG_TAG=FetchMoviesTask.class.getSimpleName();
        HttpURLConnection urlConnection;
        BufferedReader reader;
        String movieJsonStr;
        int sort_position;

        FetchMoviesTask(int sort_position){
            this.sort_position=sort_position;
        }


        @Override
        protected String doInBackground(Void... voids) {

            try {
                final String FORECAST_URL_BASE=getString(R.string.movie_base_url);
                String sorting="";
                if (sort_position==1){
                    //user select top rated movies
                    sorting=getString(R.string.top_rated_movies_remain_url);
                }
                else {
                    //user select popular movies movies
                    sorting=getString(R.string.popular_movies_remain_url);
                }

                final String api_key=getString(R.string.api_key);
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
    }//finsh of async task
}
