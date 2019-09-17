/** **************************************************************************
 * Copyright (C) 2019 ecsec GmbH.
 * All rights reserved.
 * Contact: ecsec GmbH (info@ecsec.de)
 *
 * This file may be used in accordance with the terms and conditions
 * contained in a signed written agreement between you and ecsec GmbH.
 *
 ************************************************************************** */
package org.openecard.mobile.activation.common;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openecard.addon.AddonManager;
import org.openecard.addon.AddonNotFoundException;
import org.openecard.addon.AddonSelector;
import org.openecard.addon.bind.AppPluginAction;
import org.openecard.addon.bind.AuxDataKeys;
import org.openecard.addon.bind.BindingResult;
import org.openecard.common.interfaces.EventDispatcher;
import org.openecard.common.util.HttpRequestLineUtils;
import org.openecard.mobile.activation.ActivationInteraction;
import org.openecard.mobile.activation.ActivationResult;
import static org.openecard.mobile.activation.ActivationResultCode.CLIENT_ERROR;
import static org.openecard.mobile.activation.ActivationResultCode.DEPENDING_HOST_UNREACHABLE;
import static org.openecard.mobile.activation.ActivationResultCode.INTERNAL_ERROR;
import static org.openecard.mobile.activation.ActivationResultCode.INTERRUPTED;
import static org.openecard.mobile.activation.ActivationResultCode.OK;
import static org.openecard.mobile.activation.ActivationResultCode.REDIRECT;
import org.openecard.mobile.activation.ControllerCallback;
import org.openecard.mobile.system.OpeneCardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neil Crossley
 */
public class ActivationControllerService {

    private static final Logger LOG = LoggerFactory.getLogger(ActivationControllerService.class);

    private final Object processLock = new Object();
    private final OpeneCardContextProvider contextProvider;
    private boolean isRunning = false;
    private Thread currentProccess = null;
    private ControllerCallback currentCallback = null;
    private ControllerCallback cancelledCallback = null;
    private AutoCloseable closable = null;

    public ActivationControllerService(OpeneCardContextProvider contextProvider) {
	this.contextProvider = contextProvider;
    }

    public void start(final URL requestURI, Set<String> supportedCards, final ControllerCallback controllerCallback, ActivationInteraction interaction) {
	if (requestURI == null) {
	    throw new IllegalArgumentException("Request url cannot be null.");
	}
	if (controllerCallback == null) {
	    throw new IllegalArgumentException("Controller callback cannot be null.");
	}

	new Thread(() -> {
	    ActivationResult result = this.activate(requestURI, supportedCards, controllerCallback, interaction);
	    synchronized (processLock) {
		if (cancelledCallback == controllerCallback) {
		    return;
		}
	    }
	    controllerCallback.onAuthenticationCompletion(result);
	}).start();
    }

    public void cancelAuthentication(final ControllerCallback controllerCallback) {
	if (controllerCallback == null) {
	    throw new IllegalArgumentException("Please pass the controller callback used to start the activation.");
	}
	Thread cancellableThread;
	AutoCloseable currentClosable;
	synchronized (this.processLock) {
	    if (!this.isRunning) {
		LOG.info("The activation could not be cancelled because an activation process is not currently running.");
		return;
	    }
	    if (this.currentCallback != controllerCallback) {
		LOG.info("The activation could not be cancelled because the controller callback does not match the callback used by the current activation process.");
		return;
	    }
	    this.cancelledCallback = controllerCallback;
	    cancellableThread = this.currentProccess;
	    this.currentProccess = null;
	    this.isRunning = false;
	    currentClosable = this.closable;
	    this.closable = null;
	}
	if (currentClosable != null) {
	    try {
		currentClosable.close();
	    } catch (Exception ex) {
		LOG.info("Non-critical error occured while cleaning up the event dispatch hooks.", ex);
	    }
	}
	controllerCallback.onAuthenticationCompletion(new ActivationResult(INTERRUPTED, ""));

	cancellableThread.interrupt();
	try {
	    cancellableThread.join();
	} catch (InterruptedException ex) {
	    LOG.warn("Could not interrupt the thread running the interuppted exception.", ex);
	}
    }

    public boolean isRunning() {
	return this.isRunning;
    }

    /**
     * Performs an activation according to BSI TR-03124-1, but does not perform the return to web session part. A result
     * containing the outcome of the
     *
     * @param requestURI
     * @return
     */
    private ActivationResult activate(URL requestURI, Set<String> supportedCards, ControllerCallback callback, ActivationInteraction interaction) {
	if (this.isRunning) {
	    return new ActivationResult(INTERRUPTED, "The activation process is already running");
	}
	// create request uri and extract query strings
	String path = requestURI.getPath();
	String resourceName;
	try {
	    // remove leading '/'
	    resourceName = path.substring(1, path.length());
	} catch (IndexOutOfBoundsException ex) {
	    return new ActivationResult(INTERRUPTED, "The given activation URL is not valid: " + requestURI.toExternalForm());
	}

	// find suitable addon
	String failureMessage;
	final OpeneCardContext context = this.contextProvider.getContext();
	AddonManager manager = context.getManager();
	AddonSelector selector = null;
	AppPluginAction action = null;
	try {
	    if (manager == null) {
		throw new IllegalStateException("Addon initialization failed.");
	    } else {
		selector = new AddonSelector(manager);
		action = selector.getAppPluginAction(resourceName);

		String rawQuery = requestURI.getQuery();
		final Map<String, String> queries;
		if (rawQuery != null) {
		    queries = HttpRequestLineUtils.transform(rawQuery);
		} else {
		    queries = new HashMap<>(0);
		}
		synchronized (this.processLock) {
		    if (this.isRunning) {
			throw new IllegalStateException("The activation process is already running");
		    }
		    this.isRunning = true;
		    this.currentCallback = callback;
		    this.cancelledCallback = null;
		    this.currentProccess = Thread.currentThread();
		}
		EventDispatcher eventDispatcher = context.getEventDispatcher();
		try {
		    Map.Entry<CardEventHandler, AutoCloseable> entry = CardEventHandler.create(supportedCards, eventDispatcher, interaction);

		    this.closable = entry.getValue();

		    BindingResult result = action.execute(null, queries, null, null);
		    return createActivationResult(result);
		} catch (Exception ex) {
		    String interruptMessage = ex.getMessage();
		    LOG.warn("The activation was interrupted with the following message: {}", interruptMessage, ex);
		    return new ActivationResult(INTERRUPTED, interruptMessage);
		} finally {
		    synchronized (this.processLock) {
			this.isRunning = false;
			this.currentCallback = null;
			this.currentProccess = null;
		    }
		}
	    }
	} catch (AddonNotFoundException ex) {
	    failureMessage = ex.getMessage();
	    LOG.info("Addon not found.", ex);
	} catch (UnsupportedEncodingException ex) {
	    failureMessage = "Unsupported encoding.";
	    LOG.warn(failureMessage, ex);
	} catch (Exception ex) {
	    failureMessage = ex.getMessage();
	    LOG.warn(ex.getMessage(), ex);
	} finally {
	    if (selector != null && action != null) {
		selector.returnAppPluginAction(action);
	    }
	}

	LOG.info(
		"Returning error as INTERRUPTED result.");
	return new ActivationResult(INTERRUPTED, failureMessage);
    }

    private ActivationResult createActivationResult(BindingResult result) {
	LOG.info("Returning result: {}", result);
	ActivationResult activationResult;
	switch (result.getResultCode()) {
	    case REDIRECT:
		String location = result.getAuxResultData().get(AuxDataKeys.REDIRECT_LOCATION);
		activationResult = new ActivationResult(location, REDIRECT);
		break;
	    case OK:
		activationResult = new ActivationResult(OK);
		break;
	    case INTERRUPTED:
		activationResult = new ActivationResult(INTERRUPTED, result.getResultMessage());
		break;
	    case DEPENDING_HOST_UNREACHABLE:
		activationResult = new ActivationResult(DEPENDING_HOST_UNREACHABLE, result.getResultMessage());
		break;
	    case WRONG_PARAMETER:
	    case MISSING_PARAMETER:
	    case RESOURCE_UNAVAILABLE:
		activationResult = new ActivationResult(CLIENT_ERROR, result.getResultMessage());
		break;
	    default:
		activationResult = new ActivationResult(INTERNAL_ERROR, result.getResultMessage());
	}
	return activationResult;
    }

}
