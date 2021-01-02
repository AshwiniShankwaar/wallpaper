<?php
session_start();
require_once 'config.php';
$type = $_GET['Type'];
$tag = $_GET['Tag'];
$stmt = $link->prepare("SELECT Type, DisplayName, downloadurl FROM items  WHERE Type = '".$type."' AND Tag = '".$tag."' ORDER BY id DESC LIMIT 12;");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($type, $displayname, $downloadurl);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['Type'] = $type;
	      	              $index['DisplayName'] = $displayname;
	      	              $index['downloadurl'] = $downloadurl;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);
$link->close();
?>