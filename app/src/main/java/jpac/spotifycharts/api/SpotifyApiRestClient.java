package jpac.spotifycharts.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by syspaulo on 9/24/2015.
 */
public class SpotifyApiRestClient {

    private static final String BASE_URL = "http://charts.spotify.com/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        client.get(BASE_URL + url, params, handler);
    }
}
