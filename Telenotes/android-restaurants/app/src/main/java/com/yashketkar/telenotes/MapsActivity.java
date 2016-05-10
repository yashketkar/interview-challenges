package com.yashketkar.telenotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String name;
    public String address;
    public String rating;
    public String type;
    public String tel;
    public String website;
    public String email;

    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        Restaurant r = (Restaurant) intent.getSerializableExtra("restaurant");
        lat=r.getMlatitude();
        lon=r.getMlongitude();

        TextView t1= (TextView) findViewById(R.id.map_name);
        name=r.getmName();
        t1.setText(name);

        TextView t2= (TextView) findViewById(R.id.map_address);
        address=r.getmAddress();
        t2.setText(address);

        RatingBar r1= (RatingBar) findViewById(R.id.map_ratingBar);
        rating=r.getmRating();
        r1.setRating(Float.parseFloat(rating));

        TextView t3= (TextView) findViewById(R.id.map_type);
        type=r.getmType();
        t3.setText(type);

        TextView t4= (TextView) findViewById(R.id.map_tel);
        tel=r.getMtel();
        t4.setText(tel);

        TextView t5= (TextView) findViewById(R.id.map_website);
        website=r.getMwebsite();
        t5.setText(website);

        TextView t6= (TextView) findViewById(R.id.map_email);
        email=r.getMemail();
        t6.setText(email);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title(name));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
    }
}