package onur.timey;

import android.*;
import android.Manifest;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by onurh on 1.08.2016.
 */
public class AppIntro extends AppIntro2 {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        skipButton.setVisibility(View.INVISIBLE);
        showStatusBar(false);
        getSupportActionBar().hide();
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name),getString(R.string.app_intro_desc1),R.drawable.tomato, Color.parseColor("#009688")));
        addSlide(AppIntroFragment.newInstance(getString(R.string.app_name),getString(R.string.app_intro_desc2),R.drawable.timer, Color.parseColor("#4CAF50")));
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDonePressed() {
        finish();
    }
}
