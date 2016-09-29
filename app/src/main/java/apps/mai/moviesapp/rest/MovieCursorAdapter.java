package apps.mai.moviesapp.rest;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import apps.mai.moviesapp.MoviesFragment;
import apps.mai.moviesapp.R;
import apps.mai.moviesapp.data.MovieColumns;
import apps.mai.moviesapp.data.MovieProvider;

/**
 * Created by Mai_ on 24-Sep-16.
 */
public class MovieCursorAdapter extends CursorRecyclerViewAdapter<MovieCursorAdapter.ViewHolder> {
    Context mContext;
    ViewHolder mVh;
    public MovieCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
        mContext = context;

    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public ImageView movieImageView;
        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            movieImageView = (ImageView) view.findViewById(R.id.gridview_image);
        }

        @Override
        public void onClick(View view) {
            Cursor cursor = getCursor();
            if (cursor != null) {
                int position = getAdapterPosition();
                cursor.moveToPosition(position);
                int movie_id = cursor.getInt(cursor.getColumnIndex(MovieColumns.MOVIE_ID));
                ((MoviesFragment.Callback)mContext).onItemSelected(MovieProvider.Movies.
                        withId(movie_id));
            }
            cursor.close();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_custom_layout, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        mVh = vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor){

        byte[] image = cursor.getBlob(cursor.getColumnIndex(MovieColumns.IMAGE));
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        viewHolder.movieImageView.setImageBitmap(bitmap);
        DatabaseUtils.dumpCursor(cursor);



    }
}

