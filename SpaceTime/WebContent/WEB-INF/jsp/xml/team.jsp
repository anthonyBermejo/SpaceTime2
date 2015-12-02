<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/xml; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<?xml version="1.0" encoding="UTF-8"?>
<game>
<status>success</status>
<team id="${team.id}" version="${team.version}" name="${team.name}" player="${team.player.id}">
<c:forEach items="${team.members }" var="pilot">
<pilot id="${pilot.id}" version="${pilot.version}" name="${pilot.name}"></pilot>
</c:forEach>
</team>
</game>