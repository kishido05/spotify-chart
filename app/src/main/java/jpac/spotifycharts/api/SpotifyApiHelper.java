package jpac.spotifycharts.api;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by syspaulo on 9/24/2015.
 */
public class SpotifyApiHelper {

    public void getTracks(AsyncHttpResponseHandler handler) {
        SpotifyApiRestClient.get("tracks", null, handler);
    }

    public void getTracks(String rank, AsyncHttpResponseHandler handler) {
        SpotifyApiRestClient.get("tracks/" + rank, null, handler);
    }

    public void getTracks(String rank, String country, AsyncHttpResponseHandler handler) {
        SpotifyApiRestClient.get("tracks/" + rank + "/" + country, null, handler);
    }

    public void getTracks(String rank, String country, String windowType, AsyncHttpResponseHandler handler) {
        SpotifyApiRestClient.get("tracks/" + rank + "/" + country + "/" + windowType, null, handler);
    }

    public void getTracks(String rank, String country, String windowType, String date, AsyncHttpResponseHandler handler) {
        SpotifyApiRestClient.get("tracks/" + rank + "/" + country + "/" + windowType + "/" + date, null, handler);
    }
}
