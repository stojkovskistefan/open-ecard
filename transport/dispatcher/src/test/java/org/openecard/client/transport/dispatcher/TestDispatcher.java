/*
 * Copyright 2012 Tobias Wich ecsec GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openecard.client.transport.dispatcher;

import iso.std.iso_iec._24727.tech.schema.EstablishContext;
import iso.std.iso_iec._24727.tech.schema.EstablishContextResponse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.openecard.client.common.ClientEnv;
import org.openecard.client.common.interfaces.Environment;
import org.openecard.ws.IFD;


/**
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public class TestDispatcher {

    @Test
    public void testDispatcher1() throws Exception {
	// test with direct annotation with explicit class specification
	IFD ifd = new TestIFD();
	Environment env = new TestEnv1();
	MessageDispatcher disp = new MessageDispatcher(env);

	env.setIFD(ifd);

	Object req = new EstablishContext();
	Object res = disp.deliver(req);

	assertTrue(res instanceof EstablishContextResponse);
    }

    @Test
    public void testDispatcher2() throws Exception {
	// test with direct annotation without explicit class specification
	IFD ifd = new TestIFD();
	Environment env = new TestEnv2();
	MessageDispatcher disp = new MessageDispatcher(env);

	env.setIFD(ifd);

	Object req = new EstablishContext();
	Object res = disp.deliver(req);

	assertTrue(res instanceof EstablishContextResponse);
    }

    @Test
    public void testDispatcher3() throws Exception {
	// test with inherited annotation without explicit class specification
	IFD ifd = new TestIFD();
	Environment env = new TestEnv3();
	MessageDispatcher disp = new MessageDispatcher(env);

	env.setIFD(ifd);

	Object req = new EstablishContext();
	Object res = disp.deliver(req);

	assertTrue(res instanceof EstablishContextResponse);
    }

}
