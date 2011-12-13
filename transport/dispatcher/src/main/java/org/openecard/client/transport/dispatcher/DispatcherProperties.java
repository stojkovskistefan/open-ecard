package org.openecard.client.transport.dispatcher;

import org.openecard.client.common.OverridingProperties;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public class DispatcherProperties {

    private static class Internal extends OverridingProperties {
	public Internal() throws IOException {
	    super("dispatcher.properties");
	}
    }

    static {
	try {
	    properties = new Internal();
	} catch (IOException ex) {
	    // in that case a null pointer occurs when properties is accessed
	    Logger.getLogger(DispatcherProperties.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    private static Internal properties;


    public static String getProperty(String key) {
	return properties.getProperty(key);
    }

    public static Object setProperty(String key, String value) {
	return properties.setProperty(key, value);
    }

    public static Properties properties() {
	return properties.properties();
    }

}
