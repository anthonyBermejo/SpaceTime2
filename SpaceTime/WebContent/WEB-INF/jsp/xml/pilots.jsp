<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/xml; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<?xml version="1.0" encoding="UTF-8"?>
<game>
<status>success</status>
<pilots>
<c:forEach items="${pilots }" var="pilot">
<pilot id="${pilot.id}" version="${pilot.version}" name="${pilot.name}"></pilot>
</c:forEach>
</pilots>
</game>