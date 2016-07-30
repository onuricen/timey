package onur.timey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.db.chart.model.LineSet;

/**
 * Created by onurh on 30.07.2016.
 */
public class StaticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        setTheme(R.style.ZaaTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statics_activity);
    }
}
