package com.github.florent37.camerafragment.internal.utils.zoom;

import android.view.View;

/**
 * @author Marco Zanella
 * @version 0.01
 * @since 0.01
 */

public class SimpleZoomHandler extends AbstractZoomHandler {

    private IZoomHandlerListener zoomHandlerListener;

    SimpleZoomHandler(View touchableView) {
        super( touchableView );
    }

    public void setZoomHandlerListener(IZoomHandlerListener zoomHandlerListener) {
        this.zoomHandlerListener = zoomHandlerListener;
    }

    @Override
    public void notifyZoomChanged(int zoom) {
        zoomHandlerListener.onZoomChanged( zoom );
    }

    @Override
    public boolean isPrepared() {
        return zoomHandlerListener != null;
    }

    public interface IZoomHandlerListener {

        void onZoomChanged(int newZoom);

    }
}
