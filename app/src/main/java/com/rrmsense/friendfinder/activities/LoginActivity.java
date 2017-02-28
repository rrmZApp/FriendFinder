package com.rrmsense.friendfinder.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.firebase.ui.auth.ui.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmsense.friendfinder.R;
import com.rrmsense.friendfinder.models.UserInformation;
import com.rrmsense.friendfinder.service.NetworkStateReceiver;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    private static final int RC_SIGN_IN = 123;
    FirebaseAuth auth;
    private NetworkStateReceiver networkStateReceiver;
    //SignInButton buttonGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

//        buttonGoogle = (SignInButton) findViewById(R.id.login_with_google);
//        buttonGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            final IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                updateFirebaseUserDatabase();

            } else {
                // Sign in failed
                if (response == null) {
                    // UserInformation pressed back button

                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {

                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                    return;
                }
            }
        }
    }

    private void updateFirebaseUserDatabase() {

        final FirebaseUser firebaseUser = auth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(UserInformation.class).getId()==null){
                    String photoURL = firebaseUser.getPhotoUrl()==null?"":firebaseUser.getPhotoUrl().toString();
                    UserInformation userInformation = new UserInformation(firebaseUser.getUid(),firebaseUser.getEmail(),firebaseUser.getDisplayName(),photoURL);
                    databaseReference.child(firebaseUser.getUid()).setValue(userInformation);
                    databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }else{
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        //Toast.makeText(this,auth.getCurrentUser().getUid()+auth.getCurrentUser().getPhotoUrl()+auth.getCurrentUser().getDisplayName(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    @Override
    public void networkAvailable() {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            updateFirebaseUserDatabase();
        }else{
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .setTheme(R.style.FirebaseLoginTheme)
                            .build(), RC_SIGN_IN);
        }
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(this,"No internet connection!",Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(networkStateReceiver);
    }
}
