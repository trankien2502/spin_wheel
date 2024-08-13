package com.tkt.spin_wheel.ui.coin;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;


public class Flip3dAnimation extends Animation {
    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;
    private final Camera mCamera;
    private final ImageView mCoinHead;
    private final ImageView mCoinBack;


    public Flip3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY, ImageView coinHead, ImageView coinBack) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mCamera = new Camera();
        mCoinHead = coinHead;
        mCoinBack = coinBack;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();
        if (degrees<=90||degrees>270){
            mCoinHead.setVisibility(View.VISIBLE);
            mCoinBack.setVisibility(View.GONE);
        }
        if (degrees>90&&degrees<=270){
            mCoinHead.setVisibility(View.GONE);
            mCoinBack.setVisibility(View.VISIBLE);
        }
        degrees = degrees%360;
        camera.rotateX(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}

