# javafx-virtual-render-box

This is a POC for a pixel perfect component of virtual renderings.

Usually, the ImageView or the Canvas render virtual pixels. The virtual pixels are mapped to the physical pixels by the scaling factor set by the operating system.

If you do not have fix pixel images, but algorithm, that can generate the output at any pixel size, your result becomes blurry needlessly.

Therefore, this components calculates the real size of physical pixels and uses the synthetically generated image as basis.

# Future usages
In the future, the following components are planned:
* SVGImageView
* PDFImageView