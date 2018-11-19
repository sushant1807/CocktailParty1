package com.example.susha.StudioProjects.cocktailparty.activities.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.susha.StudioProjects.cocktailparty.R;
import com.example.susha.StudioProjects.cocktailparty.activities.helper.Constants;
import com.example.susha.StudioProjects.cocktailparty.activities.model.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.List;

public class ViewEventActivity extends AppCompatActivity {
    Calendar myCalendar ;
    EditText date,time,title,description;
    private static final int SELECT_PICTURE = 0;
    private ImageView imageView,like;
    private RatingBar ratingBar;
    private static TextView title1,desc1,date1,time1,place1,nlikes;
    private Button submit;
    FirebaseDatabase database;
    DatabaseReference myRef ;
    List<Event> list;
    public Query query;
    private DatabaseReference mDatabase;
    private String encodedImage="";
    private String createdat="";
    private static String email11="";
    private static Event value;
// ...


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewevent);
        String title = getIntent().getStringExtra("title");
        String desc  = getIntent().getStringExtra("desc");
        String time = getIntent().getStringExtra("date");
        String date = getIntent().getStringExtra("time");
        String place = getIntent().getStringExtra("place");
        //String place = getIntent().getStringExtra("place");
        String likes= getIntent().getStringExtra("likes");
        createdat = getIntent().getStringExtra("createdat");
        email11 = getIntent().getStringExtra("email");
        String img1 = "";
        imageView = (ImageView) findViewById(R.id.imgview);
        String img="";
        img= Constants.getImage();
        Log.d("image--->",""+img);
        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Glide.with(getApplicationContext()).load(decodedByte).into(imageView);
        like = (ImageView) findViewById(R.id.like);
        title1= (TextView) findViewById(R.id.title);
        desc1= (TextView) findViewById(R.id.desc);
        date1= (TextView) findViewById(R.id.date);
        time1= (TextView) findViewById(R.id.time);
        place1= (TextView) findViewById(R.id.place);
        nlikes= (TextView) findViewById(R.id.nLikes);
        int nl = Integer.parseInt(likes);

        title1.setText("Title : "+title);
        //like.setImageBitmap(R.drawable.liked);
        date1.setText("Date :  "+date);
        nlikes.setText((nl-1)+" likes");
        place1.setText("Place : "+place);
        desc1.setText("Description : "+desc);
        time1.setText("Time : "+time);

    }
}
