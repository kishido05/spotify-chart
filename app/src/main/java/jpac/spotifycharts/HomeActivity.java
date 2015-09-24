package jpac.spotifycharts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jpac.spotifycharts.adapter.TrackListAdapter;
import jpac.spotifycharts.api.SpotifyApiHelper;
import jpac.spotifycharts.model.Track;

/**
 * Created by syspaulo on 9/24/2015.
 */
public class HomeActivity extends Activity {

    private SpotifyApiHelper apiHelper;

    private ViewGroup panelSpinner;
    private Spinner rankSpin, countrySpin, windowTypeSpin, dateSpin;

    private ListView trackList;
    private TrackListAdapter trackListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rankSpin = (Spinner) findViewById(R.id.rankSpinner);
        panelSpinner = (ViewGroup) findViewById(R.id.panelSpinner);

        trackList = (ListView) findViewById(R.id.listTracks);
        trackListAdapter = new TrackListAdapter(this);
        trackList.setAdapter(trackListAdapter);

        apiHelper = new SpotifyApiHelper();
        loadChartRanks();
    }

    private void loadChartRanks() {
        apiHelper.getTracks(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    initializeSpinners(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }
        });
    }

    protected void toggleLoadingIndicator(boolean display) {
        int visibility = display ? View.VISIBLE : View.INVISIBLE;

        findViewById(R.id.loadingBar).setVisibility(visibility);
        findViewById(R.id.textLoading).setVisibility(visibility);
    }

    private void initializeSpinners(JSONArray response) throws JSONException {
        int len = response.length();

        ArrayList<String> ranks = new ArrayList<String>();

        for (int i=0; i<len; i++) {
            ranks.add(response.getString(i));
        }

        ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ranks);
        rankSpin.setAdapter(rankAdapter);

        loadChartCountries();
    }

    private void loadChartCountries() {
        apiHelper.getTracks(getSelectedItem(rankSpin), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    createCountrySpinner(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }
        });
    }

    private void createCountrySpinner(JSONArray response) throws JSONException {
        int len = response.length();

        ArrayList<String> countries = new ArrayList<String>();

        for (int i=0; i<len; i++) {
            countries.add(response.getString(i));
        }

        countrySpin = new Spinner(this);
        createSpinner(countrySpin, countries);

        loadChartWindowTypes();
    }

    private void loadChartWindowTypes() {
        apiHelper.getTracks(getSelectedItem(rankSpin), getSelectedItem(countrySpin), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    createWindowTypeSpinner(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }
        });
    }

    private void createWindowTypeSpinner(JSONArray response) throws JSONException {
        int len = response.length();

        ArrayList<String> windowTypes = new ArrayList<String>();

        for (int i=0; i<len; i++) {
            windowTypes.add(response.getString(i));
        }

        windowTypeSpin = new Spinner(this);
        createSpinner(windowTypeSpin, windowTypes);

        loadChartDates();
    }

    private void loadChartDates() {
        apiHelper.getTracks(getSelectedItem(rankSpin), getSelectedItem(countrySpin), getSelectedItem(windowTypeSpin), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                toggleLoadingIndicator(false);

                try {
                    createDateSpinner(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }
        });
    }

    private void createDateSpinner(JSONArray response) throws JSONException {
        int len = response.length();

        ArrayList<String> dates = new ArrayList<String>();

        for (int i=0; i<len; i++) {
            dates.add(response.getString(i));
        }

        dateSpin = new Spinner(this);
        createSpinner(dateSpin, dates);

        initializeSpinnerAction();

        loadChartTracks();
    }

    private String getSelectedItem(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    private void adjustSpinnerLayout(Spinner spinner) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        spinner.setLayoutParams(params);
    }

    private void createSpinner(Spinner spinner, List<String> items) {
        adjustSpinnerLayout(spinner);

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(itemAdapter);

        panelSpinner.addView(spinner);
    }

    private void loadChartTracks() {
        String rank = getSelectedItem(rankSpin);
        String country = getSelectedItem(countrySpin);
        String windowType = getSelectedItem(windowTypeSpin);
        String date = getSelectedItem(dateSpin);

        toggleLoadingIndicator(true);

        apiHelper.getTracks(rank, country, windowType, date, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                toggleLoadingIndicator(false);

                try {
                    displayTracks(response.getJSONArray("tracks"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                toggleLoadingIndicator(false);
            }
        });
    }

    private void initializeSpinnerAction() {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadChartTracks();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        rankSpin.setOnItemSelectedListener(listener);
        countrySpin.setOnItemSelectedListener(listener);
        windowTypeSpin.setOnItemSelectedListener(listener);
        dateSpin.setOnItemSelectedListener(listener);
    }

    private void displayTracks(JSONArray tracks) throws JSONException {
        int len = tracks.length();

        trackListAdapter.clear();
        trackList.setEmptyView(findViewById(R.id.emptyView));

        for (int i=0; i<len; i++) {
            JSONObject object = tracks.getJSONObject(i);

            Track track = new Track();

            track.setTrackName(object.getString("track_name"));
            track.setArtistName(object.getString("artist_name"));
            track.setArtworkUrl(object.getString("artwork_url"));

            trackListAdapter.add(track);
        }

        trackListAdapter.notifyDataSetChanged();
    }

}
