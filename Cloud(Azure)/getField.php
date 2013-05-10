<?php
include 'databasecon.php';
 
$query = "CALL getField(".$_POST["idVehicle"].","."'".$_POST["field"]."'".");";

$value = mysqli_query($con,$query);

echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>";


$result = mysqli_fetch_assoc($value);
  echo "<value>";
  echo $result['result'];
  echo "</value>";

mysqli_close($con);
?>


