package de.vonrauchhaupt.javafx.virtualrenderbox;

import org.jetbrains.annotations.NotNull;

public interface IVirtualRendererInput<INPUT> {
    @NotNull
    VirtualRendererType<INPUT> getType();

    @NotNull
    INPUT getData();

    @NotNull
    String getCacheId();
}
