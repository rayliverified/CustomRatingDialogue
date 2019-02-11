package stream.customratingdialoguesample;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import stream.customratingdialogue.RatingDialog;

public class MainActivity extends AppCompatActivity {

    private RatingDialog mRatingDialog;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplication().getApplicationContext();
        setContentView(R.layout.activity_main);

        mRatingDialog = new RatingDialog(this);
        mRatingDialog.setRatingDialogListener(new RatingDialog.RatingDialogInterFace() {
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
        });

        TextView textView = findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRatingDialog.showDialog();
            }
        });
    }
}