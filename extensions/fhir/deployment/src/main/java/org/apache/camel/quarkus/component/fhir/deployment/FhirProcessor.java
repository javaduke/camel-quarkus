/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.quarkus.component.fhir.deployment;

import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.util.jar.DependencyLogImpl;
import ca.uhn.fhir.validation.schematron.SchematronBaseValidator;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.AdditionalApplicationArchiveMarkerBuildItem;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBundleBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

final class FhirProcessor {
    private static final String FEATURE = "camel-fhir";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    ExtensionSslNativeSupportBuildItem activateSslNativeSupport() {
        return new ExtensionSslNativeSupportBuildItem(FEATURE);
    }

    @BuildStep()
    NativeImageResourceBundleBuildItem hapiMessages() {
        return new NativeImageResourceBundleBuildItem("ca.uhn.fhir.i18n.hapi-messages");
    }

    @BuildStep
    AdditionalApplicationArchiveMarkerBuildItem hl7ArchiveMarker() {
        return new AdditionalApplicationArchiveMarkerBuildItem("org/hl7/fhir");
    }

    @BuildStep
    AdditionalApplicationArchiveMarkerBuildItem hapiArchiveMarker() {
        return new AdditionalApplicationArchiveMarkerBuildItem("ca/uhn/fhir");
    }

    @BuildStep()
    void processFhir(BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
        reflectiveClass.produce(new ReflectiveClassBuildItem(true, false, true, SchematronBaseValidator.class));
        reflectiveClass.produce(new ReflectiveClassBuildItem(true, false, true, DependencyLogImpl.class));
        reflectiveClass.produce(new ReflectiveClassBuildItem(true, true, true, ApacheRestfulClientFactory.class));
    }

}
