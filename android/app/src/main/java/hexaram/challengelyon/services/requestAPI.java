package hexaram.challengelyon.services;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.models.User;

/**
 * Created by maria on 05/05/15.
 */
public class requestAPI {
        private JSONObject mJsonObject ;
        private class TaskGetUser extends AsyncTask <Void, Void, JSONObject> {
            private JSONObject mJSONObjetT;
            private static final String TAG = "Get user log";
            public String serverUrl = "http://vps165185.ovh.net/users/1" ;

            @Override
            protected JSONObject doInBackground(Void... params) {
                try {
                    //Create an HTTP client
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(serverUrl);
                    httpGet.addHeader("Authorization", "Token 9cd348ec7010d544cc74a44311ea22ff5b7dc02a");

                    //Perform the request and check the status code
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    String responseBody = httpClient.execute(httpGet, responseHandler);

                    JSONObject response = new JSONObject(responseBody);
                    mJSONObjetT = response;

                    //Just for testing
                    String fluxJson = "";
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity entity = httpResponse.getEntity();
                    fluxJson = EntityUtils.toString(entity, HTTP.UTF_8);
                    Log.d("my JSON response", fluxJson);
                    //end testing

                } catch (Exception ex) {
                    Log.e(TAG, "Failed to send request due to: " + ex);
                }
                return mJSONObjetT;
            }
        }
        public requestAPI(String url, String token, String type) {
            TaskGetUser getMyUser = new TaskGetUser();
            getMyUser.execute();

        }
        public requestAPI() throws ExecutionException, InterruptedException {
            TaskGetUser getMyUser = new TaskGetUser();
            getMyUser.execute();
            mJsonObject = getMyUser.get();

        }
        public JSONObject getMyJSONObjet() {
            return mJsonObject;

        }


    }


