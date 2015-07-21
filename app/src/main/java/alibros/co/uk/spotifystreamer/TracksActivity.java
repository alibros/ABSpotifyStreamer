package alibros.co.uk.spotifystreamer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import java.util.List;

import alibros.co.uk.spotifystreamer.logic.ABSpotify;
import butterknife.ButterKnife;
import kaaes.spotify.webapi.android.models.Track;


public class TracksActivity extends AppCompatActivity implements TracksFragment.TracksFragmentListener {


    private TracksFragment tracksFragment;
    private FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        ButterKnife.inject(this);

        String artistname = getIntent().getStringExtra("ARTISTNAME");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Top 10 Tracks");
        getSupportActionBar().setSubtitle(artistname);

        if (null == savedInstanceState) {

            tracksFragment = new TracksFragment();
            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.tracks_fragment_container, tracksFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void trackSelected(int index) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(getString(R.string.current_index_bundle_key),index);
        startActivity(intent);
    }
}
