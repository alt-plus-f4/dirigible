/*
 * Generated by Eclipse Dirigible based on model and template.
 *
 * Do not modify the content as it may be re-generated again.
 */
import * as schemaTemplateManager from "template-application-schema/template/template";
import * as daoTemplateManager from "template-application-dao/template/template";
import * as generateUtils from "service-generate/template/generateUtils";
import * as parameterUtils from "service-generate/template/parameterUtils";

export function generate(model, parameters) {
    model = JSON.parse(model).model;
    let templateSources = getTemplate(parameters).sources;
    parameterUtils.process(model, parameters)
    return generateUtils.generateFiles(model, parameters, templateSources);
};

export function getTemplate(parameters) {
    let schemaTemplate = schemaTemplateManager.getTemplate(parameters);
    let daoTemplate = daoTemplateManager.getTemplate(parameters);

    let templateSources = [];
    templateSources = templateSources.concat(schemaTemplate.sources);
    templateSources = templateSources.concat(daoTemplate.sources);

    return {
        name: "Application - Data",
        description: "Application with a Database Schema and DAO",
        extension: "model",
        sources: templateSources,
        parameters: parameterUtils.getUniqueParameters(schemaTemplate.parameters, daoTemplate.parameters)
    };
};

