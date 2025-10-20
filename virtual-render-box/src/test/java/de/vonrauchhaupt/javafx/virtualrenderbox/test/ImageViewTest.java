package de.vonrauchhaupt.javafx.virtualrenderbox.test;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

class ImageViewTest extends BaseJavaFXTest<ImageView> {

    @Test
    void startSimpleImageView() throws Exception {
        super.startTest();
    }

    @Override
    void doTestOnTestNode(ImageView testableNode) {

    }

    @Override
    @NotNull
    ImageView createTestNode() throws Exception {
        try (InputStream is = ImageViewTest.class.getResourceAsStream("/de/vonrauchhaupt/javafx/virtualrenderbox/test/exampleJpg.jpg")) {
            Image image = new Image(is);
            ImageView imageView = new ImageView(image);
            return imageView;
        }
    }
}
