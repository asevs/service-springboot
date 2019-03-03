<table width="100%" border="0" cellpadding="4" cellspacing="4" class="tableMenuBg" bgcolor="#ccccee">
    <tr>
            <td align="left" window="900">
                <a href="/"><s:message code="menu.MainPage"/></>
             </td>

             <td align="left">
             <sec:authorize access="hasRole('ANONYMOUS')">
                <a href="/login"><s:message code="menu.login"/></a>&nbsp;
                <a href="/register"><s:message code="menu.register"/></a>&nbsp;
             </sec:authorize>
             <sec:authorize access="isAuthenticated()">
                <a href="/logout"><s:message code="menu.logout"/></a>&nbsp;
             </sec:authorize>
             </td>
     </tr>
</table>