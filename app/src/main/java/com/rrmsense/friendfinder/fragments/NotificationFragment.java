package com.rrmsense.friendfinder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmsense.friendfinder.R;
import com.rrmsense.friendfinder.activities.MainActivity;
import com.rrmsense.friendfinder.adapter.NotificationAdapter;
import com.rrmsense.friendfinder.adapter.UserInformationAdapter;
import com.rrmsense.friendfinder.models.NotificationModel;
import com.rrmsense.friendfinder.models.UserInformationModel;
import com.rrmsense.friendfinder.service.TrackGPS;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;

    public NotificationFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getNotification();
        return view;
    }

    private void getNotification() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notification");

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationModelArrayList.clear();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    NotificationModel notificationModel = postSnapshot.getValue(NotificationModel.class);
                    notificationModelArrayList.add(notificationModel);

                }
                adapter = new NotificationAdapter(notificationModelArrayList, getActivity());
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
