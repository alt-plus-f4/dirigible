/*
 * Copyright (c) 2025 Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: Eclipse Dirigible contributors SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.components.data.export.endpoint;

import static java.text.MessageFormat.format;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.dirigible.components.base.endpoint.BaseEndpoint;
import org.eclipse.dirigible.components.data.export.service.DataImportService;
import org.eclipse.dirigible.components.data.management.service.DatabaseMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.security.RolesAllowed;

/**
 * Front facing REST service serving sql processing.
 */
@RestController
@RequestMapping(BaseEndpoint.PREFIX_ENDPOINT_DATA + "sql")
@RolesAllowed({"ADMINISTRATOR", "OPERATOR"})
public class DataSQLEndpoint {

    /** The constant logger. */
    private static final Logger logger = LoggerFactory.getLogger(DataSQLEndpoint.class);

    /**
     * The database metadata service.
     */
    private final DatabaseMetadataService databaseMetadataService;

    /**
     * The database metadata service.
     */
    private final DataImportService dataImportService;

    /**
     * Instantiates a new data export endpoint.
     *
     * @param databaseMetadataService the database metadata service
     * @param dataImportService the data import service
     */
    public DataSQLEndpoint(DatabaseMetadataService databaseMetadataService, DataImportService dataImportService) {
        this.databaseMetadataService = databaseMetadataService;
        this.dataImportService = dataImportService;
    }


    /**
     * Process SQL in schema.
     *
     * @param datasource the datasource
     * @param schema the schema
     * @param file the file
     * @return the response entity
     * @throws Exception the exception
     */
    @PostMapping(value = "/{datasource}/{schema}", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<?> importDataInTable(@Validated @PathVariable("datasource") String datasource,
            @Validated @PathVariable("schema") String schema, @Validated @RequestParam("file") MultipartFile file) throws Exception {
        try {
            return processSQL(datasource, schema, file);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SQL upload failed: " + e.getMessage());
        }
    }


    /**
     * Process SQL.
     *
     * @param datasource the datasource
     * @param schema the schema
     * @param file the file
     * @return the response entity
     * @throws Exception the exception
     */
    private ResponseEntity<?> processSQL(String datasource, String schema, MultipartFile file) throws Exception {

        if (!databaseMetadataService.existsDataSourceMetadata(datasource)) {
            String error = format("Datasource {0} does not exist.", datasource);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, error);
        }

        InputStream is = file.getInputStream();
        long fileSize = file.getSize();
        dataImportService.processSQL(datasource, schema, is, fileSize);
        return ResponseEntity.ok()
                             .build();
    }

}
