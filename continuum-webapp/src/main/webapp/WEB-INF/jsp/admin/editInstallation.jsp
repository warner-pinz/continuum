<%--
  ~ Licensed to the Apache Software Foundation (ASF) under one
  ~ or more contributor license agreements.  See the NOTICE file
  ~ distributed with this work for additional information
  ~ regarding copyright ownership.  The ASF licenses this file
  ~ to you under the Apache License, Version 2.0 (the
  ~ "License"); you may not use this file except in compliance
  ~ with the License.  You may obtain a copy of the License at
  ~
  ~   http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~ KIND, either express or implied.  See the License for the
  ~ specific language governing permissions and limitations
  ~ under the License.
  --%>

<%@ taglib uri="/webwork" prefix="ww" %>
<%@ taglib uri="continuum" prefix="c1" %>
<html>
<ww:i18n name="localization.Continuum">
  <head>
    <title>
      <ww:text name="installation.page.title"/>
    </title>
  </head>

  <body>
  <div id="axial" class="h3">
    <h3>
      <ww:text name="installation.section.title"/>
    </h3>

    <ww:form action="saveInstallation!save" method="post">

      <ww:if test="hasActionErrors()">
        <h3>Action Error</h3>
      </ww:if>
      <p>
        <ww:actionerror/>
      </p>

      <div class="axial">

        <table>
          <tbody>
            <ww:hidden name="installation.installationId" />
            <ww:hidden name="installationType" />
            <ww:textfield label="%{getText('installation.name.label')}" name="installation.name"
                            required="true"/>
            <ww:if test="displayTypes">
              <ww:select label="%{getText('installation.type.label')}" name="installation.type" list="typesLabels" />
            </ww:if>
            <ww:if test="varNameUpdatable">
              <ww:if test="varNameDisplayable">
                <ww:textfield label="%{getText('installation.varName.label')}" name="installation.varName" required="${varNameUpdatable}" />
              </ww:if>
            </ww:if>
            <ww:else>
              <ww:if test="varNameDisplayable">
                <ww:textfield label="%{getText('installation.varName.label')}" name="installation.varName" required="true" readonly="true"/>
              </ww:if>
            </ww:else>
            <ww:textfield label="%{getText('installation.value.label')}" name="installation.varValue"
                          required="true"/>
            <ww:if test="(automaticProfileDisplayable && installation == null) ||  (installation.installationId == 0)">
              <ww:checkbox label="%{getText('installation.automaticProfile.label')}" name="automaticProfile" />
            </ww:if>
          </tbody>
        </table>
        <div class="functnbar3">
          <c1:submitcancel value="%{getText('save')}" cancel="%{getText('cancel')}"/>
        </div>

      </div>
    </ww:form>
  </div>
  </body>
</ww:i18n>
</html>
