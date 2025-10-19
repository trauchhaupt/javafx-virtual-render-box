package de.vonrauchhaupt.javafx.virtualrenderbox;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;

import java.nio.IntBuffer;

public class NGVirtualRenderBox extends NGNode {

    private IVirtualRendererInput<?> rendererInput = null;
    private float width, height;

    protected NGVirtualRenderBox() {
        super();
    }

    @Override
    protected void renderContent(Graphics graphics) {
        int realWidth = (int) Math.floor(width * graphics.getAssociatedScreen().getPlatformScaleX());
        int realHeight = (int) Math.floor(height * graphics.getAssociatedScreen().getPlatformScaleY());
        //int realWidth = (int) Math.floor(width);
        //int realHeight = (int) Math.floor(height);

        IVirtualRenderTextureFactory factory = VirtualRenderTextureFactoryIndex.getFactoryFor(rendererInput);

        if (factory != null) {
            PixelFormat format = rendererInput == null ? PixelFormat.INT_ARGB_PRE : rendererInput.getType().pixelFormat();
            Texture tex = graphics.getResourceFactory().createTexture(format,
                    Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED, realWidth, realHeight);
            int bytesPerRow = realWidth * tex.getPixelFormat().getBytesPerPixelUnit();
            int capacity = realWidth * realHeight;
            IntBuffer tmpByteBuffer = IntBuffer.allocate(capacity);
            factory.render(rendererInput, tmpByteBuffer, capacity, realWidth, realHeight);

            System.out.println("Creating texture with " + realWidth + "/" + realHeight + " at " + tex.getPixelFormat().getBytesPerPixelUnit());
            tex.update(tmpByteBuffer, format, 0, 0, 0, 0, realWidth, realHeight, bytesPerRow, false);
            graphics.drawTexture(tex, 0, 0, width, height);
        } else {
            graphics.clear(Color.TRANSPARENT);
        }
    }


    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    public void setRendererInput(IVirtualRendererInput rendererInput, float width, float height) {
        this.rendererInput = rendererInput;
        this.width = width;
        this.height = height;
        geometryChanged();
    }
}
