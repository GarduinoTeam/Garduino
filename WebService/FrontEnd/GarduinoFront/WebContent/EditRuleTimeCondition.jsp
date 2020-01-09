<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@page import="beans.RuleTimeCondition"%>
     <%@page import="java.sql.Date"%>
<!DOCTYPE html>
<html lang="en">
 <%RuleTimeCondition ruleTimeCondition = (RuleTimeCondition)session.getAttribute("ruleTimeCondition");
 boolean status;
 status=ruleTimeCondition.getStatus();
 /*Days of the Week*/
 boolean mo=false;
 boolean tu=false;
 boolean wed=false;
 boolean th=false;
 boolean fr=false;
 boolean sa=false;
 boolean su=false;
 boolean jan=false;boolean feb=false;boolean mar=false;boolean apr=false;
 boolean may=false;boolean jun=false;boolean jul=false;boolean aug=false;
 boolean sep=false;boolean oct=false;boolean nov=false;boolean dec=false;
 Date sepecificDate=ruleTimeCondition.getSpecificDates()[0];
 /*Months of the Year*/
 /*Check days of the Week*/
 String days=ruleTimeCondition.getDaysOfWeek();
 if(days.charAt(0)=='1'){
	 mo=true;
 }
 if(days.charAt(1)=='1'){
	 tu=true;
 }
 if(days.charAt(2)=='1'){
	 wed=true;
 }
 if(days.charAt(3)=='1'){
	 th=true;
 }
 if(days.charAt(4)=='1'){
	 fr=true;
 }
 if(days.charAt(5)=='1'){
	 sa=true;
 }
 if(days.charAt(6)=='1'){
	 su=true;
 }
 String months=ruleTimeCondition.getMonthsOfTheYear();
 if(months.charAt(0)=='1'){
	 jan=true;
 }
 if(months.charAt(1)=='1'){
	 feb=true;
 }
 if(months.charAt(2)=='1'){
	 mar=true;
 }
 if(months.charAt(3)=='1'){
	 apr=true;
 }
 if(months.charAt(4)=='1'){
	 may=true;
 }
 if(months.charAt(5)=='1'){
	 jun=true;
 }
 if(months.charAt(6)=='1'){
	 jul=true;
 }
 if(months.charAt(7)=='1'){
	 aug=true;
 }
 if(months.charAt(8)=='1'){
	 sep=true;
 }
 if(months.charAt(9)=='1'){
	 oct=true;
 }
 if(months.charAt(10)=='1'){
	 nov=true;
 }
 if(months.charAt(11)=='1'){
	 dec=true;
 }
 /*Check Days of The Year*/
 
 %>
<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>Garduino</title>

  <!-- Custom fonts for this template-->
  <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

  <!-- Custom styles for this template-->
  <link href="css/sb-admin-2.min.css" rel="stylesheet">
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
  <link rel="stylesheet" type="text/css" href="css/jquery.datepick.css"> 
  <script type="text/javascript" src="js/jquery.plugin.js"></script> 
  <script type="text/javascript" src="js/jquery.datepick.js"></script>
</head>

<body id="page-top">

	<%String deviceId = (String)session.getAttribute("deviceId");
 	String userId = (String)session.getAttribute("userId");
    String conditionId = (String)request.getAttribute("conditionId");%>
 	
  
  <!-- Page Wrapper -->
  <div id="wrapper">

    <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

      <!-- Sidebar - Brand -->
      <a class="sidebar-brand d-flex align-items-center justify-content-center" href="AdminMenu.jsp">
        <div class="sidebar-brand-icon rotate-n-15">
          <i class="fas fa-laugh-wink"></i>
        </div>
        <div class="sidebar-brand-text mx-3">Garduino <sup>Admin</sup></div>
      </a>

      <!-- Divider -->
      <hr class="sidebar-divider my-0">

      <!-- Nav Item - Dashboard -->
      <li class="nav-item ">
        <a class="nav-link" href="AdminMenu.jsp">
          <i class="fas fa-fw fa-tachometer-alt"></i>
          <span>Dashboard</span></a>
      </li>

      <!-- Divider -->
      <hr class="sidebar-divider">

      <!-- Heading -->
      <div class="sidebar-heading">
        Developer
      </div>

      <!-- Nav Item - Pages Collapse Menu -->
       <li class="nav-item active">
        <a class="nav-link collapsed" href="UsersList.jsp" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo">
          <i class="fas fa-fw fa-user"></i>
          <span>Users</span>
        </a>
        <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
          <div class="bg-white py-2 collapse-inner rounded">
            <h6 class="collapse-header">Custom Components:</h6>
            <a class="collapse-item " href="UsersList.jsp">Users List</a>     
            <a class="collapse-item " href="UserDevices.jsp">Devices</a>     
            <a class="collapse-item active" href="Rules.jsp">Rules</a>     
          </div>
        </div>
      </li>


      <!-- Divider -->
      <hr class="sidebar-divider d-none d-md-block">

      <!-- Sidebar Toggler (Sidebar) -->
      <div class="text-center d-none d-md-inline">
        <button class="rounded-circle border-0" id="sidebarToggle"></button>
      </div>

    </ul>
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

      <!-- Main Content -->
      <div id="content">

        <!-- Topbar -->
        <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

          <!-- Sidebar Toggle (Topbar) -->
          <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
            <i class="fa fa-bars"></i>
          </button>

          <!-- Topbar Search -->
          <form class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
            <div class="input-group">
              <input type="text" class="form-control bg-light border-0 small" placeholder="Search for..." aria-label="Search" aria-describedby="basic-addon2">
              <div class="input-group-append">
                <button class="btn btn-primary" type="button">
                  <i class="fas fa-search fa-sm"></i>
                </button>
              </div>
            </div>
          </form>

          <!-- Topbar Navbar -->
          <ul class="navbar-nav ml-auto">
          
            <!-- Nav Item - User Information -->
            <li class="nav-item dropdown no-arrow">
              <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <span class="mr-2 d-none d-lg-inline text-gray-600 small">Admin Valerie</span>
                <img class="img-profile rounded-circle" src="https://source.unsplash.com/QAB-WJcbgJk/60x60">
              </a>
              <!-- Dropdown - User Information -->
              <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="userDropdown">
                <a class="dropdown-item" href="#">
                  <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                  Profile
                </a>
                <a class="dropdown-item" href="#">
                  <i class="fas fa-cogs fa-sm fa-fw mr-2 text-gray-400"></i>
                  Settings
                </a>
                <a class="dropdown-item" href="#">
                  <i class="fas fa-list fa-sm fa-fw mr-2 text-gray-400"></i>
                  Activity Log
                </a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                  <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                  Logout
                </a>
              </div>
            </li>

          </ul>

        </nav>
        <!-- End of Topbar -->

        <!-- Begin Page Content -->
   <div class="container">

    <div class="card o-hidden border-0 shadow-lg my-5">
      <div class="card-body p-0">
        <!-- Nested Row within Card Body -->
        <div class="row">
          <div class="col">
            <div class="p-5">
              <div class="text-center">
                <h1 class="h4 text-gray-900 mb-4">Edit a Rule Time Condition!</h1>
              </div>
              <form class="user" method="post" action="EditRuleTimeConditions">
              <input type="hidden" value=<%=conditionId%> name="conditionId">
                <div class="form-group">
                	<label  for="startTime">Start Time</label>
                    <input type="time" class="form-control form-control-user" min="00:00" max="23:59" id="startTime" name="startTime" placeholder="Start Time" value=<%=ruleTimeCondition.getStartTime()%>>
                </div>
                <div class="form-group">
                	<label  for="endTime">End Time</label>
                    <input type="time" class="form-control form-control-user" min="00:00" max="23:59" id="endTime" name="endTime" placeholder="End Time" value=<%=ruleTimeCondition.getEndTime()%>>
                </div>
                <label  for="days">Days of the Week</label>
                <div class="form-group" id="days">
                	<label class="checkbox-inline">
                	 <%
                      if(mo){
                      %>
                      <input type="checkbox" name="Mo" checked >Mo
                       <%}else{ %>
      					<input type="checkbox" name="Mo" >Mo
      					<%} %>
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(tu){
                      		
                      	%>
                      	<input type="checkbox" name="Tu" checked >Tu
                      	<%}else{ %>
      					<input type="checkbox" name="Tu" >Tu
      					<%} %>
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(wed){
                      		
                      	%>
                      	<input type="checkbox" name="Wed" checked  >Wed
                      	<%}else{ %>
      					<input type="checkbox" name="Wed"  >Wed
      					<%} %>
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(th){
                      		
                      	%>
      					<input type="checkbox" name="Th" checked >Th
      					<%}else{ %>
      					<input type="checkbox" name="Th" >Th
      					<%} %>
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(fr){
                      		
                      	%>
                      	<input type="checkbox" name="Fr" checked >Fr
      					
      					<%}else{ %>
      					<input type="checkbox" name="Fr" >Fr
      					<%} %>
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(sa){
                      		
                      	%>
      					<input type="checkbox" name="Sa" checked >Sa
      					<%}else{ %>
      					<input type="checkbox" name="Sa" >Sa
      					<%} %>
      					
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(su){
                      		
                      	%>
                      	<input type="checkbox" name="Su" checked >Su
                      	<%}else{ %>
      					<input type="checkbox" name="Su" >Su
      					<%} %>
    				</label>
    				
                    
                </div>
                 <label  for="months">Months of the Year</label>
                <div class="form-group" id="months">
                	<label class="checkbox-inline">
                		<%
                      	if(jan){
                      		
                      	%>
                      	<input type="checkbox" name="Jan"  checked  >Jan
                      	<%}else{ %>
      					<input type="checkbox" name="Jan"  >Jan
      					<%} %>
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(feb){
                      		
                      	%>
                      	<input type="checkbox" name="Feb" checked >Feb
                      	<%}else{ %>
                      	<input type="checkbox" name="Feb" >Feb
                      	<%} %>
      					
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(mar){
                      		
                      	%>
                      	<input type="checkbox" name="Mar" checked >Mar
                      	<%}else{ %>
                      	<input type="checkbox" name="Mar" >Mar
                      	<%} %>
      					
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(apr){
                      		
                      	%>
                      	<input type="checkbox" name="Apr" checked >Apr
                      	<%}else{ %>
                      	<input type="checkbox" name="Apr" >Apr
                      	<%} %>
      					
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(may){
                      		
                      	%>
                      	<input type="checkbox" name="May" checked >May
                      	<%}else{ %>
                      	<input type="checkbox" name="May" >May
                      	<%} %>
      					
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(jun){
                      		
                      	%>
                      	<input type="checkbox" name="Jun" checked >Jun
                      	<%}else{ %>
                      	<input type="checkbox" name="Jun" >Jun
                      	<%} %>
      					
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(jul){
                      		
                      	%>
                      	<input type="checkbox" name="Jul" checked >Jul
                      	<%}else{ %>
                      	<input type="checkbox" name="Jul" >Jul
                      	<%} %>
      					
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(aug){
                      		
                      	%>
                      	<input type="checkbox" name="Aug" checked >Aug
                      	<%}else{ %>
                      	<input type="checkbox" name="Aug" >Aug
                      	<%} %>
      					
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(sep){
                      		
                      	%>
                      	<input type="checkbox" name="Sep" checked >Sep
                      	<%}else{ %>
                      	<input type="checkbox" name="Sep" >Sep
                      	<%} %>
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(oct){
                      		
                      	%>
                      	<input type="checkbox" name="Oct" checked >Oct
                      	<%}else{ %>
                      	<input type="checkbox" name="Oct" >Oct
                      	<%} %>
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(nov){
                      		
                      	%>
                      	<input type="checkbox" name="Nov" checked >Nov
                      	<%}else{ %>
                      	<input type="checkbox" name="Nov" >Nov
                      	<%} %>
      					
    				</label>
    				<label class="checkbox-inline">
    					<%
                      	if(dec){
                      		
                      	%>
                      	<input type="checkbox" name="Dec" checked>Dec
                      	<%}else{ %>
                      	<input type="checkbox" name="Dec" >Dec
                      	<%} %>
      					
    				</label>
    				
                    
                </div>
                
                <div class="form-group">
                	<p>Specific Date<input type="text" id="datepick" name="date"></p>
					<br/>
					<h4 id="selectedDtaeVal"></h4>
					
					<script>
					<%
                  	if(sepecificDate!=null){
                  		
                  	%>
					$(function() {
    
    					$("#datepick").datepick({defaultDate:new Date(<%=sepecificDate.getYear()+1900%>,<%=sepecificDate.getMonth()%>,<%=sepecificDate.getDate()%>),dateFormat: 'yyyy-mm-dd',selectDefaultDate: true});
					});
					<%
                  	}else{
                  		
                  	%>
                  	$(function() {
                  	    
    					$("#datepick").datepick({dateFormat: 'yyyy-mm-dd'});
					});
                  	<%
                  	}
                  		
                  	%>
					</script>
                </div>
                <div class="form-group">
                      <div class="custom-control custom-checkbox small">
                      <%
                      if(status){
                      %>
                        <input type="checkbox" class="custom-control-input" id="customCheck" name="statusCheck" checked>
                        <%}else{ %>
                        <input type="checkbox" class="custom-control-input" id="customCheck" name="statusCheck">
                        <%} %>
                        
                        <label class="custom-control-label" for="customCheck">Active</label>
                      </div>
                </div>
                <input type="hidden" value=<%=ruleTimeCondition.getId()%> name="ruleTimeConditionId">
                <input type="submit" class="btn btn-primary btn-user btn-block" value="Edit Rule Time Condition">
                <hr>
                
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
        
      <!-- End of Main Content -->

      <!-- Footer -->
      <footer class="sticky-footer bg-white">
        <div class="container my-auto">
          <div class="copyright text-center my-auto">
            <span>Copyright &copy; Your Website 2019</span>
          </div>
        </div>
      </footer>
      <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

  </div>
  <!-- End of Page Wrapper -->

  <!-- Scroll to Top Button-->
  <a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
  </a>

  <!-- Logout Modal-->
  <div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
          <button class="close" type="button" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">×</span>
          </button>
        </div>
        <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
        <div class="modal-footer">
          <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
          <a class="btn btn-primary" href="LogIn.jsp">Logout</a>
        </div>
      </div>
    </div>
  </div>

 

</body>

</html>
       