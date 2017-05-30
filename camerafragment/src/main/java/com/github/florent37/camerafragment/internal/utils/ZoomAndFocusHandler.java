package com.github.florent37.camerafragment.internal.utils;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
    private int zoom_level = 1;


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // Get the pointer ID
        mCamera = Camera1Manager.getCameraInstance();
        currentCameraId = Camera2Manager.getCurrentCameraIdInstance();

        if (mCamera != null) {
            Log.d("ZOOM", "CAMERA1");
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
            Log.d("ZOOM", "CAMERA2");
            CameraManager manager = (CameraManager) view.getContext().getSystemService(Context.CAMERA_SERVICE);

            int action = event.getAction();
            CameraCharacteristics characteristics = null;
            try {
                characteristics = manager.getCameraCharacteristics(currentCameraId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            float maxzoom = (characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM))*10;

            Rect m = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            float current_finger_spacing;
            if (event.getPointerCount() > 1) {
                // Multi touch logic
                current_finger_spacing = getFingerSpacing(event);
                Log.d("ZOOM", "RESIZE");
                if (mDist != 0) {

                    if (current_finger_spacing > mDist && maxzoom > zoom_level) {
                        zoom_level++;

                    } else if (current_finger_spacing < mDist && zoom_level > 1) {
                        zoom_level--;

                    }
                    int minW = (int) (m.width() / maxzoom);
                    int minH = (int) (m.height() / maxzoom);
                    int difW = m.width() - minW;
                    int difH = m.height() - minH;
                    int cropW = difW / 100 * (int) zoom_level;
                    int cropH = difH / 100 * (int) zoom_level;
                    cropW -= cropW & 3;
                    cropH -= cropH & 3;
                    Rect zoom = new Rect(cropW, cropH, m.width() - cropW, m.height() - cropH);
                    Camera2Manager.getPreviewRequestBuilder().set(CaptureRequest.SCALER_CROP_REGION, zoom);
                }
                mDist = current_finger_spacing;
            }
            else {
                // handle single touch events
                Log.d("ZOOM", "SINGLE TOUCH");
                Log.d("ZOOM", event.toString());
                if (action == MotionEvent.ACTION_UP) {
                    //handleFocusCamera1(event, params);
                }
            }
            try {
                Camera2Manager.getCaptureSession().setRepeatingRequest(Camera2Manager.getPreviewRequestBuilder().build(), Camera2Manager.getCaptureCallbackInstance(),
                        null);
            } catch (Exception e){

            }
        }
        return true;
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

        try {
            if (supportedFocusModes != null &&
                    supportedFocusModes.contains(android.hardware.Camera.Parameters.FOCUS_MODE_AUTO)) {
                mCamera.autoFocus(new android.hardware.Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, android.hardware.Camera camera) {}
                });
            }
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }


    }

    /** Determine the space between the first two fingers */
    private float getFingerSpacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

}