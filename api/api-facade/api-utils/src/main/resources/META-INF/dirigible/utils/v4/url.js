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
exports.encode = function(input, charset) {
	return org.eclipse.dirigible.api.v3.utils.UrlFacade.encode(input, charset);
};

exports.decode = function(input, charset) {
	return org.eclipse.dirigible.api.v3.utils.UrlFacade.decode(input, charset);
};

exports.escape = function(input) {
	return org.eclipse.dirigible.api.v3.utils.UrlFacade.escape(input);
};

exports.escapePath = function(input) {
	return org.eclipse.dirigible.api.v3.utils.UrlFacade.escapePath(input);
};

exports.escapeForm = function(input) {
	return org.eclipse.dirigible.api.v3.utils.UrlFacade.escapeForm(input);
};
