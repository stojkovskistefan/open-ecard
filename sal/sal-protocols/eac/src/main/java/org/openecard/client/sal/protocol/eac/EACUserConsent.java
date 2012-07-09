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

import java.util.Map;
import org.openecard.client.common.I18n;
import org.openecard.client.gui.ResultStatus;
import org.openecard.client.gui.StepResult;
import org.openecard.client.gui.UserConsent;
import org.openecard.client.gui.UserConsentNavigator;
import org.openecard.client.gui.definition.*;
import org.openecard.client.gui.executor.*;
import org.openecard.client.sal.protocol.eac.gui.CHATStep;
import org.openecard.client.sal.protocol.eac.gui.CVCStep;
import org.openecard.client.sal.protocol.eac.gui.GUIContentMap;
import org.openecard.client.sal.protocol.eac.gui.PINStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the EAC user consent dialog.
 *
 * @author Dirk Petrautzki <petrautzki@hs-coburg.de>
 * @author Moritz Horsch <horsch@cdc.informatik.tu-darmstadt.de>
 * @author Tobias Wich <tobias.wich@ecsec.de>
 */
public class EACUserConsent {

    private static final Logger _logger = LoggerFactory.getLogger(EACUserConsent.class);

    // GUI translation constants
    private static final String TITLE = "eac_user_consent_title";

    private final I18n lang = I18n.getTranslation("sal");
    private final UserConsent gui;
    private final boolean capturePin;


    /**
     * Creates a new EAC user consent.
     *
     * @param gui GUI
     * @param capturePin True if PIN is captured in dialog, false if reader takes care of the PIN entry.
     */
    protected EACUserConsent(UserConsent gui, boolean capturePin) {
	this.gui = gui;
	this.capturePin = capturePin;
    }

    /**
     * Shows the GUI.
     *
     * @param content GUI Content
     */
    public ResultStatus show(GUIContentMap content) {
	final UserConsentDescription uc = new UserConsentDescription(lang.translationForKey(TITLE));

	final CVCStep cvcStep = new CVCStep(content);
	final CHATStep chatStep = new CHATStep(content);
	final PINStep pinStep = new PINStep(content);

	uc.getSteps().add(cvcStep.getStep());
	uc.getSteps().add(chatStep.getStep());
	if (capturePin) { // don't capture PIN when terminal supports native PACE
	    uc.getSteps().add(pinStep.getStep());
	}

	// Custom action for CVC step
	StepAction cvcStepAction = new StepAction(cvcStep.getStep()) {

	    @Override
	    public StepActionResult perform(Map<String, ExecutionResults> oldResults, StepResult result) {
		switch (result.getStatus()) {
		    case BACK:
			return new StepActionResult(StepActionResultStatus.BACK);
		    case OK:
			// write back result from last visit of chatStep
			if (oldResults.get(chatStep.getStep().getID()) != null) {
			    Checkbox cc = null;
			    for (OutputInfoUnit o : oldResults.get(chatStep.getStep().getID()).getResults()) {
				if (o instanceof Checkbox) {
				    cc = (Checkbox) o;
				}
			    }

			    for (InputInfoUnit i : chatStep.getStep().getInputInfoUnits()) {
				if (i instanceof Checkbox) {
				    Checkbox c = (Checkbox) i;
				    c.getBoxItems().clear();
				    for (BoxItem b : cc.getBoxItems()) {
					BoxItem ii = b;
					ii.setDisabled(b.isDisabled());
					ii.setChecked(b.isChecked());
					c.getBoxItems().add(ii);
				    }
				}
			    }
			}
			return new StepActionResult(StepActionResultStatus.NEXT);
		    default:
			return new StepActionResult(StepActionResultStatus.REPEAT);
		}
	    }
	};

	// Custom action for PIN step
	StepAction pinStepAction = new StepAction(pinStep.getStep()) {

	    @Override
	    public StepActionResult perform(Map<String, ExecutionResults> oldResults, StepResult result) {
		switch (result.getStatus()) {
		    case BACK:
			// write back result from last visit of chatStep
			Checkbox cc = null;
			for (OutputInfoUnit o : oldResults.get(chatStep.getStep().getID()).getResults()) {
			    if (o instanceof Checkbox) {
				cc = (Checkbox) o;
			    }
			}

			for (InputInfoUnit i : chatStep.getStep().getInputInfoUnits()) {
			    if (i instanceof Checkbox) {
				Checkbox c = (Checkbox) i;
				c.getBoxItems().clear();
				for (BoxItem b : cc.getBoxItems()) {
				    BoxItem ii = b;
				    ii.setDisabled(b.isDisabled());
				    ii.setChecked(b.isChecked());
				    c.getBoxItems().add(ii);
				}
			    }
			}
			return new StepActionResult(StepActionResultStatus.BACK);
		    case OK:
			return new StepActionResult(StepActionResultStatus.NEXT);
		    default:
			return new StepActionResult(StepActionResultStatus.REPEAT);
		}
	    }
	};

	UserConsentNavigator navigator = gui.obtainNavigator(uc);
	ExecutionEngine exec = new ExecutionEngine(navigator);

	// Add custom action
	exec.addCustomAction(cvcStepAction);
	exec.addCustomAction(pinStepAction);

	ResultStatus processResult = exec.process();

	if (processResult.equals(ResultStatus.OK)) {
	    Map<String, ExecutionResults> results = exec.getResults();
	    cvcStep.processResult(results);
	    chatStep.processResult(results);
	    pinStep.processResult(results);
	}

	return processResult;
    }

}
