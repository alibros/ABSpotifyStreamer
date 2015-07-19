package alibros.co.uk.spotifystreamer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
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

    //playButton
    @InjectView(R.id.play_button) ImageButton playButton;


    private boolean trackIsLoaded;


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

        trackIsLoaded = false;

        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        //Update Views
        albumName.setText(mCurrentTrack.albumName);
        trackName.setText(mCurrentTrack.pName);
        artistName.setText(mCurrentTrack.artistName);

        Picasso.with(getActivity()).load(mCurrentTrack.albumCoverUrl).into(albumArt);


        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(mCurrentTrack.previewUrl);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    trackIsLoaded = true;
                    mediaPlayer.start();
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                }
            });
            mMediaPlayer.prepareAsync();

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                }
            });


        }
        catch (IOException e) { Log.w("IO Exception: ", e.toString()); }



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
    @OnClick(R.id.play_button) void playPressed(){
        if (!trackIsLoaded) return;

        if(mMediaPlayer.isPlaying()){
            playButton.setImageResource(android.R.drawable.ic_media_play);
            mMediaPlayer.pause();
        }else{
            playButton.setImageResource(android.R.drawable.ic_media_pause);
            mMediaPlayer.start();
        }
    }







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
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
    }

    public interface PlayerFragmentListener {

    }

}
