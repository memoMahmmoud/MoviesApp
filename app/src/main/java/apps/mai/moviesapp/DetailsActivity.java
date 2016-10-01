package apps.mai.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    private static final String FRAGMENT_TAG = "detail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Bundle arguments = new Bundle();
        //arguments.putInt("adapter",getIntent().getIntExtra("adapter",0));


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

}
