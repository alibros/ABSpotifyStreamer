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

import java.util.List;

import alibros.co.uk.spotifystreamer.R;
import alibros.co.uk.spotifystreamer.logic.ABSpotify;
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

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);


        abSpotify = new ABSpotify();

//        String artistid = getIntent().getStringExtra("ARTISTID");
//        String artistname = getIntent().getStringExtra("ARTISTNAME");
//
//        getActivity().getActionBar().setSubtitle(artistname);
//        getActivity().getActionBar().setTitle("Top 10 Tracks");
//
//        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String countryCode = tm.getSimCountryIso();
//
//        abSpotify.searchForTracks(artistid, countryCode ,new ABSpotify.ABSpotifySearchTracksListener() {
//            @Override
//            public void onSearchSuccessfulWithResult(List<Track> _tracks) {
//                tracks = _tracks;
//                if (tracks.size()==0){
//                    displayErrorToastWithText(getString(R.string.no_tracks_found_tag));
//                }
//                updateUI();
//            }
//
//            @Override
//            public void onSearchError() {
//                displayErrorToastWithText(getString(R.string.spotify_call_error_text));
//            }
//        });



        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
