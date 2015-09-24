package jpac.spotifycharts;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jpac.spotifycharts.api.SpotifyApiHelper;

/**
 * Created by syspaulo on 9/24/2015.
 */
public class HomeActivity extends Activity {

    private SpotifyApiHelper apiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        apiHelper = new SpotifyApiHelper();
        apiHelper.getTracks(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                toggleLoadingIndicator(false);
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
}
