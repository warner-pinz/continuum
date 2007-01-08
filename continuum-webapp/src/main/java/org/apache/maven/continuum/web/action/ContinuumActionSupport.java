package org.apache.maven.continuum.web.action;

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

import org.apache.maven.continuum.Continuum;
import org.codehaus.plexus.xwork.action.PlexusActionSupport;

import com.opensymphony.xwork.Preparable;

/**
 * ContinuumActionSupport
 *
 * @author Jesse McConnell <jesse@codehaus.org>
 * @version $Id$
 */
public class ContinuumActionSupport
    extends PlexusActionSupport
    implements Preparable
{
    /**
     * @plexus.requirement
     */
    private Continuum continuum;

    public void prepare()
        throws Exception
    {
 
    }

    public Continuum getContinuum()
    {
        return continuum;
    }

    public void setContinuum( Continuum continuum )
    {
        this.continuum = continuum;
    }
}