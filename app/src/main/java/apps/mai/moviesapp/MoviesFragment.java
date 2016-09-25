package apps.mai.moviesapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    //GridView gridView;
    RecyclerAdapter recyclerAdapter;
    Spinner sortSpinner;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int sort_position;
    LinearLayout sort_layout;
    GridLayoutManager lLayout;
    RecyclerView rView;


    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        lLayout= new GridLayoutManager(getActivity(),3);
        //gridView = (GridView) view.findViewById(R.id.grid);
        //if there is internet on device
        rView= (RecyclerView)view.findViewById(R.id.listView_movies);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

            if (Utility.isOnline(getActivity())){
                updateSort();

            }
            else {
                Toast.makeText(getActivity(),"No network to fetch online data",Toast.LENGTH_SHORT).show();
            }



        return view;




    }

    public void updateSort(){
        String sort = Utility.getPreferredSort(getContext());
        int sort_position = -1;
        if (sort.contains("popular")){
            sort_position = 0;
        }
        else if (sort.contains("top")){
            sort_position = 1;
        }
        Log.e("maiiiiiiiiiiiiiiiii",""+sort_position);


        FetchMoviesTask fetchMoviesTask=new FetchMoviesTask(sort_position,rView,getActivity());
        fetchMoviesTask.execute();
    }


    @Override
    public void onResume() {
        super.onResume();
        updateSort();
    }
}


