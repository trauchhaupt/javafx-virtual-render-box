package de.vonrauchhaupt.javafx.virtualrenderbox.test;

import com.sun.prism.PixelFormat;
import de.vonrauchhaupt.javafx.virtualrenderbox.IVirtualRenderTextureFactory;
import de.vonrauchhaupt.javafx.virtualrenderbox.IVirtualRendererInput;
import de.vonrauchhaupt.javafx.virtualrenderbox.VirtualRendererOutput;
import de.vonrauchhaupt.javafx.virtualrenderbox.VirtualRendererType;
import org.jetbrains.annotations.NotNull;

import java.nio.IntBuffer;

public class StripeTextureRenderer implements IVirtualRenderTextureFactory<String> {

    static final int COLOR1 = -234234;
    static final int COLOR2 = -123456;
    private static final VirtualRendererType<String> TYPE = new VirtualRendererType<>("DiagonalTexture", String.class, PixelFormat.INT_ARGB_PRE);

    @Override
    @NotNull
    public VirtualRendererType<String> getTypeOfInput() {
        return TYPE;
    }

    @Override
    public boolean isUsableForInput(@NotNull String object) {
        return true;
    }

    @Override
    public @NotNull VirtualRendererOutput render(IVirtualRendererInput<String> rendererInput, int width, int height) {
        int capacity = width * height;
        IntBuffer tmpByteBuffer = IntBuffer.allocate(width * height);
        for (int i = 0; i < capacity; i++) {
            tmpByteBuffer.put(i, (i % 2 == 0) ? COLOR1 : COLOR2);
        }
        return new VirtualRendererOutput(TYPE.pixelFormat(), tmpByteBuffer, width, height);
    }

    @Override
    public double calculateWidthOfHeight(double height) {
        return height;
    }

    @Override
    public double calculateHeightOfWidth(double width) {
        return width;
    }
}
