package com.rrmsense.friendfinder.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmsense.friendfinder.R;
import com.rrmsense.friendfinder.models.UserInformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    ImageView image;
    TextView name;
    TextView email;
    EditText mobile;
    Button update;
    Switch gps;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    UserInformation userInformation;


    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        image = (ImageView) view.findViewById(R.id.image);
        name = (TextView) view.findViewById(R.id.name);
        email = (TextView) view.findViewById(R.id.email);
        mobile = (EditText) view.findViewById(R.id.mobile);
        update = (Button) view.findViewById(R.id.update);
        update.setOnClickListener(this);
        gps = (Switch) view.findViewById(R.id.gps);
        gps.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getPhotoUrl() != null)
            Glide.with(getActivity()).load(firebaseUser.getPhotoUrl()).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).into(new BitmapImageViewTarget(image) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    image.setImageDrawable(circularBitmapDrawable);
                }
            });
        if (firebaseUser.getDisplayName() != null)
            name.setText("Name: " + firebaseUser.getDisplayName());
        if (firebaseUser.getEmail() != null)
            email.setText("E-Mail: " + firebaseUser.getEmail());
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInformation = dataSnapshot.getValue(UserInformation.class);
                if (userInformation.getMobile() != null)
                    mobile.setText(userInformation.getMobile());
                if (userInformation.getShowLocation() != null)
                    gps.setChecked(userInformation.getShowLocation());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                if (mobile.getText() != null)
                    databaseReference.child(firebaseUser.getUid()).child("mobile").setValue(mobile.getText().toString());
                break;
            case R.id.gps:
                databaseReference.child(firebaseUser.getUid()).child("showLocation").setValue(gps.isChecked());
                break;
        }

    }
}
