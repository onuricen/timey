package onur.timey;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by onurh on 27.07.2016.
 */
public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.ZaaTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF303030));
        getSupportActionBar().setTitle("Hakkında");

    }


}
