# javafx-virtual-render-box

This was a try out, if one could create pixel perfect images from renderings.

E.g. trying to display SVG graphics coming out from Batik or PDF renderings were always a bit blurry on displays with special display factors (such as 125%).

Sad to say there even was no way to render bitmap textures 1:1 in the Graphics object of a GNode.

So -> There seems to be no way to render virtual images pixel perfect on screens with a scale factor.
That makes a big minus point for javaFX, as even Swing had a way to do so.