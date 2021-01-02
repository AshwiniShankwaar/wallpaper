<?php


session_start();
require_once "config.php";


$id = $_POST['id'];
$type = $_POST['type'];
$tag = $_POST['tag'];
$filename = $_POST['filename'];
$displayname = $_POST['displayname'];
$downloadby = $_POST['downloadBy'];

$sql = "INSERT INTO downloads (fileId, FileType, Filetag, Filename,DisplayName,Downloadby)
VALUES ('".$id."', '".$type."', '".$tag."','".$filename."','".$displayname."','".$downloadby."')";

if ($link->query($sql) === TRUE) {
    $sql_for_check_file_exit_in_count = "SELECT * FROM downloadcount WHERE fileid = '".$id."'";
    $Count_result = $link->query($sql_for_check_file_exit_in_count);
    if ($Count_result->num_rows > 0) {
    // output data of each row
	    $getcount = "SELECT id,downloadcount FROM downloadcount WHERE fileid = '".$id."'";
	    $getcount_result = $link->query($getcount);
	    if($getcount_result->num_rows > 0){
	    	while($row = $getcount_result->fetch_assoc()) {
	    		$count_data = $row['downloadcount'];
	    		$row_id = $row['id'];
	    		$final_data = $count_data + 1 ;
	    		updatecountdata($row_id,$final_data);
           } 
	    }else{
	    	
	    }
    } else {
    //insert count data for the first time
    	insertcountdata($id,1,$type);
    }
} else {
    echo "Error: " . $sql . "<br>" . $link->error;
}

$link->close();

function updatecountdata($row_id,$downloadcount)
{
	include "config.php";
	$sql_data = "UPDATE downloadcount SET downloadcount='".$downloadcount."' WHERE id=".$row_id."";

		if ($link->query($sql_data) === TRUE) {
		    echo "Record updated successfully";
		} else {
		    echo "Error updating record: " . $conn->error;
		}
		$link->close();
}


 function insertcountdata($id,$downloadcount,$filetype)
{
	include "config.php";
	$datainsert = "INSERT INTO downloadcount (fileid,downloadcount,filetype) VALUES ('".$id."','".$downloadcount."','".$filetype."')";
	//die($datainsert);
    	if($link->query($datainsert) === TRUE){
    		echo "data inserted";
    	}else{
    		echo "Error".$datainsert."<br>".$link->error;
    	}

    	$link->close();
}
?>