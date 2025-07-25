<%-- 
	Start of common sidebar element for XHIBIT Public Display Manager 
	Has been revised to use a Bootstrap accordion type layout with the 
	collapse functions.
	M.Scullion 30/11/2016 - Separate manage local proxies into 2 new pages, one to manage the View/Unregister local proxy, the other
	to manage the register local proxy
--%>
<!-- Sidebar Wrapper with new 'collapsible Accordion' based menus -->
        <div id="sidebar-wrapper">
            <div id="panel-group" id="sidebaraccordion" role="tablist" aria-multiselectable="true">

				<div class="panel panel-default sb-panel sb-panel-default">
					<div class="panel panel-default sb-panel sb-panel-default">
						<div class="panel-heading sb-panel-heading" id="headingDashboard1" role="tab">
							<h4 class="panel-title sb-panel-title">							
								<a href="${context}/dashboard/dashboard">  <span class="glyphicon glyphicon-dashboard"></span>&nbsp;&nbsp;RAG Status</a>
							</h4>
							<!-- /h4-->
						</div>
						<!-- /#heading1-->
					</div>
					<!-- /panel panel-default-->
				</div>
				<!-- /panel panel-default-->
				
				
				<div class="panel panel-default sb-panel sb-panel-default">
					<div class="panel-heading sb-panel-heading" id="headingProxies" role="tab">
						<h4 class="panel-title sb-panel-title">
							<a class="collapsed" data-toggle="collapse" href="#sidebarcollapse2" data-parent="#sidebaraccordion"> <span class="glyphicon glyphicon-cog"></span>&nbsp;&nbsp;Local Proxies</a>
						</h4>
						<!-- /h4-->
					</div>
					<!-- /#headingProxies-->
					<div class="panel-collapse collapse" id="sidebarcollapse2">					
						<div>
							<a class="list-group-item"  href="${context}/proxies/view_localproxy">  <span class="glyphicon glyphicon-eye-open"></span>&nbsp;&nbsp;Manage Local Proxy</a>							
							<a class="list-group-item"  href="${context}/proxies/register_localproxy">  <span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;Register Local Proxy</a>							
						</div>
					</div>
					<!-- /panel-collapse -->
				</div>
				<!-- /panel panel-default-->
				
				<div class="panel panel-default sb-panel sb-panel-default">
					<div class="panel panel-default sb-panel sb-panel-default">
						<div class="panel-heading sb-panel-heading" id="headingCdus" role="tab">
							<h4 class="panel-title sb-panel-title">
								<a href="${context}/cdus/cdus">  <span class="glyphicon glyphicon-console"></span>&nbsp;&nbsp;Manage CDUS</a>
							</h4>
							<!-- /h4-->
						</div>
						<!-- /#heading1-->
					</div>
					<!-- /panel panel-default-->
				</div>
				<!-- /panel panel-default-->
				
				<div class="panel panel-default sb-panel sb-panel-default">
					<div class="panel panel-default sb-panel sb-panel-default">
						<div class="panel-heading sb-panel-heading" id="headingUsers" role="tab">
							<h4 class="panel-title sb-panel-title">
								<a href="${context}/users/users">  <span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;Manage Users</a>
							</h4>
							<!-- /h4-->
						</div>
						<!-- /#heading1-->
					</div>
					<!-- /panel panel-default-->
				</div>
				<!-- /panel panel-default-->			
				<%--</security:authorize>--%>

				
				<div class="panel panel-default sb-panel sb-panel-default">
					<div class="panel-heading sb-panel-heading" id="headingManageData" role="tab">
						<h4 class="panel-title sb-panel-title">
							<a class="collapsed" data-toggle="collapse" href="#sidebarcollapse3" data-parent="#sidebaraccordion"> <span class="glyphicon glyphicon-console"></span>&nbsp;&nbsp;Manage Data</a>
						</h4>
						<!-- /h4-->
					</div>
					<!-- /#headingProxies-->
					<div class="panel-collapse collapse" id="sidebarcollapse3">					
						<div>
							<a class="list-group-item"  href="${context}/display/view_display">  <span class="glyphicon glyphicon-eye-open"></span>&nbsp;&nbsp;Manage Display Data</a>							
							<a class="list-group-item"  href="${context}/court/view_court">  <span class="glyphicon glyphicon-eye-open"></span>&nbsp;&nbsp;Manage Court Data</a>							
							<a class="list-group-item"  href="${context}/courtroom/view_courtroom">  <span class="glyphicon glyphicon-eye-open"></span>&nbsp;&nbsp;Manage Court Room Data</a>
							<a class="list-group-item"  href="${context}/judge/view_judge">  <span class="glyphicon glyphicon-eye-open"></span>&nbsp;&nbsp;Manage Judge Data</a>							
							<a class="list-group-item"  href="${context}/hearing/view_hearing">  <span class="glyphicon glyphicon-eye-open"></span>&nbsp;&nbsp;Manage Hearing Data</a>							
							<a class="list-group-item"  href="${context}/judgetype/view_judgetype">  <span class="glyphicon glyphicon-eye-open"></span>&nbsp;&nbsp;Manage Judge Type Data</a>							
						</div>
					</div>
					<!-- /panel-collapse -->
				</div>
			</div>
			<!-- /#sidebaraccordion -->			
        </div>
        <!-- /#sidebar-wrapper -->
<!-- End of common sidebar element for XHIBIT Public Display Manager -->