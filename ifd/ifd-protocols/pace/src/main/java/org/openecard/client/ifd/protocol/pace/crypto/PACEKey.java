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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.openecard.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.openecard.bouncycastle.crypto.params.ECDomainParameters;
import org.openecard.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.openecard.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.openecard.bouncycastle.crypto.params.ElGamalParameters;
import org.openecard.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.openecard.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.openecard.bouncycastle.jce.spec.ECParameterSpec;
import org.openecard.bouncycastle.jce.spec.ElGamalParameterSpec;
import org.openecard.bouncycastle.math.ec.ECPoint;
import org.openecard.client.common.logging.LoggingConstants;
import org.openecard.client.common.util.ByteUtils;
import org.openecard.client.ifd.protocol.pace.PACEImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 */
public final class PACEKey {

    private static final Logger logger = LoggerFactory.getLogger(PACEImplementation.class);
    private AsymmetricKeyParameter sk;
    private AsymmetricKeyParameter pk;
    private PACEDomainParameter pdp;

    /**
     * Creates a new key for PACE.
     *
     * @param pdp PACEDomainParameter
     */
    public PACEKey(PACEDomainParameter pdp) {
	this.pdp = pdp;
    }

    /**
     * Decodes a public key from a byte array.
     *
     * @param data Encoded key
     * @return Decoded key
     */
    public byte[] decodePublicKey(byte[] data) {
	byte[] keyBytes = ByteUtils.cut(data, 4, data.length - 4);

	if (pdp.isECDH()) {
	    ECParameterSpec p = (ECParameterSpec) pdp.getParameter();
	    ECDomainParameters ecp = new ECDomainParameters(p.getCurve(), p.getG(), p.getN(), p.getH());

	    ECPoint q = p.getCurve().decodePoint(keyBytes);
	    pk = new ECPublicKeyParameters(q, ecp);

	    return getEncodedPublicKey();
	} else if (pdp.isDH()) {
	    logger.error(LoggingConstants.INFO, "Not implemented yet.");
	    throw new UnsupportedOperationException("Not implemented yet.");
	} else {
	    throw new IllegalArgumentException();
	}
    }

    /**
     * Generate a key pair.
     */
    public void generateKeyPair() {
	if (pdp.isDH()) {
	    ElGamalParameterSpec p = (ElGamalParameterSpec) pdp.getParameter();
	    int numBits = p.getG().bitLength();
	    BigInteger d = new BigInteger(numBits, new SecureRandom());
	    ElGamalParameters egp = new ElGamalParameters(p.getP(), p.getG());

	    sk = new ElGamalPrivateKeyParameters(d, egp);
	    pk = new ElGamalPublicKeyParameters(egp.getG().multiply(d), egp);

	} else if (pdp.isECDH()) {
	    ECParameterSpec p = (ECParameterSpec) pdp.getParameter();
	    int numBits = p.getN().bitLength();
	    BigInteger d = new BigInteger(numBits, new SecureRandom());
	    ECDomainParameters ecp = new ECDomainParameters(p.getCurve(), p.getG(), p.getN(), p.getH());

	    sk = new ECPrivateKeyParameters(d, ecp);
	    pk = new ECPublicKeyParameters(ecp.getG().multiply(d), ecp);
	} else {
	    throw new IllegalArgumentException();
	}
    }

    /**
     * Returns the public key.
     *
     * @return Public key
     */
    public AsymmetricKeyParameter getPublicKey() {
	return pk;
    }

    /**
     * Returns the byte encoded public key.
     *
     * @return Public key
     */
    public byte[] getEncodedPublicKey() {
	if (pdp.isDH()) {
	    return ((ElGamalPublicKeyParameters) pk).getY().toByteArray();
	} else if (pdp.isECDH()) {
	    return ((ECPublicKeyParameters) pk).getQ().getEncoded();
	} else {
	    throw new IllegalArgumentException();
	}
    }

    /**
     * Returns the byte encoded compressed public key.
     *
     * @return Public key
     */
    public byte[] getEncodedCompressedPublicKey() {
	if (pdp.isDH()) {
	    try {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] input = ((ElGamalPublicKeyParameters) pk).getY().toByteArray();
		byte[] hash = md.digest(input);

		return hash;
	    } catch (NoSuchAlgorithmException ex) {
		// <editor-fold defaultstate="collapsed" desc="log exception">
		logger.error(LoggingConstants.THROWING, "Exception", ex);
		// </editor-fold>
		throw new RuntimeException(ex);
	    }
	} else if (pdp.isECDH()) {
	    return ((ECPublicKeyParameters) pk).getQ().getX().toBigInteger().toByteArray();
	} else {
	    throw new IllegalArgumentException();
	}
    }

    /**
     * Returns the private key.
     *
     * @return Private key
     */
    public AsymmetricKeyParameter getPrivateKey() {
	return sk;
    }

    /**
     * Returns the byte encoded private key.
     *
     * @return Private key
     */
    public byte[] getEncodedPrivateKey() {
	if (pdp.isDH()) {
	    return ((ElGamalPrivateKeyParameters) sk).getX().toByteArray();
	} else if (pdp.isECDH()) {
	    return ((ECPrivateKeyParameters) sk).getD().toByteArray();
	} else {
	    throw new IllegalArgumentException();
	}
    }
}
