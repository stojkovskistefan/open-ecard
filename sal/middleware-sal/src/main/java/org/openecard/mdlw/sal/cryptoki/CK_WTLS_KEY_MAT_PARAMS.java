package org.openecard.mdlw.sal.cryptoki;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * <i>native declaration : pkcs11_v2.40/pkcs11t.h</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class CK_WTLS_KEY_MAT_PARAMS extends Structure {
	/** C type : CK_MECHANISM_TYPE */
	public long DigestMechanism;
	public long getDigestMechanism() {
		return DigestMechanism;
	}
	public void setDigestMechanism(long DigestMechanism) {
		this.DigestMechanism = DigestMechanism;
	}
	/** C type : CK_ULONG */
	public long ulMacSizeInBits;
	public long getUlMacSizeInBits() {
		return ulMacSizeInBits;
	}
	public void setUlMacSizeInBits(long ulMacSizeInBits) {
		this.ulMacSizeInBits = ulMacSizeInBits;
	}
	/** C type : CK_ULONG */
	public long ulKeySizeInBits;
	public long getUlKeySizeInBits() {
		return ulKeySizeInBits;
	}
	public void setUlKeySizeInBits(long ulKeySizeInBits) {
		this.ulKeySizeInBits = ulKeySizeInBits;
	}
	/** C type : CK_ULONG */
	public long ulIVSizeInBits;
	public long getUlIVSizeInBits() {
		return ulIVSizeInBits;
	}
	public void setUlIVSizeInBits(long ulIVSizeInBits) {
		this.ulIVSizeInBits = ulIVSizeInBits;
	}
	/** C type : CK_ULONG */
	public long ulSequenceNumber;
	public long getUlSequenceNumber() {
		return ulSequenceNumber;
	}
	public void setUlSequenceNumber(long ulSequenceNumber) {
		this.ulSequenceNumber = ulSequenceNumber;
	}
	/** C type : CK_BBOOL */
	public byte bIsExport;
	public byte getBIsExport() {
		return bIsExport;
	}
	public void setBIsExport(byte bIsExport) {
		this.bIsExport = bIsExport;
	}
	/** C type : CK_WTLS_RANDOM_DATA */
	public CK_WTLS_RANDOM_DATA RandomInfo;
	public CK_WTLS_RANDOM_DATA getRandomInfo() {
		return RandomInfo;
	}
	public void setRandomInfo(CK_WTLS_RANDOM_DATA RandomInfo) {
		this.RandomInfo = RandomInfo;
	}
	/** C type : CK_WTLS_KEY_MAT_OUT_PTR */
	public org.openecard.mdlw.sal.cryptoki.CK_WTLS_KEY_MAT_OUT.ByReference pReturnedKeyMaterial;
	public org.openecard.mdlw.sal.cryptoki.CK_WTLS_KEY_MAT_OUT.ByReference getPReturnedKeyMaterial() {
		return pReturnedKeyMaterial;
	}
	public void setPReturnedKeyMaterial(org.openecard.mdlw.sal.cryptoki.CK_WTLS_KEY_MAT_OUT.ByReference pReturnedKeyMaterial) {
		this.pReturnedKeyMaterial = pReturnedKeyMaterial;
	}
	public CK_WTLS_KEY_MAT_PARAMS() {
		super();
	}
	 protected List<String> getFieldOrder() {
		return Arrays.asList("DigestMechanism", "ulMacSizeInBits", "ulKeySizeInBits", "ulIVSizeInBits", "ulSequenceNumber", "bIsExport", "RandomInfo", "pReturnedKeyMaterial");
	}
	/**
	 * @param DigestMechanism C type : CK_MECHANISM_TYPE<br>
	 * @param ulMacSizeInBits C type : CK_ULONG<br>
	 * @param ulKeySizeInBits C type : CK_ULONG<br>
	 * @param ulIVSizeInBits C type : CK_ULONG<br>
	 * @param ulSequenceNumber C type : CK_ULONG<br>
	 * @param bIsExport C type : CK_BBOOL<br>
	 * @param RandomInfo C type : CK_WTLS_RANDOM_DATA<br>
	 * @param pReturnedKeyMaterial C type : CK_WTLS_KEY_MAT_OUT_PTR
	 */
	public CK_WTLS_KEY_MAT_PARAMS(long DigestMechanism, long ulMacSizeInBits, long ulKeySizeInBits, long ulIVSizeInBits, long ulSequenceNumber, byte bIsExport, CK_WTLS_RANDOM_DATA RandomInfo, org.openecard.mdlw.sal.cryptoki.CK_WTLS_KEY_MAT_OUT.ByReference pReturnedKeyMaterial) {
		super();
		this.DigestMechanism = DigestMechanism;
		this.ulMacSizeInBits = ulMacSizeInBits;
		this.ulKeySizeInBits = ulKeySizeInBits;
		this.ulIVSizeInBits = ulIVSizeInBits;
		this.ulSequenceNumber = ulSequenceNumber;
		this.bIsExport = bIsExport;
		this.RandomInfo = RandomInfo;
		this.pReturnedKeyMaterial = pReturnedKeyMaterial;
	}
	public CK_WTLS_KEY_MAT_PARAMS(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends CK_WTLS_KEY_MAT_PARAMS implements Structure.ByReference {
		
	};
	public static class ByValue extends CK_WTLS_KEY_MAT_PARAMS implements Structure.ByValue {
		
	};
}
