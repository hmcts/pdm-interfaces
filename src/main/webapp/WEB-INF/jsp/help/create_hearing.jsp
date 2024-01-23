<%@ include file="../common/include.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, shrink-to-fit=no, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Create Hearing Type - Help</title>

    <%@ include file="../common/stylesheets.jsp"%>

</head>

<body>

	<header>

		<h2>Create Hearing Type - Help</h2>

	</header>

	<div class="container-fluid">

		<div class="row">

			<div class="col-md-12">

				<h3>Create Hearing Type</h3>

				<%-- COMMENTING OUT AUTHENTICATION BLOCK TEMPORARILY FOR TESTING --%>
				<%-- <security:authorize access="hasRole('ROLE_ADMIN')"> --%>
					<p>The &quot;Create Hearing Type&quot; creates a new Hearing Type entry.</p>

					<p>The &quot;Return to Manage Hearing Type&quot; returns back to the Manage Hearing Type screen.</p>
				<%--</security:authorize> --%>
			</div>
			<!-- /col-md-12 -->
		</div>
		<!-- /row -->
	</div>
	<!-- /container-fluid -->

	<%@ include file="../common/scripts.jsp"%>

</body>

</html>