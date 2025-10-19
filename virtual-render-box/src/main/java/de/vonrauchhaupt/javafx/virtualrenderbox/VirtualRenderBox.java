package de.vonrauchhaupt.javafx.virtualrenderbox;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.AbstractNode;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;

public class VirtualRenderBox extends AbstractNode {

    static {
        VirtualRenderBoxHelper.setVirtualRenderBoxAccessor(new VirtualRenderBoxHelper.VirtualRenderBoxAccessor() {
            @Override
            public NGNode doCreatePeer(Node node) {
                return ((VirtualRenderBox) node).doCreatePeer();
            }

            @Override
            public void doUpdatePeer(Node node) {
                ((VirtualRenderBox) node).doUpdatePeer();
            }

            @Override
            public BaseBounds doComputeGeomBounds(Node node,
                                                  BaseBounds bounds, BaseTransform tx) {
                return ((VirtualRenderBox) node).doComputeGeomBounds(bounds, tx);
            }

            @Override
            public boolean doComputeContains(Node node, double localX, double localY) {
                return ((VirtualRenderBox) node).doComputeContains(localX, localY);
            }
        });
    }

    private ObjectProperty<IVirtualRendererInput> inputData;
    private DoubleProperty width;
    private DoubleProperty height;

    {
        // To initialize the class helper at the begining each constructor of this class
        VirtualRenderBoxHelper.initHelper(this);
    }

    public VirtualRenderBox() {
        this(0, 0);
    }

    /**
     * Creates a new instance of Canvas with the given size.
     *
     * @param width  width of the canvas
     * @param height height of the canvas
     */
    public VirtualRenderBox(double width, double height) {
        setWidth(width);
        setHeight(height);
    }

    public IVirtualRendererInput getInputData() {
        return inputData == null ? null : inputData.get();
    }

    public void setInputData(IVirtualRendererInput inputData) {
        inputDataProperty().set(inputData);
    }

    public ObjectProperty<IVirtualRendererInput> inputDataProperty() {
        if (inputData == null) {
            inputData = new ObjectPropertyBase<>() {

                @Override
                public void invalidated() {
                    NodeHelper.markDirty(VirtualRenderBox.this, DirtyBits.NODE_CONTENTS);
                }

                @Override
                public Object getBean() {
                    return VirtualRenderBox.this;
                }

                @Override
                public String getName() {
                    return "inputData";
                }
            };
        }
        return inputData;
    }

    public final double getWidth() {
        return width == null ? 0.0 : width.get();
    }

    public final void setWidth(double value) {
        widthProperty().set(value);
    }

    public final DoubleProperty widthProperty() {
        if (width == null) {
            width = new DoublePropertyBase() {

                @Override
                public void invalidated() {
                    NodeHelper.markDirty(VirtualRenderBox.this, DirtyBits.NODE_GEOMETRY);
                    NodeHelper.geomChanged(VirtualRenderBox.this);
                }

                @Override
                public Object getBean() {
                    return VirtualRenderBox.this;
                }

                @Override
                public String getName() {
                    return "width";
                }
            };
        }
        return width;
    }

    public final double getHeight() {
        return height == null ? 0.0 : height.get();
    }

    public final void setHeight(double value) {
        heightProperty().set(value);
    }

    public final DoubleProperty heightProperty() {
        if (height == null) {
            height = new DoublePropertyBase() {

                @Override
                public void invalidated() {
                    NodeHelper.markDirty(VirtualRenderBox.this, DirtyBits.NODE_GEOMETRY);
                    NodeHelper.geomChanged(VirtualRenderBox.this);
                }

                @Override
                public Object getBean() {
                    return VirtualRenderBox.this;
                }

                @Override
                public String getName() {
                    return "height";
                }
            };
        }
        return height;
    }

    NGVirtualRenderBox doCreatePeer() {
        return new NGVirtualRenderBox();
    }

    private boolean doComputeContains(double localX, double localY) {
        double w = getWidth();
        double h = getHeight();
        return (w > 0 && h > 0 &&
                localX >= 0 && localY >= 0 &&
                localX < w && localY < h);
    }

    /*
     * Note: This method MUST only be called via its accessor method.
     */
    private BaseBounds doComputeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        bounds = new RectBounds(0f, 0f, (float) getWidth(), (float) getHeight());
        bounds = tx.transform(bounds, bounds);
        return bounds;
    }

    private void doUpdatePeer() {
        if (NodeHelper.isDirty(this, DirtyBits.NODE_GEOMETRY) ||
                NodeHelper.isDirty(this, DirtyBits.NODE_CONTENTS)) {
            NGVirtualRenderBox peer = NodeHelper.getPeer(this);
            peer.setRendererInput(getInputData(), (float) getWidth(), (float) getHeight());
        }
    }
}
