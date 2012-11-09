/****************************************************************************
 * Copyright (C) 2012 ecsec GmbH.
 * All rights reserved.
 * Contact: ecsec GmbH (info@ecsec.de)
 *
 * This file is part of the Open eCard App.
 *
 * GNU General Public License Usage
 * This file may be used under the terms of the GNU General Public
 * License version 3.0 as published by the Free Software Foundation
 * and appearing in the file LICENSE.GPL included in the packaging of
 * this file. Please review the following information to ensure the
 * GNU General Public License version 3.0 requirements will be met:
 * http://www.gnu.org/copyleft/gpl.html.
 *
 * Other Usage
 * Alternatively, this file may be used in accordance with the terms
 * and conditions contained in a signed written agreement between
 * you and ecsec GmbH.
 *
 ***************************************************************************/

package org.openecard.client.gui.graphics;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A collection of methods for creating images using classes generated by SVG2Java. <br /><br />
 *
 * SVG2Java is available at <a href="https://code.google.com/p/svg2java/">https://code.google.com/p/svg2java/</a>.
 *
 * @author Johannes Schmölz <johannes.schmoelz@ecsec.de>
 */
public class GraphicsUtil {

    private static final Logger logger = LoggerFactory.getLogger(GraphicsUtil.class);

    /**
     * Creates an image with the specified width and height. <br /><br />
     *
     * The class passed to this method <b>must</b> have been created using
     * <a href="https://code.google.com/p/svg2java/">SVG2Java</a>.
     *
     * @param clazz class generated by SVG2Java
     * @param imageWidth image width (must be > 0)
     * @param imageHeight image height (must be > 0)
     * @return java.awt.Image
     */
    public static Image createImage(Class<?> clazz, int imageWidth, int imageHeight) {
	return createImage(clazz, imageWidth, imageHeight, imageWidth, imageHeight, 0, 0);
    }

    /**
     * Creates an image with the specified width and height and paints it on a canvas.
     * Since the size of the canvas can differ from the size of the image, the position of the image within the canvas
     * must be specified via <code>posX</code> and <code>posY</code>, with <code>posX</code> specifying the X coordinate
     * and <code>posY</code> specifying the Y coordinate of the upper left corner of the image. <br /><br />
     *
     * <b>Warning:</b> Make sure that the image resides within the canvas, otherwise the image may be partly or
     * completely hidden. <br /><br />
     *
     * The class passed to this method <b>must</b> have been created using
     * <a href="https://code.google.com/p/svg2java/">SVG2Java</a>.
     *
     * @param clazz class generated by SVG2Java
     * @param imageWidth image width
     * @param imageHeight image height
     * @param canvasWidth canvas width (must be > 0)
     * @param canvasHeight canvas height (must be > 0)
     * @param posX X coordinate of the upper left corner of the image
     * @param posY Y coordinate of the upper left corner of the image
     * @return java.awt.Image
     */
    public static Image createImage(Class<?> clazz, int imageWidth, int imageHeight, int canvasWidth, int canvasHeight, int posX, int posY) {
	BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
	Graphics g = image.createGraphics();

	// The methods used in the following try-catch-block throw a lot of different exceptions, which should be
	// handled separately. If an exception is thrown, regardless of its specific type, the result is always the
	// same: the image doesn't get painted onto the canvas. Hence catching Exception is acceptable.
	try {
	    Object svgIcon = clazz.newInstance();

	    Method method = clazz.getMethod("setDimension", Dimension.class);
	    method.invoke(svgIcon, new Dimension(imageWidth, imageHeight));

	    method = clazz.getMethod("paintIcon", Component.class, Graphics.class, int.class, int.class);
	    method.invoke(svgIcon, null, g, posX, posY);

	} catch (Exception ex) {
	    logger.error(ex.getMessage(), ex);
	}

	return image;
    }

}
