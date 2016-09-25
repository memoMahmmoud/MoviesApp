package apps.mai.moviesapp.rest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import apps.mai.moviesapp.DetailsActivity;
import apps.mai.moviesapp.R;

/**
 * Created by Mai_ on 24-Sep-16.
 */
public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.ViewHolder> {
    Context mContext;
    ViewHolder mVh;
    ArrayList<Movie> movies=new ArrayList<>();
    public MovieCursorAdapter(Context context,ArrayList<Movie> movies){
        mContext = context;
        this.movies = movies;
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
            int position = getAdapterPosition();
            Movie movie1= movies.get(position);
            Intent intent=new Intent(mContext,DetailsActivity.class);
            intent.putExtra("movie",movie1);
            mContext.startActivity(intent);

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
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        //DatabaseUtils.dumpCursor(cursor);
        //byte[] byteArray = movies.get(position).getImage();
        //Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
        //viewHolder.movieImageView.setImageBitmap(bm);
        Movie movie = movies.get(position);

        saveImageAsBitmap imageAsBitmap=new saveImageAsBitmap(mContext,viewHolder.movieImageView,
                movie);
        imageAsBitmap.execute();
        Picasso.with(mContext).load(movie.getPoster_path()).into(viewHolder.movieImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private class saveImageAsBitmap extends AsyncTask<Void,Void,Void> implements Serializable {
        Bitmap imageBitmap;
        Context context;
        ImageView imageView;
        Movie movie;
        saveImageAsBitmap(Context context,ImageView imageView,Movie movie){
            this.context=context;
            this.imageView=imageView;
            this.movie=movie;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                imageBitmap = Picasso.with(context).load(movie.getPoster_path()).get();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                movie.setImage(byteArray);
                //movieImage.setImageBitmap(movies.get(i).getImage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //imageView.setImageBitmap(imageBitmap);
        }
    }


}
