/**
 *
 * Copyright 2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.gbuild.agent;

import org.codehaus.plexus.logging.AbstractLogEnabled;

import java.util.Map;

/**
 * @version $Rev$ $Date$
 */
public class ReportActivityExtension extends AbstractLogEnabled implements BuildAgentExtension {

    /**
     * @plexus.requirement
     */
    private BuildActivityNotifier notifier;

    public void preProcess(Map build) {
        notifier.sendNotification(build, "Started");
    }

    public void postProcess(Map build, Map results) {
        notifier.sendNotification(build, "Completed");
    }
}
