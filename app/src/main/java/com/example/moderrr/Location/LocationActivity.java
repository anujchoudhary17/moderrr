package com.example.moderrr.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.moderrr.AddLocation.AddLocationActivity;
import com.example.moderrr.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button btnAddLocation;
    private final float  DEFAULT_ZOOM = 18;
    LatLng latLngOfPlace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

materialSearchBar=findViewById(R.id.searchBar);
btnAddLocation=findViewById(R.id.btnAddLocation);
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView=mapFragment.getView();

mFusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(LocationActivity.this);
        Places.initialize(LocationActivity.this,"AIzaSyDomy6SVbfrtw2E8onL7FQ08Vt3nQusWlw");
    placesClient=Places.createClient(this);
       final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
            startSearch(text.toString(),true,null,true);


            }

            @Override
            public void onButtonClicked(int buttonCode) {
                //OPENING NAVIGATION DRAWER
                if(buttonCode==MaterialSearchBar.BUTTON_NAVIGATION)
                {


                }

                else if(buttonCode==MaterialSearchBar.BUTTON_BACK)
                {
                materialSearchBar.disableSearch();
                }

            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

String countryCode=getCurrentCountryCode(LocationActivity.this);

                Toast.makeText(LocationActivity.this,
                        " HEY I AM COUNTRY========"+countryCode, Toast.LENGTH_LONG).show();
                final FindAutocompletePredictionsRequest predictionsRequest= FindAutocompletePredictionsRequest.builder()
                        .setCountry(countryCode)
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(charSequence.toString())
                        .build();
            placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                    if(task.isSuccessful())
                    {
                        FindAutocompletePredictionsResponse predictionsResponse=task.getResult();
                        if(predictionsResponse!=null)
                        {
                            predictionList=predictionsResponse.getAutocompletePredictions();
                            List<String> suggestionList=new ArrayList<>();
                            for(int i=0;i<predictionList.size();i++)
                            {
                                AutocompletePrediction prediction=predictionList.get(i);
                                suggestionList.add(prediction.getFullText(null).toString());

                            }
                            materialSearchBar.updateLastSuggestions(suggestionList);
                            if(!materialSearchBar.isSuggestionsVisible())
                            {
                                materialSearchBar.showSuggestionsList();
                            }
                        }
                    }else {
                        Log.i("mytag","Prediction Fetching Task Unsuccessful");

                    }
                }
            });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

            materialSearchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
                @Override
                public void OnItemClickListener(int position, View v) {
                    if(position >= predictionList.size())
                    {
                        return;
                    }
                    AutocompletePrediction selectedPrediction=predictionList.get(position);
                    String suggestion=materialSearchBar.getLastSuggestions().get(position).toString();
                    materialSearchBar.setText(suggestion);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            materialSearchBar.clearSuggestions();
                        }
                    },1000);
                    InputMethodManager imm=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if(imm!=null)
                    {
                        imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                    String placeId=selectedPrediction.getPlaceId();
                    List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);
                    final FetchPlaceRequest fetchPlaceRequest= FetchPlaceRequest.builder(placeId,placeFields).build();
                    placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @Override
                        public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {

                            Place place=fetchPlaceResponse.getPlace();
                            Log.i("mytag","Place Found"+place.getName());
                            latLngOfPlace =place.getLatLng();
                            if(latLngOfPlace!=null)
                            {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace,DEFAULT_ZOOM));

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e instanceof ApiException)
                            {
                                ApiException apiException=(ApiException) e;
                                apiException.printStackTrace();
                                int statusCode=apiException.getStatusCode();
                                Log.i("mytag","Place Not Found" + e.getMessage());
                                Log.i("mytag","Status Code" + statusCode);
                            }
                        }
                    });

                }

                @Override
                public void OnItemDeleteListener(int position, View v) {

                }
            });



        btnAddLocation.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(LocationActivity.this,
                        " HEY I AM BUTTON CLICKED", Toast.LENGTH_LONG).show();

                Intent addLocationIntent = new Intent(getApplicationContext(), AddLocationActivity.class);
                addLocationIntent.putExtra("latlngOfPlace", latLngOfPlace);

                Toast.makeText(LocationActivity.this,
                        " HEY I AM COUNTRY"+latLngOfPlace, Toast.LENGTH_LONG).show();
                startActivity(addLocationIntent);



            }
        });




    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap=googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if(mapView!=null && mapView.findViewById(Integer.parseInt("1")) !=null)
        {
            View locationButton=((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
            layoutParams.setMargins(0,0,40,180);

        }

        LocationRequest locationRequest=LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient= LocationServices.getSettingsClient(LocationActivity.this);
        Task<LocationSettingsResponse> task=settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(LocationActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            getDeviceLocation();
            }
        });

        task.addOnFailureListener(LocationActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if(e instanceof ResolvableApiException)
                {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(LocationActivity.this,51);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
          if(materialSearchBar.isSuggestionsVisible())
              materialSearchBar.clearSuggestions();
          if(materialSearchBar.isEnabled())
              materialSearchBar.disableSearch();


                return false;
             }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==51)
        {
            if(resultCode==RESULT_OK)
            {
                getDeviceLocation();
            }

        }


    }

    private void getDeviceLocation()
    {
    mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
        @Override
        public void onComplete(@NonNull Task<Location> task) {
        if(task.isSuccessful())
        {
            mLastKnownLocation=task.getResult();
            if(mLastKnownLocation!=null)
            {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()),DEFAULT_ZOOM));
            }
            else
            {
                LocationRequest locationRequest=LocationRequest.create();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(5000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationCallback =new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                 if(locationResult==null) {
                     return;
                 }
                mLastKnownLocation=locationResult.getLastLocation();
                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()),DEFAULT_ZOOM));
                  mFusedLocationProviderClient.removeLocationUpdates(locationCallback);

                    }
                };
                mFusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);


            }

        }else
        {

            Toast.makeText(LocationActivity.this,"Unable to get last Location",Toast.LENGTH_SHORT).show();
        }



        }
    });

    }

    public static String getCurrentCountryCode(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso = telephonyManager.getSimCountryIso().toUpperCase();
        return countryIso;
//        return PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryIso);
    }



}
