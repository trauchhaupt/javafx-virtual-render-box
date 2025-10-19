package de.vonrauchhaupt.javafx.virtualrenderbox;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class NGVirtualRenderBox extends NGNode {

    private IVirtualRendererInput<?> rendererInput = null;
    private float width, height;

    @Override
    protected void renderContent(Graphics graphics) {
        int realWidth = (int) Math.floor(width * graphics.getPixelScaleFactorX());
        int realHeight = (int) Math.floor(height * graphics.getPixelScaleFactorY());

        PixelFormat format = rendererInput == null ? PixelFormat.BYTE_RGB : rendererInput.getType().pixelFormat();

        Texture tex = graphics.getResourceFactory().createTexture(format,
                Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED, realWidth, realHeight);
        IVirtualRenderTextureFactory factory = VirtualRenderTextureFactoryIndex.getFactoryFor(rendererInput);
        if (factory != null) {
            int bytesPerRow = realWidth * tex.getPixelFormat().getBytesPerPixelUnit();
            int capacity = bytesPerRow * realHeight;
            ByteBuffer tmpByteBuffer = ByteBuffer.allocate(capacity);
            factory.render(rendererInput, tmpByteBuffer, capacity, realWidth, realHeight);

            System.out.println("Creating texture with " + realWidth + "/" + realHeight + " at " + tex.getPixelFormat().getBytesPerPixelUnit());
            tex.update(tmpByteBuffer, format, 0, 0, 0, 0, realWidth, realHeight, bytesPerRow, false);
        }
        graphics.drawTexture(tex, 0, 0, width, height);
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
