package com.rrmsense.friendfinder.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rrmsense.friendfinder.R;
import com.rrmsense.friendfinder.models.NotificationModel;
import com.rrmsense.friendfinder.models.UserInformationModel;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Talha on 2/26/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<NotificationModel> notificationModelArrayList;
    private Context context;


    public NotificationAdapter(ArrayList<NotificationModel> notificationModelArrayList, Context context) {
        this.notificationModelArrayList = notificationModelArrayList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationModel notificationModel = notificationModelArrayList.get(position);

        holder.call.setText("Call");
        holder.notify.setText("Notify");
        holder.from.setText(notificationModel.getFrom());
        holder.title.setText(notificationModel.getTitle());
        holder.body.setText(notificationModel.getBody());
        /*final ImageView image = holder.image;
        Glide.with(context).load(notificationModel.ge).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).into(new BitmapImageViewTarget(image) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                image.setImageDrawable(circularBitmapDrawable);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView image;
        private TextView from;
        private TextView title;
        private TextView body;
        private Button call;
        private Button notify;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            image = (ImageView) itemView.findViewById(R.id.image);
            from = (TextView) itemView.findViewById(R.id.from);
            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.body);
            call = (Button) itemView.findViewById(R.id.call);
            notify = (Button) itemView.findViewById(R.id.notify);

            call.setOnClickListener(this);
            notify.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.call:
                   break;
                case R.id.notify:

                    break;
                default:

                    break;

            }

        }
    }
}
