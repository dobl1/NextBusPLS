package nextbuspns_d.polytech.unice.fr.nextbuspls;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String GET_URL = "http://evening-shore-3074.herokuapp.com/rest/coordinate/getRandom";
    private static final String POST_URL = "http://evening-shore-3074.herokuapp.com/rest/coordinate/";
    private GoogleMap mMap;
    private Button buttonSend;
    private Button buttonStartTracking;
    private EditText editTextUrl;
    private Marker bus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonSend = (Button) findViewById(R.id.button_send);
        buttonSend.setEnabled(false);

        buttonStartTracking = (Button) findViewById(R.id.button_startTracking);

        editTextUrl = (EditText) findViewById(R.id.editText_url);
        editTextUrl.setHint(GET_URL);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSend.setEnabled(false);
                new RESTClient(new RESTClient.AsyncResponse() {
                    @Override
                    public void processFinish(JSONObject location) {
                        try {
                            MarkerAnimation.animateMarker(bus, new LatLng((Double) location.get("latitude"), (Double) location.get("longitude")), new LatLngInterpolator.Spherical());
                            buttonSend.setEnabled(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).execute(GET_URL);
            }
        });

        buttonStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buttonSend.setEnabled(true);
        LatLng polytech = new LatLng(43.5977442, 7.098906);
        bus = mMap.addMarker(new MarkerOptions().position(polytech)
                .title("Le bus magique")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.logo_bus))
                .alpha(0.7f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(polytech, 14.0f));
    }
}