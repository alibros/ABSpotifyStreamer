package alibros.co.uk.spotifystreamer.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import alibros.co.uk.spotifystreamer.R;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Ali on 21/06/15.
 */
public class ParcelableTrack extends Track implements Parcelable {

    public String artistName;
    public String pName;
    public String albumName;


    public static final Creator<ParcelableTrack> CREATOR =
            new Creator<ParcelableTrack>() {

                @Override
                public ParcelableTrack createFromParcel(Parcel source) {
                    return new ParcelableTrack(source);
                }

                @Override
                public ParcelableTrack[] newArray(int size) {
                    return new ParcelableTrack[size];
                }

            };

    //Constructor to create a Parcelable track from Spotify's Track.
    public ParcelableTrack(Track track) {
        pName = track.name;
        artistName = track.artists.get(0).name;
        albumName = track.album.name;
    }

    private ParcelableTrack(Parcel in) {
        id = in.readString();
        pName = in.readString();
        artistName = in.readString();
        albumName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(pName);
        dest.writeString(artistName);
        dest.writeString(albumName);
    }


    //Saving the top 10 Tracks in SharedPreferences.
    public static void saveTop10Tracks(List<ParcelableTrack> tracks, Context ctx){

        SharedPreferences prefs = ctx.getSharedPreferences(
                ctx.getString(R.string.preferences_tag), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tracks);
        prefsEditor.putString(ctx.getString(R.string.top10TracksTag), json);
        prefsEditor.commit();

    }

    //Loading the top 10 Tracks from SharedPreferences.
    public static List<Track> loadTop10Tracks(Context ctx){
        List<Track> tracks;
        SharedPreferences prefs = ctx.getSharedPreferences(
                ctx.getString(R.string.preferences_tag), Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString(ctx.getString(R.string.top10TracksTag), "");

        Type listType = new TypeToken<ArrayList<ParcelableTrack>>() {
        }.getType();

        tracks = gson.fromJson(json, listType);

        return tracks;
    }

}
