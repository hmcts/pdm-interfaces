<%@ include file="../common/include.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, shrink-to-fit=no, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Manage CDUS</title>

    <%@ include file="../common/stylesheets.jsp"%>

</head>

<body data-help-page="cdus">

    <div id="wrapper">

       	<%@ include file="../common/sidebar.jsp"%>

		<%@ include file="../common/header.jsp"%>

        <!-- Page Content -->
        <div id="page-content-wrapper">

            <div class="container-fluid">
                <div class="row">

					<div class="col-md-12">

						<h3>Manage CDUS</h3>

						<%-- Make this form/command object responsible for all actions on the page. --%>


						<form:form 	commandName="cduSearchCommand"
										action="${context}/cdus/cdus"
										method="POST"
										cssClass="form-horizontal">

							<%-- CSRF token --%>
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							
							<%-- messages_success is always present so the JS can insert its own messages --%>
							<div id="messages_success" class="alert alert-success" style="${empty successMessage ? 'display:none;' : ''}">
								<c:if test="${not empty successMessage}" >
									<p>${successMessage}</p>
								</c:if>
							</div>

							<%-- messages_error is always present so the JS can insert its own messages --%>
							<spring:hasBindErrors name="cduSearchCommand">
								<c:forEach items="${errors.globalErrors}" var="errorMessage">
									<c:if test="${not empty errorMessage.defaultMessage}">
										<div class="alert alert-danger" id="errors">
											<c:out value="${errorMessage.defaultMessage}" />
										</div>
									</c:if>
								</c:forEach>
							</spring:hasBindErrors>

							<%-- form group class may be thought of as a row on the form --%>
							<div class="form-group">

								<%--It could be possible that we get an empty court site list so we need to check --%>
								<c:choose>

									<c:when test="${not empty courtSiteList}">

										<%-- Bind to the xhibitCourtSiteId --%>
										<spring:bind path="xhibitCourtSiteId">

											<%-- apply the has-error class if we get any errors back from the bindingresult --%>
											<div class="col-md-6 ${requestScope['org.springframework.validation.BindingResult.cduSearchCommand'].hasFieldErrors('xhibitCourtSiteId') ? 'has-error' : ''}">
													<%-- Use Spring form:select & spring:eval with form:option to encrypt id --%>
													<form:select path="xhibitCourtSiteId" cssClass="form-control" id="selectSite">
														<form:option value="" label="--- Please select a court site ---" />
														<c:forEach var="courtSite" items="${courtSiteList}">
															<%--<c:set var="courtSiteId"><spring:eval expression="courtSite.id"/></c:set>--%>
															<c:set var="courtSiteId"><spring:eval expression="courtSite.id"/></c:set>
															<form:option value="${courtSiteId}" label="${courtSite.courtSiteName}" />
														</c:forEach>
													</form:select>
													<%-- Add the actual spring error message underneath the select box --%>
													<spring:hasBindErrors name="cduSearchCommand">
														<div class="help-block" element="span">
															${errors.hasFieldErrors('xhibitCourtSiteId') ? errors.getFieldError('xhibitCourtSiteId').defaultMessage : ''}
														</div>
													</spring:hasBindErrors>
											</div>
										</spring:bind>
									</c:when>

									<c:otherwise>
										<%-- No court site data found, so just render a simple warning block and only permit search by MAC Address --%>
										<div class="col-md-6 alert alert-info">
											<p>No Court Sites present in database so unable to Search CDUS by Site/local proxy.</p>
										</div>
									</c:otherwise>

								</c:choose>

								<spring:bind path="macAddress">
									<div class="col-md-6 ${requestScope['org.springframework.validation.BindingResult.cduSearchCommand'].hasFieldErrors('macAddress') ? 'has-error' : ''}">
										<form:input id="macAddress" path="macAddress" type="text" cssClass="form-control" placeHolder="MAC Address"/>
										<spring:hasBindErrors name="cduSearchCommand">
											<div class="help-block" element="span">
												${errors.hasFieldErrors('macAddress') ? errors.getFieldError('macAddress').defaultMessage : ''}
											</div>
										</spring:hasBindErrors>
									</div>
								</spring:bind>
							</div>
							<!-- /form-group -->

							<div class="form-group">
								<div class="col-md-2">
									<%-- Search Button - no need for type attribute --%>
									<button name="btnSearchSites"
									        id="btnSearchSites"
									        class="btn btn-primary"
									        value="searchCdus">
										<span class="glyphicon glyphicon-search"> </span>
										Search CDUS
									</button>
								</div>
							</div>
							<!-- /form-group -->

							<%--
								Search has been performed so render our cdulist as a drop down
							--%>
							<c:if test="${cduList != null}">
								<div id="cduSearchResults">
									<c:choose>
										<c:when test="${not empty cduList}">
											<%-- Bind to the id of the cdu we want --%>
											<div class="form-group">
												<spring:bind path="selectedMacAddress">
													<%-- apply the has-error class if we get any errors back from the bindingresult --%>
													<div class="col-md-6 ${status.error ? 'has-error' : ''}">
															<%--
																Use Spring form:select & spring:eval with form:option to encrypt mac address
																Need to add data-options here for registered/unregistered
															 --%>
															<form:select path="selectedMacAddress" cssClass="form-control" id="selectCdu">
																<form:option value="" label="--- Please select a CDU ---" />
																<c:forEach var="cdu" items="${cduList}">
																	<c:set var="cduIdentifier"><spring:eval expression="cdu.identifier"/></c:set>
																	<c:set var="cduLabel">${cdu.macAddress}</c:set>
																	<c:if test="${not empty cdu.cduNumber}">
																		<c:set var="cduLabel">${cduLabel} - ${cdu.cduNumber}</c:set>
																	</c:if>
																	<c:if test="${not empty cdu.location}">
																		<c:set var="cduLabel">${cduLabel} - ${cdu.location}</c:set>
																	</c:if>
																	<form:option data-registered="${cdu.registeredIndicator}" data-macaddress="${cdu.macAddress}" value="${cduIdentifier}" label="${cduLabel}" />
																</c:forEach>
															</form:select>
															<%-- Add the actual spring error message underneath the select box --%>
															<spring:hasBindErrors name="cduSearchCommand">
																<div class="help-block" element="span">
																	${errors.hasFieldErrors('selectedMacAddress') ? errors.getFieldError('selectedMacAddress').defaultMessage : ''}
																</div>
															</spring:hasBindErrors>
													</div>
												</spring:bind>
												<div class="col-md-6">
													<button type="button" id="btnRestartAllCdu" name="btnRestartAllCdu" class="btn btn-primary" data-toggle="modal" data-target="#restartAllModal">
														<span class="glyphicon glyphicon-off"> </span>
														Restart All CDUs
													</button>
												</div>
											</div>

											<div class="form-group">
												<div class="col-md-12">
													<%-- Search, register & unregister buttons --%>

													<button name="btnShowCdu" id="btnShowCdu" class="btn btn-primary" value="showCdu">
														<span class="glyphicon glyphicon-search"> </span>
														Show CDU Information
													</button>

													<button id="btnShowRegisterCdu" name="btnShowRegisterCdu" class="btn btn-primary" >
														<span class="glyphicon glyphicon-plus-sign"> </span>
														Register CDU
													</button>

													<button type="button" id="btnUnRegisterCdu" name="btnUnRegisterCdu" class="btn btn-primary" data-toggle="modal" data-target="#unregisterModal">
														<span class="glyphicon glyphicon-minus-sign"> </span>
														Unregister CDU
													</button>

													<button type="button" id="btnRestartCdu" name="btnRestartCdu" class="btn btn-primary" data-toggle="modal" data-target="#restartModal">
														<span class="glyphicon glyphicon-off"> </span>
														Restart CDU
													</button>
												</div>
											</div>
											<!-- /form-group -->

											<%--
												Render the show cdu results if cdu present
											--%>
											<c:if test="${not empty cdu}">
												<div id="cduInformation" class="form-group">
													<div class="col-md-12">
														<div class="panel panel-primary">
															<div class="panel-heading"><h3 class="panel-title">Details for CDU with MAC Address: <c:out value="${cdu.macAddress}"/></h3></div>
															<div class="panel-body">
																<div class="col-md-6">
																	<p><strong>IP Address : </strong> <c:out value="${cdu.ipAddress}"/></p>
																	<p><strong>Court Site : </strong> <c:out value="${cdu.courtSiteName}"/></p>
																	<p><strong>CDU Number : </strong> <c:out value="${cdu.cduNumber}"/></p>
																	<p><strong>Description : </strong> <c:out value="${cdu.description}"/></p>
																	<p><strong>Location : </strong> <c:out value="${cdu.location}"/></p>
																	<p><strong>Registered : </strong> <c:out value="${cdu.registeredIndicator}"/></p>
																	<p><strong>Known Offline : </strong> <c:out value="${cdu.offlineIndicator}"/></p>
																</div>
																<div class="col-md-6">
																	<c:if test="${not empty cdu.urls}">
																		<p><strong>Associated URLS : </strong></p>
																		<ul>
																			<c:forEach var="myUrl" items="${cdu.urls}">
																				<li><a href=<c:url value="${cdu.courtSitePageUrl}uri=${myUrl.url}"/>
																				target="pdm_xhibit"><c:out value ="${myUrl.url}"/></a></li>
																			</c:forEach>
																		</ul>
																	</c:if>
																	
																</div>
															</div>
														</div>
														<!-- /panel panel-default -->
													</div>
													<!-- /col-md-12 -->
												</div>
												<!-- /form-group -->
											</c:if>

											<div class="form-group">
												<div class="col-md-12">
													<button name="btnShowAmendCdu" id="btnShowAmendCdu" class="btn btn-primary" value="amend">
														<span class="glyphicon glyphicon-edit"> </span>
														Amend CDU
													</button>
													<%-- Note: Same controller method used for both #btnAmendUrl & #btnRemoveUrl  --%>
													<button name="btnManageUrl" id="btnAddUrl" class="btn btn-primary" value="add">
														<span class="glyphicon glyphicon-plus-sign"> </span>
														Add URL Mapping
													</button>
													<button name="btnManageUrl" id="btnRemoveUrl" class="btn btn-primary" value="remove">
														<span class="glyphicon glyphicon-minus-sign"> </span>
														Remove URL Mapping
													</button>
												</div>
											</div>
												<!-- /form-group -->
										</c:when>

										<c:otherwise>
											<%-- Empty cduList --%>
											<div class="alert alert-warning">
												<p>No CDUS located by search.</p>
											</div>
										</c:otherwise>
									</c:choose>
								</div>
							</c:if>

							<!-- Modal dialog for background processing, could do this as an include if required -->
							<div id="waitingModal" class="modal" data-backdrop="static" data-keyboard="false" role="dialog">
								<div class="modal-dialog modal-sm">
									<div class="modal-content">

										<div class="modal-header">
											<h4 class="modal-title"><span class="glyphicon glyphicon-time"></span>&nbsp;Please Wait</h4>
										</div>

										<div class="modal-body">
											<p>&nbsp;<span class="glyphicon glyphicon-refresh glyphicon-refresh-animate"></span>&nbsp;&nbsp;Processing your request...</p>
										</div>
									</div>
								</div>
							</div><!-- end of waitingModal -->

							<!-- Modal dialog for unregister CDU, could do this as an include if required -->
							<div id="unregisterModal" class="modal fade" role="dialog">
								<div class="modal-dialog">
									<div class="modal-content">

										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal">&times;</button>
											<h4 class="modal-title">Unregister CDU</h4>
										</div>

										<div class="modal-body">
											<p>You are about to unregister this CDU. Do you wish to continue?</p>
										</div>

										<div class="modal-footer">
											<button class="btn btn-primary" id="btnUnRegisterCduConfirm" name="btnUnRegisterCduConfirm">Confirm</button>
											<button class="btn btn-default" data-dismiss="modal">Cancel</button>
										</div>
									</div>
								</div>
							</div><!-- end of unregisterModal -->

							<!-- Modal dialog for restart CDU, could do this as an include if required -->
							<div id="restartModal" class="modal fade" role="dialog">
								<div class="modal-dialog">
									<div class="modal-content">

										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal">&times;</button>
											<h4 class="modal-title">Restart CDU</h4>
										</div>

										<div class="modal-body">
											<p>You are about to restart this CDU. Do you wish to continue?</p>
										</div>

										<div class="modal-footer">
											<button class="btn btn-primary" id="btnRestartCduConfirm" name="btnRestartCduConfirm">Confirm</button>
											<button class="btn btn-default" data-dismiss="modal">Cancel</button>
										</div>
									</div>
								</div>
							</div><!-- end of restartModal -->

							<!-- Modal dialog for restart all CDUs, could do this as an include if required -->
							<div id="restartAllModal" class="modal fade" role="dialog">
								<div class="modal-dialog">
									<div class="modal-content">

										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal">&times;</button>
											<h4 class="modal-title">Restart All CDUs</h4>
										</div>

										<div class="modal-body">
											<p>You are about to restart all CDUs for this court site. Do you wish to continue?</p>
										</div>

										<div class="modal-footer">
											<button class="btn btn-primary" id="btnRestartAllCduConfirm" name="btnRestartAllCduConfirm">Confirm</button>
											<button class="btn btn-default" data-dismiss="modal">Cancel</button>
										</div>
									</div>
								</div>
							</div><!-- end of restartAllModal -->


						</form:form>
						<!-- end of form -->
						
						<!-- Separate form for cdu screenshot, target is set to _blank to open the screenshot in a new window -->
						<form:form name="screenshotForm" modelAttribute="command" action="/cdus/screenshot" target="_blank" method="GET">
							<%--Hidden fields here --%>
							<form:hidden path="xhibitCourtSiteId" />
							<form:hidden path="courtSiteId" />
							<form:hidden path="ipAddress" />
							<form:hidden path="selectedMacAddress" />
							<!-- Only show screenshot button if a cdu is selected -->
							<c:if test="${not empty cdu}">
								<button class="btn btn-primary">
									<span class="glyphicon glyphicon-picture" onclick="screenshotForm.submit()"> </span>
									Show CDU Screenshot
								</button>
							</c:if>
						</form:form>
						
					</div>
					<!-- /col-md-12 -->
                </div>
				<!-- /row -->




            </div>
			<!-- /container-fluid -->
        </div>
        <!-- /#page-content-wrapper -->

		<%@ include file="../common/footer.jsp"%>

    </div>
    <!-- /#wrapper -->

	<%@ include file="../common/scripts.jsp"%>

	<%-- CDU Specific javascript files --%>
	<script type="text/javascript" src="${context}/js/cdus.js"></script>

</body>

</html>
