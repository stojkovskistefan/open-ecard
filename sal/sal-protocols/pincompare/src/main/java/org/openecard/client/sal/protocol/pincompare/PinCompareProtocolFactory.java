/* Copyright 2012, Hochschule fuer angewandte Wissenschaften Coburg
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

package org.openecard.client.sal.protocol.pincompare;

import org.openecard.client.common.ECardConstants;
import org.openecard.client.common.sal.Protocol;
import org.openecard.client.common.sal.ProtocolFactory;
import org.openecard.client.gui.UserConsent;
import org.openecard.ws.IFD;
import org.openecard.ws.SAL;


/**
 *
 * @author Dirk Petrautzki <petrautzki@hs-coburg.de>
 */
public class PinCompareProtocolFactory implements ProtocolFactory {

    @Override
    public String getProtocol() {
	return ECardConstants.Protocol.PIN_COMPARE;
    }

    @Override
    public Protocol createInstance(SAL sal, IFD ifd, UserConsent gui) {
	return new PinCompareProtocol(ifd, sal);
    }

}
