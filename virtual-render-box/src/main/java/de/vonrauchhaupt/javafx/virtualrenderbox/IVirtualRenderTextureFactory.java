package de.vonrauchhaupt.javafx.virtualrenderbox;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface IVirtualRenderTextureFactory<INPUT> {

    @NotNull
    VirtualRendererType<INPUT> getTypeOfInput();

    /**
     * There may be different texture factories for the same input.
     */
    boolean isUsableForInput(@NotNull INPUT input);

    void render(IVirtualRendererInput<INPUT> rendererInput, ByteBuffer byteBuffer, int capacity, int width, int height);
}
