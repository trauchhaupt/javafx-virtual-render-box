package de.vonrauchhaupt.javafx.virtualrenderbox;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface IVirtualRenderTextureFactory<INPUT> {

    @NotNull
    VirtualRendererType<INPUT> getTypeOfInput();

    /**
     * There may be different texture factories for the same input.
     */
    boolean isUsableForInput(@NotNull INPUT input);

    void render(IVirtualRendererInput<INPUT> rendererInput, IntBuffer byteBuffer, int capacity, int width, int height);
}
