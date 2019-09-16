/** **************************************************************************
 * Copyright (C) 2019 ecsec GmbH.
 * All rights reserved.
 * Contact: ecsec GmbH (info@ecsec.de)
 *
 * This file may be used in accordance with the terms and conditions
 * contained in a signed written agreement between you and ecsec GmbH.
 *
 ************************************************************************** */
package org.openecard.mobile.activation.common;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Neil Crossley
 */
public class SanityCheckTests {

    @Test
    void checkEquivalanceOfQueryInUriAndUrl() throws URISyntaxException, MalformedURLException {

	final String input = "http://www.example.com?url=www%2Eotherexample%2Ecom%3Fb%3D2%26c%3D3&d=4";
	URI uri = new URI(input);
	URL url = new URL(input);

	Assert.assertEquals(uri.getRawQuery(), url.getQuery());
    }
}
