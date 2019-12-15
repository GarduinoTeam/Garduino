<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

  <!-- Custom styles for this template-->
  <link href="css/sb-admin-2.min.css" rel="stylesheet">
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
  <link rel="stylesheet" type="text/css" href="css/jquery.datepick.css"> 
  <script type="text/javascript" src="js/jquery.plugin.js"></script> 
  <script type="text/javascript" src="js/jquery.datepick.js"></script>
</head>
<body>
<p>Date: <input type="text" id="datepick"></p>
<br/>
<h4 id="selectedDtaeVal"></h4>
<script>
$(function() {
    $.datepick.setDefaults({
        onClose:function(date, inst){
            $("#selectedDtaeVal").html(date);
        }
    });

    $( "#datepick" ).datepick({ 
        multiSelect: 999, monthsToShow: 2, 
        showTrigger: '#calImg'});
});
</script>
</body>
</html>