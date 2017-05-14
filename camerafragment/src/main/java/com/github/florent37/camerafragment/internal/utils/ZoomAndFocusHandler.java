package com.github.florent37.camerafragment.internal.utils;

import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.florent37.camerafragment.internal.manager.impl.Camera1Manager;
import com.github.florent37.camerafragment.internal.manager.impl.Camera2Manager;

import java.util.List;

/**
 * @author Marco Zanella
 * @version 0.01
 * @since 0.01
 */

@SuppressWarnings("deprecated")
public class ZoomAndFocusHandler implements View.OnTouchListener {
    private Camera mCamera;
    private String currentCameraId;
    float mDist = 0;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // Get the pointer ID
        mCamera = Camera1Manager.getCameraInstance();
        currentCameraId = Camera2Manager.getCurrentCameraIdInstance();
        if (Camera1Manager.getCameraInstance() != null) {
            Log.i("ZOOM_FEATURE_CAMERA", "camera1");
            mCamera = Camera1Manager.getCameraInstance();
            android.hardware.Camera.Parameters params = mCamera.getParameters();
            int action = event.getAction();
            if (event.getPointerCount() > 1) {
                // handle multi-touch events
                if (action == MotionEvent.ACTION_POINTER_DOWN) {
                    mDist = getFingerSpacing(event);
                } else if (action == MotionEvent.ACTION_MOVE && params.isZoomSupported()) {
                    mCamera.cancelAutoFocus();
                    handleZoomCamera1(event, params);
                }
            } else {
                // handle single touch events
                if (action == MotionEvent.ACTION_UP) {
                    handleFocusCamera1(event, params);
                }
            }
        } else if (currentCameraId != null){
            Log.i("ZOOM_FEATURE_CAMERA", "camera2" + currentCameraId);
            /*CameraManager manager = (CameraManager) view.getContext().getSystemService(Context.CAMERA_SERVICE);
            int action = event.getAction();
            if (event.getPointerCount() > 1) {
                // handle multi-touch events
                if (action == MotionEvent.ACTION_POINTER_DOWN) {
                    mDist = getFingerSpacing(event);
                } else if (action == MotionEvent.ACTION_MOVE) {

                    //handleZoomCamera2(event, params);
                }
            } else {
                // handle single touch events
                if (action == MotionEvent.ACTION_UP) {
                    //handleFocusCamera1(event, params);
                }
            }*/
        }
        return true;
    }

    private void handleZoomCamera2(MotionEvent event) {
        /*try {
            CameraManager manager =
                    (CameraManager) view.getContext().getSystemService(Context.CAMERA_SERVICE);
            CameraCharacteristics cameraCharacteristics =
                    manager.getCameraCharacteristics(currentCameraId);
            Rect zoom = ???
            mCaptureRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoom);
            updatePreview();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }*/
    }

    private void handleZoomCamera1(MotionEvent event, android.hardware.Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        mCamera.setParameters(params);
    }

    private void handleFocusCamera1(MotionEvent event, android.hardware.Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null &&
                supportedFocusModes.contains(android.hardware.Camera.Parameters.FOCUS_MODE_AUTO)) {
            mCamera.autoFocus(new android.hardware.Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, android.hardware.Camera camera) {}
            });
        }
    }

    /** Determine the space between the first two fingers */
    private float getFingerSpacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

}