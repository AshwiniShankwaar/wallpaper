<?php
session_start();
require_once "config.php";
$fileid = $_GET['id'];
$stmt = $link->prepare("SELECT user_id,comment FROM comment WHERE fileid ='".$fileid."' ORDER BY id DESC LIMIT 1;");
	
	                     //executing the query 
	                     $stmt->execute();
	
	                     //binding results to the query 
	                     $stmt->bind_result($userid,$comment);
	                     $resultarray = array();
	                     while ($stmt->fetch()) {
	      	              $index = array();
	      	              $index['userid'] = $userid;
                          $index['comment'] = $comment;
	      	              array_push($resultarray, $index);
	      	              }
	      	
	                    echo json_encode($resultarray);

?>
