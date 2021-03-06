package alibros.co.uk.spotifystreamer.logic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import alibros.co.uk.spotifystreamer.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Ali on 19/06/15.
 * Adapter for Tracks Recycler View. (See TracksFragment)
 */
public class TracksRecyclerViewAdapter extends RecyclerView.Adapter<TracksRecyclerViewAdapter.ViewHolder> {

    private final TracksRecyclerViewAdapterListener listener;
    private List<ParcelableTrack> tracks;

    public interface TracksRecyclerViewAdapterListener{
        void itemClicked(int trackIndex);
    }

    public TracksRecyclerViewAdapter(List<ParcelableTrack> tracks, TracksRecyclerViewAdapterListener listener) {
        this.tracks = tracks;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.track_name) TextView trackName;
        @InjectView(R.id.album_name) TextView albumName;
        @InjectView(R.id.track_image_view) ImageView albumImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int)view.getTag(R.string.tag_cell_position);
            listener.itemClicked(position);
        }
    }

    @Override
    public TracksRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ParcelableTrack track = tracks.get(position);
        String imagePath;

        imagePath = track.albumCoverUrl;

        Picasso.with(viewHolder.albumImage.getContext())
                .load(imagePath)
                .into(viewHolder.albumImage);

        viewHolder.albumName.setText(track.albumName);
        viewHolder.trackName.setText(track.pName);
        viewHolder.itemView.setTag(R.string.tag_cell_position, position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(tracks !=null) {
            return tracks.size();
        }
        else
            return 0;
    }
}
