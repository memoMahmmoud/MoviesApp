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

/**
 * Created by Mai_ on 23-Sep-16.
 */
public class TrailersTask extends AsyncTask<Void,Void,String> {
    private final String LOG_TAG=FetchMoviesTask.class.getSimpleName();
    HttpURLConnection urlConnection;
    BufferedReader reader;
    String trailerJsonStr;
    Context context;
    int movie_id;
    RecyclerView trailer_list_view;
    DetailFragment detailFragment;
    TrailersTask(Context context,int movie_id,RecyclerView trailer_list_view){
        this.context = context;
        this.movie_id = movie_id;
        this.trailer_list_view = trailer_list_view;

    }


    @Override
    protected String doInBackground(Void... voids) {

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority(context.getString(R.string.movie_base_url))
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(String.valueOf(movie_id))
                    .appendPath("videos")
                    .appendQueryParameter("api_key", context.getString(R.string.api_key));
            /*final String TRAILERS_URL=context.getString(R.string.movie_base_url)+
                    String.format(context.getString(R.string.remain_trailer_url),""+movie_id);
            final String api_key=context.getString(R.string.api_key);*/
            //build uri for movies api
            Uri builtUri=builder
                    .build();

            URL urlForTrailerMovieApi=new URL(builtUri.toString());

            // Create the request to OpenMoviesApi, and open the connection
            urlConnection= (HttpURLConnection) urlForTrailerMovieApi.openConnection();
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
                trailerJsonStr=stringBuffer.toString();
                return trailerJsonStr;
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
        return null;

    }//finish do in background

    @Override
    protected void onPostExecute(String string) {
        try {
            fetchTrailerFromJson(trailerJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fetchTrailerFromJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");
        final ArrayList<String> trailers = new ArrayList<>();
        for (int i = 0; i<results.length();i++){
            JSONObject trailerObject = results.getJSONObject(i);
            String trailerKey = trailerObject.getString("key");

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
}
