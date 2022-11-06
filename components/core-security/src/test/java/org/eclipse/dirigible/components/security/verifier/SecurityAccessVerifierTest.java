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
package org.eclipse.dirigible.components.security.verifier;

import org.eclipse.dirigible.components.security.domain.SecurityAccess;
import org.eclipse.dirigible.components.security.repository.SecurityAccessRepository;
import org.eclipse.dirigible.components.security.repository.SecurityRoleRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.eclipse.dirigible.components.security.repository.SecurityAccessRepositoryTest.createSecurityAccess;
import static org.eclipse.dirigible.components.security.repository.SecurityRoleRepositoryTest.createSecurityRole;
import static org.junit.Assert.*;

@SpringBootTest(classes = {SecurityAccessRepository.class, SecurityRoleRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"org.eclipse.dirigible.components"})
@EntityScan("org.eclipse.dirigible.components")
@Transactional
class SecurityAccessVerifierTest {

    @Autowired
    private SecurityAccessVerifier securityAccessVerifier;

    @Autowired
    private SecurityAccessRepository securityAccessRepository;

    @Autowired
    private SecurityRoleRepository securityRoleRepository;

    @BeforeEach
    public void setup() {
        // Create test security roles and accesses
        securityRoleRepository.save(createSecurityRole("/a/b/c/test1.role", "test1", "description"));
        securityRoleRepository.save(createSecurityRole("/a/b/c/test2.role", "test2", "description"));
        securityAccessRepository.save(createSecurityAccess("/a/b/c/test.access", "test1", "description", "HTTP", "/a" +
                "/b/c/test.txt", "GET", "test1"));
        securityAccessRepository.save(createSecurityAccess("/a/b/c/test.access", "test2", "description", "HTTP", "/a" +
                "/b/c/test.txt", "GET", "test2"));
    }

    @AfterEach
    public void cleanup() {
        // Delete test security roles and accesses
        securityRoleRepository.findAll().stream().forEach(securityRole -> securityRoleRepository.delete(securityRole));
        securityAccessRepository.findAll().stream().forEach(securityAccess -> securityAccessRepository.delete(securityAccess));
    }

    @Test
    void testGetMatchingSecurityAccesses() {
        List<SecurityAccess> matchingSecurityAccesses =
                securityAccessVerifier.getMatchingSecurityAccesses("HTTP", "/a/b/c", "GET");
        assertTrue(matchingSecurityAccesses.isEmpty());

        matchingSecurityAccesses = securityAccessVerifier.getMatchingSecurityAccesses("HTTP",
                "/a/b/c/test.txt", "GET");
        assertFalse(matchingSecurityAccesses.isEmpty());
        assertEquals(2, matchingSecurityAccesses.size());
    }

    @SpringBootApplication
    static class TestConfiguration {
    }
}