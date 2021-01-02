<?php
session_start();
require_once "config.php";

     $tag = $_GET['type'];

     $stmt = $link->prepare("SELECT id, downloadurl FROM items  WHERE Type = '".$tag."'  ORDER BY id DESC LIMIT 5;");
	
	//executing the query 
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($id, $downloadurl);
	$resultarray = array();
	      while ($stmt->fetch()) {
	      	 $index = array();
	      	 $index['id'] = $id;
	      	 $index['downloadurl'] = $downloadurl;
	      	 array_push($resultarray, $index);
	      	}
	      	
	     echo json_encode($resultarray);
	    
?>