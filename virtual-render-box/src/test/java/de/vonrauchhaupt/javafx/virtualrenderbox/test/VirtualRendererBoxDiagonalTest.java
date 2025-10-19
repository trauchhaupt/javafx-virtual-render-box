package de.vonrauchhaupt.javafx.virtualrenderbox.test;

import de.vonrauchhaupt.javafx.virtualrenderbox.IVirtualRendererInput;
import de.vonrauchhaupt.javafx.virtualrenderbox.VirtualRenderBox;
import de.vonrauchhaupt.javafx.virtualrenderbox.VirtualRenderTextureFactoryIndex;
import de.vonrauchhaupt.javafx.virtualrenderbox.VirtualRendererType;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

class VirtualRendererBoxDiagonalTest extends BaseJavaFXTest<VirtualRenderBox> {

    @Test
    void startSimpleImageView() throws Exception {
        super.startTest();
    }

    @Override
    boolean doTestOnTestNode(VirtualRenderBox testableNode) {
        return false;
    }

    @Override
    @NotNull
    VirtualRenderBox createTestNode() throws Exception {
        DiagonalTextureRenderer textureFactory = new DiagonalTextureRenderer();
        VirtualRenderTextureFactoryIndex.registerFactory(textureFactory);
        VirtualRenderBox virtualRenderBox = new VirtualRenderBox();
        virtualRenderBox.setWidth(200);
        virtualRenderBox.setHeight(200);
        virtualRenderBox.setInputData(new IVirtualRendererInput<String>() {
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
        return virtualRenderBox;
    }
}

