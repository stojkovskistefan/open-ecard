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

package org.openecard.client.common.sal.state.cif;

import java.util.HashMap;
import java.util.Map;
import iso.std.iso_iec._24727.tech.schema.AccessRuleType;
import iso.std.iso_iec._24727.tech.schema.DataSetInfoType;
import iso.std.iso_iec._24727.tech.schema.SecurityConditionType;


/**
 *
 * @author Dirk Petrautzki <petrautzki@hs-coburg.de>
 */
public class DataSetInfoWrapper {

    private DataSetInfoType dataSetInfo;
    private Map<Enum<?>, SecurityConditionType> securityConditions = new HashMap<Enum<?>, SecurityConditionType>();

    public DataSetInfoWrapper(DataSetInfoType dataSetInfo){
	this.dataSetInfo = dataSetInfo;
    }

    public DataSetInfoType getDataSetInfo() {
	return dataSetInfo;
    }

    public SecurityConditionType getSecurityCondition(Enum<?> serviceAction) {
	if(securityConditions.isEmpty()){
	    for(AccessRuleType accessRule : this.dataSetInfo.getDataSetACL().getAccessRule()){
		if (accessRule.getAction().getConnectionServiceAction() != null) {
		    securityConditions.put(accessRule.getAction().getConnectionServiceAction(), accessRule.getSecurityCondition());
		} else if (accessRule.getAction().getAuthorizationServiceAction() != null) {
		    securityConditions.put(accessRule.getAction().getAuthorizationServiceAction(), accessRule.getSecurityCondition());
		} else if (accessRule.getAction().getDifferentialIdentityServiceAction() != null) {
		    securityConditions.put(accessRule.getAction().getDifferentialIdentityServiceAction(), accessRule.getSecurityCondition());
		} else if (accessRule.getAction().getNamedDataServiceAction() != null) {
		    securityConditions.put(accessRule.getAction().getNamedDataServiceAction(), accessRule.getSecurityCondition());
		} else if (accessRule.getAction().getCryptographicServiceAction() != null) {
		    securityConditions.put(accessRule.getAction().getCryptographicServiceAction(), accessRule.getSecurityCondition());
		} else if (accessRule.getAction().getCardApplicationServiceAction() != null) {
		    securityConditions.put(accessRule.getAction().getCardApplicationServiceAction(), accessRule.getSecurityCondition());
		}
	    }
	}
	return securityConditions.get(serviceAction);
    }

}
