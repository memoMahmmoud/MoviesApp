package apps.mai.moviesapp;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import apps.mai.moviesapp.data.MovieColumns;
import apps.mai.moviesapp.data.MovieProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    @BindView(R.id.movie_title) TextView movie_title;
    @BindView(R.id.movie_release_date) TextView movie_release_date;
    @BindView(R.id.movie_vote_average) TextView movie_average_vote;
    @BindView(R.id.movie_description) TextView movie_description;
    @BindView(R.id.movie_poster) ImageView movie_poster;
    @BindView(R.id.listView_trailers) RecyclerView trailer_list_view;
    @BindView(R.id.listView_reviews) RecyclerView review_list_view;
    @BindView(R.id.button_favorite) Button favorite_button;
    private static final int CURSOR_LOADER_ID = 0;

    TrailersTask trailersTask;
    ReviewsTask reviewsTask;
    static final String DETAIL_URI = "URI";
    private Uri mUri;
    private Unbinder unbinder;
    boolean isFavorite;
    int movie_id;
    private static final String[] MOVIE_COLUMNS = {
            MovieColumns.MOVIE_ID,
            MovieColumns.TITLE,
            MovieColumns.DESCRIPTION,
            MovieColumns.FAVORITE,
            MovieColumns.IMAGE,
            MovieColumns.RELEASE_DATE,
            MovieColumns.VOTE_AVERAGE
    };
    int positionAdapter;

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_MOVIE_ID = 0;
    private static final int COL_TITLE = 1;
    private static final int COL_DESCRIPTION = 2;
    private static final int COL_FAVORITE = 3;
    private static final int COL_IMAGE = 4;
    private static final int COL_RELEASE_DATE = 5;
    private static final int COL_VOTE_AVERAGE = 6;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle arguments = getArguments();
        unbinder = ButterKnife.bind(this,view);

        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = (String) ((Button)view).getText();
                if (text.contains("Make As Favorite")){
                    isFavorite = true;
                    favorite_button.setBackgroundColor(getResources().
                            getColor(R.color.colorLightGreen));
                    favorite_button.setText("My Favorite");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieColumns.FAVORITE,1);
                    getActivity().getContentResolver().
                            update(MovieProvider.Movies.withId(movie_id),contentValues,null,null);
                }
                else {
                    isFavorite = false;
                    favorite_button.setBackgroundResource(android.R.drawable.btn_default);
                    favorite_button.setText("Make As Favorite");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieColumns.FAVORITE,0);
                    getActivity().getContentResolver().
                            update(MovieProvider.Movies.withId(movie_id),contentValues,null,null);
                }

            }
        });

        // Initialize recycler view

        //trailer_list_view = (RecyclerView) view.findViewById(R.id.listView_trailers);
        trailer_list_view.setHasFixedSize(true);
        trailer_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        //review_list_view = (RecyclerView) view.findViewById(R.id.listView_reviews);
        review_list_view.setHasFixedSize(true);
        review_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null,this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null){
            return new CursorLoader(getActivity(), mUri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()){
            movie_id= data.getInt(COL_MOVIE_ID);

            String title = data.getString(COL_TITLE);
            this.movie_title.setText(title);

            String description = data.getString(COL_DESCRIPTION);
            this.movie_description.setText(description);

            byte[] image = data.getBlob(COL_IMAGE);
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            if (bmp != null){
                movie_poster.setImageBitmap(bmp);
            }
            String release_date = data.getString(COL_RELEASE_DATE);
            this.movie_release_date.setText(release_date);

            double vote_average = data.getFloat(COL_VOTE_AVERAGE);
            isFavorite = ((data.getInt(COL_FAVORITE)) != 0);
            if (isFavorite){
                favorite_button.setBackgroundColor(getResources().
                        getColor(R.color.colorLightGreen));
                favorite_button.setText("My Favorite");
            }
            else {
                favorite_button.setBackgroundResource(android.R.drawable.btn_default);
                favorite_button.setText("Make As Favorite");
            }

            String vote = String.format(getActivity().getString(R.string.average_rate),vote_average);
            this.movie_average_vote.setText(vote);
            if (Utility.isOnline(getActivity()))
            {
                trailersTask = new TrailersTask(getActivity(),movie_id,trailer_list_view);
                trailersTask.execute();

                reviewsTask = new ReviewsTask(getActivity(),movie_id,review_list_view);
                reviewsTask.execute();
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
