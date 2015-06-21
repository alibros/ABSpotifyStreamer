package alibros.co.uk.spotifystreamer;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import alibros.co.uk.spotifystreamer.logic.ABSpotify;
import alibros.co.uk.spotifystreamer.logic.ParcelableTrack;
import alibros.co.uk.spotifystreamer.logic.TracksRecyclerViewAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;


public class TracksActivity extends AppCompatActivity {

    @InjectView(R.id.results_recyler_view)
    RecyclerView resultsRecyclerView;


    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private ABSpotify abSpotify;
    private List<Track> tracks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        ButterKnife.inject(this);

        resultsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        resultsRecyclerView.setLayoutManager(layoutManager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        abSpotify = new ABSpotify();

        String artistid = getIntent().getStringExtra("ARTISTID");
        String artistname = getIntent().getStringExtra("ARTISTNAME");

        getSupportActionBar().setSubtitle(artistname);
        getSupportActionBar().setTitle("Top 10 Tracks");

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();

        abSpotify.searchForTracks(artistid, countryCode ,new ABSpotify.ABSpotifySearchTracksListener() {
            @Override
            public void onSearchSuccessfulWithResult(List<Track> _tracks) {
                    tracks = _tracks;
                    updateUI();
            }

            @Override
            public void onSearchError() {

            }
        });
    }

    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Save the tracks in SharedPreferences
                List<ParcelableTrack> pTracks = new ArrayList<ParcelableTrack>();

                for (Track track : tracks) {
                    ParcelableTrack pTrack = new ParcelableTrack(track);
                    pTracks.add(pTrack);

                }

                //As per the requirements, locally saving the last top 10 tracks.
                ParcelableTrack.saveTop10Tracks(pTracks,TracksActivity.this);



                recyclerAdapter = new TracksRecyclerViewAdapter(TracksActivity.this.tracks, new TracksRecyclerViewAdapter.TracksRecyclerViewAdapterListener() {
                    @Override
                    public void itemClicked(Track track) {

                    }
                });

                resultsRecyclerView.setAdapter(recyclerAdapter);
            }

        });
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
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
