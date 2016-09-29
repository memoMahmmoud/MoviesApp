package apps.mai.moviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class DetailsActivity extends AppCompatActivity {
    Toolbar toolbar;
    private static final String FRAGMENT_TAG = "detail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle arguments = new Bundle();
        arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());

        if (savedInstanceState==null){


            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().
                    add(R.id.movie_detail,fragment).commit();

        }

        //getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail,
                //new DetailFragment(),FRAGMENT_TAG).commit();
    }

}
