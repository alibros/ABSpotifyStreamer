package alibros.co.uk.spotifystreamer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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
    private SeekTrack seekerTask;
    private int currentPostion;


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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentPostion = i;
                if (mMediaPlayer!=null && b) {

                    mMediaPlayer.seekTo(currentPostion);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


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

                    seekBar.setMax(mMediaPlayer.getDuration());
                    int minutes = (int)(mMediaPlayer.getDuration()/60000);
                    int seconds = (int) (mMediaPlayer.getDuration() * .001) - minutes;
                    duration.setText(minutes+":" + seconds);
                    playTrack();

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

    private void playTrack(){
        mMediaPlayer.start();
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        seekerTask = (SeekTrack) new SeekTrack().execute();
    }

    private void pauseTrack(){
        mMediaPlayer.pause();
        playButton.setImageResource(android.R.drawable.ic_media_play);
        if (seekerTask!=null)seekerTask.cancel(true);
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
            pauseTrack();
        }else{
            playTrack();
        }
    }

    public void newTrackSelected(int index) {
        mTracks = ParcelableTrack.loadTop10Tracks(getActivity());
        if (mCurrentIndex<mTracks.size()){
            mCurrentTrack = mTracks.get(index);
            loadCurrentTrack();
        }
    }


    private class SeekTrack extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args){
            while(mMediaPlayer.isPlaying()) {

                try{
                    Thread.sleep(100);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

                if (mMediaPlayer!=null && mMediaPlayer.isPlaying()) {
                    seekBar.setProgress(mMediaPlayer.getCurrentPosition());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            int minutes = (int) (mMediaPlayer.getCurrentPosition() / 60000);
                            int seconds = (int) (mMediaPlayer.getCurrentPosition() * .001) - minutes;

                            timeProgress.setText(minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
                        }
                    });
                }

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
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
        if (seekerTask!=null) {
            seekerTask.cancel(true);
        }
        if (mMediaPlayer!=null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
    }

    public interface PlayerFragmentListener {

    }

}
