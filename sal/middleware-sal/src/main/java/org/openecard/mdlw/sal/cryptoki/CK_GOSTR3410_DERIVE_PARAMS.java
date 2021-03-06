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
public class CK_GOSTR3410_DERIVE_PARAMS extends Structure {
	/** C type : CK_EC_KDF_TYPE */
	public long kdf;
	public long getKdf() {
		return kdf;
	}
	public void setKdf(long kdf) {
		this.kdf = kdf;
	}
	/** C type : CK_BYTE_PTR */
	public Pointer pPublicData;
	public Pointer getPPublicData() {
		return pPublicData;
	}
	public void setPPublicData(Pointer pPublicData) {
		this.pPublicData = pPublicData;
	}
	/** C type : CK_ULONG */
	public long ulPublicDataLen;
	public long getUlPublicDataLen() {
		return ulPublicDataLen;
	}
	public void setUlPublicDataLen(long ulPublicDataLen) {
		this.ulPublicDataLen = ulPublicDataLen;
	}
	/** C type : CK_BYTE_PTR */
	public Pointer pUKM;
	public Pointer getPUKM() {
		return pUKM;
	}
	public void setPUKM(Pointer pUKM) {
		this.pUKM = pUKM;
	}
	/** C type : CK_ULONG */
	public long ulUKMLen;
	public long getUlUKMLen() {
		return ulUKMLen;
	}
	public void setUlUKMLen(long ulUKMLen) {
		this.ulUKMLen = ulUKMLen;
	}
	public CK_GOSTR3410_DERIVE_PARAMS() {
		super();
	}
	 protected List<String> getFieldOrder() {
		return Arrays.asList("kdf", "pPublicData", "ulPublicDataLen", "pUKM", "ulUKMLen");
	}
	/**
	 * @param kdf C type : CK_EC_KDF_TYPE<br>
	 * @param pPublicData C type : CK_BYTE_PTR<br>
	 * @param ulPublicDataLen C type : CK_ULONG<br>
	 * @param pUKM C type : CK_BYTE_PTR<br>
	 * @param ulUKMLen C type : CK_ULONG
	 */
	public CK_GOSTR3410_DERIVE_PARAMS(long kdf, Pointer pPublicData, long ulPublicDataLen, Pointer pUKM, long ulUKMLen) {
		super();
		this.kdf = kdf;
		this.pPublicData = pPublicData;
		this.ulPublicDataLen = ulPublicDataLen;
		this.pUKM = pUKM;
		this.ulUKMLen = ulUKMLen;
	}
	public CK_GOSTR3410_DERIVE_PARAMS(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends CK_GOSTR3410_DERIVE_PARAMS implements Structure.ByReference {
		
	};
	public static class ByValue extends CK_GOSTR3410_DERIVE_PARAMS implements Structure.ByValue {
		
	};
}
