/** **************************************************************************
 * Copyright (C) 2019 ecsec GmbH.
 * All rights reserved.
 * Contact: ecsec GmbH (info@ecsec.de)
 *
 * This file may be used in accordance with the terms and conditions
 * contained in a signed written agreement between you and ecsec GmbH.
 *
 ************************************************************************** */
package org.openecard.mobile.activation;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.openecard.gui.definition.BoxItem;
import org.openecard.gui.mobile.eac.types.ServerData;
import org.openecard.gui.mobile.pinmanagement.PinStatus;

/**
 *
 * @author Neil Crossley
 */
public interface EacInteraction extends ActivationInteraction {

    void onPinRequest(int attempt, Function<String, Boolean> enterPin);
    void onPinCanRequest(BiFunction<String, String, Boolean> enterPinCan);
    void onCardBlocked();
    void onCardDeactivated();
    void onServerData(ServerData data, BiFunction<List<BoxItem>, List<BoxItem>, Boolean> selectReadWrite);
    void onTransactionInfo(String data);
    void onPinStatus(PinStatus status, String cardType);
    void onInteractionComplete();
}
