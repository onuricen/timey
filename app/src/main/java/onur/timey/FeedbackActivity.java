package onur.timey;



import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by onurh on 18.07.2016.
 */


public class FeedbackActivity extends AppCompatActivity {


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    DatabaseReference feedbackRef = databaseReference.child("Feedback");


    @BindView(R.id.feedbackEdittext)
    TextView feedbackEditText;

    @OnClick(R.id.sendButton)
    void send() {
        String insideEditText = feedbackEditText.getEditableText().toString();
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add(insideEditText);
        feedbackRef.push().setValue(linkedList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ZaaTheme);
        setContentView(R.layout.feedback_activity);


        ButterKnife.bind(this);


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF303030));
        getSupportActionBar().setTitle("Feedback");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    protected void onStart() {
        super.onStart();

        feedbackRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(FeedbackActivity.this, "Gönderildi!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FeedbackActivity.this, "İptal Edildi (databaseError)", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}








