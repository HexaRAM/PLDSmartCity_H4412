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
import org.json.JSONArray;
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
        private String token;
        private class TaskGetUser extends AsyncTask <String, Void, JSONObject> {
            private JSONObject mJSONObjetT;
            private static final String TAG = "TaskGetUser";
            public String serverUrl = "http://vps165185.ovh.net/users/" ;

            @Override
            protected JSONObject doInBackground(String... params) {
                String token = params[0];
                String id = params[1];
                serverUrl += id;
                try {
                    //Create an HTTP client

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(serverUrl);
                    httpGet.addHeader("Authorization", "Token " + token);

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
        private class TaskChallenge extends AsyncTask <String, Void, JSONObject> {
        private JSONObject mJSONObjetT;
        private static final String TAG = "TaskChallenge";
        public String serverUrl = "http://vps165185.ovh.net/challenges" ;

        @Override
        protected JSONObject doInBackground(String... params) {
            String token = params[0];
            try {
                //Create an HTTP client
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(serverUrl);
                httpGet.addHeader("Authorization", "Token " + token);

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
        private class TaskChallengesToValidate extends AsyncTask <String, Void, JSONObject> {
            private JSONObject mJSONObjetT;
            private static final String TAG = "TaskChallToValidate";
            public String serverUrl = "http://vps165185.ovh.net/toValidate/" ;

            @Override
            protected JSONObject doInBackground(String... params) {
                String token = params[0];
                try {
                    //Create an HTTP client
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(serverUrl);

                    httpGet.addHeader("Authorization", "Token " + token);

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
                    Log.e(TAG, "Failed to send request: " + ex);
                }
                return mJSONObjetT;
            }
    }
        private class TaskChallengesPlayed extends AsyncTask <String, Void, JSONArray> {
            private JSONArray mJSONArray;
            private static final String TAG = "TaskChallToValidate";
            public String serverUrl = "http://vps165185.ovh.net/challengePlayed/";

            @Override
            protected JSONArray doInBackground(String... params) {
                String token = params[0];
                try {
                    //Create an HTTP client
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(serverUrl);

                    httpGet.addHeader("Authorization", "Token " + token);

                    //Perform the request and check the status code
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    String responseBody = httpClient.execute(httpGet, responseHandler);

                    JSONArray response = new JSONArray(responseBody);
                    mJSONArray = response;

                    //TODO remove comments when all done
                    //Just for testing
                    String fluxJson = "";
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    HttpEntity entity = httpResponse.getEntity();
                    fluxJson = EntityUtils.toString(entity, HTTP.UTF_8);
                    Log.d("my JSON response", fluxJson);
                    //end testing

                } catch (Exception ex) {
                    Log.e(TAG, "Failed to send request: " + ex);
                }
                return mJSONArray;
            }
        }

        private class TaskPlayChallenge extends AsyncTask <String, Void, JSONObject> {
            private JSONObject mJSONObject;
            private static final String TAG = "TaskChallToValidate";
            public String serverUrl = "";

            @Override
            protected JSONObject doInBackground(String... params) {
                String url = params[0];
                serverUrl = url;
                try {
                    //Create an HTTP client
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(serverUrl);

                    httpGet.addHeader("Authorization", "Token " + token);

                    //Perform the request and check the status code
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    String responseBody = httpClient.execute(httpGet, responseHandler);

                    JSONObject response = new JSONObject(responseBody);
                    mJsonObject = response;


                } catch (Exception ex) {
                    Log.e(TAG, "Failed to send request: " + ex);
                }
                return mJsonObject;
            }
        }

        public requestAPI(String token)  {
            this.token = token;
        }

        public JSONObject getAllChallenges() throws ExecutionException, InterruptedException {
            TaskChallenge getAll = new TaskChallenge();
            getAll.execute(token);
            mJsonObject = getAll.get();
            return mJsonObject;
        }

        public JSONObject getChallengesToValidate() throws ExecutionException, InterruptedException {
            TaskChallengesToValidate getAlltoValidate = new TaskChallengesToValidate();
            getAlltoValidate.execute(token);
            mJsonObject = getAlltoValidate.get();
            return mJsonObject;
        }

        public JSONArray getAllChallengesPlayed() throws ExecutionException, InterruptedException {
            TaskChallengesPlayed getChallengesPlayed = new TaskChallengesPlayed();
            getChallengesPlayed.execute(token);
            JSONArray mJSONArray;
            mJSONArray = getChallengesPlayed.get();
            return mJSONArray;
        }

        public JSONObject getUser(String id) throws ExecutionException, InterruptedException {
            TaskGetUser getUser = new TaskGetUser();
            getUser.execute(token,id);
            mJsonObject = getUser.get();
            return mJsonObject;
        }

        public JSONObject playChallenge(String url) throws ExecutionException, InterruptedException {
            TaskPlayChallenge getChallengesPlayedById = new TaskPlayChallenge();
            getChallengesPlayedById.execute(url);
            mJsonObject = getChallengesPlayedById.get();
            return mJsonObject;
        }



    }


