package stream.customratingdialogue;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class RatingDialog {

    SharedPreferences pre;
    SharedPreferences.Editor edit;
    RatingDialogInterFace mRatingDialogListener;
    private Context mContext;
    private Dialog dialog;
    private RelativeLayout main;
    private ImageView btnCacncel, ratingFace;
    private RotationRatingBar rotationratingbar_main;
    private TextView btnSubmit;
    private boolean isEnable = true;
    private int defRating = 0;

    public RatingDialog(Context context) {
        mContext = context;
        pre = mContext.getSharedPreferences("rateData", MODE_PRIVATE);
        edit = pre.edit();
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogmain);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        btnCacncel = dialog.findViewById(R.id.btnCacncel);
        ratingFace = dialog.findViewById(R.id.ratingFace);
        main = dialog.findViewById(R.id.main);
        rotationratingbar_main = dialog.findViewById(R.id.rotationratingbar_main);
        btnSubmit = dialog.findViewById(R.id.btnSubmit);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                main.setRotation(0);
                main.setAlpha(0);
                main.setScaleY(0);
                main.setScaleX(0);
                main.clearAnimation();
                rotationratingbar_main.setVisibility(View.INVISIBLE);
                if (mRatingDialogListener != null) {
                    mRatingDialogListener.onDismiss();
                }
            }
        });
        btnCacncel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();

            }
        });

        rotationratingbar_main.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                if (ratingBar.getRating() < 5.0f) {
                    setRatingFace(false);
                } else {
                    setRatingFace(true);
                }
                if (mRatingDialogListener != null) {
                    mRatingDialogListener.onRatingChanged(rotationratingbar_main.getRating());
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.animate().scaleY(0).scaleX(0).alpha(0).rotation(-360).setDuration(400).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        dialog.dismiss();
                        main.clearAnimation();
                        rotationratingbar_main.setVisibility(View.INVISIBLE);
                        if (mRatingDialogListener != null) {
                            mRatingDialogListener.onSubmit(rotationratingbar_main.getRating());
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();
            }
        });

    }

    public void showDialog() {
        isEnable = pre.getBoolean("enb", true);
        if (isEnable) {
            dialog.show();
            rotationratingbar_main.clearAnimation();
            rotationratingbar_main.setRating(defRating);
            setRatingFace(true);
            main.animate().scaleY(1).scaleX(1).rotation(1080).alpha(1).setDuration(600).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    main.clearAnimation();
                    rotationratingbar_main.setVisibility(View.VISIBLE);
                    rotationratingbar_main.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.bounce_amn));
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        }
    }

    public boolean getEnable() {
        return pre.getBoolean("enb", true);
    }

    public void setEnable(boolean isEnable) {

        edit.putBoolean("enb", isEnable);
        edit.commit();
    }

    private void setRatingFace(boolean isTrue) {
        if (isTrue) {
            ratingFace.setImageResource(R.drawable.favorite);
        } else {
            ratingFace.setImageResource(R.drawable.favorite2);
        }

    }

    public void closeDialog() {
        main.animate().scaleY(0).scaleX(0).alpha(0).rotation(-360).setDuration(400).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                dialog.dismiss();
                main.clearAnimation();
                rotationratingbar_main.setVisibility(View.INVISIBLE);
                if (mRatingDialogListener != null) {
                    mRatingDialogListener.onDismiss();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    public void setDefaultRating(int defaultRating) {
        this.defRating = defaultRating;
    }

    public void setRatingDialogListener(RatingDialogInterFace mRatingDialogListener) {
        this.mRatingDialogListener = mRatingDialogListener;
    }

    public interface RatingDialogInterFace {
        void onDismiss();

        void onSubmit(float rating);

        void onRatingChanged(float rating);
    }
}
