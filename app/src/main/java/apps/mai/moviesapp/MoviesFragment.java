package apps.mai.moviesapp;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import apps.mai.moviesapp.data.MovieColumns;
import apps.mai.moviesapp.data.MovieProvider;
import apps.mai.moviesapp.rest.MovieCursorAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    @BindView(R.id.listView_movies) RecyclerView rView;
    @BindView(R.id.empty_view) TextView emptyView;

    GridLayoutManager lLayout;

    private static final int CURSOR_LOADER_ID = 0;
    private MovieCursorAdapter mCursorAdapter;
    String selection;
    String[] arg_selections;
    int mPosition;
    public static final String SELECTED_KEY_POSITION = "position";
    private Unbinder unbinder;


    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        unbinder = ButterKnife.bind(this,view);

        lLayout= new GridLayoutManager(getActivity(),3);
        //rView= (RecyclerView)view.findViewById();
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        mCursorAdapter = new MovieCursorAdapter(getContext(), null);
        rView.setAdapter(mCursorAdapter);



        return view;




    }

    public interface Callback {
        public void onItemSelected(Uri uri);
    }
    void onSortChanged(int sort){
        updateSort(sort);
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null,this);
    }

    public void updateSort(int sort){

        if (Utility.isOnline(getActivity())){
            FetchMoviesTask fetchMoviesTask=new FetchMoviesTask(sort,getActivity());
            fetchMoviesTask.execute();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int sort = Utility.getPreferredSort(getContext());

        //user select popular
        if (sort == 0){
            selection = MovieColumns.POPULAR_MOVIE+ " =?";
            arg_selections = new String[]{"1"};
        }
        //user select top rated
        else if (sort == 1){
            selection = MovieColumns.TOP_RATED+ " =?";
            arg_selections = new String[]{"1"};
        }
        else {
            selection = MovieColumns.FAVORITE+ " =?";
            arg_selections = new String[]{"1"};
        }
        return new CursorLoader(getActivity(), MovieProvider.Movies.CONTENT_URI,
                null,
                selection,
                arg_selections,
                null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        if (data.getCount()> 0){
            rView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        // no data to display for user
        else {
            rView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
        //Toast.makeText(getActivity(),"on loader reset on movies fragment",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}


