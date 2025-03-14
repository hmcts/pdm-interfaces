<%@ include file="../common/include.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, shrink-to-fit=no, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Create Judge Type</title>

    <%@ include file="../common/stylesheets.jsp"%>

</head>

<body data-help-page="create_judgetype">

    <div id="wrapper">

       
        <%@ include file="../common/sidebar.jsp"%>
        
		<%@ include file="../common/header.jsp"%>
	
        <!-- Page Content -->
        <div id="page-content-wrapper">
		
            <div class="container-fluid">
                <div class="row">
					<div class="col-md-12">
						<h3>Create Judge Type</h3>
						
						<c:if test="${not empty successMessage}" >
							<div class="alert alert-success">
								<p>${successMessage}</p>
							</div>
						</c:if>		
						
						<form:form commandName="judgeTypeCreateCommand" 
						           action="${context}/judgetype/create_judgetype" 
						           method="POST" 
						           class="form-horizontal">

						<%-- CSRF token --%>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						
						<%-- 
							This error block must be within the form:form tags , otherwise you won't get any errors back !!
						--%>
						<spring:hasBindErrors name="judgeTypeCreateCommand">
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
							<%-- Code --%>
							<spring:bind path="code">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.judgeTypeCreateCommand'].hasFieldErrors('code') ? 'has-error' : ''}">
									<label for="code">Code *</label>
									<form:input placeholder="Code"
												path="code"
												id="code"
												maxlength="10"
												cssClass="form-control"/>
									<spring:hasBindErrors name="judgeTypeCreateCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('code') ? errors.getFieldError('code').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>
						</div>
						
						<div class="form-group">
							<%-- Description --%>
							<spring:bind path="description">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.judgeTypeCreateCommand'].hasFieldErrors('description') ? 'has-error' : ''}">
									<label for="description">Description *</label>
									<form:input placeholder="Description"
												path="description"
												id="description"
												maxlength="150"
												cssClass="form-control"/>
									<spring:hasBindErrors name="judgeTypeCreateCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('description') ? errors.getFieldError('description').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>						
						</div>

						<%--Hidden fields here --%>
						<form:hidden path="courtId" />

						<div class="form-group">
							<div class="col-md-12">																												
							    <button id="btnCreateConfirm" class="btn btn-primary" name="btnCreateConfirm"><span class="glyphicon glyphicon-edit"></span> Create Judge Type</button>
								<a href="<c:url value="/judgetype/view_judgetype?reset=false"/>" class="btn btn-primary"><span class="glyphicon glyphicon-circle-arrow-left"></span> Return to Manage Judge Type</a>																	
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
	
	<script type="text/javascript" src="${context}/js/judgetype.js"></script>   


</body>

</html>
