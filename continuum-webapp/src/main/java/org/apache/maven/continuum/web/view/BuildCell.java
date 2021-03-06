package org.apache.maven.continuum.web.view;

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

import com.opensymphony.webwork.views.util.UrlHelper;
import com.opensymphony.xwork.ActionContext;
import org.apache.maven.continuum.security.ContinuumRoleConstants;
import org.apache.maven.continuum.web.model.ProjectSummary;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.redback.authorization.AuthorizationException;
import org.codehaus.plexus.redback.system.SecuritySession;
import org.codehaus.plexus.redback.system.SecuritySystem;
import org.codehaus.plexus.redback.system.SecuritySystemConstants;
import org.codehaus.plexus.xwork.PlexusLifecycleListener;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.DisplayCell;
import org.extremecomponents.table.core.TableModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.util.HashMap;

/**
 * Used in Summary view
 *
 * @author <a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>
 * @version $Id$
 * @deprecated use of cells is discouraged due to lack of i18n and design in java code.
 *             Use jsp:include instead.
 */
public class BuildCell
    extends DisplayCell
{
    protected String getCellValue( TableModel tableModel, Column column )
    {
        ProjectSummary project = (ProjectSummary) tableModel.getCurrentRowBean();
        String contextPath = tableModel.getContext().getContextPath();

        int buildNumber = project.getBuildNumber();

        String result = "<div align=\"center\">";

        if ( project.isInBuildingQueue() )
        {
            result +=
                "<img src=\"" + contextPath + "/images/inqueue.gif\" alt=\"In Queue\" title=\"In Queue\" border=\"0\">";
        }
        else if ( project.isInCheckoutQueue() )
        {
            result += "<img src=\"" + contextPath +
                "/images/checkingout.gif\" alt=\"Checking Out sources\" title=\"Checking Out sources\" border=\"0\">";
        }
        else
        {
            if ( project.getState() == 1 || project.getState() == 10 || project.getState() == 2 ||
                project.getState() == 3 || project.getState() == 4 )
            {
                if ( buildNumber > 0 )
                {
                    HashMap params = new HashMap();

                    params.put( "projectId", new Integer( project.getId() ) );

                    params.put( "projectName", project.getName() );

                    params.put( "buildId", new Integer( project.getBuildInSuccessId() ) );

                    params.put( "projectGroupId", new Integer( project.getProjectGroupId() ) );

                    PageContext pageContext = (PageContext) tableModel.getContext().getContextObject();

                    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

                    HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

                    String url = UrlHelper.buildUrl( "/buildResult.action", request, response, params );

                    if ( isAuthorized( project ) )
                    {
                        // we are authzd so act normally
                        result += "<a href=\"" + url + "\">" + buildNumber + "</a>";
                    }
                    else
                    {
                        result += buildNumber;
                    }
                }
                else
                {
                    result += "&nbsp;";
                }
            }
            else if ( project.getState() == 6 )
            {
                result += "<img src=\"" + contextPath +
                    "/images/building.gif\" alt=\"Building\" title=\"Building\" border=\"0\">";
            }
            else if ( project.getState() == 7 )
            {
                result += "<img src=\"" + contextPath +
                    "/images/checkingout.gif\" alt=\"Checking Out sources\" title=\"Checking Out sources\" border=\"0\">";
            }
            else if ( project.getState() == 8 )
            {
                result += "<img src=\"" + contextPath +
                    "/images/checkingout.gif\" alt=\"Updating sources\" title=\"Updating sources\" border=\"0\">";
            }
            else
            {
                result += "<img src=\"" + contextPath +
                    "/images/inqueue.gif\" alt=\"In Queue\" title=\"In Queue\" border=\"0\">";
            }
        }

        return result + "</div>";
    }

    private boolean isAuthorized( ProjectSummary project )
    {
        // do the authz bit
        ActionContext context = ActionContext.getContext();

        PlexusContainer container = (PlexusContainer) context.getApplication().get( PlexusLifecycleListener.KEY );
        SecuritySession securitySession =
            (SecuritySession) context.getSession().get( SecuritySystemConstants.SECURITY_SESSION_KEY );

        try
        {
            SecuritySystem securitySystem = (SecuritySystem) container.lookup( SecuritySystem.ROLE );

            if ( !securitySystem.isAuthorized( securitySession, ContinuumRoleConstants.CONTINUUM_VIEW_GROUP_OPERATION,
                                               project.getProjectGroupName() ) )
            {
                return false;
            }
        }
        catch ( ComponentLookupException cle )
        {
            return false;
        }
        catch ( AuthorizationException ae )
        {
            return false;
        }

        return true;
    }
}
