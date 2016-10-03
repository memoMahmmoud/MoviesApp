package apps.mai.moviesapp;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;

import apps.mai.moviesapp.data.MovieColumns;
import apps.mai.moviesapp.data.MovieProvider;

/**
 * Created by Mai_ on 29-Sep-16.
 */
public class App extends Application {
    public static Cursor cursor;
    public static int adapterPosition;
    public static String firstTrailerLink;
    public static Uri getUri(){
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
