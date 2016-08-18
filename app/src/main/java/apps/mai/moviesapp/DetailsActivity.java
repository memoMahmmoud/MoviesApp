package apps.mai.moviesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView movie_title,movie_release_date,movie_average_vote,movie_description;
    ImageView movie_poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movie_title= (TextView) findViewById(R.id.movie_title);
        movie_release_date= (TextView) findViewById(R.id.movie_release_date);
        movie_average_vote= (TextView) findViewById(R.id.movie_vote_average);
        movie_description= (TextView) findViewById(R.id.movie_description);
        movie_poster= (ImageView) findViewById(R.id.movie_poster);

        Intent intent=getIntent();
        if (intent!=null){
            Movie movie= (Movie) intent.getSerializableExtra("movie");
            movie_title.setText(movie.getTitle());
            movie_release_date.setText(movie.getRelease_date());
            movie_average_vote.setText(String.valueOf(movie.getVote_average()));
            movie_description.setText(movie.getDescription());
            Bitmap bmp = BitmapFactory.decodeByteArray(movie.getImage(), 0, movie.getImage().length);
            movie_poster.setImageBitmap(bmp);
            //movie_poster.setar);
            /*Picasso.with(this).load(getString(R.string.poster_path_base_url)+
                    getString(R.string.size_poster)+
                    movie.getPoster_path()).into(movie_poster);*/

        }
    }
}
