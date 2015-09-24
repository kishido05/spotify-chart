package jpac.spotifycharts.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jpac.spotifycharts.R;
import jpac.spotifycharts.model.Track;
import jpac.spotifycharts.utils.FontUtils;

/**
 * Created by syspaulo on 9/24/2015.
 */
public class TrackListAdapter extends BaseAdapter {

    protected Context context;
    protected LayoutInflater inflater;

    protected ArrayList<Track> tracks;

    public TrackListAdapter(Context c) {
        context = c;
        inflater = LayoutInflater.from(c);

        tracks = new ArrayList<Track>();
    }

    public void add(Track track) {
        tracks.add(track);
    }

    public void clear() {
        tracks.clear();
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int i) {
        return tracks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_track, null);

            Typeface tf = FontUtils.open(context, "fonts/Roboto-Light.ttf");
            holder = new ViewHolder();

            holder.imageArtwork = (ImageView) view.findViewById(R.id.imageArtwork);
            holder.textTrackName = (TextView) view.findViewById(R.id.textTrackName);
            holder.textArtistName = (TextView) view.findViewById(R.id.textArtistName);

            holder.textTrackName.setTypeface(tf, Typeface.BOLD);
            holder.textArtistName.setTypeface(tf);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Track track = tracks.get(i);

        holder.textTrackName.setText(track.getTrackName());
        holder.textArtistName.setText(track.getArtistName());

        Glide.with(context)
                .load(track.getArtworkUrl())
                .override(200, 200)
                .into(holder.imageArtwork);

        return view;
    }

    static class ViewHolder {

        ImageView imageArtwork;
        TextView textTrackName, textArtistName;
    }
}
