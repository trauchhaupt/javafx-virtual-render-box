package de.vonrauchhaupt.javafx.virtualrenderbox.test;

import com.sun.prism.PixelFormat;
import de.vonrauchhaupt.javafx.virtualrenderbox.IVirtualRenderTextureFactory;
import de.vonrauchhaupt.javafx.virtualrenderbox.IVirtualRendererInput;
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
    public void render(IVirtualRendererInput<String> rendererInput, IntBuffer byteBuffer, int capacity, int width, int height) {
        for (int i = 0; i < capacity; i++) {
            byteBuffer.put(i, (i % 2 == 0) ? COLOR1 : COLOR2);
        }
    }
}
