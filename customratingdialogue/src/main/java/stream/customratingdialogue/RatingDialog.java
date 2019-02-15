package stream.customratingdialogue;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import static android.content.Context.MODE_PRIVATE;

public class RatingDialog {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static final String TAG = RatingDialog.class.getSimpleName();
    public static final String RATING_ENABLED = "RATING_ENABLED";

    private Builder builder;
    private Style style = Style.NORMAL;
    private int rotation = 0;
    private SharedPreferences mSharedPref;
    private Context mContext;
    private Dialog dialog;
    private FrameLayout mLayoutMain;
    private AppCompatImageView mRatingHeaderImage;
    private AppCompatImageView mHeaderBackground;
    private AppCompatImageView mBtnClose;
    private LinearLayout mLayoutBackground;
    private TextView mTextTitle;
    private TextView mTextSubtitle;
    private ScaleRatingBar mRatingBarScale;
    private TextView mBtnSubmit;
    private AppCompatImageView mSubmitButtonRibbonLeft;
    private AppCompatImageView mSubmitButtonRibbonRight;
    private boolean isEnable = true;
    private boolean overrideDismissListener = false;

    private RatingDialog(Context context, Builder builderObject) {
        this.builder = builderObject;
        mContext = context;
        mSharedPref = mContext.getSharedPreferences("RATING", MODE_PRIVATE);

        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_rating);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        mLayoutMain = dialog.findViewById(R.id.layout_dialog_rating);

        mRatingHeaderImage = dialog.findViewById(R.id.img_rating_header);

        mHeaderBackground = dialog.findViewById(R.id.bg_header_bar);
        if (builder.headerBackgroundColor != 0) {
            mHeaderBackground.setColorFilter(ContextCompat.getColor(mContext, builder.headerBackgroundColor), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            mHeaderBackground.setColorFilter(Color.parseColor("#34495e"), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        mBtnClose = dialog.findViewById(R.id.btn_close);
        if (builder.closeButtonColor != 0) {
            mBtnClose.setColorFilter(ContextCompat.getColor(mContext, builder.closeButtonColor), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            mBtnClose.setColorFilter(Color.parseColor("#23323F"), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
            }
        });

        mLayoutBackground = dialog.findViewById(R.id.layout_content);
        if (builder.layoutBackgroundColor != 0)
            mLayoutBackground.setBackgroundColor(ContextCompat.getColor(mContext, builder.layoutBackgroundColor));

        mTextTitle = dialog.findViewById(R.id.text_title);
        if (builder.title != null) {
            if (!builder.title.isEmpty()) {
                mTextTitle.setText(builder.title);
            } else {
                mTextTitle.setVisibility(View.GONE);
            }
        }
        if (builder.titleColor != 0)
            mTextTitle.setTextColor(ContextCompat.getColor(mContext, builder.titleColor));
        if (builder.titleFont != 0)
            mTextTitle.setTypeface(ResourcesCompat.getFont(context, builder.titleFont));

        mTextSubtitle = dialog.findViewById(R.id.text_subtitle);
        if (builder.subtitle != null) {
            if (!builder.subtitle.isEmpty()) {
                mTextSubtitle.setText(builder.subtitle);
            } else {
                mTextSubtitle.setVisibility(View.GONE);
            }
        }
        if (builder.subtitleColor != 0)
            mTextSubtitle.setTextColor(ContextCompat.getColor(mContext, builder.subtitleColor));
        if (builder.subtitleFont != 0)
            mTextSubtitle.setTypeface(ResourcesCompat.getFont(context, builder.subtitleFont));

        mRatingBarScale = dialog.findViewById(R.id.rating_bar);
        mRatingBarScale.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                if (ratingBar.getRating() < 5.0f) {
                    setRatingHeaderImage(false);
                } else {
                    setRatingHeaderImage(true);
                }
                if (builder.ratingDialogInterface != null) {
                    builder.ratingDialogInterface.onRatingChanged(mRatingBarScale.getRating());
                }
            }
        });

        mBtnSubmit = dialog.findViewById(R.id.btn_submit);
        mSubmitButtonRibbonLeft = dialog.findViewById(R.id.img_ribbon_left);
        mSubmitButtonRibbonRight = dialog.findViewById(R.id.img_ribbon_right);
        if (builder.submitText != null)
            mBtnSubmit.setText(builder.submitText);
        if (builder.submitTextColor != 0)
            mBtnSubmit.setTextColor(ContextCompat.getColor(mContext, builder.submitTextColor));
        if (builder.submitFont != 0)
            mBtnSubmit.setTypeface(ResourcesCompat.getFont(context, builder.submitFont));
        if (builder.submitButtonRibbonColor != 0) {
            mSubmitButtonRibbonLeft.setColorFilter(ContextCompat.getColor(mContext, builder.submitButtonRibbonColor), android.graphics.PorterDuff.Mode.SRC_IN);
            mSubmitButtonRibbonRight.setColorFilter(ContextCompat.getColor(mContext, builder.submitButtonRibbonColor), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            mSubmitButtonRibbonLeft.setColorFilter(Color.parseColor("#16A085"), android.graphics.PorterDuff.Mode.SRC_IN);
            mSubmitButtonRibbonRight.setColorFilter(Color.parseColor("#16A085"), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        if (builder.submitButtonDrawable != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mBtnSubmit.setBackground(ContextCompat.getDrawable(mContext, builder.submitButtonDrawable));
            } else {
                mBtnSubmit.setBackgroundDrawable((ContextCompat.getDrawable(mContext, builder.submitButtonDrawable)));
            }
        }
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRating();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mLayoutMain.setRotation(0);
                mLayoutMain.setAlpha(0);
                mLayoutMain.setScaleY(0);
                mLayoutMain.setScaleX(0);
                mLayoutMain.clearAnimation();
                mRatingBarScale.setVisibility(View.INVISIBLE);
                if (!overrideDismissListener && builder.ratingDialogInterface != null) {
                    builder.ratingDialogInterface.onDismiss(mRatingBarScale.getRating());
                }
            }
        });
        dialog.setCancelable(builder.cancelable);
    }

    private void submitRating() {
        if (!builder.allowSubmitEmpty && mRatingBarScale.getRating() == 0) {
            ScaleAnimation scale = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(250);
            scale.setInterpolator(new DecelerateInterpolator());
            mRatingBarScale.startAnimation(scale);
        } else {
            submitRatingAnimation();
        }
    }

    private void submitRatingAnimation() {
        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                overrideDismissListener = true;
                mLayoutMain.clearAnimation();
                mRatingBarScale.setVisibility(View.INVISIBLE);
                if (builder.ratingDialogInterface != null) {
                    builder.ratingDialogInterface.onSubmit(mRatingBarScale.getRating());
                }
                dialog.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
        switch (builder.animateOutStyle) {
            case SCALE:
                mLayoutMain.animate().scaleY(0).scaleX(0).alpha(0).rotation(rotation).setDuration(300).setListener(animatorListener).start();
                break;
            case SPIN:
                mLayoutMain.animate().scaleY(0).scaleX(0).alpha(0).rotation(rotation - 1080).setDuration(400).setListener(animatorListener).start();
                break;
        }
    }

    private void showDialog() {
        isEnable = mSharedPref.getBoolean(RATING_ENABLED, true);
        if (isEnable) {
            dialog.show();
            mRatingBarScale.clearAnimation();
            mRatingBarScale.setRating(builder.defaultRating);
            setRatingHeaderImage(true);
            showDialogAnimation();
        }
    }

    private void showDialogAnimation() {
        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
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
        };
        switch (builder.animateInStyle) {
            case SCALE:
                rotation = 0;
                mLayoutMain.animate().scaleY(1).scaleX(1).rotation(0).alpha(1).setDuration(400).setListener(animatorListener).start();
                break;
            case SPIN:
                rotation = 1080;
                mLayoutMain.animate().scaleY(1).scaleX(1).rotation(1080).alpha(1).setDuration(600).setListener(animatorListener).start();
                break;
        }
    }

    private void closeDialog() {
        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                overrideDismissListener = true;
                mLayoutMain.clearAnimation();
                mRatingBarScale.setVisibility(View.INVISIBLE);
                if (builder.ratingDialogInterface != null) {
                    builder.ratingDialogInterface.onDismiss(mRatingBarScale.getRating());
                }
                dialog.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
        switch (builder.animateCloseStyle) {
            case SCALE:
                mLayoutMain.animate().scaleY(0).scaleX(0).alpha(0).rotation(rotation).setDuration(300).setListener(animatorListener).start();
                break;
            case SPIN:
                mLayoutMain.animate().scaleY(0).scaleX(0).alpha(0).rotation(rotation - 1080).setDuration(400).setListener(animatorListener).start();
                break;
        }
    }

    private void setRatingHeaderImage(boolean isTrue) {
        if (isTrue) {
            if (builder.headerImage1Drawable != 0) {
                mRatingHeaderImage.setImageResource(builder.headerImage1Drawable);
            } else {
                mRatingHeaderImage.setImageResource(R.drawable.favorite);
            }
        } else {
            if (builder.headerImage2Drawable != 0) {
                mRatingHeaderImage.setImageResource(builder.headerImage2Drawable);
            } else {
                mRatingHeaderImage.setImageResource(R.drawable.favorite2);
            }
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

    public enum Style {
        NORMAL
    }

    public enum AnimateInStyle {
        SCALE,
        SPIN
    }

    public enum AnimateOutStyle {
        SCALE,
        SPIN
    }

    public enum AnimateCloseStyle {
        SCALE,
        SPIN
    }

    public interface RatingDialogInterface {
        void onDismiss(float rating);

        void onSubmit(float rating);

        void onRatingChanged(float rating);
    }

    public static class Builder implements Parcelable {

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
        private RatingDialog ratingDialog;
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
        private int headerImage1Drawable;
        private int headerImage2Drawable;
        private int submitButtonDrawable;
        private int titleFont;
        private int subtitleFont;
        private int submitFont;
        private int defaultRating = 0;
        private AnimateInStyle animateInStyle = AnimateInStyle.SCALE;
        private AnimateOutStyle animateOutStyle = AnimateOutStyle.SCALE;
        private AnimateCloseStyle animateCloseStyle = AnimateCloseStyle.SCALE;
        private RatingDialogInterface ratingDialogInterface;
        private boolean allowSubmitEmpty = false;
        private boolean cancelable = false;
        private Integer gravity = Gravity.CENTER;
        private Style style;
        private Context context;

        @SuppressWarnings("WeakerAccess")
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
            this.headerImage1Drawable = in.readInt();
            this.headerImage2Drawable = in.readInt();
            this.submitButtonDrawable = in.readInt();
            this.defaultRating = in.readInt();
            this.animateInStyle = AnimateInStyle.values()[in.readInt()];
            this.animateOutStyle = AnimateOutStyle.values()[in.readInt()];
            this.animateCloseStyle = AnimateCloseStyle.values()[in.readInt()];
            this.allowSubmitEmpty = in.readByte() != 0;
            this.cancelable = in.readByte() != 0;
            this.gravity = in.readInt();
            this.style = Style.valueOf(in.readString());
        }

        /**
         * The Builder dialogue requires an Activity and passing a Context does not work!
         *
         * @param context - pass the Dialogue's parent activity.
         */
        public Builder(Context context) {
            this.context = context;
        }

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
            dest.writeInt(this.headerImage1Drawable);
            dest.writeInt(this.headerImage2Drawable);
            dest.writeInt(this.submitButtonDrawable);
            dest.writeInt(this.titleFont);
            dest.writeInt(this.subtitleFont);
            dest.writeInt(this.submitFont);
            dest.writeInt(this.defaultRating);
            dest.writeInt(this.animateInStyle.ordinal());
            dest.writeInt(this.animateOutStyle.ordinal());
            dest.writeInt(this.animateCloseStyle.ordinal());
            dest.writeByte((byte) (allowSubmitEmpty ? 1 : 0));
            dest.writeByte((byte) (cancelable ? 1 : 0));
            dest.writeInt(gravity);
            dest.writeString(style.toString());
        }

        public Style getStyle() {
            return style;
        }

        /**
         * @param style - set dialog style.
         */
        public Builder setStyle(Style style) {
            if (style != null) {
                this.style = style;
            }
            return this;
        }

        public Integer getGravity() {
            return gravity;
        }

        public Builder setGravity(Integer gravity) {
            if (gravity != null) {
                this.gravity = gravity;
            }
            return this;
        }

        public String getTitle() {
            return title;
        }

        /**
         * @param title - set title text.
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getSubtitle() {
            return subtitle;
        }

        /**
         * @param subtitle - set subtitle text.
         */
        public Builder setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public String getSubmitText() {
            return submitText;
        }

        /**
         * @param submitText - set submit button text.
         */
        public Builder setSubmitText(String submitText) {
            this.submitText = submitText;
            return this;
        }

        public int getTitleColor() {
            return titleColor;
        }

        /**
         * @param titleColor - set title text color.
         */
        public Builder setTitleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public int getSubtitleColor() {
            return subtitleColor;
        }

        /**
         * @param subtitleColor - set subtitle text color.
         */
        public Builder setSubtitleColor(int subtitleColor) {
            this.subtitleColor = subtitleColor;
            return this;
        }

        public int getSubmitTextColor() {
            return submitTextColor;
        }

        /**
         * @param positiveTextColor - set positive button text color.
         */
        public Builder setSubmitTextColor(int positiveTextColor) {
            this.submitTextColor = positiveTextColor;
            return this;
        }

        public int getHeaderBackgroundColor() {
            return headerBackgroundColor;
        }

        /**
         * @param headerBackgroundColor - set header drawable tint color.
         */
        public Builder setHeaderBackgroundColor(int headerBackgroundColor) {
            this.headerBackgroundColor = headerBackgroundColor;
            return this;
        }

        public int getCloseButtonColor() {
            return closeButtonColor;
        }

        /**
         * @param closeButtonColor - set close drawable tint color.
         */
        public Builder setCloseButtonColor(int closeButtonColor) {
            this.closeButtonColor = closeButtonColor;
            return this;
        }

        public int getLayoutBackgroundColor() {
            return layoutBackgroundColor;
        }

        /**
         * @param layoutBackgroundColor - set main layout background color.
         */
        public Builder setLayoutBackgroundColor(int layoutBackgroundColor) {
            this.layoutBackgroundColor = layoutBackgroundColor;
            return this;
        }

        public int getSubmitButtonRibbonColor() {
            return submitButtonRibbonColor;
        }

        /**
         * @param submitButtonRibbonColor - set ribbon drawable tint color. Intended to match submit button drawable normal state color.
         */
        public Builder setSubmitButtonRibbonColor(int submitButtonRibbonColor) {
            this.submitButtonRibbonColor = submitButtonRibbonColor;
            return this;
        }

        public int getHeaderImage1Drawable() {
            return headerImage1Drawable;
        }

        /**
         * @param headerImage1Drawable - set happy header image drawable.
         */
        public Builder setHeaderImage1Drawable(int headerImage1Drawable) {
            this.headerImage1Drawable = headerImage1Drawable;
            return this;
        }

        public int getHeaderImage2Drawable() {
            return headerImage2Drawable;
        }

        /**
         * @param headerImage2Drawable - set submit button drawable.
         */
        public Builder setHeaderImage2Drawable(int headerImage2Drawable) {
            this.headerImage2Drawable = headerImage2Drawable;
            return this;
        }

        public int getSubmitButtonDrawable() {
            return submitButtonDrawable;
        }

        /**
         * @param submitButtonDrawable - set submit button drawable.
         */
        public Builder setSubmitButtonDrawable(int submitButtonDrawable) {
            this.submitButtonDrawable = submitButtonDrawable;
            return this;
        }

        public int getTitleFont() {
            return titleFont;
        }

        /**
         * @param titleFontPath - set title text font as font resource.
         */
        public Builder setTitleFont(int titleFontPath) {
            this.titleFont = titleFontPath;
            return this;
        }

        public int getSubtitleFont() {
            return subtitleFont;
        }

        /**
         * @param subtitleFontPath - set subtitle text font as font resource.
         */
        public Builder setSubtitleFont(int subtitleFontPath) {
            this.subtitleFont = subtitleFontPath;
            return this;
        }

        public int getSubmitFont() {
            return submitFont;
        }

        /**
         * @param submitFont - set positive button text font as font resource.
         */
        public Builder setSubmitFont(int submitFont) {
            this.submitFont = submitFont;
            return this;
        }

        public int getDefaultRating() {
            return defaultRating;
        }

        /**
         * @param defaultRating - set initial number of stars to display.
         *                      Value must be greater than or equal to 0 and less than the number of stars or value will default to max and min bounds.
         */
        public Builder setDefaultRating(int defaultRating) {
            this.defaultRating = defaultRating;
            return this;
        }

        public AnimateInStyle getAnimateInStyle() {
            return animateInStyle;
        }

        /**
         * @param animateInStyle - set dialog appearance animation.
         *                       SCALE, SPIN
         */
        public Builder setAnimateInStyle(AnimateInStyle animateInStyle) {
            this.animateInStyle = animateInStyle;
            return this;
        }

        public AnimateOutStyle getAnimateOutStyle() {
            return animateOutStyle;
        }

        /**
         * @param animateOutStyle - set dialog appearance animation.
         *                        SCALE, SPOut
         */
        public Builder setAnimateOutStyle(AnimateOutStyle animateOutStyle) {
            this.animateOutStyle = animateOutStyle;
            return this;
        }

        public AnimateCloseStyle getAnimateCloseStyle() {
            return animateCloseStyle;
        }

        /**
         * @param animateCloseStyle - set dialog appearance animation.
         *                          SCALE, SPIN
         */
        public Builder setAnimateCloseStyle(AnimateCloseStyle animateCloseStyle) {
            this.animateCloseStyle = animateCloseStyle;
            return this;
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

        public boolean getAllowSubmitEmpty() {
            return allowSubmitEmpty;
        }

        /**
         * @param allowSubmitEmpty - set true to allow user to submit without rating. Default false to force user to rate.
         */
        public Builder setAllowSubmitEmpty(boolean allowSubmitEmpty) {
            this.allowSubmitEmpty = allowSubmitEmpty;
            return this;
        }

        public boolean getCancelable() {
            return cancelable;
        }

        /**
         * @param cancelable - set true to allow dialogue dismissal through tapping outside or pressing the back button.
         *                   Default false to force the user to an choose option and avoid accidental dismissal.
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

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
            ratingDialog = new RatingDialog(context, this);
            ratingDialog.showDialog();
        }

        /**
         * Programmatically close the dialog.
         */
        public void dismiss() {
            if (ratingDialog != null)
                ratingDialog.closeDialog();
        }
    }
}
