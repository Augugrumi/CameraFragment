package com.github.florent37.camerafragment.internal.utils.zoom;

import android.view.View;

/**
 * @author Marco Zanella
 * @version 0.01
 * @since 0.01
 */

public class SimpleZoomHandlerBuilder {

    private SimpleZoomHandler simpleZoomHandler;

    public static SimpleZoomHandlerBuilder forView(View touchableView) {
        return new SimpleZoomHandlerBuilder(touchableView);
    }

    private SimpleZoomHandlerBuilder(View touchableView) {
        simpleZoomHandler = new SimpleZoomHandler(touchableView);
    }

    public SimpleZoomHandlerBuilder setZoomListener(SimpleZoomHandler.IZoomHandlerListener listener) {
        simpleZoomHandler.setZoomHandlerListener(listener);
        return this;
    }

    public SimpleZoomHandlerBuilder setMaxZoom(float maxZoom) {
        simpleZoomHandler.setMaxZoom(maxZoom);
        return this;
    }

    public SimpleZoomHandler build() {
        return simpleZoomHandler;
    }
}