<%@ include file="../common/include.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, shrink-to-fit=no, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Amend Proxies</title>

    <%@ include file="../common/stylesheets.jsp"%>

</head>

<body data-help-page="amend_localproxy">

    <div id="wrapper">


        <%@ include file="../common/sidebar.jsp"%>

		<%@ include file="../common/header.jsp"%>

        <!-- Page Content -->
        <div id="page-content-wrapper">

            <div class="container-fluid">
                <div class="row">
					<div class="col-md-12">
						<h3>Amend Local Proxy</h3>

						<c:if test="${not empty successMessage}" >
							<div class="alert alert-success">
								<p>${successMessage}</p>
							</div>
						</c:if>

						<form:form commandName="localProxyAmendCommand"
								   action="${context}/proxies/amend_localproxy"
								   method="POST"
								   class="form-horizontal">

						<%-- CSRF Guard token where uri equals form action --%>
						<%-- <input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue uri="${context}/proxies/amend_localproxy"/>"/> --%>

						<%--
							This error block must be within the form:form tags , otherwise you won't get any errors back !!
						--%>
						<spring:hasBindErrors name="localProxyAmendCommand">
							<c:forEach items="${errors.globalErrors}" var="errorMessage">
								<c:if test="${not empty errorMessage.defaultMessage}">
									<div class="alert alert-danger" id="errors">
										<c:out value="${errorMessage.defaultMessage}" />
									</div>
								</c:if>
							</c:forEach>
						</spring:hasBindErrors>

						<div class="form-group">
							<%-- Read-only ip address --%>
							<p class="h4 form-control-static col-md-6">IP Address : <c:out value="${courtSite.ipAddress}"/></p>
						</div>

						<%-- Local Proxy Title, again use spring:bind  so we can access any binding errors--%>
						<div class="form-group">
							<spring:bind path="title">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.localProxyAmendCommand'].hasFieldErrors('title') ? 'has-error' : ''}">
									<label for="title">Title *</label>
									<form:input placeholder="Title"
												path="title"
												id="title"
												maxlength="255"
												cssClass="form-control"/>
									<spring:hasBindErrors name="localProxyAmendCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('title') ? errors.getFieldError('title').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>
						</div>

						<%-- Schedule, mandatory numeric --%>
						<div class="form-group">
							<spring:bind path="scheduleId">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.localProxyAmendCommand'].hasFieldErrors('scheduleId') ? 'has-error' : ''}">
									<label for="scheduleId">Site Operating Hours *</label>
									<%-- Use Spring form:select & spring:eval with form:option to encrypt id --%>
									<form:select path="scheduleId" cssClass="form-control" id="scheduleId">
										<form:option value="" label="--- Please select the Operating Hours ---" />
										<c:forEach var="schedules" items="${scheduleList}">
											<c:set var="scheduleId"><spring:eval expression="schedules.id"/></c:set>
											<form:option value="${scheduleId}" label="${schedules.title}" />
										</c:forEach>
									</form:select>
									<spring:hasBindErrors name="localProxyAmendCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('scheduleId') ? errors.getFieldError('scheduleId').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>
						</div>

						<%-- Notification, optional up to 500 chars --%>
						<div class="form-group">
							<spring:bind path="notification">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.localProxyAmendCommand'].hasFieldErrors('notification') ? 'has-error' : ''}">
									<label for="notification">Notification</label>
									<form:textarea placeholder="Notification"
												path="notification"
												id="notification"
												rows="10"
												cols="50"
												cssClass="form-control"/>
									<spring:hasBindErrors name="localProxyAmendCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('notification') ? errors.getFieldError('notification').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>
						</div>

						<form:hidden path="courtSiteId" />

						<div class="form-group">
							<div class="col-md-12">
								<button id="btnUpdate" class="btn btn-primary" name="btnUpdate" >
									<span class="glyphicon glyphicon-edit"></span> Update Local Proxy</button>
								<a href="<c:url value="/proxies/view_localproxy?reset=false"/>" class="btn btn-primary"><span class="glyphicon glyphicon-circle-arrow-left"></span> Return to Local Proxy page</a>
							</div>
						</div>

						<!-- Modal dialog for notification change on the local proxy -->
						<div id="notificationModal" class="modal fade" role="dialog">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal">&times;</button>
										<h4 class="modal-title">Update CDU Notifications</h4>
									</div>

									<div class="modal-body">
										<p>You are about to update the notification for all CDUs associated with this Local Proxy. Do you wish to continue?</p>
									</div>

									<div class="modal-footer">
										<button class="btn btn-primary" id="btnUpdateConfirm" name="btnUpdateConfirm">Confirm</button>
										<button class="btn btn-default" data-dismiss="modal">Cancel</button>
									</div>
								</div>
							</div>
						</div><!-- end of notificationModal -->

						</form:form>

						<%--Hidden fields here --%>
						<input type="hidden" id="originalNotification" value="<c:out value="${courtSite.notification}"/>"/>
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

	<%-- Local Proxy Specific javascript files --%>
    <script type="text/javascript" src="${context}/js/proxies.js"></script>

</body>

</html>
