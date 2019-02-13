package stream.customratingdialogue;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;

import static android.content.Context.MODE_PRIVATE;

public class RatingDialog {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public enum Style{
        NORMAL
    }

    public static final String RATING_ENABLED = "RATING_ENABLED";

    public static final String TAG = RatingDialog.class.getSimpleName();
    private Builder builder;
    private Style style = Style.NORMAL;

    private SharedPreferences mSharedPref;
    private RatingDialogInterface mRatingDialogListener;
    private Context mContext;
    private Dialog dialog;
    private FrameLayout mLayoutMain;
    private AppCompatImageView mRatingHeaderImage;
    private AppCompatImageView mBtnCancel;
    private ScaleRatingBar mRatingBarScale;
    private TextView mBtnSubmit;
    private boolean isEnable = true;
    private int defRating = 0;

    public RatingDialog(Context context) {
        mContext = context;
        mSharedPref = mContext.getSharedPreferences("RATING", MODE_PRIVATE);
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_rating);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        mBtnCancel = dialog.findViewById(R.id.btn_cancel);
        mRatingHeaderImage = dialog.findViewById(R.id.img_rating_header);
        mLayoutMain = dialog.findViewById(R.id.layout_dialog_rating);
        mRatingBarScale = dialog.findViewById(R.id.rating_bar);
        mBtnSubmit = dialog.findViewById(R.id.btn_submit);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mLayoutMain.setRotation(0);
                mLayoutMain.setAlpha(0);
                mLayoutMain.setScaleY(0);
                mLayoutMain.setScaleX(0);
                mLayoutMain.clearAnimation();
                mRatingBarScale.setVisibility(View.INVISIBLE);
                if (mRatingDialogListener != null) {
                    mRatingDialogListener.onDismiss();
                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();

            }
        });

        mRatingBarScale.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                if (ratingBar.getRating() < 5.0f) {
                    setRatingHeaderImage(false);
                } else {
                    setRatingHeaderImage(true);
                }
                if (mRatingDialogListener != null) {
                    mRatingDialogListener.onRatingChanged(mRatingBarScale.getRating());
                }
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayoutMain.animate().scaleY(0).scaleX(0).alpha(0).rotation(-360).setDuration(400).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        dialog.dismiss();
                        mLayoutMain.clearAnimation();
                        mRatingBarScale.setVisibility(View.INVISIBLE);
                        if (mRatingDialogListener != null) {
                            mRatingDialogListener.onSubmit(mRatingBarScale.getRating());
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
        isEnable = mSharedPref.getBoolean(RATING_ENABLED, true);
        if (isEnable) {
            dialog.show();
            mRatingBarScale.clearAnimation();
            mRatingBarScale.setRating(defRating);
            setRatingHeaderImage(true);
            mLayoutMain.animate().scaleY(1).scaleX(1).rotation(1080).alpha(1).setDuration(600).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mLayoutMain.clearAnimation();
                    mRatingBarScale.setVisibility(View.VISIBLE);
                    mRatingBarScale.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.bounce_amn));
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
        return mSharedPref.getBoolean(RATING_ENABLED, true);
    }

    public void setEnable(boolean isEnable) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(RATING_ENABLED, isEnable);
        editor.apply();
    }

    private void setRatingHeaderImage(boolean isTrue) {
        if (isTrue) {
            mRatingHeaderImage.setImageResource(R.drawable.favorite);
        } else {
            mRatingHeaderImage.setImageResource(R.drawable.favorite2);
        }
    }

    public void closeDialog() {
        mLayoutMain.animate().scaleY(0).scaleX(0).alpha(0).rotation(-360).setDuration(400).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                dialog.dismiss();
                mLayoutMain.clearAnimation();
                mRatingBarScale.setVisibility(View.INVISIBLE);
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

    public void setRatingDialogListener(RatingDialogInterface mRatingDialogListener) {
        this.mRatingDialogListener = mRatingDialogListener;
    }

    public static class Builder implements Parcelable {

        private String title;
        private String subtitle;
        private String submitText;

        private int titleColor;
        private int subtitleColor;
        private int submitTextColor;
        private int headerBackgroundColor;
        private int closeButtonColor;
        private int layoutBackgroundColor;
        private int submitButtonRibbonColor;

        private int submitButtonDrawable;

        private Typeface titleFont;
        private Typeface subtitleFont;
        private Typeface submitFont;

        private RatingDialogInterface ratingDialogInterface;

        private boolean cancelable = true;

        private Integer gravity = Gravity.CENTER;
        private Style style;
        private Context context;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.subtitle);
            dest.writeString(this.submitText);
            dest.writeInt(this.titleColor);
            dest.writeInt(this.subtitleColor);
            dest.writeInt(this.submitTextColor);
            dest.writeInt(this.headerBackgroundColor);
            dest.writeInt(this.closeButtonColor);
            dest.writeInt(this.layoutBackgroundColor);
            dest.writeInt(this.submitButtonRibbonColor);
            dest.writeInt(this.submitButtonDrawable);
            dest.writeByte((byte) (cancelable ? 1 : 0));
            dest.writeInt(gravity);
            dest.writeString(style.toString());
        }

        protected Builder(Parcel in) {
            this.title = in.readString();
            this.subtitle = in.readString();
            this.submitText = in.readString();
            this.titleColor = in.readInt();
            this.subtitleColor = in.readInt();
            this.submitTextColor = in.readInt();
            this.headerBackgroundColor = in.readInt();
            this.closeButtonColor = in.readInt();
            this.layoutBackgroundColor = in.readInt();
            this.submitButtonRibbonColor = in.readInt();
            this.submitButtonDrawable = in.readInt();
            cancelable = in.readByte() != 0;
            gravity = in.readInt();
            style = Style.valueOf(in.readString());
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel source) {
                return new Builder(source);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };

        /**
         * @param style - set dialog style.
         */
        public Builder setStyle(Style style) {
            if(style != null) {
                this.style = style;
            }
            return this;
        }
        public Style getStyle() { return style; }

        public Builder setGravity(Integer gravity) {
            if(gravity != null) {
                this.gravity = gravity;
            }
            return this;
        }
        public Integer getGravity() { return gravity; }

        /**
         * @param title - set title text.
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public String getTitle() { return title; }

        /**
         * @param subtitle - set subtitle text.
         */
        public Builder setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }
        public String getSubtitle() {
            return subtitle;
        }

        /**
         * @param submitText - set submit button text.
         */
        public Builder setSubmitText(String submitText) {
            this.submitText = submitText;
            return this;
        }
        public String getSubmitText() {
            return submitText;
        }

        /**
         * @param titleColor - set title text color.
         */
        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }
        public int getTitleColor() {
            return titleColor;
        }

        /**
         * @param subtitleColor - set subtitle text color.
         */
        public Builder setSubtitleColor(int subtitleColor) {
            this.subtitleColor = subtitleColor;
            return this;
        }
        public int getSubtitleColor() {
            return subtitleColor;
        }

        /**
         * @param positiveTextColor - set positive button text color.
         */
        public Builder setSubmitTextColor(int positiveTextColor) {
            this.submitTextColor = positiveTextColor;
            return this;
        }
        public int getSubmitTextColor() {
            return submitTextColor;
        }

        /**
         * @param headerBackgroundColor - set header drawable tint color.
         */
        public Builder setHeaderBackgroundColor(int headerBackgroundColor) {
            this.headerBackgroundColor = headerBackgroundColor;
            return this;
        }
        public int getHeaderBackgroundColor() {
            return headerBackgroundColor;
        }

        /**
         * @param closeButtonColor - set close drawable tint color.
         */
        public Builder setCloseButtonColor(int closeButtonColor) {
            this.closeButtonColor = closeButtonColor;
            return this;
        }
        public int getCloseButtonColor() {
            return closeButtonColor;
        }

        /**
         * @param layoutBackgroundColor - set main layout background color.
         */
        public Builder setLayoutBackgroundColor(int layoutBackgroundColor) {
            this.layoutBackgroundColor = layoutBackgroundColor;
            return this;
        }
        public int getLayoutBackgroundColor() {
            return layoutBackgroundColor;
        }

        /**
         * @param submitButtonRibbonColor - set ribbon drawable tint color. Intended to match submit button drawable normal state color.
         */
        public Builder setSubmitButtonRibbonColor(int submitButtonRibbonColor) {
            this.submitButtonRibbonColor = submitButtonRibbonColor;
            return this;
        }
        public int getSubmitButtonRibbonColor() {
            return submitButtonRibbonColor;
        }

        /**
         * @param submitButtonDrawable - set submit button drawable.
         */
        public Builder setSubmitButtonDrawable(int submitButtonDrawable) {
            this.submitButtonDrawable = submitButtonDrawable;
            return this;
        }
        public int getSubmitButtonDrawable() {
            return submitButtonDrawable;
        }

        /**
         * @param titleFontPath - set title text font. Must pass the path to the font in the assets folder.
         */
        public Builder setTitleFont(String titleFontPath) {
            this.titleFont = Typeface.createFromAsset(context.getAssets(), titleFontPath);
            return this;
        }
        public Typeface getTitleFont() {
            return titleFont;
        }

        /**
         * @param subtitleFontPath - set subtitle text font. Must pass the path to the font in the assets folder.
         */
        public Builder setSubtitleFont(String subtitleFontPath) {
            this.subtitleFont = Typeface.createFromAsset(context.getAssets(), subtitleFontPath);
            return this;
        }
        public Typeface getSubtitleFont() {
            return subtitleFont;
        }

        /**
         * @param submitFont - set positive button text typeface.
         */
        public Builder setSubmitFont(Typeface submitFont) {
            this.submitFont = submitFont;
            return this;
        }
        public Typeface getSubmitFont() {
            return submitFont;
        }

        /**
         * @param ratingDialogInterface - pass a listener to be called.
         */
        public Builder setRatingDialogInterface(RatingDialogInterface ratingDialogInterface) {
            this.ratingDialogInterface = ratingDialogInterface;
            return this;
        }
        public RatingDialogInterface getOnPositiveClicked() {
            return ratingDialogInterface;
        }

        /**
         * @param cancelable - set false to prevent dialogue dismissal through tapping outside or pressing the back button.
         *                   Force the user to an choose option.
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }
        public boolean getCancelable() { return cancelable; }

        /**
         * The Builder dialogue requires an Activity and passing a Context does not work!
         * @param context - pass the Dialogue's parent activity.
         */
        public Builder(Context context) { this.context = context; }

        /**
         * Construct the Dialogue Builder.
         */
        public Builder build() {
            return this;
        }

        /**
         * Display the Dialogue with Builder parameters.
         */
        public void show() {
            new RatingDialog((Activity) context).showDialog();
        }
    }

    public interface RatingDialogInterface {
        void onDismiss();

        void onSubmit(float rating);

        void onRatingChanged(float rating);
    }
}
