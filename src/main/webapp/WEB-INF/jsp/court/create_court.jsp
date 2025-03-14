<%@ include file="../common/include.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>

	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, shrink-to-fit=no, initial-scale=1">
	<meta name="description" content="">
	<meta name="author" content="">

	<title>Create Court</title>

	<%@ include file="../common/stylesheets.jsp"%>

</head>

<body data-help-page="create_court">

	<div id="wrapper">


		<%@ include file="../common/sidebar.jsp"%>

		<%@ include file="../common/header.jsp"%>

		<!-- Page Content -->
		<div id="page-content-wrapper">

			<div class="container-fluid">
				<div class="row">
					<div class="col-md-12">
						<h3>Create Court Site for ${court.courtName}</h3>

						<c:if test="${not empty successMessage}">
							<div class="alert alert-success">
								<p>${successMessage}</p>
							</div>
						</c:if>

						<form:form commandName="courtCreateCommand"
									action="${context}/court/create_court"
									method="POST"
									class="form-horizontal">

							<%-- CSRF token --%>
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

							<%--
							This error block must be within the form:form tags , otherwise you won't get any errors back !!
							--%>
							<spring:hasBindErrors name="courtCreateCommand">
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
								<spring:bind path="courtSiteName">
									<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.courtCreateCommand'].hasFieldErrors('courtSiteName') ? 'has-error' : ''}">
										<label for="courtSiteName">Court Site Name*</label>
										<form:input placeholder=""
													path="courtSiteName"
													id="courtSiteName"
													style="text-transform: uppercase"
													maxlength="255"
													cssClass="form-control" />
										<spring:hasBindErrors name="courtCreateCommand">
											<div class="help-block" element="span">
												${errors.hasFieldErrors('courtSiteName') ? errors.getFieldError('courtSiteName').defaultMessage : ''}
											</div>
										</spring:hasBindErrors>
									</div>
								</spring:bind>

								<%--  Court site code --%>
								<spring:bind path="courtSiteCode">
									<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.courtCreateCommand'].hasFieldErrors('courtSiteCode') ? 'has-error' : ''}">
										<label for="courtSiteCode">Court Site Code*</label>
										<form:input placeholder=""
													path="courtSiteCode"
													id="courtSiteCode"
													style="text-transform: uppercase"
													maxlength="50"
													cssClass="form-control" />
										<spring:hasBindErrors name="courtCreateCommand">
											<div class="help-block" element="span">
												${errors.hasFieldErrors('courtSiteCode') ? errors.getFieldError('courtSiteCode').defaultMessage : ''}
											</div>
										</spring:hasBindErrors>
									</div>
								</spring:bind>
							</div>

							<%--Hidden fields here --%>
							<form:hidden path="courtId" />
							<form:hidden path="addressId" />

							<div class="form-group">
								<div class="col-md-12">
									<button id="btnCreateConfirm" class="btn btn-primary" name="btnCreateConfirm"><span class="glyphicon glyphicon-edit"></span> Create Court</button>
									<a href="<c:url value="/court/view_court?reset=false"/>" class="btn btn-primary"><span class="glyphicon glyphicon-circle-arrow-left"></span> Return to Manage Court</a>
								</div>
							</div>
							<!-- /form-group -->

						</form:form>
						<!-- /form -->

					</div>
				</div>
				<!-- End row 1 -->
			</div>
			<!-- /container-fluid -->
		</div>
		<!-- /#page-content-wrapper -->
		<%@ include file="../common/footer.jsp"%>

	</div>
	<!-- /#wrapper -->

	<%@ include file="../common/scripts.jsp"%>

	<script type="text/javascript" src="${context}/js/court.js"></script>


</body>

</html>
