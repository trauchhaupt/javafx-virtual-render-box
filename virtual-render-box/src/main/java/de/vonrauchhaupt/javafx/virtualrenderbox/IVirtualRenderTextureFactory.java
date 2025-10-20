package de.vonrauchhaupt.javafx.virtualrenderbox;

import org.jetbrains.annotations.NotNull;

public interface IVirtualRenderTextureFactory<INPUT> {

    @NotNull
    VirtualRendererType<INPUT> getTypeOfInput();

    /**
     * There may be different texture factories for the same input.
     */
    boolean isUsableForInput(@NotNull INPUT input);

    @NotNull
    VirtualRendererOutput render(IVirtualRendererInput<INPUT> rendererInput, int width, int height);

    double calculateWidthOfHeight(double height);

    double calculateHeightOfWidth(double width);

}
