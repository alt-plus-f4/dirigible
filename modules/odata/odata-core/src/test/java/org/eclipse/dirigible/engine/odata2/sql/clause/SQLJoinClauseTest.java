/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.engine.odata2.sql.clause;

import org.apache.olingo.odata2.annotation.processor.core.edm.AnnotationEdmProvider;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.core.edm.provider.EdmImplProv;
import org.eclipse.dirigible.engine.odata2.sql.binding.EdmTableBindingProvider;
import org.eclipse.dirigible.engine.odata2.sql.builder.SQLContext;
import org.eclipse.dirigible.engine.odata2.sql.builder.SQLSelectBuilder;
import org.eclipse.dirigible.engine.odata2.sql.builder.SQLQueryBuilder;
import org.eclipse.dirigible.engine.odata2.sql.builder.SQLQueryTestUtils;
import org.eclipse.dirigible.engine.odata2.sql.edm.Entity1;
import org.eclipse.dirigible.engine.odata2.sql.edm.Entity2;
import org.eclipse.dirigible.engine.odata2.sql.edm.Entity3;
import org.eclipse.dirigible.engine.odata2.sql.mapping.DefaultEdmTableMappingProvider;
import org.eclipse.dirigible.engine.odata2.sql.test.util.OData2TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SQLJoinClauseTest {
    AnnotationEdmProvider provider;
    EdmImplProv edm;
    SQLQueryBuilder builder;
    SQLContext context;
    EdmTableBindingProvider tableMappingProvider;

    @Before
    public void setUp() throws Exception {
        Class<?>[] classes = { //
                Entity1.class, //
                Entity2.class, //
                Entity3.class //
        };
        provider = new AnnotationEdmProvider(Arrays.asList(classes));
        edm = new EdmImplProv(provider);
        tableMappingProvider = new DefaultEdmTableMappingProvider(OData2TestUtils.resources(classes));
        builder = new SQLQueryBuilder(tableMappingProvider);
        context = new SQLContext();
    }

    @Test
    public void testSimpleJoin() throws Exception {
        EdmEntityType mpl = edm.getEntityType(Entity1.class.getPackage().getName(), Entity1.class.getSimpleName());
        EdmEntityType uda = edm.getEntityType(Entity2.class.getPackage().getName(), Entity2.class.getSimpleName());
        SQLSelectBuilder noop = new SQLSelectBuilder(tableMappingProvider);

        SQLJoinClause join = new SQLJoinClause(noop, mpl, uda);
        //the start condition will be registered as T0 of from and T1 for to
        SQLQueryTestUtils.grantTableAliasForStructuralTypeInQuery(noop, mpl);
        SQLQueryTestUtils.grantTableAliasForStructuralTypeInQuery(noop, uda);
        //Use the left join to tolerate null elements
        assertEquals("LEFT JOIN MPLHEADER AS T0 ON T0.ID = T1.HEADER_ID", join.evaluate(context));

    }

    @Test
    public void testSimpleJoinFlippedBecauseOfMapping() throws Exception {
        EdmEntityType mpl = edm.getEntityType(Entity1.class.getPackage().getName(), Entity1.class.getSimpleName());
        EdmEntityType uda = edm.getEntityType(Entity2.class.getPackage().getName(), Entity2.class.getSimpleName());
        SQLSelectBuilder noop = new SQLSelectBuilder(tableMappingProvider);

        SQLJoinClause join = new SQLJoinClause(noop, uda, mpl);
        SQLQueryTestUtils.grantTableAliasForStructuralTypeInQuery(noop, uda);
        assertEquals("LEFT JOIN ITOP_MPLUSERDEFINEDATTRIBUTE AS T0 ON T0.HEADER_ID = T1.ID", join.evaluate(context));
    }

    @Test
    public void testNoNeedForJoin_fromAndToAreTheSame() throws Exception {
        EdmEntityType from = edm.getEntityType(Entity1.class.getPackage().getName(), Entity1.class.getSimpleName());
        SQLSelectBuilder noop = new SQLSelectBuilder(tableMappingProvider);
        SQLJoinClause join = new SQLJoinClause(noop, from, from);
        assertEquals("", join.evaluate(context));
    }
}