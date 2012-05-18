/*
 * Copyright 2012 Moritz Horsch.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openecard.client.ifd.protocol.pace.crypto;

import org.openecard.client.crypto.common.asn1.eac.PACEDomainParameter;



/**
 * Implements the Integrated Mapping for PACE.
 * See BSI-TR-03110, version 2.10, part 3, section A.3.5.2.
 *
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 */
public final class PACEIntegratedMapping extends PACEMapping {

    /**
     * Creates an new integrated mapping for PACE.
     *
     * @param pdp PACEDomainParameter
     */
    public PACEIntegratedMapping(PACEDomainParameter pdp) {
        super(pdp);
    }

    @Override
    public PACEDomainParameter map(byte[] keyPICC, byte[] keyPCD) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
