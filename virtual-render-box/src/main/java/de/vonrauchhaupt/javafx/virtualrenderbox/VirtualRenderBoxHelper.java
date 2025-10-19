package de.vonrauchhaupt.javafx.virtualrenderbox;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;

public class VirtualRenderBoxHelper extends NodeHelper {
    private static final VirtualRenderBoxHelper theInstance;

    private static VirtualRenderBoxHelper.VirtualRenderBoxAccessor virtualRenderBoxAccessor;

    static {
        theInstance = new VirtualRenderBoxHelper();
        Utils.forceInit(Canvas.class);
    }

    static VirtualRenderBoxHelper getInstance() {
        return theInstance;
    }

    public static void initHelper(VirtualRenderBox virtualRenderBox) {
        setHelper(virtualRenderBox, getInstance());
    }

    @Override
    protected NGNode createPeerImpl(Node node) {
        return virtualRenderBoxAccessor.doCreatePeer(node);
    }

    @Override
    protected void updatePeerImpl(Node node) {
        super.updatePeerImpl(node);
        virtualRenderBoxAccessor.doUpdatePeer(node);
    }

    @Override
    protected BaseBounds computeGeomBoundsImpl(Node node, BaseBounds bounds,
                                               BaseTransform tx) {
        return virtualRenderBoxAccessor.doComputeGeomBounds(node, bounds, tx);
    }

    @Override
    protected boolean computeContainsImpl(Node node, double localX, double localY) {
        return virtualRenderBoxAccessor.doComputeContains(node, localX, localY);
    }

    public static void setVirtualRenderBoxAccessor(final VirtualRenderBoxHelper.VirtualRenderBoxAccessor newAccessor) {
        if (virtualRenderBoxAccessor != null) {
            throw new IllegalStateException();
        }

        virtualRenderBoxAccessor = newAccessor;
    }

    public interface VirtualRenderBoxAccessor {
        NGNode doCreatePeer(Node node);

        void doUpdatePeer(Node node);

        BaseBounds doComputeGeomBounds(Node node, BaseBounds bounds, BaseTransform tx);

        boolean doComputeContains(Node node, double localX, double localY);
    }
}
