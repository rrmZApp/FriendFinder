package com.rrmsense.friendfinder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rrmsense.friendfinder.R;
import com.rrmsense.friendfinder.models.OnListInformation;
import com.rrmsense.friendfinder.models.UserInformationModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment implements View.OnClickListener {

    EditText id;
    Button add;

    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
                id = (EditText) view.findViewById(R.id.id);
        add = (Button) view.findViewById(R.id.add);
        add.setText("Add");
        add.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                getFirebaseUser();
                break;
        }
    }

    private void getFirebaseUser() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query queryRef = databaseReference.orderByChild("email").equalTo(id.getText().toString());
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    UserInformationModel userInformationModel = dataSnapshot.getChildren().iterator().next().getValue(UserInformationModel.class);
                    Toast.makeText(getActivity(), userInformationModel.getId(),Toast.LENGTH_SHORT).show();
                    sendAddNotification(userInformationModel);
                }
                else{
                    Toast.makeText(getActivity(),"User Not Found!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void sendAddNotification(final UserInformationModel userInformationModel) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("friendList").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Query queryRef = databaseReference.orderByChild("email").equalTo(userInformationModel.getEmail());
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){

                    Toast.makeText(getActivity(),"Already Added!",Toast.LENGTH_SHORT).show();

                }
                else{
                    OnListInformation onListInformation = new OnListInformation(userInformationModel.getEmail(), false);
                    databaseReference.child(userInformationModel.getId()).setValue(onListInformation);
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            sendNoTification();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void sendNoTification() {

    }

}
