package de.vonrauchhaupt.javafx.virtualrenderbox;

import com.sun.prism.PixelFormat;

import java.util.Objects;

public record VirtualRendererType<INPUT_TYPE>
        (
                /*
                 * A short describing ID that tells, which image format it is used for
                 */
                String id,
                /**
                 * The type of input. e.g. file, String, etc.
                 */
                Class<INPUT_TYPE> inputType,

                PixelFormat pixelFormat
        ) {

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        VirtualRendererType<?> that = (VirtualRendererType<?>) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}