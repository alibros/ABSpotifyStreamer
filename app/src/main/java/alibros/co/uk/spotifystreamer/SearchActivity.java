package alibros.co.uk.spotifystreamer;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import alibros.co.uk.spotifystreamer.logic.ABSpotify;
import alibros.co.uk.spotifystreamer.logic.SearchRecyclerViewAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;


public class SearchActivity extends AppCompatActivity {


    //Injecting view elements
    @InjectView(R.id.search_edit_text) EditText searchEditText;
    @InjectView(R.id.results_recyler_view) RecyclerView resultRecyclerView;

    //
    private final int TRIGGER_SERACH = 1;
    private final long SEARCH_TRIGGER_DELAY_IN_MS = 1000;
    private ABSpotify abSpotify;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.inject(this);

        abSpotify = new ABSpotify();

        getSupportActionBar().setTitle("Spotify Streamer");

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handler.removeMessages(TRIGGER_SERACH);
                handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
            }

        });

        //Set the Recyycler View
        resultRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        resultRecyclerView.setLayoutManager(layoutManager);



        }

    private ABSpotify.ABSpotifySearchArtistListener abSpotifyListener = new ABSpotify.ABSpotifySearchArtistListener() {
        @Override
        public void onSearchSuccessfulWithResult(final List<Artist> artists) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (artists.size() == 0){

                        Toast.makeText(SearchActivity.this,"No artists",Toast.LENGTH_LONG).show();



                    } else {
                        updateRecylcerView(artists);
                    }
                }
            });

        }

        @Override
        public void onSearchError() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SearchActivity.this,"ERROR",Toast.LENGTH_LONG).show();

                }
            });


        }
    };

    private void updateRecylcerView(List<Artist> artists) {

        recyclerAdapter = new SearchRecyclerViewAdapter(artists, new SearchRecyclerViewAdapter.SearchRecyclerViewAdapterListener() {
            @Override
            public void itemClicked(Artist artist) {

                Intent intent = new Intent(SearchActivity.this, TracksActivity.class);
                intent.putExtra("ARTISTID",artist.id);
                intent.putExtra("ARTISTNAME", artist.name);
                startActivity(intent);




            }
        });

        resultRecyclerView.setAdapter(recyclerAdapter);


    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TRIGGER_SERACH) {
                triggerSearch();
            }
        }


    };

    private void triggerSearch() {

        String query = searchEditText.getText().toString();
        abSpotify.searchForArtist(query,abSpotifyListener);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
