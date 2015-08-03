package alibros.co.uk.spotifystreamer.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ali on 19/06/15.
 * Custom interface class that simplifies the Spotify SDK
 */


public class ABSpotify {


    private static SpotifyService service;

    /*
     *  Listener Interface for Artist Search
     */
    public interface ABSpotifySearchArtistListener {

        void onSearchSuccessfulWithResult(List<Artist> artists);
        void onSearchError();
    }

    /*
     *  Listener Interface for Tracks Search
     */
    public interface ABSpotifySearchTracksListener {

        void onSearchSuccessfulWithResult(List<Track> tracks);
        void onSearchError();
    }


    //Main constructor
    public ABSpotify() {

        SpotifyApi api = new SpotifyApi();
        service = api.getService();
    }

    /*
     *  Searches for an artist based on string query
     *  TODO:Handle Error Case More Gracefully
     */
    public void searchForArtist(String query, final ABSpotifySearchArtistListener listener) {

        if (service != null) {

            service.searchArtists(query, new Callback<ArtistsPager>() {
                @Override
                public void success(ArtistsPager artistsPager, Response response) {

                    List<Artist> artists = artistsPager.artists.items;
                    listener.onSearchSuccessfulWithResult(artists);
                }

                @Override
                public void failure(RetrofitError error) {

                    listener.onSearchError();
                    RetrofitError.Kind kind = error.getKind();
                    Object body = error.getBody();
                }
            });

        } else {
            listener.onSearchError();
        }
    }

    /*
     *  Searches for an artist based on string query
     *  TODO:Handle Error Case More Gracefully
     */
    public void searchForTracks(String query, String countryCode, final ABSpotifySearchTracksListener listener) {
        if (service != null) {

            Map<String, Object> params = new HashMap<>();
            params.put("country", countryCode);

            service.getArtistTopTrack(query, params, new Callback<Tracks>() {
                @Override
                public void success(Tracks tracks, Response response) {
                    listener.onSearchSuccessfulWithResult(tracks.tracks);
                }

                @Override
                public void failure(RetrofitError error) {
                    Object body = error.getBody();
                    listener.onSearchError();
                }
            });

        } else {
            listener.onSearchError();
        }
    }
}
