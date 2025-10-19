package de.vonrauchhaupt.javafx.virtualrenderbox;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;

import java.nio.IntBuffer;
import java.util.Map;
import java.util.WeakHashMap;

public class NGVirtualRenderBox extends NGNode {

    static final Map<String, Texture> textureCache = new WeakHashMap<>();
    private IVirtualRendererInput<?> rendererInput = null;
    private float width, height;
    private String lastImageKey = null;

    protected NGVirtualRenderBox() {
        super();
    }


    @Override
    protected void doRender(Graphics graphics) {
        graphics.setState3D(false);
        // save current depth test state
        boolean prevDepthTest = graphics.isDepthTest();

        // Apply Depth test for this node
        // (note that this will only be used if we have a depth buffer for the
        // surface to which we are rendering)
        graphics.setDepthTest(isDepthTest());

        // save current transform state
        BaseTransform prevXform = graphics.getTransformNoClone();

        double mxx = prevXform.getMxx();
        double mxy = prevXform.getMxy();
        double mxz = prevXform.getMxz();
        double mxt = prevXform.getMxt();

        double myx = prevXform.getMyx();
        double myy = prevXform.getMyy();
        double myz = prevXform.getMyz();
        double myt = prevXform.getMyt();

        double mzx = prevXform.getMzx();
        double mzy = prevXform.getMzy();
        double mzz = prevXform.getMzz();
        double mzt = prevXform.getMzt();

        BaseTransform scaleInstance = BaseTransform.getScaleInstance(1 / graphics.getAssociatedScreen().getPlatformScaleX(),
                1 / graphics.getAssociatedScreen().getPlatformScaleY());

        graphics.transform(scaleInstance);

        int realWidth = (int) Math.floor(width * graphics.getAssociatedScreen().getPlatformScaleX());
        int realHeight = (int) Math.floor(height * graphics.getAssociatedScreen().getPlatformScaleY());

        IVirtualRenderTextureFactory factory;
        lastImageKey = rendererInput.getCacheId() + "_" + realWidth + "_" + realHeight;
        Texture texture = textureCache.get(lastImageKey);
        if (texture != null) {
            graphics.drawTexture(texture, 0, 0, realWidth, realHeight);
        } else if ((factory = VirtualRenderTextureFactoryIndex.getFactoryFor(rendererInput)) != null) {
            PixelFormat format = rendererInput == null ? PixelFormat.INT_ARGB_PRE : rendererInput.getType().pixelFormat();
            texture = graphics.getResourceFactory().createTexture(format,
                    Texture.Usage.STATIC, Texture.WrapMode.CLAMP_NOT_NEEDED, realWidth, realHeight);
            int bytesPerRow = realWidth * texture.getPixelFormat().getBytesPerPixelUnit();
            int capacity = realWidth * realHeight;
            IntBuffer tmpByteBuffer = IntBuffer.allocate(capacity);
            factory.render(rendererInput, tmpByteBuffer, capacity, realWidth, realHeight);

            System.out.println("Creating texture with " + realWidth + "/" + realHeight + " at " + texture.getPixelFormat().getBytesPerPixelUnit());
            texture.update(tmpByteBuffer, format, 0, 0, 0, 0, realWidth, realHeight, bytesPerRow, false);
            graphics.drawTexture(texture, 0, 0, realWidth, realHeight);
        } else {
            graphics.clear(Color.TRANSPARENT);
        }

        // restore previous transform state
        graphics.setTransform3D(mxx, mxy, mxz, mxt,
                myx, myy, myz, myt,
                mzx, mzy, mzz, mzt);
        // restore previous depth test state
        graphics.setDepthTest(prevDepthTest);

    }

    @Override
    protected void renderContent(Graphics g) {

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
