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
        tracks =  ParcelableTrack.loadTop10Tracks(getActivity());
        updateUI();

        return view;
    }

    private void updateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
