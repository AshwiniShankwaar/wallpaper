<?php 
session_start();
require_once "config.php";
$type = $_GET['Type'];
$sql = "SELECT Tag FROM items WHERE Type = '".$type."'";
$result = $link->query($sql);
$catagory_array = array();
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        if(in_array( $row['Tag'], array_column($catagory_array, 'Tag'))){
        	//echo "found";
        }else{
        	$index = array();
        	$index['Tag'] =  $row['Tag'];
	      	array_push($catagory_array, $index);
        	
        }
    }
} else {
    echo "0 results";
}
echo json_encode($catagory_array);
$link->close();
?>