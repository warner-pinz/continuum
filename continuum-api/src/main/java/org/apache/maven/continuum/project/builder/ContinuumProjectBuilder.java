package org.apache.maven.continuum.project.builder;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.net.URL;

import org.apache.maven.continuum.model.project.BuildDefinitionTemplate;

/**
 * @author <a href="mailto:trygvis@inamo.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public interface ContinuumProjectBuilder
{
    String ROLE = ContinuumProjectBuilder.class.getName();

    ContinuumProjectBuildingResult buildProjectsFromMetadata( URL url, String username, String password )
        throws ContinuumProjectBuilderException;

    ContinuumProjectBuildingResult buildProjectsFromMetadata( URL url, String username, String password,
                                                              boolean recursiveProjects )
        throws ContinuumProjectBuilderException;

    ContinuumProjectBuildingResult buildProjectsFromMetadata( URL url, String username, String password,
                                                              boolean recursiveProjects,
                                                              BuildDefinitionTemplate buildDefinitionTemplate )
        throws ContinuumProjectBuilderException;

    BuildDefinitionTemplate getDefaultBuildDefinitionTemplate()
        throws ContinuumProjectBuilderException;
}
