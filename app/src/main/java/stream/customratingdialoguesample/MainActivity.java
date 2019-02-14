package stream.customratingdialoguesample;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import stream.customratingdialogue.RatingDialog;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplication().getApplicationContext();
        setContentView(R.layout.activity_main);

        Button mBtnAction = findViewById(R.id.btn_action);
        mBtnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingDialog.Builder ratingDialog = new RatingDialog.Builder(MainActivity.this)
                        .setRatingDialogInterface(new RatingDialog.RatingDialogInterface() {
                            @Override
                            public void onDismiss(float rating) {
                                Log.d("RATELISTERNER", "onDismiss " + rating);
                            }

                            @Override
                            public void onSubmit(float rating) {
                                Log.d("RATELISTERNER", "onSubmit " + rating);
                            }

                            @Override
                            public void onRatingChanged(float rating) {
                                Log.d("RATELISTERNER", "onRatingChanged " + rating);
                            }
                        })
                        .build();
                ratingDialog.show();
            }
        });

        Button mBtnAction2 = findViewById(R.id.btn_action_2);
        mBtnAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingDialog.Builder ratingDialog = new RatingDialog.Builder(MainActivity.this)
                        .setHeaderBackgroundColor(R.color.gray_medium)
                        .setLayoutBackgroundColor(R.color.gray_dark)
                        .setCloseButtonColor(R.color.gray_light)
                        .setTitle("How are we doing?")
                        .setTitleColor(android.R.color.white)
                        .setTitleFont(R.font.museosans500)
                        .setSubtitle("Touch to Rate")
                        .setSubtitleColor(android.R.color.white)
                        .setSubtitleFont(R.font.museosans500)
                        .setSubmitButtonDrawable(R.drawable.bg_submit_custom_selector)
                        .setSubmitButtonRibbonColor(R.color.dark_red)
                        .setSubmitText("Submit")
                        .setSubmitTextColor(android.R.color.white)
                        .setSubmitFont(R.font.museosans500)
                        .setAnimateInStyle(RatingDialog.AnimateInStyle.SPIN)
                        .setAnimateOutStyle(RatingDialog.AnimateOutStyle.SPIN)
                        .setAnimateCloseStyle(RatingDialog.AnimateCloseStyle.SCALE)
                        .setCancelable(true)
                        .setRatingDialogInterface(new RatingDialog.RatingDialogInterface() {
                            @Override
                            public void onDismiss(float rating) {
                                Log.d("RATELISTERNER", "onDismiss " + rating);
                            }

                            @Override
                            public void onSubmit(float rating) {
                                Log.d("RATELISTERNER", "onSubmit " + rating);
                            }

                            @Override
                            public void onRatingChanged(float rating) {
                                Log.d("RATELISTERNER", "onRatingChanged " + rating);
                            }
                        })
                        .build();
                ratingDialog.show();
            }
        });

        Button mBtnAction3 = findViewById(R.id.btn_action_3);
        mBtnAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingDialog.Builder ratingDialog = new RatingDialog.Builder(MainActivity.this)
                        .setHeaderBackgroundColor(R.color.branded_primary_dark)
                        .setCloseButtonColor(R.color.branded_primary_medium)
                        .setTitle("How are we doing?")
                        .setTitleColor(R.color.branded_text_primary)
                        .setTitleFont(R.font.sfuitextregular)
                        .setSubtitle("Touch to Rate")
                        .setSubtitleColor(R.color.branded_text_secondary_light)
                        .setSubtitleFont(R.font.sfuitextregular)
                        .setHeaderImage1Drawable(R.drawable.img_avatar_smile)
                        .setHeaderImage2Drawable(R.drawable.img_avatar_frown)
                        .setSubmitButtonDrawable(R.drawable.bg_submit_branded_selector)
                        .setSubmitButtonRibbonColor(R.color.branded_button_normal_dark)
                        .setSubmitText("Review")
                        .setSubmitTextColor(android.R.color.white)
                        .setSubmitFont(R.font.sfuitextregular)
                        .setAnimateInStyle(RatingDialog.AnimateInStyle.SPIN)
                        .setAnimateOutStyle(RatingDialog.AnimateOutStyle.SPIN)
                        .setAnimateCloseStyle(RatingDialog.AnimateCloseStyle.SCALE)
                        .setRatingDialogInterface(new RatingDialog.RatingDialogInterface() {
                            @Override
                            public void onDismiss(float rating) {
                                Log.d("RATELISTERNER", "onDismiss " + rating);
                            }

                            @Override
                            public void onSubmit(float rating) {
                                Log.d("RATELISTERNER", "onSubmit " + rating);
                            }

                            @Override
                            public void onRatingChanged(float rating) {
                                Log.d("RATELISTERNER", "onRatingChanged " + rating);
                            }
                        })
                        .build();
                ratingDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
    }
}