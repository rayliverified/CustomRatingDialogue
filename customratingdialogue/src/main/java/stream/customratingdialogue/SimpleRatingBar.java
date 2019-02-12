package stream.customratingdialogue;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

interface SimpleRatingBar {

    int getNumStars();

    void setNumStars(int numStars);

    float getRating();

    void setRating(float rating);

    int getStarPadding();

    void setStarPadding(int ratingPadding);

    void setEmptyDrawable(Drawable drawable);

    void setEmptyDrawableRes(@DrawableRes int res);

    void setFilledDrawable(Drawable drawable);

    void setFilledDrawableRes(@DrawableRes int res);
}