package apps.mai.moviesapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import apps.mai.moviesapp.rest.Movie;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    TextView movie_title,movie_release_date,movie_average_vote,movie_description;
    ImageView movie_poster;
    RecyclerView trailer_list_view,review_list_view;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        movie_title= (TextView) view.findViewById(R.id.movie_title);
        movie_release_date= (TextView) view.findViewById(R.id.movie_release_date);
        movie_average_vote= (TextView) view.findViewById(R.id.movie_vote_average);
        movie_description= (TextView) view.findViewById(R.id.movie_description);
        movie_poster= (ImageView) view.findViewById(R.id.movie_poster);

        // Initialize recycler view

        trailer_list_view = (RecyclerView) view.findViewById(R.id.listView_trailers);
        trailer_list_view.setHasFixedSize(true);
        trailer_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        review_list_view = (RecyclerView) view.findViewById(R.id.listView_reviews);
        review_list_view.setHasFixedSize(true);
        review_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));


        Intent intent=getActivity().getIntent();
        if (intent!=null){
            Movie movie= (Movie) intent.getSerializableExtra("movie");
            movie_title.setText(movie.getTitle());
            movie_release_date.setText(movie.getRelease_date());
            movie_average_vote.setText(String.valueOf(movie.getVote_average()));
            movie_description.setText(movie.getDescription());
            /*Bitmap bmp = BitmapFactory.decodeByteArray(movie.getImage(), 0, movie.getImage().length);
            if (bmp != null){
                movie_poster.setImageBitmap(bmp);
            }*/
            Picasso.with(getActivity()).load(movie.getPoster_path()).into(movie_poster);

            TrailersTask trailersTask = new TrailersTask(getActivity(),movie.getMovie_id(),trailer_list_view);
            trailersTask.execute();

            ReviewsTask reviewsTask = new ReviewsTask(getActivity(),movie.getMovie_id(),review_list_view);
            reviewsTask.execute();

        }




        return view;
    }


}
