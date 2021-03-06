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
            <ww:text name="notifier.page.title">
                <ww:param>Jabber</ww:param>
            </ww:text>
        </title>
    </head>
    <body>
      <div id="axial" class="h3">
        <ww:if test="${projectId > 0}">
            <ww:url id="actionUrl" action="jabberProjectNotifierSave" includeContext="false" includeParams="none" />
        </ww:if>
        <ww:else>
            <ww:url id="actionUrl" action="jabberProjectGroupNotifierSave" includeContext="false" includeParams="none"/>
        </ww:else>
        <h3>
            <ww:text name="notifier.section.title">
                <ww:param>Jabber</ww:param>
            </ww:text>
        </h3>

        <div class="axial">
          <ww:form action="%{actionUrl}" method="post" validate="true">
            <ww:hidden name="notifierId"/>
            <ww:hidden name="projectId"/>
            <ww:hidden name="projectGroupId"/>
            <ww:hidden name="notifierType"/>
            <ww:hidden name="fromGroupPage"/>
            <table>
              <tbody>
                <ww:textfield label="%{getText('notifier.jabber.host.label')}" name="host" required="true"/>
                <ww:textfield label="%{getText('notifier.jabber.port.label')}" name="port"/>
                <ww:textfield label="%{getText('notifier.jabber.login.label')}" name="login" required="true"/>
                <ww:password label="%{getText('notifier.jabber.password.label')}" name="password" required="true"/>
                <ww:textfield label="%{getText('notifier.jabber.domainName.label')}" name="domainName"/>
                <ww:textfield label="%{getText('notifier.jabber.address.label')}" name="address" required="true"/>
                <ww:checkbox label="%{getText('notifier.jabber.isSslConnection.label')}" name="sslConnection" value="sslConnection" fieldValue="true"/>
                <ww:checkbox label="%{getText('notifier.jabber.isGroup.label')}" name="group" value="group" fieldValue="true"/>
                <ww:checkbox label="%{getText('notifier.event.sendOnSuccess')}" name="sendOnSuccess" value="sendOnSuccess" fieldValue="true"/>
                <ww:checkbox label="%{getText('notifier.event.sendOnFailure')}" name="sendOnFailure" value="sendOnFailure" fieldValue="true"/>
                <ww:checkbox label="%{getText('notifier.event.sendOnError')}" name="sendOnError" value="sendOnError" fieldValue="true"/>
                <ww:checkbox label="%{getText('notifier.event.sendOnWarning')}" name="sendOnWarning" value="sendOnWarning" fieldValue="true"/>
              </tbody>
            </table>
            <div class="functnbar3">
              <c1:submitcancel value="%{getText('save')}" cancel="%{getText('cancel')}"/>
            </div>
          </ww:form>
        </div>
      </div>
    </body>
  </ww:i18n>
</html>
