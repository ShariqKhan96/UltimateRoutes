package com.example.shariqkhan.wfdsacloning;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.shariqkhan.wfdsacloning.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    EditText edt1;
    EditText edt2;
    Button b1;
    List<Address> addressListStart;
    List<Address> addressListEnd;
    LatLng startPoint;
    LatLng endPoint;
    private List<Polyline> polylines;
    ProgressDialog progressDialog;
    Spinner sp1;
    boolean decideRouteType = false;

    TextView text1;
    TextView text2;
    String singleRouteDistance;
    String singleRouteTime;
    Address startingAddress;
    Address endingAddress;


    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        polylines = new ArrayList<>();

        sp1 = (Spinner) findViewById(R.id.sp1);

        text1= (TextView) findViewById(R.id.distance);
        text2 = (TextView)findViewById(R.id.time);

        String array[] = {"","Single", "Alternative"};
        ArrayAdapter<String >adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        sp1.setAdapter(adapter);

   sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       @Override
       public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
           String item = adapterView.getItemAtPosition(i).toString();
           if(item.equals("Single"))
           {
               decideRouteType = false;
           }else{decideRouteType = true;}
       }

       @Override
       public void onNothingSelected(AdapterView<?> adapterView) {

       }
   });

        edt1 = (EditText) findViewById(R.id.editText);
        edt2 = (EditText) findViewById(R.id.editText2);
        b1 = (Button) findViewById(R.id.button2);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        PolylineOptions options = new PolylineOptions().add(new LatLng(9.281509, 76.453900))
//                .add(new LatLng(9.250578, 76.537851))
//                .add(new LatLng(9.164237, 76.501683)).width(4).color(Color.BLACK).geodesic(true);


        // Add a marker in Sydney and move the camera
        //   LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //   mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //   mMap.addPolyline(options);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start = edt1.getText().toString();
                String end = edt2.getText().toString();

                Log.e("AddressStart", start);
                Log.e("AddressEnd", end);


                final Geocoder geocoder = new Geocoder(getApplicationContext());
                addressListStart = null;
                addressListEnd = null;

                try {
                    addressListStart = geocoder.getFromLocationName(start, 1);
                    addressListEnd = geocoder.getFromLocationName(end, 1);

                } catch (IOException e) {
                    Log.e("Exception", e.getMessage());
                }
                Address startingAddress = addressListStart.get(0);
                Address endingAddress = addressListEnd.get(0);

                startPoint = new LatLng(startingAddress.getLatitude(), startingAddress.getLongitude());
                endPoint = new LatLng(endingAddress.getLatitude(), endingAddress.getLongitude());
                Log.e("start", String.valueOf(startPoint));
                Log.e("end", String.valueOf(endPoint));

                  Turtle turtle = new Turtle();
              //  turtle.getPoints(start,end);

                mMap.addMarker(new MarkerOptions().position(startPoint).title(startingAddress.getCountryName()));
                mMap.addMarker(new MarkerOptions().position(endPoint).title(endingAddress.getCountryName()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 4));


                turtle.drawPath(startPoint);

                //  drawRoute(startPoint);

            }
        });


    }

    private void drawRoute(LatLng startPoint) {

//        Log.e("RoutingBegins", "Draw");
//        Routing routing = new Routing.Builder()
//                .travelMode(AbstractRouting.TravelMode.DRIVING)
//                .withListener(this)
//                .alternativeRoutes(decideRouteType)
//                .waypoints(startPoint, endPoint)
//                .build();
//        routing.execute();
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {

            Toast.makeText(this, "No Such Route Exists Or Network Exceptoion!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }


    @Override
    public void onRoutingStart() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }


        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
       //
            // int colorIndex = i % COLORS.length;

            Log.e("i", String.valueOf(route.size()));


            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(Color.RED);
            polyOptions.width(10);
            polyOptions.addAll(route.get(i).getPoints());

            List<LatLng> routeList = route.get(i).getPoints();
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

            try {
                List<Address> nameOfLocality = geocoder.getFromLocation(routeList.get(0).latitude, routeList.get(0).longitude, 1);
                String state = nameOfLocality.get(0).getLocality();
              //  String ss = nameOfLocality.get(0).getAddressLine(2);
                String sss = nameOfLocality.get(0).getAddressLine(0);
                Log.e("s", state);
              //  Log.e("ss", ss);
                Log.e("sss", sss);


            } catch (IOException e) {
                e.printStackTrace();
            }


            Log.e("points", String.valueOf(routeList.size()));

            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
            //route.get(i).getDistanceValue();

            if (!decideRouteType)
            {
                text1.setText(route.get(i).getDistanceText());
                text2.setText(route.get(i).getDurationText());

            }
            Log.e("RouteValue", "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue());

        }
        progressDialog.dismiss();

    }

    @Override
    public void onRoutingCancelled() {
        progressDialog.dismiss();
    }


    private class Turtle {
        Turtle() {
        }

        public void drawPath(LatLng startPoint) {
            Log.e("RoutingBegins", "Draw");
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(MapsActivity.this)
                    .alternativeRoutes(decideRouteType)
                    .waypoints(startPoint, endPoint)
                    .build();
            routing.execute();


            //here asynctask shuru hojaiga package ka then api call hogi internally...
        }

        public void getPoints(String start, String end)
        {
            final Geocoder geocoder = new Geocoder(getApplicationContext());
            addressListStart = null;
            addressListEnd = null;

            try {
                addressListStart = geocoder.getFromLocationName(start, 1);
                addressListEnd = geocoder.getFromLocationName(end, 1);

            } catch (IOException e) {
                Log.e("Exception", e.getMessage());
            }
            Address startingAddress = addressListStart.get(0);
            Address endingAddress = addressListEnd.get(0);

            startPoint = new LatLng(startingAddress.getLatitude(), startingAddress.getLongitude());
            endPoint = new LatLng(endingAddress.getLatitude(), endingAddress.getLongitude());
            Log.e("start", String.valueOf(startPoint));
            Log.e("end", String.valueOf(endPoint));

        }




    }

}
