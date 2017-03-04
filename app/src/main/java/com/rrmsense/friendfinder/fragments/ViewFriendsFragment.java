package com.rrmsense.friendfinder.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmsense.friendfinder.R;
import com.rrmsense.friendfinder.adapter.UserInformationAdapter;
import com.rrmsense.friendfinder.models.LocationGPS;
import com.rrmsense.friendfinder.models.UserInformationModel;
import com.rrmsense.friendfinder.service.TrackGPS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFriendsFragment extends Fragment {
    MapView mapView;
    GoogleMap map;
    double longitude;
    double latitude;
    boolean mapReady = false;
    String userUid;
    ArrayList<UserInformationModel> userInformationModelArray = new ArrayList<>();
    private TrackGPS gps;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public ViewFriendsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_friends, container, false);


        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setCompassEnabled(true);
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String tag = marker.getTag().toString();
                        //Toast.makeText(getActivity(), userInformationModelArray.get(Integer.parseInt(tag)).getName(), Toast.LENGTH_SHORT).show();
                        marker.showInfoWindow();
                        return true;
                    }
                });
                mapView.onResume();
                mapReady = true;
                updateLocation();

            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void updateLocationFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userUid);
        Map<String, Object> locationGPS = new HashMap<>();
        locationGPS.put("locationGPS", new LocationGPS(latitude, longitude));
        databaseReference.updateChildren(locationGPS);
    }

    public void updateLocation() {

        gps = new TrackGPS(getActivity());
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            //Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            map.animateCamera(cameraUpdate);
            updateLocationFirebase();
            updateLocationFriends();

        } else {
            gps.showSettingsAlert();
        }
    }

    private void updateLocationFriends() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInformationModelArray.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    UserInformationModel userInformationModel = postSnapshot.getValue(UserInformationModel.class);

                    if (userInformationModel.getId().equals(userUid))
                        continue;
                    //Toast.makeText(getActivity(),userInformationModel.getId()+" "+userUid,Toast.LENGTH_SHORT).show();
                    if (userInformationModel.getLocationGPS() != null) {

                        userInformationModelArray.add(userInformationModel);
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(userInformationModel.getLocationGPS().getLatitude(), userInformationModel.getLocationGPS().getLongitude()))
                                .title(userInformationModel.getName())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                        );
                        marker.setTag(userInformationModelArray.size() - 1);

                    }
                }
                adapter = new UserInformationAdapter(userInformationModelArray, getActivity(), map);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapReady)
            updateLocation();
    }
}
