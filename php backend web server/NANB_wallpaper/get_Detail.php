<?php
session_start();
require_once "config.php";

     
      $filename = $_GET['filename'];
      $type = $_GET['type'];
      //die($filename);	
     /**$stmt = $link->prepare("SELECT id, Tag, Type, size, DisplayName  FROM items  WHERE Filename = '".$filename."';");
	//executing the query 
	$stmt->execute();
	
	//binding results to the query 
	$stmt->bind_result($id, $type, $tag, $size, $displayname);
	$resultarray = array();
	      while ($stmt->fetch()) {
	      	 $index = array();
	      	 $index['id'] = $id;
	      	 $index['Tag'] = $tag;
	      	 $index['Type'] = $type;
	      	 $index['size'] = $size;
	      	 $index['DisplayName'] = $displayname;
	      	 array_push($resultarray, $index);
	      	}
	      	
	     echo json_encode($resultarray);
	    **/
	     $sql = "SELECT id, Tag, Type, size, DisplayName, Filename,backgroundurl  FROM items  WHERE Filename = '".$filename."' && Type = '".$type."'";
	     $result = $link->query($sql);
		$resultarray = array();
		if ($result->num_rows > 0) {
		    // output data of each row
		    $row = $result->fetch_assoc();
		    $id = $row['id'];
		    $type = $row['Type'];
		    $tag = $row['Tag'];
		    $size = $row['size'];
		    $displayname = $row['DisplayName'];
		    $filename = $row['Filename'];
             $backgroundurl = $row['backgroundurl'];
             $downloadsql = "SELECT id FROM downloads WHERE fileId = '".$id."' && Filename = '".$filename."' && FileType = '".$type."' && Filetag = '".$tag."' && DisplayName = '".$displayname."'";
             $downloadresult = $link->query($downloadsql);
             $numrows = $downloadresult->num_rows;
              $index = array();
		      	 $index['id'] = $id;
		      	 $index['Tag'] = $tag;
		      	 $index['Type'] = $type;
		      	 $index['size'] = $size;
		      	 $index['DisplayName'] = $displayname;
		      	 $index['Filename'] = $filename;
		      	 $index['backgroundurl']=$backgroundurl;
		      	 $index['Total_downloads'] = strval($numrows);
		      	 array_push($resultarray, $index);


		} else {
		    echo "0 results";
		}
		echo json_encode($resultarray);
		$link->close();
		?>
