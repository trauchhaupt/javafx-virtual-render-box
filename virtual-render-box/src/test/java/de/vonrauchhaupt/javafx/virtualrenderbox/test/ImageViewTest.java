package de.vonrauchhaupt.javafx.virtualrenderbox.test;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

class ImageViewTest extends BaseJavaFXTest<ImageView> {
    private static double LENGTH_TO_TEST = 200; // must be even

    @Test
    void startSimpleImageView() throws Throwable {
        super.startTest();
    }

    @Override
    void doTestOnTestNode(ImageView testableNode) {
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

    @Override
    @NotNull
    ImageView createTestNode() throws Exception {
        WritableImage image = new WritableImage((int) LENGTH_TO_TEST, (int) LENGTH_TO_TEST);
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int curX = 0; curX < LENGTH_TO_TEST; curX++) {
            for (int curY = 0; curY < LENGTH_TO_TEST; curY++) {
                int curTotalPos = (curY * ((int) LENGTH_TO_TEST)) + curX;
                int expected = curTotalPos % 2 == 0 ? StripeTextureRenderer.COLOR1 : StripeTextureRenderer.COLOR2;
                pixelWriter.setArgb(curX, curY, expected);
            }
        }
        ImageView imageView = new ImageView(image);
        return imageView;
    }
}
