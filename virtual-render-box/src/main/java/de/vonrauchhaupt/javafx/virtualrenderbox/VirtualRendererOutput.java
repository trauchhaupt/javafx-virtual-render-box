package de.vonrauchhaupt.javafx.virtualrenderbox;

import com.sun.prism.PixelFormat;

import java.nio.Buffer;

public record VirtualRendererOutput(
        PixelFormat pixelFormat,
        Buffer resultingImage,
        int width,
        int height
) {

}
