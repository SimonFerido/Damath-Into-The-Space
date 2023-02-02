package com.unityx.damath.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListPopupWindow;

import androidx.cardview.widget.CardView;

import com.unityx.damath.MainActivity;
import com.unityx.damath.R;

public class IndicatorVictory extends Activity {

    private AlertDialog alertDialog;
    private AlertDialog.Builder dialogBuilder;
    private CardView layoutGet, cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout._indicator_victory);

        CardView layoutGet = findViewById(R.id.cdVictorID);
        ViewGroup.LayoutParams layoutParams = layoutGet.getLayoutParams();
        int width = layoutParams.width;

        getWindow().setLayout(width, ListPopupWindow.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        applyDim(root);

        cardView = findViewById(R.id.cdVictorID);

    }

    private static void applyDim(ViewGroup parent){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0,parent.getWidth(),parent.getHeight());
        dim.setAlpha(200);

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public void continueOnClick(View view) {
        Intent intent = new Intent(IndicatorVictory.this, MainActivity.class);
        startActivity(intent);
    }
}
