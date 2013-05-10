<?php
include 'databasecon.php';
 
$query = "CALL setField(".$_POST["idVehicle"].","."'".$_POST["field"]."'".","."'".$_POST["value"]."'".");";

$setField = mysqli_query($con,$query);


mysqli_close($con);
?>


