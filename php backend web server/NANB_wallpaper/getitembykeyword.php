<?php
session_start();
require_once 'config.php';

$keyword = $_GET['keyword'];
//$keywordType = $_GET['Type'];
$sql = "SELECT * FROM items WHERE Tag = '".$keyword."' OR Type = '".$keyword."'";
$result = $link->query($sql);

$result_data = array();
if ($result->num_rows > 0) {
    // output data of each row
    $sqlsearch = "SELECT id,keycount FROM keywordcount WHERE keyword = '".$keyword."'";
        $resultsearch = $link->query($sqlsearch);

		if ($resultsearch->num_rows > 0) {
		    // output data of each row
		    $rowsearch = $resultsearch->fetch_assoc();
		    $count = $rowsearch['keycount']+1;
		    $id = $rowsearch['id'];
		    $sqldata = "UPDATE keywordcount SET keycount='".$count."' WHERE id='".$id."'";

			if ($link->query($sqldata) === TRUE) {
			    //echo "Record updated successfully";
			    while($row = $result->fetch_assoc()) {
                $index = array();
		        $index['Type'] = $row['Type'];
		        $index['DisplayName'] = $row['DisplayName'];
		        $index['downloadurl'] = $row['downloadurl'];
		        array_push($result_data, $index);
             }
			    
			} else {
			    echo "Error updating record: " . $conn->error;
			}
		} else {
		    //insert
		     $sqlinsert = "INSERT INTO keywordcount (keyword,keycount)VALUES ('".$keyword."',1)";

		if ($link->query($sqlinsert) === TRUE) {
		    //echo "data inserted";
		     while($row = $result->fetch_assoc()) {
                $index = array();
		        $index['Type'] = $row['Type'];
		        $index['DisplayName'] = $row['DisplayName'];
		        $index['downloadurl'] = $row['downloadurl'];
		        array_push($result_data, $index);
		    }
		} else {
		    echo "Error: " . $sql . "<br>" . $conn->error;
		}

		}
} else {
    
    
}
echo json_encode($result_data);
$link->close();





?>





