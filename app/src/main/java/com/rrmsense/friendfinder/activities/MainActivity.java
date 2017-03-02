package com.rrmsense.friendfinder.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rrmsense.friendfinder.R;
import com.rrmsense.friendfinder.fragments.AboutFragment;
import com.rrmsense.friendfinder.fragments.AddFriendFragment;
import com.rrmsense.friendfinder.fragments.EditProfileFragment;
import com.rrmsense.friendfinder.fragments.NotificationFragment;
import com.rrmsense.friendfinder.fragments.SettingsFragment;
import com.rrmsense.friendfinder.fragments.ViewFriendsFragment;
import com.rrmsense.friendfinder.models.Fragments;
import com.rrmsense.friendfinder.models.UserInformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public UserInformation userInformation;
    boolean doubleBackToExitPressedOnce = false;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    int CURRENT_FRAGMENT;
    FloatingActionButton fab;
    ProgressDialog progressDialog;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProgressDialog();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getPermissions();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if (CURRENT_FRAGMENT != Fragments.FRAGMENT_ADD_FRIEND)
                    openFragment(Fragments.FRAGMENT_ADD_FRIEND);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        final ImageView userImage = (ImageView) view.findViewById(R.id.imageUser);
        Glide.with(this).load(user.getPhotoUrl()).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).into(new BitmapImageViewTarget(userImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getBaseContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                userImage.setImageDrawable(circularBitmapDrawable);
            }
        });
        //Toast.makeText(this,UserInformation.getPhotoUrl().toString(),Toast.LENGTH_LONG).show();
        TextView userName = (TextView) view.findViewById(R.id.textName);
        userName.setText(user.getDisplayName());
        //Toast.makeText(this,user.getDisplayName(),Toast.LENGTH_LONG).show();
        TextView userEmail = (TextView) view.findViewById(R.id.textEmail);
        userEmail.setText(user.getEmail());
        navigationView.setNavigationItemSelectedListener(this);

//        try {
//            MapsInitializer.initialize(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //openFragment(Fragments.FRAGMENT_VIEW_FRIENDS);
        getUserInformation();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void hideProgressDialog() {
        progressDialog.cancel();
    }


    private void getUserInformation() {

        if (getIntent().hasExtra("Fragment")) {
            int value = getIntent().getExtras().getInt("Fragment");
            Log.d("ACTIVITY","Main");
            openFragment(value);

        } else {
            FirebaseUser firebaseUser = auth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference("users");
            databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userInformation = dataSnapshot.getValue(UserInformation.class);
                    if (userInformation.getMobile() == null) {
                        openFragment(Fragments.FRAGMENT_EDIT_PROFILE);
                    } else {
                        openFragment(Fragments.FRAGMENT_VIEW_FRIENDS);
                    }
                    hideProgressDialog();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (CURRENT_FRAGMENT != Fragments.FRAGMENT_VIEW_FRIENDS) {
            openFragment(Fragments.FRAGMENT_VIEW_FRIENDS);

        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                openFragment(Fragments.FRAGMENT_NOTIFICATION);
                break;
//            case R.id.action_settings:
//                openFragment(Fragments.FRAGMENT_SETTINGS);
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_edit:
                if (CURRENT_FRAGMENT != Fragments.FRAGMENT_EDIT_PROFILE)
                    openFragment(Fragments.FRAGMENT_EDIT_PROFILE);
                break;
            case R.id.nav_home:
                if (CURRENT_FRAGMENT != Fragments.FRAGMENT_VIEW_FRIENDS)
                    openFragment(Fragments.FRAGMENT_VIEW_FRIENDS);
                break;
            case R.id.nav_sign_out:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                break;
            case R.id.nav_about:
                if (CURRENT_FRAGMENT != Fragments.FRAGMENT_ABOUT)
                    openFragment(Fragments.FRAGMENT_ABOUT);
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(int fragmentID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        switch (fragmentID) {
            case Fragments.FRAGMENT_VIEW_FRIENDS:
                fab.show();
                fragment = new ViewFriendsFragment();
                CURRENT_FRAGMENT = Fragments.FRAGMENT_VIEW_FRIENDS;
                //bundle.putInt("ID", SelectFragment.FRAGMENT_BANGLA_RADIO);
                break;
            case Fragments.FRAGMENT_EDIT_PROFILE:
                fab.hide();
                //bundle.putInt("ID", SelectFragment.FRAGMENT_FAVOURITE);
                fragment = new EditProfileFragment();
                CURRENT_FRAGMENT = Fragments.FRAGMENT_EDIT_PROFILE;
                break;
            case Fragments.FRAGMENT_SETTINGS:
                fab.hide();
                // bundle.putInt("ID", SelectFragment.FRAGMENT_RECENT);
                fragment = new SettingsFragment();
                CURRENT_FRAGMENT = Fragments.FRAGMENT_SETTINGS;
                break;
            case Fragments.FRAGMENT_ABOUT:
                fab.hide();
                fragment = new AboutFragment();
                break;
            case Fragments.FRAGMENT_ADD_FRIEND:
                fab.hide();
                fragment = new AddFriendFragment();
                CURRENT_FRAGMENT = Fragments.FRAGMENT_ABOUT;
                break;
            case Fragments.FRAGMENT_NOTIFICATION:
                fab.hide();
                CURRENT_FRAGMENT = Fragments.FRAGMENT_NOTIFICATION;
                fragment = new NotificationFragment();
                break;
        }
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CALL_PHONE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }


}
