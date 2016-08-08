package onur.timey;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import java.sql.Time;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by onurh on 30.07.2016.
 */
public class StaticsActivity extends AppCompatActivity {


    @BindView(R.id.finished_times)
    TextView finishedTimesText;

    public TextView getFinishedTimesText() {
        return finishedTimesText;
    }

    int staticsInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().setTitle("İstatistikler");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_activity);

        //changing activity's background color (normally, I set color default on xml file but my theme has problems :/ )
        View someView = findViewById(R.id.card);
        View root = someView.getRootView();
        root.setBackgroundColor(getResources().getColor(R.color.grayMaterial));

        ButterKnife.bind(this);


        TimeActivity time = new TimeActivity();
        int totalStatics = time.getTotalStatics();
        finishedTimesText.setText(Integer.toString(totalStatics));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: {
                Intent zaa=new Intent(this,TimeActivity.class);
                startActivity(zaa);
                finish();
                return true;
            }


        }
        return super.onOptionsItemSelected(item);
    }
}


