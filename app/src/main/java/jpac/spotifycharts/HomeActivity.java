package jpac.spotifycharts;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import jpac.spotifycharts.adapter.TrackListAdapter;
import jpac.spotifycharts.api.SpotifyApiHelper;
import jpac.spotifycharts.model.Track;
import jpac.spotifycharts.utils.FontUtils;

/**
 * Created by syspaulo on 9/24/2015.
 */
public class HomeActivity extends Activity {

    private SpotifyApiHelper apiHelper;

    private ViewGroup panelSpinner;
    private Spinner rankSpin, countrySpin, windowTypeSpin, dateSpin;

    private ListView trackList;
    private TrackListAdapter trackListAdapter;

    // keep track of ISO list provided by Spotify Chart
    private HashMap<String, String> countryISOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rankSpin = (Spinner) findViewById(R.id.rankSpinner);
        panelSpinner = (ViewGroup) findViewById(R.id.panelSpinner);

        trackList = (ListView) findViewById(R.id.listTracks);
        trackListAdapter = new TrackListAdapter(this);
        trackList.setEmptyView(findViewById(R.id.emptyView));
        trackList.setAdapter(trackListAdapter);

        findViewById(R.id.emptyView).setVisibility(View.INVISIBLE);

        Typeface tf = FontUtils.open(this, "fonts/Roboto-Light.ttf");
        ((TextView) findViewById(R.id.textLoading)).setTypeface(tf);
        ((TextView) findViewById(R.id.emptyView)).setTypeface(tf);
        ((TextView) findViewById(R.id.textError)).setTypeface(tf);

        countryISOList = new HashMap<String, String>();

        apiHelper = new SpotifyApiHelper();
        loadChartRanks();
    }

    private void loadChartRanks() {
        apiHelper.getTracks(new SimpleJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    initializeSpinners(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

        Locale locale = Locale.getDefault();
        for (int i=0; i<len; i++) {
            ranks.add(response.getString(i).replace('_', ' ').toUpperCase(locale));
        }

        ArrayAdapter<String> rankAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ranks) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                TextView textView = (TextView) v.findViewById(android.R.id.text1);

                Typeface tf = FontUtils.open(getApplication(), "fonts/Roboto-Light.ttf");
                textView.setTypeface(tf);

                return v;
            }
        };
        rankSpin.setAdapter(rankAdapter);

        loadChartCountries();
    }

    private void loadChartCountries() {
        apiHelper.getTracks(getSelectedItem(rankSpin), new SimpleJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    createCountrySpinner(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createCountrySpinner(JSONArray response) throws JSONException {
        int len = response.length();

        ArrayList<String> countries = new ArrayList<String>();

        countryISOList.clear();

        Locale locale = Locale.getDefault();
        for (int i=0; i<len; i++) {
            String iso = response.getString(i);
            if (iso.equals("global")) {
                countryISOList.put(iso, iso);
                countries.add(0, iso.toUpperCase(locale));
            } else {
                Locale fromIso = new Locale("", iso.toUpperCase(locale));
                countryISOList.put(fromIso.getDisplayCountry().toLowerCase(locale), iso);
                countries.add(fromIso.getDisplayCountry().toUpperCase(locale));
            }
        }

        countrySpin = new Spinner(this);
        createSpinner(countrySpin, countries);

        loadChartWindowTypes();
    }

    private void loadChartWindowTypes() {
        apiHelper.getTracks(getSelectedItem(rankSpin), getSelectedItem(countrySpin), new SimpleJsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    createWindowTypeSpinner(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createWindowTypeSpinner(JSONArray response) throws JSONException {
        int len = response.length();

        ArrayList<String> windowTypes = new ArrayList<String>();

        Locale locale = Locale.getDefault();
        for (int i=0; i<len; i++) {
            windowTypes.add(response.getString(i).toUpperCase(locale));
        }

        windowTypeSpin = new Spinner(this);
        createSpinner(windowTypeSpin, windowTypes);

        loadChartDates();
    }

    private void loadChartDates() {
        apiHelper.getTracks(getSelectedItem(rankSpin), getSelectedItem(countrySpin), getSelectedItem(windowTypeSpin), new SimpleJsonHttpResponseHandler() {

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
        });
    }

    private void createDateSpinner(JSONArray response) throws JSONException {
        int len = response.length();

        ArrayList<String> dates = new ArrayList<String>();

        Locale locale = Locale.getDefault();
        for (int i=0; i<len; i++) {
            dates.add(response.getString(i).toUpperCase(locale));
        }

        dateSpin = new Spinner(this);
        createSpinner(dateSpin, dates);

        displaySpinner();
        initializeSpinnerAction();

        loadChartTracks();
    }

    private void displaySpinner() {
        rankSpin.setVisibility(View.VISIBLE);
        panelSpinner.setVisibility(View.VISIBLE);
    }

    private String getSelectedItem(Spinner spinner) {
        Locale locale = Locale.getDefault();
        return spinner.getSelectedItem().toString().replace(' ', '_').toLowerCase(locale);
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

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                TextView textView = (TextView) v.findViewById(android.R.id.text1);

                Typeface tf = FontUtils.open(getApplication(), "fonts/Roboto-Light.ttf");
                textView.setTypeface(tf);

                return v;
            }
        };
        spinner.setAdapter(itemAdapter);

        panelSpinner.addView(spinner);
    }

    private void loadChartTracks() {
        String rank = getSelectedItem(rankSpin);
        String country = countryISOList.get(getSelectedItem(countrySpin));
        String windowType = getSelectedItem(windowTypeSpin);
        String date = getSelectedItem(dateSpin);

        toggleLoadingIndicator(true);

        findViewById(R.id.emptyView).setVisibility(View.INVISIBLE);
        findViewById(R.id.textError).setVisibility(View.INVISIBLE);

        apiHelper.getTracks(rank, country, windowType, date, new SimpleJsonHttpResponseHandler() {

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

    private class SimpleJsonHttpResponseHandler extends JsonHttpResponseHandler {

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

            HomeActivity.this.toggleLoadingIndicator(false);

            displayError(throwable.getMessage());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            HomeActivity.this.toggleLoadingIndicator(false);

            displayError(throwable.getMessage());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            HomeActivity.this.toggleLoadingIndicator(false);

            displayError(throwable.getMessage());
        }

        private void displayError(String message) {
            HomeActivity.this.trackListAdapter.clear();
            HomeActivity.this.trackListAdapter.notifyDataSetChanged();

            findViewById(R.id.emptyView).setVisibility(View.INVISIBLE);

            TextView error = (TextView) findViewById(R.id.textError);

            error.setVisibility(View.VISIBLE);
            error.setText(message);
        }
    }
}
