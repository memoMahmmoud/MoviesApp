package apps.mai.moviesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import apps.mai.moviesapp.data.MovieColumns;
import apps.mai.moviesapp.data.MovieProvider;

/**
 * Created by Mai_ on 23-Sep-16.
 */
public class Utility {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public static int getPreferredSort(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String sort = prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default_value));
        if (sort.contains("popular")){
            return 0;
        }
        else if (sort.contains("top")){
            return  1;
        }
        else{
            return 2;
        }

    }
    public static Uri getUri(Cursor cursor, int adapterPosition){
        if (cursor != null){
            if(cursor.moveToPosition(adapterPosition)){
                int movie_id = cursor.getInt(cursor.getColumnIndex(MovieColumns.MOVIE_ID));
                Uri mUri = MovieProvider.Movies.withId(movie_id);
                /*cursor = App.getgetContentResolver().query(mUri,MOVIE_COLUMNS,null,null,null);
                if (cursor.moveToFirst()){
                    bindDataFromCursor(cursor);
                }*/
                return mUri;
            }

        }
        cursor.close();
        return null;
    }
}
