package alibros.co.uk.spotifystreamer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeActivity extends AppCompatActivity implements SearchFragment.SearchFragmentListener, TracksFragment.TracksFragmentListener, PlayerFragment.PlayerFragmentListener{


    private boolean isOnTablet;
    private boolean playerIsVisible;
    SearchFragment searchFragment;
    TracksFragment tracksFragment;
    PlayerFragment playerFragment;
    FragmentTransaction transaction;

    FrameLayout playerContainer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Init ButterKnife
        ButterKnife.inject(this);


        /*
         * Search Fragment is present regardless of the screensize so
         * we initialize it and commit it here.
         */
        if(null == savedInstanceState){

        searchFragment = new SearchFragment();
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.search_fragment_container, searchFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        }

        isOnTablet = getResources().getBoolean(R.bool.isTablet);
        if (isOnTablet) {
            playerContainer = (FrameLayout)findViewById(R.id.player_fragment_container);
        }

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

    @Override
    public void searchItemSelected(String artistID, String artistName) {

        if (isOnTablet){

            tracksFragment = new TracksFragment();

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.artist_id_bundle_key), artistID);
            bundle.putString(getString(R.string.artist_name_bundle_key), artistName);
            tracksFragment.setArguments(bundle);

            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.tracks_fragment_container, tracksFragment);
            transaction.addToBackStack(null);
            transaction.commit();


        } else {
            Intent intent = new Intent(this, TracksActivity.class);
            intent.putExtra(getString(R.string.artistid_intent_tag), artistID);
            intent.putExtra(getString(R.string.artistname_intent_tag), artistName);
            startActivity(intent);
        }
    }

    @Override
    public void trackSelected(int index) {

        if (playerFragment==null) {

            playerFragment = new PlayerFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.current_index_bundle_key), index);
            playerFragment.setArguments(bundle);

            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.player_fragment_container, playerFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            playerFragment.newTrackSelected(index);
        }

        playerContainer.setVisibility(View.VISIBLE);
        playerIsVisible = true;

    }

    @Override
    public void onBackPressed() {
        if (playerIsVisible){
            playerContainer.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
