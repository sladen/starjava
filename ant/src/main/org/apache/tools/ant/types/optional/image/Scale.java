/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.types.optional.image;

import org.apache.tools.ant.types.EnumeratedAttribute;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

/**
 *
 * @see org.apache.tools.ant.taskdefs.optional.image.Image
 */
public class Scale extends TransformOperation implements DrawOperation {
    private static final int HUNDRED = 100;

    private String widthStr = "100%";
    private String heightStr = "100%";
    private boolean xPercent = true;
    private boolean yPercent = true;
    private String proportions = "ignore";

    /** Enumerated class for proportions attribute. */
    public static class ProportionsAttribute extends EnumeratedAttribute {
        /** {@inheritDoc}. */
        public String[] getValues() {
            return new String[] {"ignore", "width", "height", "cover", "fit"};
        }
    }

    /**
     *  Sets the behaviour regarding the image proportions.
     * @param pa the enumerated value.
     */
    public void setProportions(ProportionsAttribute pa) {
        proportions = pa.getValue();
    }

    /**
     * Sets the width of the image, either as an integer or a %.
     * Defaults to 100%.
     * @param width the value to use.
     */
    public void setWidth(String width) {
        widthStr = width;
    }

    /**
     *  Sets the height of the image, either as an integer or a %.  Defaults to 100%.
     * @param height the value to use.
     */
    public void setHeight(String height) {
        heightStr = height;
    }

    /**
     * Get the width.
     * @return the value converted from the width string.
     */
    public float getWidth() {
        float width = 0.0F;
        int percIndex = widthStr.indexOf('%');
        if (percIndex > 0) {
            width = Float.parseFloat(widthStr.substring(0, percIndex));
            xPercent = true;
            return width / HUNDRED;
        } else {
            xPercent = false;
            return Float.parseFloat(widthStr);
        }
    }

    /**
     * Get the height.
     * @return the value converted from the height string.
     */
    public float getHeight() {
        int percIndex = heightStr.indexOf('%');
        if (percIndex > 0) {
            float height = Float.parseFloat(heightStr.substring(0, percIndex));
            yPercent = true;
            return height / HUNDRED;
        } else {
            yPercent = false;
            return Float.parseFloat(heightStr);
        }
    }

    /**
     * Scale an image.
     * @param image the image to scale.
     * @return the scaled image.
     */
    public PlanarImage performScale(PlanarImage image) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(image);
        float xFl = getWidth();
        float yFl = getHeight();

        if (!xPercent) {
            xFl = (xFl / image.getWidth());
        }
        if (!yPercent) {
            yFl = (yFl / image.getHeight());
        }

        if ("width".equals(proportions)) {
            yFl = xFl;
        } else if ("height".equals(proportions)) {
            xFl = yFl;
        } else if ("fit".equals(proportions)) {
            yFl = Math.min(xFl, yFl);
            xFl = yFl;
        } else if ("cover".equals(proportions)) {
            yFl = Math.max(xFl, yFl);
            xFl = yFl;
        }

        pb.add(new Float(xFl));
        pb.add(new Float(yFl));

        log("\tScaling to " + (xFl * HUNDRED) + "% x "
            + (yFl * HUNDRED) + "%");

        return JAI.create("scale", pb);
    }


    /** {@inheritDoc}. */
    public PlanarImage executeTransformOperation(PlanarImage image) {
        BufferedImage bi = null;
        for (int i = 0; i < instructions.size(); i++) {
            ImageOperation instr = ((ImageOperation) instructions.elementAt(i));
            if (instr instanceof DrawOperation) {
                return performScale(image);
            } else if (instr instanceof TransformOperation) {
                bi = image.getAsBufferedImage();
                image = ((TransformOperation) instr)
                    .executeTransformOperation(PlanarImage.wrapRenderedImage(bi));
                bi = image.getAsBufferedImage();
            }
        }
        return performScale(image);
    }


    /** {@inheritDoc}. */
    public PlanarImage executeDrawOperation() {
        for (int i = 0; i < instructions.size(); i++) {
            ImageOperation instr = ((ImageOperation) instructions.elementAt(i));
            if (instr instanceof DrawOperation) {
                PlanarImage image = null;
                // If this TransformOperation has DrawOperation children
                // then Rotate the first child and return.
                performScale(image);
                return image;
            }
        }
        return null;
    }
}
