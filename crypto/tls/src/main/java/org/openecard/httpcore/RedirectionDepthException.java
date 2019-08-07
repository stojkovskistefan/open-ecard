/****************************************************************************
 * Copyright (C) 2019 ecsec GmbH.
 * All rights reserved.
 * Contact: ecsec GmbH (info@ecsec.de)
 *
 * This file may be used in accordance with the terms and conditions
 * contained in a signed written agreement between you and ecsec GmbH.
 *
 ***************************************************************************/

package org.openecard.httpcore;


/**
 * Exception indicating an abortion after too many redirects.
 *
 * @author Tobias Wich
 */
public class RedirectionDepthException extends HttpResourceException {

    public RedirectionDepthException(String msg) {
	super(msg);
    }

}
