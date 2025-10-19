package de.vonrauchhaupt.javafx.virtualrenderbox;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class VirtualRenderTextureFactoryIndex {
    private static Map<String, IVirtualRenderTextureFactory> textureFactories = new HashMap<>();
    private static Map<Class<?>, List<IVirtualRenderTextureFactory>> textureFactoriesByInput = new HashMap<>();

    private VirtualRenderTextureFactoryIndex() {
        // only static methods
    }

    public static void registerFactory(@NotNull IVirtualRenderTextureFactory textureFactory) {
        IVirtualRenderTextureFactory iVirtualRenderTextureFactory = textureFactories.get(textureFactory.getTypeOfInput().id());
        // already registered and identical.. nothing to do
        if (iVirtualRenderTextureFactory != null && iVirtualRenderTextureFactory.equals(textureFactory)) {
            return;
        } else if (iVirtualRenderTextureFactory != null) {
            throw new RuntimeException("A texture factory for " + textureFactory.getTypeOfInput() + " was already registered.");
        }

        textureFactories.put(textureFactory.getTypeOfInput().id(), textureFactory);
        textureFactoriesByInput.computeIfAbsent(textureFactory.getTypeOfInput().inputType(),
                        x -> new LinkedList<>())
                .add(textureFactory);
    }

    public static <INPUT> IVirtualRenderTextureFactory<INPUT> getFactoryFor(IVirtualRendererInput<INPUT> input) {
        if (input == null)
            return null;
        List<IVirtualRenderTextureFactory> tmpFactories = textureFactoriesByInput.get(input.getType().inputType());
        if (tmpFactories == null || tmpFactories.isEmpty())
            throw new IllegalArgumentException("There is no texturefactory registered for " + input.getType().inputType() +
                    ". Please register with VirtualRenderTextureFactoryIndex.registerFactory() first.");
        List<IVirtualRenderTextureFactory> propertTextureFactories = tmpFactories.stream()
                .filter(x -> x.isUsableForInput(input.getData()))
                .toList();

        if (tmpFactories.isEmpty())
            throw new IllegalArgumentException("There is no texturefactory that is suitable for the given data '" + input.getData() + "'.");
        if (tmpFactories.size() > 1)
            throw new IllegalArgumentException("There is are multiple texturefactories (" +
                    propertTextureFactories.stream()
                            .map(x -> x.getClass().getName())
                    + ") that are suitable for the given data '" + input.getData() + "'.");

        return tmpFactories.get(0);
    }

}
