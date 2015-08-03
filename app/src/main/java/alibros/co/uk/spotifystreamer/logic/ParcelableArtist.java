package alibros.co.uk.spotifystreamer.logic;

import android.os.Parcel;
import android.os.Parcelable;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Ali on 22/06/15.
 * Custom class to add Parecelability(is that a word?)to Spotify Artist class
 */
public class ParcelableArtist extends Artist implements Parcelable {

    // TODO:Currently only these 3 properties are used. Should be extended to all Artist properties.
    public String pName;
    public String pId;
    public String pImageUrl;

    public static final Parcelable.Creator<ParcelableArtist> CREATOR =
            new Parcelable.Creator<ParcelableArtist>() {

                @Override
                public ParcelableArtist createFromParcel(Parcel source) {
                    return new ParcelableArtist(source);
                }

                @Override
                public ParcelableArtist[] newArray(int size) {
                    return new ParcelableArtist[size];
                }

            };

    public ParcelableArtist(Artist artist) {

        pName = artist.name;
        pId = artist.id;
        //Artist can have a null images property.
        if (artist.images.size()>0) pImageUrl = artist.images.get(0).url;
    }

    private ParcelableArtist(Parcel in) {

        pName = in.readString();
        pId = in.readString();
        if (in.dataAvail() > 0) pImageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pName);
        dest.writeString(pId);
        if (pImageUrl != null) dest.writeString(pImageUrl);
    }

}
