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
package org.eclipse.dirigible.components.base.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.eclipse.dirigible.components.base.helpers.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ObjectStoreTest.
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = { "org.eclipse.dirigible.components" })
@EntityScan("org.eclipse.dirigible.components")
@Transactional
public class ObjectStoreTest {
	
	/** The object store. */
	@Autowired
	private ObjectStore objectStore;
	
	/**
	 * Setup.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
    public void setup() throws Exception {
		objectStore.addMapping("Customer", mapping);
//		objectStore.setDataSource(...);
		objectStore.initialize();
	}
	
	/**
	 * Save object.
	 */
	@Test
    public void saveObject() {
		
		String json = "{\"name\":\"John\",\"address\":\"Sofia, Bulgaria\"}";
		
		objectStore.save("Customer", json);
		
		String results = objectStore.list("Customer");
		System.out.println(results);
		List list = JsonHelper.fromJson(results, List.class);
		
        assertNotNull(list);
        assertNotNull(list.get(0));
        assertEquals("John", ((List)list.get(0)).get(1));
    }
	
	/** The mapping. */
	String mapping = "<hibernate-mapping>\n"
			+ "\n"
			+ "    <class entity-name=\"Customer\" >\n"
			+ "\n"
			+ "        <id name=\"id\"\n"
			+ "            type=\"long\"\n"
			+ "            column=\"ID\">\n"
			+ "            <generator class=\"sequence\"/>\n"
			+ "        </id>\n"
			+ "\n"
			+ "        <property name=\"name\"\n"
			+ "            column=\"NAME\"\n"
			+ "            type=\"string\"/>\n"
			+ "\n"
			+ "        <property name=\"address\"\n"
			+ "            column=\"ADDRESS\"\n"
			+ "            type=\"string\"/>\n"
			+ "\n"
			+ "    </class>\n"
			+ "    \n"
			+ "</hibernate-mapping>";
	
	/**
	 * The Class TestConfiguration.
	 */
	@SpringBootApplication
	static class TestConfiguration {
	}

}