<?php
include 'databasecon.php';

$addVehQuery = "CALL addVehicle("."'".$_POST["regnr"]."'".","."'".$_POST["password"]."'".","."'".$_POST["owner"]."'".");";
echo $addVehQuery;
$lastLoc = mysqli_query($con,$addVehQuery);

mysqli_close($con);
?>


