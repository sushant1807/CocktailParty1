package com.example.susha.StudioProjects.cocktailparty.activities.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.susha.StudioProjects.cocktailparty.R;
import com.example.susha.StudioProjects.cocktailparty.activities.activities.AddEventActivity;
import com.example.susha.StudioProjects.cocktailparty.activities.activities.EditEvent;
import com.example.susha.StudioProjects.cocktailparty.activities.activities.UsersActivity;
import com.example.susha.StudioProjects.cocktailparty.activities.activities.ViewEventActivity;
import com.example.susha.StudioProjects.cocktailparty.activities.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Event> albumList;
    List<Event> list;
    Context context;
    private static SharedPreferences sharedpreferences;
    private static SharedPreferences.Editor editor;


    public MoviesAdapter(List<Event> list, Context context) {
        Collections.reverse(list);
        this.list = list;
        this.mContext = context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView image,contact;
        public ImageView thumbnail,like;
        public TextView overflow,likes,place;

        public MyViewHolder(View view) {
            super(view);
            //image= (ImageView) view.findViewById(R.id.backdrop);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            likes = (TextView) view.findViewById(R.id.likes);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            like = (ImageView) view.findViewById(R.id.like);
            contact = (ImageView) view.findViewById(R.id.contact);
            overflow = (TextView) view.findViewById(R.id.overflow);
            place = (TextView) view.findViewById(R.id.place);

        }
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Event album = list.get(position);
        //Event event = album;
        int likesize= album.getLikes().size();
        holder.place.setText(""+album.getPlace());


        holder.title.setText(album.getTitle()+"     ---     "+album.getDescription());
        holder.count.setText(album.getYear()+ "       "+album.getTime());

        holder.likes.setText(likesize-1+" liked this");
        if(album.getLikes().contains(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()))
        {
            Glide.with(mContext).load(R.drawable.liked).into(holder.like);
            holder.like.setClickable(false);
            holder.like.setEnabled(false);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(mContext).load(R.drawable.liked).into(holder.like);
                holder.like.setClickable(false);
                List<String> likedId = album.getLikes();
                likedId.add(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                album.setLikes(likedId);
                DatabaseReference updateData = FirebaseDatabase.getInstance()
                        .getReference();
                updateData.child("Events")
                        .child(album.getCreatedat().toString()).setValue(album);

            }
        });


        if(!FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().equalsIgnoreCase(album.getEmail())){
            String email= album.getEmail();
            holder.contact.setVisibility(View.VISIBLE);

            holder.overflow.setText(""+email.substring(0,email.indexOf("@")));
            holder.overflow.setTextSize(15);
           // holder.overflow.setVisibility(View.GONE);

        }
        else{
            holder.contact.setVisibility(View.GONE);
        }
        if(!album.getImage().equalsIgnoreCase(""))
        {
        byte[] decodedString = Base64.decode(album.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(mContext).load(decodedByte).into(holder.thumbnail);}
        // loading album cover using Glide library
        else Glide.with(mContext).load(R.drawable.noimageavailable).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.setImage(album.getImage());

                Intent i = new Intent(mContext,ViewEventActivity.class);
                i.putExtra("title", album.getTitle().toString());
                i.putExtra("desc", album.getDescription().toString());
                i.putExtra("date", album.getYear().toString());
                i.putExtra("time", album.getTime().toString());
                i.putExtra("place", album.getPlace().toString());


                i.putExtra("createdat", album.getCreatedat().toString());
                i.putExtra("likes", ""+album.getLikes().size());

                //i.putExtra("image", album.getImage().toString());
                i.putExtra("email", album.getEmail().toString());
                ((Activity) mContext).startActivityForResult(i,1);

            }
        });
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.setImage(album.getImage());

                Intent i = new Intent(mContext,EditEvent.class);
                i.putExtra("title", album.getTitle().toString());
                i.putExtra("desc", album.getDescription().toString());
                i.putExtra("date", album.getYear().toString());
                i.putExtra("time", album.getTime().toString());
                i.putExtra("createdat", album.getCreatedat().toString());
                i.putExtra("place", album.getPlace().toString());
                //i.putExtra("likes", album.getLikes());
                 //i.putExtra("image", album.getImage().toString());
                i.putExtra("email", album.getEmail().toString());
                ((Activity) mContext).startActivityForResult(i,1);

            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}