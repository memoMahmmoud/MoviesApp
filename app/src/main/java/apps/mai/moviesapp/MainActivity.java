package apps.mai.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Serializable,MoviesFragment.Callback
{
    @BindView(R.id.toolbar) Toolbar toolbar;
    private boolean mTwoPane;
    private static final String DETAIL_FRAGMENT_TAG = "deail_fragment_tag";
    int sort_position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        sort_position = Utility.getPreferredSort(this);

        if (findViewById(R.id.movie_detail)!= null){
            // there were 2 pane
            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail,
                        new DetailFragment(),
                        DETAIL_FRAGMENT_TAG).commit();
            }
            mTwoPane = true;
        }
        else {
            mTwoPane = false;
        }
        int sort_position = Utility.getPreferredSort(this);
        if (Utility.isOnline(this)){
            FetchMoviesTask fetchMoviesTask=new FetchMoviesTask(sort_position,this);
            fetchMoviesTask.execute();
        }
        else {
            Toast.makeText(this,"no internet to fetch data",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings: {
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemSelected(Uri uri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, uri);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace
                    (R.id.movie_detail, fragment, DETAIL_FRAGMENT_TAG).commit();

        } else {
            Intent intent = new Intent(this, DetailsActivity.class).setData(uri);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        int last_sort = Utility.getPreferredSort(this);
        if (last_sort != sort_position){
            MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().
                    findFragmentById(R.id.movies_fragment);

            if ( moviesFragment != null ) {
                moviesFragment.onSortChanged(last_sort);
            }
            sort_position = last_sort;

        }
        super.onResume();
    }



}
