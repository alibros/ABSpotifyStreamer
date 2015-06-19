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
import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by Ali on 19/06/15.
 */
public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private final SearchRecyclerViewAdapterListener listener;
    private List<Artist> artists;

    public interface SearchRecyclerViewAdapterListener{

        void itemClicked(Artist artist);

    }


    public SearchRecyclerViewAdapter(List<Artist> artists, SearchRecyclerViewAdapterListener listener) {
        this.artists = artists;
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.artist_name_textview) TextView artistName;
        @InjectView(R.id.artist_image_view) ImageView artistImageView;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int)view.getTag(R.string.tag_cell_position);
            listener.itemClicked(artists.get(position));

        }
    }


    @Override
    public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Artist artist = artists.get(position);
        String imagePath;
        if (!artist.images.isEmpty())
            imagePath = artist.images.get(0).url;
        else
            //set Default Image Path
            imagePath = "https://developer.android.com/assets/images/android_logo@2x.png";

        Picasso.with(viewHolder.artistImageView.getContext())
                .load(imagePath)
                .into(viewHolder.artistImageView);

        viewHolder.artistName.setText(artist.name);
        viewHolder.itemView.setTag(R.string.tag_cell_position, position);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return artists.size();
    }




}
