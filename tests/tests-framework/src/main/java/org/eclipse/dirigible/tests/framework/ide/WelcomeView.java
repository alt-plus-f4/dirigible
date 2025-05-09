/*
 * Copyright (c) 2025 Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: Eclipse Dirigible contributors SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.tests.framework.ide;

import org.eclipse.dirigible.tests.framework.browser.Browser;
import org.eclipse.dirigible.tests.framework.browser.HtmlAttribute;
import org.eclipse.dirigible.tests.framework.browser.HtmlElementType;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class WelcomeView {

    private final Browser browser;

    protected WelcomeView(Browser browser) {
        this.browser = browser;
    }

    public void searchForTemplate(String template) {
        browser.enterTextInElementByAttributePattern(HtmlElementType.INPUT, HtmlAttribute.PLACEHOLDER, "Search template by name", template);
    }

    public void selectTemplate(String templateTitle) {
        browser.clickOnElementByAttributePatternAndText(HtmlElementType.DIV, HtmlAttribute.ROLE, "heading", templateTitle);
    }

    public void typeProjectName(String projectName) {
        browser.enterTextInElementByAttributePattern(HtmlElementType.INPUT, HtmlAttribute.ID, "projectName", projectName);
    }

    public void typeFileName(String fileName) {
        browser.enterTextInElementByAttributePattern(HtmlElementType.INPUT, HtmlAttribute.ID, "fileName", fileName);
    }

    public void typeTemplateParamById(String paramId, String paramValue) {
        browser.enterTextInElementByAttributePattern(HtmlElementType.INPUT, HtmlAttribute.ID, paramId, paramValue);
    }

    public void confirmTemplate() {
        browser.clickOnElementContainingText(HtmlElementType.BUTTON, "Create");
    }

}
