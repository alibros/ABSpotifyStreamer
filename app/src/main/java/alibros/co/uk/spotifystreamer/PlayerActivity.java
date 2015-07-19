package alibros.co.uk.spotifystreamer;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import alibros.co.uk.spotifystreamer.logic.ParcelableTrack;
import butterknife.ButterKnife;


//This activity will be used in the 2nd Stage of the project
public class PlayerActivity extends AppCompatActivity implements PlayerFragment.PlayerFragmentListener {

    private PlayerFragment playersFragment;
    private FragmentTransaction transaction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.inject(this);

        if (null == savedInstanceState) {

            playersFragment = new PlayerFragment();

            Bundle bundle = new Bundle();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
