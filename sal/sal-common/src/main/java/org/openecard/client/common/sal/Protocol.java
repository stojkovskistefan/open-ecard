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

package org.openecard.client.common.sal;

import iso.std.iso_iec._24727.tech.schema.*;
import java.util.ArrayList;
import java.util.TreeMap;
import org.openecard.client.common.ECardConstants;
import org.openecard.client.common.WSHelper;


/**
 * Basic implementation of a SAL protocol.<br/>
 * Some protocols may need to override this implementation in order to control Secure Messaging or
 * provide a customized protocol flow.
 *
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public class Protocol {

    /** Object map to transport protocol specific parameters. Used when executing ProtocolStep. */
    protected final TreeMap<String,Object> internalData;
    /** List of ProtocolSteps, which are per default executed in order. */
    protected final ArrayList<ProtocolStep> steps;

    /** Index marking current step in the step list. */
    protected int curStep = 0;

    public Protocol() {
	this.internalData = new TreeMap<String, Object>();
	this.steps = new ArrayList<ProtocolStep>();
    }


    public TreeMap<String, Object> getInternalData() {
	return internalData;
    }

    private boolean hasNextStep() {
	return steps.size() > curStep;
    }

    public boolean hasNextStep(FunctionType functionName) {
	if (hasNextStep()) {
	    if (steps.get(curStep).getFunctionType() == functionName) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public boolean isFinished() {
	return ! hasNextStep();
    }


    /**
     * Get next step and advance counter.
     * @return next step or null if none exists.
     */
    private ProtocolStep next() {
	if (steps.size() > curStep) {
	    ProtocolStep step = steps.get(curStep);
	    curStep++;
	    return step;
	} else {
	    return null;
	}
    }

    private static ResponseType perform(Class responseClass, ProtocolStep step, RequestType request, TreeMap<String,Object> internalData) {
	// return not implemented result first
	if (step == null) {
	    return WSHelper.makeResponse(responseClass, WSHelper.makeResultError(ECardConstants.Minor.SAL.INAPPROPRIATE_PROTOCOL_FOR_ACTION, "There is no applicable protocol step at this point in the protocol flow."));
	} else {
	    return step.perform(request, internalData);
	}
    }

    public CardApplicationStartSessionResponse cardApplicationStartSession(CardApplicationStartSession param) {
	return (CardApplicationStartSessionResponse) perform(CardApplicationEndSessionResponse.class, next(), param, internalData);
    }

    public CardApplicationEndSessionResponse cardApplicationEndSession(CardApplicationEndSession param) {
	return (CardApplicationEndSessionResponse) perform(CardApplicationEndSessionResponse.class, next(), param, internalData);
    }

    public EncipherResponse encipher(Encipher param) {
	return (EncipherResponse) perform(EncipherResponse.class, next(), param, internalData);
    }

    public DecipherResponse decipher(Decipher param) {
	return (DecipherResponse) perform(DecipherResponse.class, next(), param, internalData);
    }

    public GetRandomResponse getRandom(GetRandom param) {
	return (GetRandomResponse) perform(GetRandomResponse.class, next(), param, internalData);
    }

    public HashResponse hash(Hash param) {
	return (HashResponse) perform(HashResponse.class, next(), param, internalData);
    }

    public SignResponse sign(Sign param) {
	return (SignResponse) perform(SignResponse.class, next(), param, internalData);
    }

    public VerifySignatureResponse verifySignature(VerifySignature param) {
	return (VerifySignatureResponse) perform(VerifySignatureResponse.class, next(), param, internalData);
    }

    public VerifyCertificateResponse verifyCertificate(VerifyCertificate param) {
	return (VerifyCertificateResponse) perform(VerifyCertificateResponse.class, next(), param, internalData);
    }

    public DIDCreateResponse didCreate(DIDCreate param) {
	return (DIDCreateResponse) perform(DIDCreateResponse.class, next(), param, internalData);
    }

    public DIDGetResponse didGet(DIDGet param) {
	return (DIDGetResponse) perform(DIDGetResponse.class, next(), param, internalData);
    }

    public DIDUpdateResponse didUpdate(DIDUpdate param) {
	return (DIDUpdateResponse) perform(DIDUpdateResponse.class, next(), param, internalData);
    }

    public DIDDeleteResponse didDelete(DIDDelete param) {
	return (DIDDeleteResponse) perform(DIDDeleteResponse.class, next(), param, internalData);
    }

    public DIDAuthenticateResponse didAuthenticate(DIDAuthenticate param) {
	return (DIDAuthenticateResponse) perform(DIDAuthenticateResponse.class, next(), param, internalData);
    }


    ///
    /// Secure Messaging functions
    /// Overwrite in subclass when needed
    ///

    public boolean needsSM() {
	return false;
    }

    public byte[] applySM(byte[] commandAPDU) {
	return commandAPDU;
    }
    public byte[] removeSM(byte[] responseAPDU) {
	return responseAPDU;
    }

}
