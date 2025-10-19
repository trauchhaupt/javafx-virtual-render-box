package de.vonrauchhaupt.javafx.virtualrenderbox.test;

import com.sun.prism.PixelFormat;
import de.vonrauchhaupt.javafx.virtualrenderbox.IVirtualRenderTextureFactory;
import de.vonrauchhaupt.javafx.virtualrenderbox.IVirtualRendererInput;
import de.vonrauchhaupt.javafx.virtualrenderbox.VirtualRendererType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class DiagonalTextureRenderer implements IVirtualRenderTextureFactory<String> {

    private static final VirtualRendererType TYPE = new VirtualRendererType("DiagonalTexture", String.class, PixelFormat.BYTE_RGB);

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
    public void render(IVirtualRendererInput<String> rendererInput, ByteBuffer buffer, int capacity, int width, int height) {
        int colorByRow = 0;
        int bytesPerRow = capacity / height;
        int iRow = 0;
        for (int i = 0; i < capacity; i++) {

            if (i % bytesPerRow == 0)
                iRow++;

            if (i > 0 &&
                    ((i - 1) % 3 == 0) &&
                    (iRow % 10 == 0))
                buffer.put(i, (byte) 255);
            else if (i % 60 == 0)
                buffer.put(i, (byte) 255);
            else if (i % 6 == 0)
                buffer.put(i, (byte) 125);
            else
                buffer.put(i, (byte) 0);



            /*byte data = 4;
            if (i % 12 == 0) {
                data += 100;
            }

            if ((i - 1) % (bytesPerRow * 20) == 0)
                colorByRow = 0;
            else if ((i - 1) % bytesPerRow  == 0)
                colorByRow += 10;

            buffer.put(i , (byte)(data + colorByRow));*/
        }
    }
}
