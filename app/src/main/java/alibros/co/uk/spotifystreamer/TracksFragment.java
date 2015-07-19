package alibros.co.uk.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alibros.co.uk.spotifystreamer.R;
import alibros.co.uk.spotifystreamer.logic.ABSpotify;
import alibros.co.uk.spotifystreamer.logic.ParcelableTrack;
import alibros.co.uk.spotifystreamer.logic.TracksRecyclerViewAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Track;


public class TracksFragment extends Fragment {


    @InjectView(R.id.results_recyler_view)
    RecyclerView resultsRecyclerView;


    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;
    private ABSpotify abSpotify;
    private List<Track> tracks;


    //Listener Interface
    public interface TracksFragmentListener {
        void trackSelected(int trackIndex);
    }

    private TracksFragmentListener mListener;



    public TracksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);

        ButterKnife.inject(this,view);

        resultsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        resultsRecyclerView.setLayoutManager(layoutManager);

        String artistid = "", artistname = "";

        abSpotify = new ABSpotify();
        if (getArguments()!=null) {
             artistid = getArguments().getString(getActivity().getString(R.string.artist_id_bundle_key));
             artistname = getArguments().getString(getActivity().getString(R.string.artist_name_bundle_key));
        }



        TelephonyManager tm = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();

        abSpotify.searchForTracks(artistid, countryCode ,new ABSpotify.ABSpotifySearchTracksListener() {
            @Override
            public void onSearchSuccessfulWithResult(List<Track> _tracks) {
                tracks = _tracks;
                if (tracks.size()==0){
                    displayErrorToastWithText(getString(R.string.no_tracks_found_tag));
                }
                updateUI();
            }

            @Override
            public void onSearchError() {
                displayErrorToastWithText(getString(R.string.spotify_call_error_text));
            }
        });
        return view;
    }

    private void updateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Save the tracks in SharedPreferences
                List<ParcelableTrack> pTracks = new ArrayList<ParcelableTrack>();

                for (Track track : tracks) {
                    ParcelableTrack pTrack = new ParcelableTrack(track);
                    pTracks.add(pTrack);

                }

                //As per the requirements, locally saving the last top 10 tracks.
                ParcelableTrack.saveTop10Tracks(pTracks, getActivity());


                recyclerAdapter = new TracksRecyclerViewAdapter(TracksFragment.this.tracks, new TracksRecyclerViewAdapter.TracksRecyclerViewAdapterListener() {
                    @Override
                    public void itemClicked(int trackIndex) {

                        if (mListener != null) {
                            mListener.trackSelected(trackIndex);
                        }
                    }
                });

                resultsRecyclerView.setAdapter(recyclerAdapter);
            }

        });
    }

    private void displayErrorToastWithText(final String errorText){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), errorText, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TracksFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
