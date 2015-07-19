package alibros.co.uk.spotifystreamer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import alibros.co.uk.spotifystreamer.logic.ParcelableTrack;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import kaaes.spotify.webapi.android.models.Track;


public class PlayerFragment extends Fragment {


    private PlayerFragmentListener mListener;
    private List<ParcelableTrack> mTracks;
    private int mCurrentIndex;
    private ParcelableTrack mCurrentTrack;
    private MediaPlayer mMediaPlayer;


    //TextViews
    @InjectView(R.id.player_album_name) TextView albumName;
    @InjectView(R.id.player_artist_name) TextView artistName;
    @InjectView(R.id.player_duration) TextView duration;
    @InjectView(R.id.player_time_progress) TextView timeProgress;
    @InjectView(R.id.player_track_name) TextView trackName;

    //ImageView
    @InjectView(R.id.player_album_art) ImageView albumArt;

    //Seekbar
    @InjectView(R.id.player_seekbar) SeekBar seekBar;





    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentIndex = getArguments().getInt(getActivity().getString(R.string.current_index_bundle_key));
            mTracks = ParcelableTrack.loadTop10Tracks(getActivity());
            if (mCurrentIndex<mTracks.size()){
                mCurrentTrack = mTracks.get(mCurrentIndex);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.inject(this,view);

        loadCurrentTrack();


        return view;
    }

    private void loadCurrentTrack() {
        if (mMediaPlayer != null) mMediaPlayer = new MediaPlayer();

        //Update Views
        albumName.setText(mCurrentTrack.albumName);
        trackName.setText(mCurrentTrack.name);
        artistName.setText(mCurrentTrack.artistName);


    }



    @OnClick(R.id.next_button) void nextPressed(){
        mCurrentIndex += 1;
        if (mCurrentIndex>=mTracks.size()) mCurrentIndex = 0;
        mCurrentTrack = mTracks.get(mCurrentIndex);
        loadCurrentTrack();

    }
    @OnClick(R.id.previous_button) void previousPressed(){
        mCurrentIndex -= 1;
        if (mCurrentIndex<0) mCurrentIndex = mTracks.size()-1;
        mCurrentTrack = mTracks.get(mCurrentIndex);
        loadCurrentTrack();
    }
    @OnClick(R.id.play_button) void playPressed(){}







    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PlayerFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface PlayerFragmentListener {

    }

}
