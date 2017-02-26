package com.rrmsense.friendfinder.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.MapsInitializer;
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
import com.rrmsense.friendfinder.fragments.SettingsFragment;
import com.rrmsense.friendfinder.fragments.ViewFriendsFragment;
import com.rrmsense.friendfinder.models.Fragments;
import com.rrmsense.friendfinder.models.UserInformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;
    FirebaseUser user;
    public UserInformation userInformation;
    DatabaseReference databaseReference;
    int CURRENT_FRAGMENT;
    //AIzaSyDKthnECxkfnAZv6noEyVROny_rF9DhEJo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getPermissions();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
        Glide.with(this).load(user.getPhotoUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(userImage) {
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
        userName.setText(user.getDisplayName()+"q");
       //Toast.makeText(this,user.getDisplayName(),Toast.LENGTH_LONG).show();
        TextView userEmail = (TextView) view.findViewById(R.id.textEmail);
        userEmail.setText(user.getEmail());
        navigationView.setNavigationItemSelectedListener(this);

//        try {
//            MapsInitializer.initialize(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        openFragment(Fragments.FRAGMENT_VIEW_FRIENDS);
        getUserInformation();
    }
    private void getUserInformation() {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String photoURL = firebaseUser.getPhotoUrl()==null?"":firebaseUser.getPhotoUrl().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInformation = dataSnapshot.getValue(UserInformation.class);
                //Toast.makeText(MainActivity.this,"Y",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(CURRENT_FRAGMENT != Fragments.FRAGMENT_VIEW_FRIENDS) {
            openFragment(Fragments.FRAGMENT_VIEW_FRIENDS);

        }else{
            super.onBackPressed();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openFragment(Fragments.FRAGMENT_SETTINGS);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit) {
            openFragment(Fragments.FRAGMENT_EDIT_PROFILE);
        }
        else if(id==R.id.nav_sign_out){
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    });

        }else if (id == R.id.nav_about) {
            openFragment(Fragments.FRAGMENT_ABOUT);
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
                fragment = new ViewFriendsFragment();
                CURRENT_FRAGMENT = Fragments.FRAGMENT_VIEW_FRIENDS;
                //bundle.putInt("ID", SelectFragment.FRAGMENT_BANGLA_RADIO);
                break;
            case Fragments.FRAGMENT_EDIT_PROFILE:
                //bundle.putInt("ID", SelectFragment.FRAGMENT_FAVOURITE);
                fragment = new EditProfileFragment();
                CURRENT_FRAGMENT = Fragments.FRAGMENT_EDIT_PROFILE;
                break;
            case Fragments.FRAGMENT_SETTINGS:
                // bundle.putInt("ID", SelectFragment.FRAGMENT_RECENT);
                fragment = new SettingsFragment();
                CURRENT_FRAGMENT = Fragments.FRAGMENT_SETTINGS;
                break;
            case Fragments.FRAGMENT_ABOUT:
                fragment = new AboutFragment();
                break;
            case Fragments.FRAGMENT_ADD_FRIEND:
                fragment = new AddFriendFragment();
                CURRENT_FRAGMENT = Fragments.FRAGMENT_ABOUT;
                break;
        }
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .commit();
    }

    void getPermissions(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }


}
