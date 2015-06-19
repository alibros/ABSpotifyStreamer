package alibros.co.uk.spotifystreamer.logic;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ali on 19/06/15.
 */



public class ABSpotify {



    private static SpotifyService service;
    public interface ABSpotifyListener {
        void onSearchSuccessfulWithResult(List<Artist> artists);
        void onSearchError();
    }

    private ABSpotifyListener listener;

    public ABSpotify(ABSpotifyListener listener) {

        SpotifyApi api = new SpotifyApi();

        service = api.getService();
        this.listener = listener;

    }

    public void searchForArtist(String query){
        if (service != null){

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

}
