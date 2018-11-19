package com.example.susha.StudioProjects.cocktailparty.activities.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.susha.StudioProjects.cocktailparty.R;
import com.example.susha.StudioProjects.cocktailparty.activities.helper.Constants;
import com.example.susha.StudioProjects.cocktailparty.activities.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditEvent extends AppCompatActivity {
    Calendar myCalendar ;
    private static EditText place1,date,time,title,description;
    private static final int SELECT_PICTURE = 0;
    private ImageView imageView;
    private RatingBar ratingBar;
    TextView rvalue;
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
        setContentView(R.layout.activty_addevent);
        String t1= getIntent().getStringExtra("title");
        String desc= getIntent().getStringExtra("desc");
        String time1= getIntent().getStringExtra("date");
        String date12= getIntent().getStringExtra("time");
        String place= getIntent().getStringExtra("place");
        String img="";
        img=Constants.getImage();
        Log.d("image--->",""+img);
        createdat= getIntent().getStringExtra("createdat");
        email11= getIntent().getStringExtra("email");
        String img1="";
        imageView = (ImageView) findViewById(R.id.image);
        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Glide.with(getApplicationContext()).load(decodedByte).into(imageView);

       /* if(!img.equalsIgnoreCase(""))
        {
            byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView = (ImageView) findViewById(R.id.image);
            imageView.setImageBitmap(decodedByte);
        }*/
        // loading album cover using Glide library

        submit= (Button) findViewById(R.id.submit);
        myCalendar = Calendar.getInstance();
        date= (EditText) findViewById(R.id.date);
        date.setText(date12);
        title= (EditText) findViewById(R.id.title);
        title.setText(t1.trim());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        description= (EditText) findViewById(R.id.description);
        place1= (EditText) findViewById(R.id.place1);
        place1.setText(""+place);
        description.setText(desc);
        time= (EditText) findViewById(R.id.time);
        time.setText(time1);

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }


        };
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditEvent.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if(selectedHour<12)
                            time.setText( selectedHour + ":" + selectedMinute + " AM" );
                        else
                            time.setText( selectedHour + ":" + selectedMinute + " PM" );

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void submit() {
        DatabaseReference mDatabase;

        Log.d("remve","removed " +createdat);
        int iend = createdat.indexOf("/");
    String year="",month="",leftout="";
        String subString;
        if (iend != -1)
        {
            subString= createdat.substring(0 , iend);
             year = subString;


            String remaining = createdat.substring(iend+1 , createdat.length());
            Log.d("remve","remaining " +remaining);
            int iend1 = remaining.indexOf("/");
            if(iend1!=1)
            {
                month=remaining.substring(0,iend1);
                Log.d("remve","month " +month);
                leftout= remaining.substring(iend1+1,remaining.length());
                Log.d("remve","leftour "+leftout);

            }

        }
        Log.d("remve",""+email11);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString().equalsIgnoreCase(email11)){
            Log.d("remve"," value -->  "+ mDatabase.child("Events").child(year).child(month).getKey());


            mDatabase.child("Events").child(year).child(month).child(leftout).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("delete task status",""+task.isSuccessful());
                }
            });

        }
        String title1=title.getText().toString();
        String desc=description.getText().toString();
        String date1=date.getText().toString();
        String time1=time.getText().toString();
        String place2=place1.getText().toString();
        String image="";
        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        if(!encodedImage.equalsIgnoreCase("")){
            image=encodedImage;}
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date  now = Calendar.getInstance().getTime();
        Log.d("Image",""+image);
        String createdat= dtf.format(now).toString();
        System.out.println(dtf.format(now));
        List<String> likes= new ArrayList<String>();
        likes.add("11111");
        Event event = new Event(title1,desc,time1,date1,image,email,createdat,place2,likes);
        Log.d("BHARATH111",""+event);
        Log.d("BHARATH111",""+dtf.format(now).toString());
        mDatabase.child("Events").child(dtf.format(now).toString()).setValue(event);
        Intent i = new Intent(EditEvent.this,UsersActivity.class);
        startActivity(i);
        finish();
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }
    public void pickPhoto(View view) {
        //TODO: launch the photo picker
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();




            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            encodedImage = encoded;

            imageView.setImageBitmap(bitmap);
        }


    }

    private Bitmap getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();

        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }

}


