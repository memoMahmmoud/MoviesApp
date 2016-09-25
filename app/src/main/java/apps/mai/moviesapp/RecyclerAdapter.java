package apps.mai.moviesapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import apps.mai.moviesapp.rest.Movie;

/**
 * Created by Mai_ on 13-Aug-16.
 */
public class RecyclerAdapter extends BaseAdapter {

    Context context;
    ArrayList<Movie> movies=new ArrayList<>();
    //Movie movie;
    public RecyclerAdapter(Context context, ArrayList<Movie> movies){

        this.context=context;
        this.movies=movies;

    }


    @Override
    public int getCount() {
        return movies.size();
    }


    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row=view;
        Movie movie=movies.get(i);
        if (row==null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.gridview_custom_layout,viewGroup,false);
        }
        ImageView movieImage= (ImageView) row.findViewById(R.id.gridview_image);
        saveImageAsBitmap imageAsBitmap=new saveImageAsBitmap(context,movieImage,movie);
        imageAsBitmap.execute();
        Picasso.with(context).load(movie.getPoster_path()).into(movieImage);

        //movieImage.setImageBitmap(movies.get(i).getImage());
            //Picasso.with(context).load(movies.get(i).getPoster_path()).into(movieImage);
        //Bitmap bitmap=((BitmapDrawable)movieImage.getDrawable()).getBitmap();
        //movies.get(i).setImage(bitmap);


            //TextView movieTitle= (TextView) row.findViewById(R.id.gridview_text);
            //movieTitle.setText(movies.get(i).getTitle());


        return row;
    }
    private class saveImageAsBitmap extends AsyncTask<Void,Void,Void> implements Serializable{
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


    /*public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movieImage;
        TextView movieTitle;
        ArrayList<Movie> movies;
        Context context;

        public RecyclerViewHolder(View itemView, ArrayList<Movie> movies, Context context) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.movies=movies;
            this.context=context;
            movieImage= (ImageView) itemView.findViewById(R.id.gridview_image);
            movieTitle= (TextView) itemView.findViewById(R.id.gridview_text);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            Movie movie=movies.get(position);
            //Intent intent=new Intent(context,ContactActivity.class);
            //intent.putExtra("contactValue",contact);

            //context.startActivity(intent);
            Toast.makeText(context,movie.getTitle(),Toast.LENGTH_LONG).show();
        }
    }*/
}
