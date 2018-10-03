package com.example.susha.StudioProjects.cocktailparty.activities.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.susha.StudioProjects.cocktailparty.R;
import com.example.susha.StudioProjects.cocktailparty.activities.model.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {
    Calendar myCalendar ;
    EditText date,time,title,description;
    private static final int SELECT_PICTURE = 0;
    private ImageView imageView;
    private RatingBar ratingBar;
    TextView rvalue;
    private Button submit;
    private DatabaseReference mDatabase;
// ...


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_addevent);
        submit= (Button) findViewById(R.id.submit);
        myCalendar = Calendar.getInstance();
        date= (EditText) findViewById(R.id.date);
        title= (EditText) findViewById(R.id.title);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        description= (EditText) findViewById(R.id.description);
        time= (EditText) findViewById(R.id.time);
        imageView = (ImageView) findViewById(R.id.image);
        submit.setOnClickListener(new View.OnClickListener() {
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
                new DatePickerDialog(AddEventActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               pickPhoto(v);
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
                mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        //getSupportActionBar().hide();


        //h.delegate = this;

//        System.out.println("ALl users", +databaseHelper.checkUser(get));
       /* ratingBar = (RatingBar) findViewById(R.id.rating);
        rvalue = (TextView) findViewById(R.id.rvalue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                rvalue.setText(String.valueOf(rating));

            }
        });*/
    }

    private void submit() {
        String title1=title.getText().toString();
        String desc=description.getText().toString();
        String date1=date.getText().toString();
        String time1=time.getText().toString();
        String image="";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        //System.out.println(dtf.format(now));
        Event event = new Event(title1,desc,time1,date1,image);
        Log.d("BHARATH111",""+event);
        Log.d("BHARATH111",""+dtf.format(now).toString());
        mDatabase.child("Events").child(dtf.format(now).toString()).setValue(event);


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

        if(resultCode == RESULT_OK) {
            Bitmap bitmap = getPath(data.getData());
            Log.d("bharath  ","entered");
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
        // Convert file path into bitmap image using below line.
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }

}
