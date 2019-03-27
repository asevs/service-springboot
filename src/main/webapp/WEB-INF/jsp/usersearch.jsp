<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="s"  uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><s:message code="menu.users"/></title>
    <script type="text/javascript">
        function changeTrBg(row){
            row.style.backgroundColor = "#e6e6e6";
        }
        function defaultTrBg(row){
            row.style.backgroundColor = "white";
        }
        function startSerach(){
            var searchWord = document.getElementById('searchString').value;

            if(searchWord.length < 3){
                document.getElementById("errorSearch").innerHTML = "<s:message code="search.errorSearchString"/>";
                return false;
            } else {
                document.getElementById("errorSearch").innerHTML = "";
                var searchLink = '${pageContext.request.contextPath}/users/search/' + searchWord;
                window.location.href=searchLink;
            }
        }
    </script>
</head>
<body>
<%@include file="menu.app" %>
<h1><s:message code="menu.users"/></h1>
<c:set var="licznik" value="${recordStartCounter }"/>
<div align="center">
    <div align="right" style="width: 1000px; padding: 2px;">
        <input type="text" id="searchString"/>&nbsp;&nbsp;<input type="button" value="<s:message code="button.search"/>"
                                                                 onclick="startSerach();"/><br/>
        <span id="errorSearch" style="color: red;"></span>
    </div>
    <table width="1000" border="0" cellpadding="6" cellspacing="2">
        <tr bgcolor="#ffddcc">
            <td width="40" align="center"></td>
            <td width="40" align="center"><b><s:message code="admin.user.id"/></b></td>
            <td width="200" align="center"><b><s:message code="register.name"/></b></td>
            <td width="200" align="center"><b><s:message code="register.lastName"/></b></td>
            <td width="220" align="center"><b><s:message code="register.email"/></b></td>
            <td width="100" align="center"><b><s:message code="profil.czyAktywny"/></b></td>
            <td width="200" align="center"><b><s:message code="profil.role"/></b></td>
        </tr>
        <c:forEach var="u" items="${userList }">
            <c:set var="licznik" value="${licznik+1}"/>
            <tr onmouseover="changeTrBg(this)" onmouseout="defaultTrBg(this)">
                <td align="right"><c:out value="${licznik }"/></td>
                <td align="right"><a href="../edit/${u.id }"><c:out value="${u.id }" /></a></td>
                <td align="left"><a href="../edit/${u.id }"><c:out value="${u.name }" /></a></td>
                <td align="left"><a href="../edit/${u.id }"><c:out value="${u.lastName }" /></a></td>
                <td align="center"><a href="../edit/${u.id }"><c:out value="${u.email }" /></a></td>
                <td align="center">
                    <c:choose>
                        <c:when test="${u.active == 1 }">
                            <font color="green"><s:message code="word.yes"/></font>
                        </c:when>
                        <c:otherwise>
                            <font color="red"><s:message code="word.no"/></font>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td align="center">
                    <c:choose>
                        <c:when test="${u.nrRole == 1 }">
                            <font color="green"><s:message code="word.admin"/></font>
                        </c:when>
                        <c:otherwise>
                            <s:message code="word.user"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
    <table width="1000" border="0" cellpadding="6" cellspacing="0" bgcolor="#ffddcc">
        <tr>
            <td width="300" align="left">
                <s:message code="info.page"/> ${currentPage} <s:message code="info.from"/> ${totalPages}
            </td>
            <td align="right">

                <c:if test="${currentPage > 1}">
                    <input type="button"
                           onclick="window.location.href='${pageContext.request.contextPath}/users/${currentPage - 1}'"
                           value="<s:message code="link.back"/>"/>&nbsp;&nbsp;
                </c:if>

                <c:if test="${currentPage < totalPages}">
                    <input type="button"
                           onclick="window.location.href='${pageContext.request.contextPath}/users/${currentPage + 1}'"
                           value="<s:message code="link.next"/>"/>
                </c:if>

            </td>
        </tr>
    </table>

</div>
</body>
</html>
