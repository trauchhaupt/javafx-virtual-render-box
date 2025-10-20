package de.vonrauchhaupt.javafx.virtualrenderbox.test;

import de.vonrauchhaupt.javafx.virtualrenderbox.IVirtualRendererInput;
import de.vonrauchhaupt.javafx.virtualrenderbox.VirtualRenderBox;
import de.vonrauchhaupt.javafx.virtualrenderbox.VirtualRenderTextureFactoryIndex;
import de.vonrauchhaupt.javafx.virtualrenderbox.VirtualRendererType;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

class VirtualRendererBoxStripeTest extends BaseJavaFXTest<VirtualRenderBox> {
    private static double LENGTH_TO_TEST = 200; // must be even

    @Test
    void startSimpleImageView() throws Throwable {
        super.startTest();
    }

    @Override
    @NotNull
    VirtualRenderBox createTestNode() throws Exception {

        Assumptions.assumeTrue(((int) LENGTH_TO_TEST) % 2 == 0, "The LENGTH_TO_TEST must be even, but was " + LENGTH_TO_TEST);

        StripeTextureRenderer textureFactory = new StripeTextureRenderer();
        VirtualRenderTextureFactoryIndex.registerFactory(textureFactory);
        VirtualRenderBox virtualRenderFixWidthBox = new VirtualRenderBox();
        virtualRenderFixWidthBox.setWidth(LENGTH_TO_TEST);
        virtualRenderFixWidthBox.setHeight(LENGTH_TO_TEST);
        virtualRenderFixWidthBox.setInputData(new IVirtualRendererInput<String>() {
            @Override
            @NotNull
            public VirtualRendererType getType() {
                return textureFactory.getTypeOfInput();
            }

            @Override
            @NotNull
            public String getData() {
                return "AnyStringWorks";
            }

            @Override
            @NotNull
            public String getCacheId() {
                return "DiagonalTextureRenderer";
            }
        });
        return virtualRenderFixWidthBox;
    }

    @Override
    void doTestOnTestNode(VirtualRenderBox testableNode) {

        // Test demonstrates it's work only on a scale factor not being 1
        Assumptions.assumeFalse(testableNode.getScene().getWindow().getOutputScaleX() == 1);
        Assumptions.assumeFalse(testableNode.getScene().getWindow().getOutputScaleY() == 1);
        Assumptions.assumeTrue(testableNode.getScene().getWindow().getOutputScaleY() ==
                testableNode.getScene().getWindow().getOutputScaleX());

        SnapshotParameters params = new SnapshotParameters();
        Scale scaleInstance = new Scale(testableNode.getScene().getWindow().getOutputScaleX(),
                testableNode.getScene().getWindow().getOutputScaleY());
        params.setTransform(scaleInstance);
        WritableImage snapshot = testableNode.snapshot(params, null);
        Assertions.assertNotNull(snapshot);

        Assertions.assertEquals(LENGTH_TO_TEST * testableNode.getScene().getWindow().getOutputScaleX(), snapshot.getWidth());
        Assertions.assertEquals(LENGTH_TO_TEST * testableNode.getScene().getWindow().getOutputScaleY(), snapshot.getHeight());

        PixelReader pixelReader = snapshot.getPixelReader();
        for (int curX = 0; curX < snapshot.getWidth(); curX++) {
            for (int curY = 0; curY < snapshot.getHeight(); curY++) {
                int argb = pixelReader.getArgb(curX, curY);
                int curTotalPos = (curY * ((int) snapshot.getWidth())) + curX;
                int expected = curTotalPos % 2 == 0 ? StripeTextureRenderer.COLOR1 : StripeTextureRenderer.COLOR2;
                Assertions.assertEquals(expected, argb,
                        "Wrong color on pixel " + curX + "/" + curY);
            }
        }
    }

}

