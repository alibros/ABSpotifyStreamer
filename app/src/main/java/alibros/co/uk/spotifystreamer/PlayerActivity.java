package alibros.co.uk.spotifystreamer;

import android.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;


/*
 * Acticity to display the player on phone UI. On tablets, Player Fragment will be committed in HomeActivity
 */
public class PlayerActivity extends AppCompatActivity implements PlayerFragment.PlayerFragmentListener {

    private PlayerFragment playersFragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (null == savedInstanceState) {
            playersFragment = new PlayerFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.current_index_bundle_key), getIntent().getIntExtra(getString(R.string.current_index_bundle_key), 0));
            playersFragment.setArguments(bundle);

            transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.player_fragment_container, playersFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
