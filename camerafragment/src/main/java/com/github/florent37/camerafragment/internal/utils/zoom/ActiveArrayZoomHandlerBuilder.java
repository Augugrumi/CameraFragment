package com.github.florent37.camerafragment.internal.utils.zoom;

import android.graphics.Rect;
import android.view.View;

/**
 * @author Marco Zanella
 * @version 0.01
 * @since 0.01
 */

public class ActiveArrayZoomHandlerBuilder {

    private ActiveArrayZoomHandler activeArrayZoomHandler;

    public static ActiveArrayZoomHandlerBuilder forView(View touchableView) {
        return new ActiveArrayZoomHandlerBuilder( touchableView );
    }

    private ActiveArrayZoomHandlerBuilder(View touchableView) {
        activeArrayZoomHandler = new ActiveArrayZoomHandler( touchableView );
    }

    public ActiveArrayZoomHandlerBuilder setZoomListener(ActiveArrayZoomHandler.IZoomHandlerListener listener) {
        activeArrayZoomHandler.setZoomHandlerListener( listener );
        return this;
    }

    public ActiveArrayZoomHandlerBuilder setMaxZoom(float maxZoom) {
        activeArrayZoomHandler.setMaxZoom( maxZoom );
        return this;
    }

    public ActiveArrayZoomHandlerBuilder setActiveArraySize(Rect activeArraySize) {
        activeArrayZoomHandler.setActiveArraySize( activeArraySize );
        return this;
    }

    public ActiveArrayZoomHandler build() {
        return activeArrayZoomHandler;
    }

}