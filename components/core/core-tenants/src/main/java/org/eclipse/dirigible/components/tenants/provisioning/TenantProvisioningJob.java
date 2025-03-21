/*
 * Copyright (c) 2025 Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: Eclipse Dirigible contributors SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.components.tenants.provisioning;

import org.eclipse.dirigible.commons.config.DirigibleConfig;
import org.eclipse.dirigible.components.jobs.SystemJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Component
@Transactional
class TenantProvisioningJob extends SystemJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantProvisioningJob.class);

    /** The tenants provisioner. */
    private final TenantsProvisioner tenantsProvisioner;

    /**
     * Instantiates a new tenant provisioning job.
     *
     * @param tenantsProvisioner the tenants provisioner
     */
    @Autowired
    TenantProvisioningJob(TenantsProvisioner tenantsProvisioner) {
        this.tenantsProvisioner = tenantsProvisioner;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.debug("Triggered tenants provisioning job...");
        tenantsProvisioner.provision();
        LOGGER.debug("Tenants provisioning job has completed.");
    }

    @Override
    protected String getTriggerKey() {
        return "TenantsProvisioningJobTrigger";
    }

    @Override
    protected String getTriggerDescription() {
        return "Tenants provisioning job trigger";
    }

    @Override
    protected String getJobKey() {
        return "TenantsProvisioningJob";
    }

    @Override
    protected String getJobDescription() {
        return "Tenants provisioning job";
    }

    @Override
    protected SimpleScheduleBuilder getSchedule() {
        int frequencyInSec = DirigibleConfig.TENANTS_PROVISIONING_FREQUENCY_SECONDS.getIntValue();
        LOGGER.info("Configuring tenant provisioning job to fire every [{}] seconds", frequencyInSec);

        return simpleSchedule().withIntervalInSeconds(frequencyInSec)
                               .repeatForever()
                               .withMisfireHandlingInstructionNextWithExistingCount();
    }

}
