package onur.timey;



import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;



/**
 * Created by onurh on 18.07.2016.
 */


public class FeedbackActivity extends AppCompatActivity {



    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();


    DatabaseReference feedbackRef=databaseReference.child("Feedback");








    private EditText feedbackEditText;
    private Button   sendButton;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.ZaaTheme);
        setContentView(R.layout.feedback_activity);


        feedbackEditText=(EditText)findViewById(R.id.feedbackEdittext);
        sendButton=(Button)findViewById(R.id.sendButton);






        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String insideEditText=feedbackEditText.getEditableText().toString();
                LinkedList<String> linkedList=new LinkedList<String>();
                linkedList.add(insideEditText);
                feedbackRef.push().setValue(linkedList);

            }
        });

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
                Toast.makeText(FeedbackActivity.this,"İptal Edildi (databaseError)",Toast.LENGTH_LONG).show();
            }
        });


    }





    }



