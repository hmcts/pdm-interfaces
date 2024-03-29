<%@ include file="../common/include.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, shrink-to-fit=no, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Delete Court Room</title>

    <%@ include file="../common/stylesheets.jsp"%>

</head>

<body data-help-page="delete_courtroom">

    <div id="wrapper">

       
        <%@ include file="../common/sidebar.jsp"%>
        
		<%@ include file="../common/header.jsp"%>
	
        <!-- Page Content -->
        <div id="page-content-wrapper">
		
            <div class="container-fluid">
                <div class="row">
					<div class="col-md-12">
						<h3>Delete Court Room</h3>
						
						<c:if test="${not empty successMessage}" >
							<div class="alert alert-success">
								<p>${successMessage}</p>
							</div>
						</c:if>		
						
						<form:form commandName="courtRoomDeleteCommand" 
						           action="${context}/courtroom/delete_courtroom" 
						           method="POST" 
						           class="form-horizontal">

						<%-- CSRF token --%>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						
						<%-- 
							This error block must be within the form:form tags , otherwise you won't get any errors back !!
						--%>
						<spring:hasBindErrors name="courtRoomDeleteCommand">
								<c:forEach items="${errors.globalErrors}" var="errorMessage">
									<c:if test="${not empty errorMessage.defaultMessage}">
										<div class="alert alert-danger" id="errors">
											<c:out value="${errorMessage.defaultMessage}" />
										</div>
									</c:if>
								</c:forEach>
							</spring:hasBindErrors>
						<!-- /form-group -->
						
						<div class="form-group">
							<%-- Court Site --%>
							<spring:bind path="xhibitCourtSiteId">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.courtRoomDeleteCommand'].hasFieldErrors('xhibitCourtSiteId') ? 'has-error' : ''}">
									<label for="xhibitCourtSiteId">Court Site *</label>	
									<%-- Use Spring form:select & spring:eval with form:option to encrypt id --%>
									<form:select path="xhibitCourtSiteId" cssClass="form-control" id="selectSite">
										<form:option value="" label="--- Please select a court site ---" />
										<c:forEach var="courtSite" items="${courtSiteList}">
											<c:set var="courtSiteId"><spring:eval expression="courtSite.id"/></c:set>
											<form:option value="${courtSiteId}" label="${courtSite.courtSiteCode} - ${courtSite.courtSiteName}" />
										</c:forEach>
									</form:select>
									<spring:hasBindErrors name="courtRoomDeleteCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('xhibitCourtSiteId') ? errors.getFieldError('xhibitCourtSiteId').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>

                            <%-- Court Room --%>
							<spring:bind path="courtRoomId">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.courtRoomDeleteCommand'].hasFieldErrors('courtRoomId') ? 'has-error' : ''}">
									<label for="courtRoomId">Court Room *</label>	
									<%-- Use Spring form:select & spring:eval with form:option to encrypt id --%>
									<form:select path="courtRoomId" cssClass="form-control" id="selectCourtRoom">
										<form:option value="" label="--- Please select a court room ---" />
										<c:forEach var="courtRoom" items="${courtRoomList}">
											<c:set var="courtRoomId"><spring:eval expression="courtRoom.id"/></c:set>
											<form:option value="${courtRoomId}" label="${courtRoom.courtRoomName}" />
										</c:forEach>
									</form:select>
									<spring:hasBindErrors name="courtRoomDeleteCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('courtRoomId') ? errors.getFieldError('courtRoomId').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>

                            <%-- Rotation Set --%>
							<div class="col-md-12">
								<div><strong>Description</strong></div><div id="description" class="form-control"></div>
							</div>
						</div>

						<div class="form-group">
							<div class="col-md-12">																												
							    <button id="btnDeleteConfirm" class="btn btn-primary" name="btnDeleteConfirm"><span class="glyphicon glyphicon-edit"></span> Delete Court Room</button>
								<a href="<c:url value="/courtroom/view_courtroom?reset=false"/>" class="btn btn-primary"><span class="glyphicon glyphicon-circle-arrow-left"></span> Return to Manage Court Room</a>																	
							</div>
						</div>
						<!-- /form-group -->
									
					</form:form>	
					<!-- /form -->	
					
					</div>
				</div> <!-- End row 1 -->
            </div>
			<!-- /container-fluid -->
        </div>
        <!-- /#page-content-wrapper -->
		<%@ include file="../common/footer.jsp"%>

    </div>
    <!-- /#wrapper -->

	<%@ include file="../common/scripts.jsp"%>
	
	<script type="text/javascript" src="${context}/js/delete_courtroom.js"></script>   


</body>

</html>
