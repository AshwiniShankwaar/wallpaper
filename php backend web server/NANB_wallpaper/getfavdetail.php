<?php

session_start();
require_once "config.php";

$id = $_GET['id'];
$userid = $_GET['userid'];
$sql = "SELECT * FROM favorite WHERE userid = '".$userid."'&& fileid = '".$id."'";
$result = $link->query($sql);
$resultarray = array();
if ($result->num_rows > 0) {
   $index = array();
	$index['message'] = "Favorite";
	array_push($resultarray, $index);
} else {
     $index = array();
	$index['message'] = "unFavorite";
	array_push($resultarray, $index);
}
echo json_encode($resultarray);
$link->close();
?>