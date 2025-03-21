<%@ include file="../common/include.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, shrink-to-fit=no, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Amend CDU</title>

    <%@ include file="../common/stylesheets.jsp"%>

</head>

<body data-help-page="amend_cdu">

    <div id="wrapper">

       
        <%@ include file="../common/sidebar.jsp"%>
        
		<%@ include file="../common/header.jsp"%>
	
        <!-- Page Content -->
        <div id="page-content-wrapper">
		
            <div class="container-fluid">
                <div class="row">
					<div class="col-md-12">
						<h3>Amend CDU</h3>
						
						<c:if test="${not empty successMessage}" >
							<div class="alert alert-success">
								<p>${successMessage}</p>
							</div>
						</c:if>		
						
						
						<form:form commandName="cduAmendCommand" 
						           action="${context}/cdus/amend_cdu" 
						           method="POST" 
						           class="form-horizontal">

						<%-- CSRF token --%>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						
						<%-- 
							This error block must be within the form:form tags , otherwise you won't get any errors back !!
						--%>
						<spring:hasBindErrors name="cduAmendCommand">
							<c:forEach items="${errors.globalErrors}" var="errorMessage">
								<c:if test="${not empty errorMessage.defaultMessage}">
									<div class="alert alert-danger" id="errors">
										<c:out value="${errorMessage.defaultMessage}" />
									</div>
								</c:if>
							</c:forEach>
						</spring:hasBindErrors>
						
						<div class="form-group">
							<%-- Read-only mac address --%>
							<p class="h4 form-control-static col-md-6">MAC Address : <c:out value="${cdu.macAddress}"/></p>

							<%-- Read-only cdu number --%>
							<p class="h4 form-control-static col-md-6">CDU Number : <c:out value="${cdu.cduNumber}"/></p>							
						</div>
						<!-- /form-group -->
						
						<div class="form-group">
							
							<%-- Location, mandatory, 50 chars --%>
							<spring:bind path="location">
								<div class="col-md-6 ${requestScope['org.springframework.validation.BindingResult.cduAmendCommand'].hasFieldErrors('location') ? 'has-error' : ''}">
									
										<label for="location">Location *</label>	
										<form:input placeholder="Location"
													path="location" 
													id="location"
													maxlength="50"
													cssClass="form-control"/>
										<spring:hasBindErrors name="cduAmendCommand">
											<div class="help-block" element="span">
												${errors.hasFieldErrors('location') ? errors.getFieldError('location').defaultMessage : ''}
											</div>
										</spring:hasBindErrors>
									
								</div>
							</spring:bind>
																				
						</div>	
						<!-- /form-group -->	
						
						<div class="form-group">
							<%-- Description, optional up to 500 chars --%>
							<spring:bind path="description">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.cduAmendCommand'].hasFieldErrors('description') ? 'has-error' : ''}">
									
										<label for="description">Description</label>	
										<form:textarea placeholder="Description"
													path="description" 
													id="description"
													rows="10"
													cols="50"
													cssClass="form-control"/>
										<spring:hasBindErrors name="cduAmendCommand">
											<div class="help-block" element="span">
												${errors.hasFieldErrors('description') ? errors.getFieldError('description').defaultMessage : ''}
											</div>
										</spring:hasBindErrors>
								</div>
							</spring:bind>
						</div>
						<!-- /form-group -->	
							
						<div class="form-group">								
							<%-- Notification, optional up to 500 chars --%>
							<spring:bind path="notification">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.cduAmendCommand'].hasFieldErrors('notification') ? 'has-error' : ''}">
									
										<label for="notification">Notification</label>	
										<form:textarea placeholder="Notification"
													path="notification" 
													id="notification"
													rows="10"
													cols="50"
													cssClass="form-control"/>
										<spring:hasBindErrors name="cduAmendCommand">
											<div class="help-block" element="span">
												${errors.hasFieldErrors('notification') ? errors.getFieldError('notification').defaultMessage : ''}
											</div>
										</spring:hasBindErrors>
								</div>
							</spring:bind>								
						</div>
						<!-- /form-group -->
						
						<div class="form-group">
							<%-- Refresh, mandatory numeric --%>
							<spring:bind path="refresh">
								<div class="col-md-6 ${requestScope['org.springframework.validation.BindingResult.cduAmendCommand'].hasFieldErrors('refresh') ? 'has-error' : ''}">
									
										<label for="refresh">Rotation Rate *</label>	
										<form:input placeholder="Rotation Rate"
													path="refresh" 
													id="refresh"
													cssClass="form-control"/>
										<spring:hasBindErrors name="cduAmendCommand">
											<div class="help-block" element="span">
												${errors.hasFieldErrors('refresh') ? errors.getFieldError('refresh').defaultMessage : ''}
											</div>
										</spring:hasBindErrors>
								</div>
							</spring:bind>
							
							<%-- Weighting, mandatory numeric --%>
							<spring:bind path="weighting">
								<div class="col-md-6 ${requestScope['org.springframework.validation.BindingResult.cduAmendCommand'].hasFieldErrors('weighting') ? 'has-error' : ''}">
									
										<label for="weighting">Weighting *</label>	
										<form:select path="weighting" cssClass="form-control" id="weighting">
											<form:option value="" label="--- Please select a Weighting ---" />
											<form:option value="1" label="Low - 1" />
											<form:option value="2" label="High - 2" />
										</form:select>
										<spring:hasBindErrors name="cduAmendCommand">
											<div class="help-block" element="span">
												${errors.hasFieldErrors('weighting') ? errors.getFieldError('weighting').defaultMessage : ''}
											</div>
										</spring:hasBindErrors>
								</div>
							</spring:bind>
						</div>
						<!-- /form-group -->
						
						<div class="form-group">
							<%-- Offline Indicator, mandatory character --%>
							<spring:bind path="offlineIndicator">
								<div class="col-md-6 ${requestScope['org.springframework.validation.BindingResult.cduAmendCommand'].hasFieldErrors('offlineIndicator') ? 'has-error' : ''}">
									
										<label for="offlineIndicator">Known Offline *</label>	
										<form:select path="offlineIndicator" cssClass="form-control" id="offlineIndicator">
											<form:option value="" label="--- Please select a Value ---" />
											<form:option value="N" label="No" />
											<form:option value="Y" label="Yes" />
										</form:select>
										<spring:hasBindErrors name="cduAmendCommand">
											<div class="help-block" element="span">
												${errors.hasFieldErrors('offlineIndicator') ? errors.getFieldError('offlineIndicator').defaultMessage : ''}
											</div>
										</spring:hasBindErrors>
								</div>
							</spring:bind>
						</div>	
						<!-- /form-group -->						
							
						<%--Hidden fields here --%>
						<form:hidden path="cduId" />
							
						<div class="form-group">
							<div class="col-md-12">																												
							    <button id="btnUpdateCdu" class="btn btn-primary" name="btnUpdateCdu"><span class="glyphicon glyphicon-edit"></span> Update CDU</button>
								<a href="<c:url value="/cdus/cdus?reset=false"/>" class="btn btn-primary"><span class="glyphicon glyphicon-circle-arrow-left"></span> Return to CDUS page</a>																	
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
	
	<script type="text/javascript" src="${context}/js/cdus.js"></script>   


</body>

</html>
