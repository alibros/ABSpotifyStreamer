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
    private List<ParcelableTrack> tracks;


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


        String locale = getActivity().getResources().getConfiguration().locale.getCountry();
        if (locale == null || locale == ""){
            locale  = "US";
        }

        if (savedInstanceState!=null){
            //this could also be read directly from the savedINstanceState
            tracks =  ParcelableTrack.loadTop10Tracks(getActivity());
            updateUI();
        } else {

            abSpotify.searchForTracks(artistid, locale, new ABSpotify.ABSpotifySearchTracksListener() {
                @Override
                public void onSearchSuccessfulWithResult(List<Track> _tracks) {
                    List<ParcelableTrack> pTracks = new ArrayList<ParcelableTrack>();

                    for (Track track : _tracks) {
                        ParcelableTrack pTrack = new ParcelableTrack(track);
                        pTracks.add(pTrack);

                    }
                    tracks = pTracks;
                    if (tracks.size() == 0) {
                        displayErrorToastWithText(getString(R.string.no_tracks_found_tag));
                    }
                    updateUI();
                }

                @Override
                public void onSearchError() {
                    displayErrorToastWithText(getString(R.string.spotify_call_error_text));
                }
            });
        }
        return view;
    }

    private void updateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Save the tracks in SharedPreferences


                //As per the requirements, locally saving the last top 10 tracks.
                ParcelableTrack.saveTop10Tracks(tracks, getActivity());


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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<ParcelableTrack> ptracks = (ArrayList<ParcelableTrack>) ParcelableTrack.loadTop10Tracks(getActivity());
        outState.putParcelableArrayList(getString(R.string.parcelable_tracks_bundle_tag),ptracks);
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
