package alibros.co.uk.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import alibros.co.uk.spotifystreamer.logic.ABSpotify;
import alibros.co.uk.spotifystreamer.logic.ParcelableArtist;
import alibros.co.uk.spotifystreamer.logic.SearchRecyclerViewAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Artist;


public class SearchFragment extends Fragment {


    //Injecting view elements
    @InjectView(R.id.search_edit_text)
    EditText searchEditText;
    @InjectView(R.id.results_recyler_view)
    RecyclerView resultRecyclerView;

    //arbitrary id
    private final int TRIGGER_SERACH = 1;

    //a guesstimate delay time based on average typing speed
    private final long SEARCH_TRIGGER_DELAY_IN_MS = 1000;

    private ABSpotify abSpotify;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private ArrayList<ParcelableArtist> pArtists = new ArrayList<>();


    //Listener Interface
    public interface SearchFragmentListener {
        void searchItemSelected(String artistID, String artistName);
    }

    private SearchFragmentListener mListener;



    public SearchFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_search, container, false);


        ButterKnife.inject(this,view);

        abSpotify = new ABSpotify();

        if (savedInstanceState!=null)
            pArtists = savedInstanceState.getParcelableArrayList(getString(R.string.parcelable_artists_bundle_tag));


        if (pArtists!=null)
            if (pArtists.size()>0)
                updateRecylcerView(pArtists);

        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Only search if at least 2 Characters have been entered.
                if (s.length()>1) {
                    handler.removeMessages(TRIGGER_SERACH);
                    handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
                }
            }

        });

        //Hide the keyboard when Search button is pressed.
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                }
                return false;
            }
        });


        //Set the Recyycler View
        resultRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        resultRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.parcelable_artists_bundle_tag),pArtists);
    }

    private ABSpotify.ABSpotifySearchArtistListener abSpotifyListener = new ABSpotify.ABSpotifySearchArtistListener() {
        @Override
        public void onSearchSuccessfulWithResult(final List<Artist> artists) {
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (artists.size() == 0) {
                            Toast.makeText(getActivity(), R.string.no_artist_error_text, Toast.LENGTH_LONG).show();
                        } else {
                            pArtists = new ArrayList<ParcelableArtist>();
                            for (Artist artist : artists) {
                                ParcelableArtist pa = new ParcelableArtist(artist);
                                pArtists.add(pa);

                            }
                            updateRecylcerView(pArtists);
                        }
                    }
                });
            }

        }

        @Override
        public void onSearchError() {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), R.string.spotify_call_error_text, Toast.LENGTH_LONG).show();

                }
            });


        }
    };

    private void updateRecylcerView(List<ParcelableArtist> artists) {

        recyclerAdapter = new SearchRecyclerViewAdapter(artists, new SearchRecyclerViewAdapter.SearchRecyclerViewAdapterListener() {
            @Override
            public void itemClicked(ParcelableArtist artist) {

                if (mListener != null) {
                    mListener.searchItemSelected(artist.pId,artist.pName);
                }
            }
        });

        resultRecyclerView.setAdapter(recyclerAdapter);


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TRIGGER_SERACH) {
                triggerSearch();
            }
        }


    };

    private void triggerSearch() {

        String query = searchEditText.getText().toString();
        abSpotify.searchForArtist(query,abSpotifyListener);



    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SearchFragmentListener) activity;
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

}
