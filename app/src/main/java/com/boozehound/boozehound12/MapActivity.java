package com.boozehound.boozehound12;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.BindDictionary;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.lang.Object;
import java.util.*;


public class MapActivity extends AppCompatActivity  implements OnMapReadyCallback, AsyncResponse {
    public ArrayList<GetLocation> locationlist = new ArrayList<GetLocation>();
    String latit;
    String longit;
    public String[] venues;
    protected void onCreate(Bundle savedInstanceState) {

        //get list of venue names
        Intent intent = getIntent();
        String[] vens = intent.getStringArrayExtra("vens");
        venues = vens;
        super.onCreate(savedInstanceState);
        Log.d("array_pass", vens[1]);

        //making AsyncTast reading from web url to get php file
        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(MapActivity.this, this);
        taskRead.execute("http://ryandeal.me/getLocation.php");

        /*Button backArrow = (Button) findViewById(R.id.arrowBack);
        Log.d("maps", "content view set");

        backArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, MainMenu.class));
            }
        }); */

        //setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //      .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
    }
    @Override
    public void processFinish(String s) {

        locationlist = new JsonConverter<GetLocation>().toArrayList(s, GetLocation.class);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BindDictionary<GetLocation> dict = new BindDictionary<GetLocation>();
        /*dict.addStringField(R.id.VenueID, new StringExtractor<Product>(){
            @Override
            public String getStringValue(Product product, int position){
                return "" + product.VenueID;
            }
       });

        /*dict.addStringField(R.id.City, new StringExtractor<Product>() {
            @Override
            public String getStringValue(Product product, int position) {
                return product.City;
            }
        });
        dict.addStringField(R.id.State, new StringExtractor<Product>() {
            @Override
            public String getStringValue(Product product, int position) {
                return product.State;
            }
        });
        dict.addStringField(R.id.ZIP, new StringExtractor<Product>() {
            @Override
            public String getStringValue(Product product, int position) {
                return "" + product.ZIP;
            }
        });*/
        dict.addStringField(R.id.Longitude, new StringExtractor<GetLocation>() {
            @Override
            public String getStringValue(GetLocation location, int position) {
                longit = location.Longitude;
                return location.Longitude;
            }
        });
        dict.addStringField(R.id.Latitude, new StringExtractor<GetLocation>() {
            @Override
            public String getStringValue(GetLocation location, int position) {
                latit = location.Latitude;
                return location.Latitude;
            }
        });

    }
    public void onMapReady(GoogleMap map) {
        //process query results
        int numBars = locationlist.size();
        Log.d("maps", Integer.toString(numBars));
        for(int i=0; i<numBars; i++) {
            //parse string
            longit = locationlist.get(i).Longitude;
            latit = locationlist.get(i).Latitude;
            String barName = venues[i];
            Log.d("maps", Integer.toString(locationlist.get(i).VenueID));
            Log.d("maps", venues[i]);
            //add markers
            AddMarker(longit, latit, map, barName);
        }
        //set camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.723526, -95.550777), 14.0f));
    }


    public void AddMarker(String Long, String Lat, GoogleMap map, String Name){
        Double Longit = Double.parseDouble(Long);
        Double Latit = Double.parseDouble(Lat);
        map.addMarker(new MarkerOptions().position(new LatLng(Latit, Longit)).title(Name));
    }


}

