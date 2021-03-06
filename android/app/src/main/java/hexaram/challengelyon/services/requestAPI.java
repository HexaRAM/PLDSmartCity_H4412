package hexaram.challengelyon.services;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by maria on 05/05/15.
 */
public class requestAPI {
        private JSONObject mJsonObject ;
        private JSONObject mJsonObjectUser ;
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
                    httpGet.addHeader("content-type","application/json;charset=utf-8");

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    HttpResponse responseBody = httpClient.execute(httpGet);
                    HttpEntity entity = responseBody.getEntity();
                    String fluxJson = EntityUtils.toString(entity, HTTP.UTF_8);

                    JSONObject response = new JSONObject(fluxJson);
                    mJSONObjetT = response;

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
                    httpGet.addHeader("Content-Type","application/json;charset=utf-8");

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    HttpResponse responseBody = httpClient.execute(httpGet);
                    HttpEntity entity = responseBody.getEntity();
                    String fluxJson = EntityUtils.toString(entity, HTTP.UTF_8);

                    JSONObject response = new JSONObject(fluxJson);
                    mJSONObjetT = response;

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

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    HttpResponse responseBody = httpClient.execute(httpGet);
                    HttpEntity entity = responseBody.getEntity();
                    String fluxJson = EntityUtils.toString(entity, HTTP.UTF_8);

                    JSONObject response = new JSONObject(fluxJson);
                    mJSONObjetT = response;

                } catch (Exception ex) {
                    Log.e(TAG, "Failed to send request: " + ex);
                }
                return mJSONObjetT;
            }
    }
        private class TaskChallengesPlayed extends AsyncTask <String, Void, JSONArray> {
            private JSONArray mJSONArray;
            private static final String TAG = "TaskChallPlayed";
            public String serverUrl = "http://vps165185.ovh.net/challengePlayed/";

            @Override
            protected JSONArray doInBackground(String... params) {
                String token = params[0];
                try {
                    //Create an HTTP client
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(serverUrl);

                    httpGet.addHeader("Authorization", "Token " + token);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    HttpResponse responseBody = httpClient.execute(httpGet);
                    HttpEntity entity = responseBody.getEntity();
                    String fluxJson = EntityUtils.toString(entity, HTTP.UTF_8);

                    JSONArray response = new JSONArray(fluxJson);
                    mJSONArray = response;

                } catch (Exception ex) {
                    Log.e(TAG, "Failed to send request: " + ex);
                }
                return mJSONArray;
            }
        }

        private class TaskGetVelo extends AsyncTask <String, Void, JSONObject> {
        private JSONObject mJSONObject;
        private static final String TAG = "TaskGetVelo";
        public String serverUrl = "http://vps165185.ovh.net/getClosestStation/";

        @Override
        protected JSONObject doInBackground(String... params) {
            String token = params[0];
            String latitude = params[1];
            String longitude = params[2];
            serverUrl = serverUrl + "?latitude=" + latitude + "&longitude=" + longitude;

            try {
                //Create an HTTP client
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(serverUrl);

                httpGet.addHeader("Authorization", "Token " + token);

                //Perform the request and check the status code
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                String responseBody = httpClient.execute(httpGet, responseHandler);

                JSONObject response = new JSONObject(responseBody);
                mJSONObject = response;



            } catch (Exception ex) {
                Log.e(TAG, "Failed to send request: " + ex);
            }
            return mJSONObject;
        }
    }



        private class TaskClickUrl extends AsyncTask <String, Void, JSONObject> {
            private JSONObject mJSONObject;
            private static final String TAG = "TaskClickUrl";
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

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    HttpResponse responseBody = httpClient.execute(httpGet);
                    HttpEntity entity = responseBody.getEntity();
                    String fluxJson = EntityUtils.toString(entity, HTTP.UTF_8);

                    JSONObject response = new JSONObject(fluxJson);
                    mJsonObject = response;


                } catch (Exception ex) {
                    Log.e(TAG, "Failed to send request: " + ex);
                }
                return mJsonObject;
            }
        }

        private class TaskGoogleDirection extends AsyncTask <String, Void, JSONObject> {
        private JSONObject mJSONObject;
        private static final String TAG = "TaskGoogleDirection";
        public String serverUrl = "https://maps.googleapis.com/maps/api/directions/json";

        @Override
        protected JSONObject doInBackground(String... params) {

            String origin = params[0];
            String destination = params[1];
            String mode = "bicycling";
            String ApiKey = "AIzaSyDYT95uE2lKZ0XFn7UkyRPSwr1qTDr0fVk";
            serverUrl = serverUrl + "?origin=" + origin + "&destination=" + destination + "&mode=" + mode + "&key=" + ApiKey;

            try {
                //Create an HTTP client
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(serverUrl);



                //Perform the request and check the status code
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                String responseBody = httpClient.execute(httpGet, responseHandler);

                JSONObject response = new JSONObject(responseBody);
                mJSONObject = response;



            } catch (Exception ex) {
                Log.e(TAG, "Failed to send request: " + ex);
            }
            return mJSONObject;
        }
    }


        private class TaskLogout extends AsyncTask<Void, Void, JSONObject> {

            private JSONObject mJSONObjetT;
            private static final String TAG = "TaskChallenge";
            public String serverUrl = "http://vps165185.ovh.net/auth/logout" ;

            @Override
            protected JSONObject doInBackground(Void... params) {

                try {
                    //Create an HTTP client
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(serverUrl);
                    httpPost.addHeader("Authorization", "Token " + token);



                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    HttpResponse httpResponse = httpClient.execute(httpPost);



                } catch (Exception ex) {
                    Log.e(TAG, "Failed to send request due to: " + ex);
                }
                return null;
            }
        }
        private class TaskGetUserByToken extends AsyncTask <String, Void, JSONObject> {
            private JSONObject mJSONObjetT;
            private static final String TAG = "TaskGetUser";
            public String serverUrl = "http://vps165185.ovh.net/auth/me" ;

            @Override
            protected JSONObject doInBackground(String... params) {
                String token = params[0];
                try {

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(serverUrl);
                    httpGet.addHeader("Authorization", "Token " + token);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    HttpResponse responseBody = httpClient.execute(httpGet);
                    HttpEntity entity = responseBody.getEntity();
                    String fluxJson = EntityUtils.toString(entity, HTTP.UTF_8);

                    JSONObject response = new JSONObject(fluxJson);
                    mJSONObjetT = response;

                } catch (Exception ex) {
                    Log.e(TAG, "Failed to send request due to: " + ex);
                }
                return mJSONObjetT;
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

        public JSONObject getUserByToken() throws ExecutionException, InterruptedException, JSONException {
            TaskGetUserByToken getUserByToken = new TaskGetUserByToken();
            getUserByToken.execute(token);
            mJsonObject = getUserByToken.get();
            this.mJsonObjectUser = mJsonObject;

            return mJsonObject;
        }

        public JSONObject getUser() throws InterruptedException, ExecutionException, JSONException {
            JSONObject myUserJson = getUserByToken();
            String idUser = mJsonObject.getString("id");
            TaskGetUser getUserT = new TaskGetUser();
            getUserT.execute(token,idUser);
            //Log.d("rrrrrrrrrrr", getUserT.get().getString("email"));
            return getUserT.get();
        }


    public JSONObject getVelo(String latitude,String longitude) throws ExecutionException, InterruptedException {
        TaskGetVelo getVelov = new TaskGetVelo();
        getVelov.execute(token,latitude, longitude);
        mJsonObject = getVelov.get();
        return mJsonObject;
    }
        public JSONObject clickURL(String url) throws ExecutionException, InterruptedException {
            TaskClickUrl click = new TaskClickUrl();
            click.execute(url);
            mJsonObject = click.get();
            return mJsonObject;
        }

        public JSONObject logout()  throws ExecutionException, InterruptedException {
            TaskLogout logMeOut = new TaskLogout();
            logMeOut.execute();
            mJsonObject = logMeOut.get();
            return mJsonObject;
        }
        public JSONObject getGoogleDirection(String origin,String destination) throws ExecutionException, InterruptedException {
            TaskGoogleDirection googleDir = new TaskGoogleDirection();
            googleDir.execute(origin, destination);
            mJsonObject = googleDir.get();
            return mJsonObject;
        }



    }


