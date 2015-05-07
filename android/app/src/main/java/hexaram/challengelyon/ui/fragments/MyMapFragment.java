package hexaram.challengelyon.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hexaram.challengelyon.R;
import hexaram.challengelyon.services.requestAPI;
import hexaram.challengelyon.ui.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback {



    private SupportMapFragment s_mapFragment;
    private OnFragmentInteractionListener mListener;
    private GoogleMap map;
    Context mapActivityContext;
    Marker destinationMarker;
    Button bSubmitDestination;
    Polyline line;
    Button bSubmitChallenge;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MyMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMapFragment newInstance() {
        MyMapFragment fragment = new MyMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setContext(Context activityContext){
        mapActivityContext = activityContext;
    }
    public void setButton(Button subButtDest, Button subButtChal) { bSubmitDestination = subButtDest; bSubmitChallenge = subButtChal;}

    public MyMapFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        bSubmitDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /********* Disable click on map **********/
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                                              @Override
                                              public void onMapClick(LatLng clickedLatLng) {
                                                  return ;
                                              }
                                          });
                /****************************************/

                LatLng destinationPosition = destinationMarker.getPosition();
                LocationManager locationManager = (LocationManager) mapActivityContext.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

                //todo tokeeeeennnnnennenenenenenenenennene!!
                requestAPI req = new requestAPI("da245e88375373c1b5bdf49f8a0b8f86fdeaecb9");
                try {
                    /******* Velo a cote de position actuelle *********/
                    JSONObject resp = req.getVelo("" + currentLocation.getLatitude(), "" + currentLocation.getLongitude());
                    JSONObject result = resp.getJSONObject("result");
                    String nom = result.getString("nom");
                    int velos_disponibles = result.getInt("velos_disponibles");
                    double longitude = result.getDouble("longitude");
                    double latitude = result.getDouble("latitude");
                    Log.d("lat ", String.valueOf(latitude));

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Station VeloV a votre proximite: ")
                            .snippet(nom + "\n Velos disponibles : " + velos_disponibles)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bike_black_36dp_360)));

                    /********** ***************************/

                /********* Velo a cote destiation ***************/
                    JSONObject respDest = req.getVelo("" + destinationPosition.latitude, "" + destinationPosition.longitude);
                    JSONObject resultDest = respDest.getJSONObject("result");
                    String nomDest = resultDest.getString("nom");
                    int velos_posablesDest = resultDest.getInt("velos_posables");
                    double longitudeDest = resultDest.getDouble("longitude");
                    double latitudeDest = resultDest.getDouble("latitude");
                    Log.d("lat dest ", String.valueOf(latitudeDest));
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitudeDest,longitudeDest))
                            .title("Station VeloV a proximite de la destination: ")
                            .snippet(nomDest + "\n Places disponibles : " + velos_posablesDest)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bike_black_36dp_360)));
                    /**************** **************************/

                    /******* Draw path *********/
                    JSONObject respGoog = req.getGoogleDirection(latitude+","+longitude,latitudeDest+","+longitudeDest );
                    JSONArray routeArray = respGoog.getJSONArray("routes");
                    JSONObject routes = routeArray.getJSONObject(0);
                    JSONObject overviewPolylines = routes
                            .getJSONObject("overview_polyline");
                    String encodedString = overviewPolylines.getString("points");
                    List<LatLng> list = decodePoly(encodedString);

                    for (int z = 0; z < list.size() - 1; z++) {
                        LatLng src = list.get(z);
                        LatLng dest = list.get(z + 1);
                        line = map.addPolyline(new PolylineOptions()
                                .add(new LatLng(src.latitude, src.longitude),
                                        new LatLng(dest.latitude, dest.longitude))
                                .width(5).color(Color.BLUE).geodesic(true));
                    }
                    bSubmitDestination.setVisibility(View.GONE);
                    bSubmitChallenge.setVisibility(View.VISIBLE);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        bSubmitChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) mapActivityContext.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                LatLng destinationPosition = destinationMarker.getPosition();
                //Convert LatLng to Location
                Location destinationLocation = new Location("Destination");
                destinationLocation.setLatitude(destinationPosition.latitude);
                destinationLocation.setLongitude(destinationPosition.longitude);
                destinationLocation.setTime(new Date().getTime()); //Set time as current Date

                float distanceInMeters = destinationLocation.distanceTo(currentLocation);
                boolean isWithin50m = distanceInMeters < 50;
                if (isWithin50m){
                    new AlertDialog.Builder(mapActivityContext)
                            .setTitle("Bravo ! Challenge valide")
                            .setMessage("Vous avez reussi ce Challenge ! Bravo")
                            .setPositiveButton("Retour a la liste des challenge", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mapActivityContext, MainActivity.class);
                                    startActivityForResult(intent, 0);
                                }
                            })

                            .setIcon(android.R.drawable.star_on)
                            .show();
                }else{
                    new AlertDialog.Builder(mapActivityContext)
                            .setTitle("Challenge non reussi")
                            .setMessage("Vous n'etes pas localise a proximite de la destination. Echec du challenge")
                            .setPositiveButton("Retour a la liste des challenge", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mapActivityContext, MainActivity.class);
                                    startActivityForResult(intent, 0);
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        s_mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment));
        if (s_mapFragment == null) {
            s_mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map_container, s_mapFragment).commit();
        }
        s_mapFragment.getMapAsync(this);


    }

        public void zoomToCurrentLocation(){
            LocationManager locationManager = (LocationManager) mapActivityContext.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null)
            {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to current location
                        .zoom(15)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                map.setMyLocationEnabled(true);
            }
        }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        zoomToCurrentLocation();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng clickedLatLng) {
                bSubmitDestination.setVisibility(View.VISIBLE);
                if (destinationMarker == null){
                    destinationMarker = map.addMarker(new MarkerOptions()
                            .position(clickedLatLng)
                            .title("Destination choisie")
                            .draggable(true));
                }else{
                    destinationMarker.remove();
                    destinationMarker = map.addMarker(new MarkerOptions()
                            .position(clickedLatLng)
                            .title("Destination choisie")
                            .draggable(true));
                }

            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public GoogleMap getMyMap(){
        return map;
    }

}
