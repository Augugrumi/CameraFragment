package com.github.florent37.camerafragment.internal.utils.zoom;

import android.graphics.Rect;
import android.view.View;

/**
 * @author Marco Zanella
 * @version 0.01
 * @since 0.01
 */

public class ActiveArrayZoomHandler extends AbstractZoomHandler {

    private IZoomHandlerListener zoomHandlerListener;
    private Rect activeArraySize;

    ActiveArrayZoomHandler(View touchableView) {
        super( touchableView );
    }

    public void setZoomHandlerListener(IZoomHandlerListener zoomHandlerListener) {
        this.zoomHandlerListener = zoomHandlerListener;
    }

    public void setActiveArraySize(Rect activeArraySize) {
        this.activeArraySize = activeArraySize;
    }


    @Override
    public void notifyZoomChanged(int zoom) {
        int minW = (int) (activeArraySize.width() / maxZoom);
        int minH = (int) (activeArraySize.height() / maxZoom);
        int difW = activeArraySize.width() - minW;
        int difH = activeArraySize.height() - minH;
        int cropW = difW / 100 * zoomLevel;
        int cropH = difH / 100 * zoomLevel;
        cropW -= cropW & 3;
        cropH -= cropH & 3;
        Rect zoomRect = new Rect( cropW, cropH, activeArraySize.width() - cropW, activeArraySize.height() - cropH );
        zoomHandlerListener.onZoomChanged( zoomRect );
    }

    @Override
    public boolean isPrepared() {
        return zoomHandlerListener != null && activeArraySize != null;
    }

    public interface IZoomHandlerListener {

        void onZoomChanged(Rect zoom);

    }
}