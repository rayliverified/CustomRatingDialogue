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
                            public void onDismiss() {
                                Log.d("RATELISTERNER", "onDismiss");
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
                        .setHeaderBackgroundColor(R.color.colorPrimary)
                        .setCloseButtonColor(android.R.color.white)
                        .setRatingDialogInterface(new RatingDialog.RatingDialogInterface() {
                            @Override
                            public void onDismiss() {
                                Log.d("RATELISTERNER", "onDismiss");
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
}