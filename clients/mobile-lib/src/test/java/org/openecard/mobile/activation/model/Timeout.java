/** **************************************************************************
 * Copyright (C) 2019 ecsec GmbH.
 * All rights reserved.
 * Contact: ecsec GmbH (info@ecsec.de)
 *
 * This file may be used in accordance with the terms and conditions
 * contained in a signed written agreement between you and ecsec GmbH.
 *
 ************************************************************************** */
package org.openecard.mobile.activation.model;

import java.lang.management.ManagementFactory;

/**
 *
 * @author Neil Crossley
 */
public final class Timeout {

    public static final int MIN_WAIT_TIMEOUT = 1000;
    public static final int WAIT_TIMEOUT = isDebug() ? Integer.MAX_VALUE : MIN_WAIT_TIMEOUT;

    private Timeout() {

    }

    static boolean isDebug() {
	for (String arg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
	    if (arg.contains("jdwp=")) {
		return true;
	    }
	};
	return false;
    }

}
