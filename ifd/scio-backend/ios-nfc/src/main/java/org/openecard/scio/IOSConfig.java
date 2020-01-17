package org.openecard.scio;

/**
 *
 * @author Florian Otto
 */
public interface IOSConfig {

    String getDefaultProvideCardMessage();

    String getDefaultCardRecognizedMessage();

    String getTagLostErrorMessage();

    String getDefaultNFCErrorMessage();

    String getAquireNFCTagTimeoutErrorMessage();

    String getNFCCompletionMessage();

    String getDefaultCardConnectedMessage();
}
