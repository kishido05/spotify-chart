package jpac.spotifycharts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import jpac.spotifycharts.model.Track;

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
        return null;
    }
}
