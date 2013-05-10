<?php
include 'databasecon.php';

$findQuery = "CALL findIdVehicle("."'".$_POST['regnr']."'".");";
echo $findQuery;
$idv = mysqli_query($con,$findQuery);
$id = mysqli_fetch_assoc($idv);
echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
echo "<idVehicle>".$id['idVehicle']."</idVehicle>";
mysqli_close($con);
?>


