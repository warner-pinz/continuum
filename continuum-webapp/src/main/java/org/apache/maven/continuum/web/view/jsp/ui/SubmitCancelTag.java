package org.apache.maven.continuum.web.view.jsp.ui;

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

import com.opensymphony.webwork.components.Component;
import com.opensymphony.webwork.views.jsp.ui.SubmitTag;
import com.opensymphony.xwork.util.OgnlValueStack;
import org.apache.maven.continuum.web.components.SubmitCancel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>
 * @version $Id$
 */
public class SubmitCancelTag
    extends SubmitTag
{
    private String cancel;

    public Component getBean( OgnlValueStack stack, HttpServletRequest req, HttpServletResponse res )
    {
        return new SubmitCancel( stack, req, res );
    }

    protected void populateParams()
    {
        super.populateParams();

        SubmitCancel submitCancel = ( (SubmitCancel) component );
        submitCancel.setCancel( cancel );
    }

    public void setCancel( String cancel )
    {
        this.cancel = cancel;
    }
}
