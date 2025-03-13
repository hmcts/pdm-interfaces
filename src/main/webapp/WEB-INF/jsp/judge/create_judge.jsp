<%@ include file="../common/include.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, shrink-to-fit=no, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Create Judge</title>

    <%@ include file="../common/stylesheets.jsp"%>

</head>

<body data-help-page="create_judge">

    <div id="wrapper">

       
        <%@ include file="../common/sidebar.jsp"%>
        
		<%@ include file="../common/header.jsp"%>
	
        <!-- Page Content -->
        <div id="page-content-wrapper">
		
            <div class="container-fluid">
                <div class="row">
					<div class="col-md-12">
						<h3>Create Judge</h3>
						
						<c:if test="${not empty successMessage}" >
							<div class="alert alert-success">
								<p>${successMessage}</p>
							</div>
						</c:if>		
						
						<form:form commandName="judgeCreateCommand" 
						           action="${context}/judge/create_judge" 
						           method="POST" 
						           class="form-horizontal">

						<%-- CSRF token --%>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
								   
						<%-- 
							This error block must be within the form:form tags , otherwise you won't get any errors back !!
						--%>
						<spring:hasBindErrors name="judgeCreateCommand">
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
							<%-- Surname --%>
							<spring:bind path="surname">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.judgeCreateCommand'].hasFieldErrors('surname') ? 'has-error' : ''}">
									<label for="surname">Surname *</label>
									<form:input placeholder="Surname"
												path="surname"
												id="surname"
												maxlength="35"
												cssClass="form-control"/>
									<spring:hasBindErrors name="judgeCreateCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('surname') ? errors.getFieldError('surname').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>						
						</div>
							
						<div class="form-group">
							<%-- First Name --%>
							<spring:bind path="firstName">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.judgeCreateCommand'].hasFieldErrors('firstName') ? 'has-error' : ''}">
									<label for="firstName">First Name *</label>
									<form:input placeholder="First Name"
												path="firstName"
												id="firstName"
												maxlength="35"
												cssClass="form-control"/>
									<spring:hasBindErrors name="judgeCreateCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('firstName') ? errors.getFieldError('firstName').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>						
						</div>
						
						<div class="form-group">
							<%-- Middle Name --%>
							<spring:bind path="middleName">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.judgeCreateCommand'].hasFieldErrors('middleName') ? 'has-error' : ''}">
									<label for="middleName">Middle Name</label>
									<form:input placeholder="Middle Name"
												path="middleName"
												id="middleName"
												maxlength="35"
												cssClass="form-control"/>
									<spring:hasBindErrors name="judgeCreateCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('middleName') ? errors.getFieldError('middleName').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>						
						</div>
						
						<div class="form-group">
							<%-- Title --%>
							<spring:bind path="title">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.judgeCreateCommand'].hasFieldErrors('title') ? 'has-error' : ''}">
									<label for="title">Title *</label>
									<form:input placeholder="Title"
												path="title"
												id="title"
												maxlength="25"
												cssClass="form-control"/>
									<spring:hasBindErrors name="judgeCreateCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('title') ? errors.getFieldError('title').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>						
						</div>
						
						<div class="form-group">
							<%-- Full List Title --%>
							<spring:bind path="fullListTitle1">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.judgeCreateCommand'].hasFieldErrors('fullListTitle1') ? 'has-error' : ''}">
									<label for="fullListTitle1">Full List Title *</label>
									<form:input placeholder="Full List Title"
												path="fullListTitle1"
												id="fullListTitle1"
												maxlength="80"
												cssClass="form-control"/>
									<spring:hasBindErrors name="judgeCreateCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('fullListTitle1') ? errors.getFieldError('fullListTitle1').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>						
						</div>
						
						<div class="form-group">
							<%-- Judge Type --%>
							<spring:bind path="judgeType">
								<div class="col-md-12 ${requestScope['org.springframework.validation.BindingResult.judgeCreateCommand'].hasFieldErrors('judgeType') ? 'has-error' : ''}">
									<label for="judgeType">Judge Type *</label>	
									<%-- Use Spring form:select & spring:eval with form:option to encrypt id --%>
									<form:select path="judgeType" cssClass="form-control" id="selectJudgeType">
										<form:option value="" label="--- Please select a Judge Type ---" />
										<c:forEach var="judgeType" items="${judgeTypeList}">
											<c:set var="judgeTypeKey"><spring:eval expression="judgeType.code"/></c:set>
											<form:option value="${judgeTypeKey}" label="${judgeType.code} - ${judgeType.deCode}" />
										</c:forEach>
									</form:select>
									<spring:hasBindErrors name="judgeCreateCommand">
										<div class="help-block" element="span">
											${errors.hasFieldErrors('judgeType') ? errors.getFieldError('judgeType').defaultMessage : ''}
										</div>
									</spring:hasBindErrors>
								</div>
							</spring:bind>
						</div>

						<%--Hidden fields here --%>
						<form:hidden path="courtId" />

						<div class="form-group">
							<div class="col-md-12">																												
							    <button id="btnCreateConfirm" class="btn btn-primary" name="btnCreateConfirm"><span class="glyphicon glyphicon-edit"></span> Create Judge</button>
								<a href="<c:url value="/judge/view_judge?reset=false"/>" class="btn btn-primary"><span class="glyphicon glyphicon-circle-arrow-left"></span> Return to Manage Judge</a>																	
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
	
	<script type="text/javascript" src="${context}/js/judge.js"></script>   


</body>

</html>
