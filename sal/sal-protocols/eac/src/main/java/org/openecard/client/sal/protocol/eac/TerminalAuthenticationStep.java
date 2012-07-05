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

package org.openecard.client.sal.protocol.eac;

import iso.std.iso_iec._24727.tech.schema.DIDAuthenticate;
import iso.std.iso_iec._24727.tech.schema.DIDAuthenticateResponse;
import java.util.Map;
import org.openecard.client.common.WSHelper;
import org.openecard.client.common.interfaces.Dispatcher;
import org.openecard.client.common.logging.LoggingConstants;
import org.openecard.client.common.sal.FunctionType;
import org.openecard.client.common.sal.ProtocolStep;
import org.openecard.client.crypto.common.asn1.cvc.CardVerifiableCertificateChain;
import org.openecard.client.crypto.common.asn1.eac.CADomainParameter;
import org.openecard.client.crypto.common.asn1.eac.CASecurityInfos;
import org.openecard.client.crypto.common.asn1.eac.SecurityInfos;
import org.openecard.client.crypto.common.asn1.eac.ef.EFCardAccess;
import org.openecard.client.crypto.common.asn1.utils.ObjectIdentifierUtils;
import org.openecard.client.sal.protocol.eac.anytype.EAC2InputType;
import org.openecard.client.sal.protocol.eac.anytype.EAC2OutputType;
import org.openecard.client.sal.protocol.eac.crypto.CAKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements TerminalAuthentication protocol step according to BSI-TR-03112-7.
 * See BSI-TR-03112, version 1.1.2, part 7, section 4.6.6.
 *
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 * @author Dirk Petrautzki <petrautzki@hs-coburg.de>
 */
public class TerminalAuthenticationStep implements ProtocolStep<DIDAuthenticate, DIDAuthenticateResponse> {

    private static final Logger logger = LoggerFactory.getLogger(TerminalAuthenticationStep.class.getName());
    private Dispatcher dispatcher;

    /**
     * Creates a new Terminal Authentication protocol step.
     *
     * @param dispatcher Dispatcher
     */
    public TerminalAuthenticationStep(Dispatcher dispatcher) {
	this.dispatcher = dispatcher;
    }

    @Override
    public FunctionType getFunctionType() {
	return FunctionType.DIDAuthenticate;
    }

    @Override
    public DIDAuthenticateResponse perform(DIDAuthenticate didAuthenticate, Map<String, Object> internalData) {
	DIDAuthenticateResponse response = new DIDAuthenticateResponse();
	byte[] slotHandle = didAuthenticate.getConnectionHandle().getSlotHandle();

	try {
	    EAC2InputType eac2Input = new EAC2InputType(didAuthenticate.getAuthenticationProtocolData());
	    EAC2OutputType eac2Output = eac2Input.getOutputType();

	    TerminalAuthentication ta = new TerminalAuthentication(dispatcher, slotHandle);

	    // TA: Step 1 - Verify certificates
	    CardVerifiableCertificateChain certificateChain = (CardVerifiableCertificateChain) internalData.get(EACConstants.INTERNAL_DATA_CERTIFICATES);
	    byte[] currentCAR = (byte[]) internalData.get(EACConstants.CURRENT_CAR);
	    ta.verifyCertificates(certificateChain, currentCAR);

	    // TA: Step 2 - MSE:SET AT
	    SecurityInfos securityInfos = (SecurityInfos) internalData.get(EACConstants.INTERNAL_DATA_SECURITY_INFOS);

	    byte[] oid = ObjectIdentifierUtils.getValue(certificateChain.getTerminalCertificate().getPublicKey().getObjectIdentifier());
	    byte[] chr = certificateChain.getTerminalCertificate().getCHR().toByteArray();
	    byte[] key = eac2Input.getEphemeralPublicKey();
	    byte[] aad = (byte[]) internalData.get(EACConstants.INTERNAL_DATA_AUTHENTICATED_AUXILIARY_DATA);

	    // Calculate comp(key)
	    EFCardAccess efca = new EFCardAccess(securityInfos);
	    CASecurityInfos cas = efca.getCASecurityInfos();
	    CADomainParameter cdp = new CADomainParameter(cas);
	    CAKey caKey = new CAKey(cdp);
	    caKey.decodePublicKey(key);
	    byte[] compKey = caKey.getEncodedCompressedPublicKey();

	    ta.mseSetAT(oid, chr, compKey, aad);

	    // TA: Step 3 - Get challenge
	    byte[] rPICC = ta.getChallenge();

	    // Store public key for Chip Authentication
	    internalData.put(EACConstants.INTERNAL_DATA_PK_PCD, eac2Input.getEphemeralPublicKey());

	    // Create response
	    eac2Output.setChallenge(rPICC);

	    response.setResult(WSHelper.makeResultOK());
	    response.setAuthenticationProtocolData(eac2Output.getAuthDataType());

	    return response;
	} catch (Exception e) {
	    // <editor-fold defaultstate="collapsed" desc="log exception">
	    logger.error(LoggingConstants.THROWING, "Exception", e);
	    // </editor-fold>
	    response.setResult(WSHelper.makeResultUnknownError(e.getMessage()));
	}

	return response;
    }

}
