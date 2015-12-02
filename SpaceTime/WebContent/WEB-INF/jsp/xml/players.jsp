<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/xml; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<?xml version="1.0" encoding="UTF-8"?>
<game>
<status>success</status>
<players>
<c:forEach items="${players }" var="player">
<player id="${player.id}" version="${player.version}" firstName="${player.firstName}" lastName="${player.lastName}" email="${player.email}">
<user id="${player.user.id}" version="${player.user.version}" username="${player.user.username}"></user>
</player>
</c:forEach>
</players>
</game>