/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.api.v4.websockets;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.eclipse.dirigible.commons.api.helpers.GsonHelper;
import org.eclipse.dirigible.commons.api.scripting.IScriptingFacade;
import org.eclipse.dirigible.engine.js.api.IJavascriptEngineExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WebsocketsFacade implements IScriptingFacade {
	
	public static final String DIRIGIBLE_WEBSOCKET_WRAPPER_MODULE_ON_OPEN = "net/wrappers/onOpen";
	public static final String DIRIGIBLE_WEBSOCKET_WRAPPER_MODULE_ON_MESSAGE = "net/wrappers/onMessage";
	public static final String DIRIGIBLE_WEBSOCKET_WRAPPER_MODULE_ON_ERROR = "net/wrappers/onError";
	public static final String DIRIGIBLE_WEBSOCKET_WRAPPER_MODULE_ON_CLOSE = "net/wrappers/onClose";
	
	private static Logger logger = LoggerFactory.getLogger(WebsocketsFacade.class);
	
	public static final List<WebsocketClient> CLIENTS = Collections.synchronizedList(new ArrayList<WebsocketClient>());
	
	/**
	 * Create a new Websocket by a given URI and Handler
	 * 
	 * @param uri the URI
	 * @param handler the handler
	 * @param engine the engine type
	 * @return the Websocket Session object
	 * @throws IOException  in case of an error
	 * @throws DeploymentException in case of an error
	 */
	public static final Session createWebsocket(String uri, String handler, String engine) throws DeploymentException, IOException {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        logger.debug("Connecting to " + uri);
        WebsocketClient client = new WebsocketClient(handler, engine);
		Session session = container.connectToServer(client, URI.create(uri));
        return session;
	}
	
	/**
	 * Create a new Websocket by a given URI and Javascript Handler
	 * 
	 * @param uri the URI
	 * @param handler the Javascript Handler
	 * @return the Websocket Session object
	 * @throws IOException  in case of an error
	 * @throws DeploymentException in case of an error
	 */
	public static final Session createWebsocket(String uri, String handler) throws DeploymentException, IOException {
        return createWebsocket(uri, handler, IJavascriptEngineExecutor.JAVASCRIPT_TYPE_DEFAULT);
	}
	
	/**
	 * Get all created clients
	 * 
	 * @return the list of clients
	 */
	public static final List<WebsocketClient> getClients() {
		return CLIENTS;
	}
	
	/**
	 * Get all created clients
	 * 
	 * @return the list in JSON
	 */
	public static final String getClientsAsJson() {
		return GsonHelper.GSON.toJson(CLIENTS);
	}
	
	/**
	 * Get a particular client by its session id
	 * 
	 * @param id the session id
	 * @return the client
	 */
	public static final WebsocketClient getClient(String id) {
		Optional<WebsocketClient> result = CLIENTS.stream().parallel().filter(client -> client.getSession().getId().equals(id)).findFirst();
		
		return result.isPresent() ? result.get() : null;
	}
	
	/**
	 * Get a particular client by its handler
	 * 
	 * @param handler the handler
	 * @return the client
	 */
	public static final WebsocketClient getClientByHandler(String handler) {
		Optional<WebsocketClient> result = CLIENTS.stream().parallel().filter(client -> client.getHandler().equals(handler)).findFirst();
		return result.isPresent() ? result.get() : null;
	}
	

}
