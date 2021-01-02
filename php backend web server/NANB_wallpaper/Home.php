<?php
session_start();

require 'config.php';
$server_ip = gethostbyname(gethostname());
//echo "<script type='text/javascript'>alert('".$server_ip."');</script>";
    $Type = $Tag = "";
    if( $_SERVER["REQUEST_METHOD"] == "POST" ){
	    if(empty(trim($_POST["type"]))){
		  echo "<script type='text/javascript'>alert('Please enter the file Type.');</script>"; 
		} else{
		  $Type = trim($_POST["type"]);
		}
		  
		  if(empty(trim($_POST["name"]))){
		  echo "<script type='text/javascript'>alert('Please enter file name.');</script>"; 
		} else{
		  $name = trim($_POST["name"]);
		}  
		    // Check if password is empty
	    if(empty(trim($_POST["tag"]))){
		  echo "<script type='text/javascript'>alert('Please enter a tag to the file.');</script>"; 
		} else{
	      $Tag = trim($_POST["tag"]);
		}

		switch ($Type) {
			case 'Themes':
				$target = 'upload/Themes/';
				break;
			case 'Ringtones':
				$target = 'upload/Ringtone/';
				break;
			case 'Wallpapers':
				$target = 'upload/Wallpaper/';
				break;
			case 'Fonts':
				$target = 'upload/Fonts/';
				break;
			case 'Slider':
				$target = 'upload/Slider/';
				break;
			default:
				$target = 'upload/';
				break;
		}

        $server_ip = gethostbyname(gethostname());

		
		$filename = basename($_FILES["file"]["name"]);
		$downloadurl = 'http://192.168.225.213:8080/NANB_wallpaper/'.$target.$filename;
		$targetfiledir = $target . $filename;
		$fileType = pathinfo($targetfiledir, PATHINFO_EXTENSION);



		
		   if (!file_exists($targetfiledir)) {
		   	$uploadTo = null;
				if(isset($_FILES['file'])){
				 $fileSize = $_FILES["file"]["size"];
				 $filesizeafterconversion = formatSizeUnits($fileSize);
				  $file_tmp = $_FILES['file']['tmp_name'];
				  $uploadTo = $target . $filename;
				  $movefile = move_uploaded_file($file_tmp,$uploadTo);
					  if($movefile){
					  	$uploadMainTo = null;
					  	$uploadLocation = "backgroundPic/";
						if(isset($_FILES['background_image'])){
						  $main_image_name = $_FILES['background_image']['name'];
						  $main_image_size = $_FILES['background_image']['size'];
						  $main_image_tmp = $_FILES['background_image']['tmp_name'];
						  $uploadMainTo = $uploadLocation.$main_image_name;
						  $backgroundurl = 'http://192.168.225.213:8080/NANB_wallpaper/backgroundPic/'.$main_image_name;
						  $moveMain = move_uploaded_file($main_image_tmp,$uploadMainTo);
						  if($moveMain){
						  	 $insert = $link->query("INSERT into items(Type,Tag,Filename,downloadurl,size,DisplayName,backgroundurl) VALUES ('".$Type."','".$Tag."','".$filename."','".$downloadurl."','".$filesizeafterconversion."','".$name."','".$backgroundurl."')");
					 	       if($insert){
					 	       	echo "<script type='text/javascript'>alert('File uploaded successfully.');</script>";
					 	        }else{
					 	        	echo "<script type='text/javascript'>alert('Error accur while inserting data.');</script>";
					 	        }
						    }else{
						    	echo "<script type='text/javascript'>alert('sorry there was an erro  while uploading file.');</script>";
						    }
						}else{
							echo "<script type='text/javascript'>alert('Please select a background image to upload.');</script>"; 
						}
			 	      
			          }else{
			 	     //sorry there was an erro  while uploading file.
			        	echo "<script type='text/javascript'>alert('sorry there was an erro  while uploading file.');</script>";
			          } 
				}else{
					echo "<script type='text/javascript'>alert('Please select a file to upload.');</script>"; 
				}
		        
		    }else{
		    	echo "<script type='text/javascript'>alert('File already exit.');</script>";
		    }
    }


     function formatSizeUnits($bytes)
    {
        if ($bytes >= 1073741824)
        {
            $bytes = number_format($bytes / 1073741824, 2) . ' GB';
        }
        elseif ($bytes >= 1048576)
        {
            $bytes = number_format($bytes / 1048576, 2) . ' MB';
        }
        elseif ($bytes >= 1024)
        {
            $bytes = number_format($bytes / 1024, 2) . ' KB';
        }
        elseif ($bytes > 1)
        {
            $bytes = $bytes . ' bytes';
        }
        elseif ($bytes == 1)
        {
            $bytes = $bytes . ' byte';
        }
        else
        {
            $bytes = '0 bytes';
        }

        return $bytes;
}
?>
<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Themes</title>
    <link rel="icon" href="res/Themes.png">
    <style type="text/css">
		    	body {
		  background-image: url('res/wallpapwerbackground.jpg');
		  background-repeat: no-repeat;
		  background-attachment: fixed;
		  background-size: cover;
		}

		    </style>
     <link rel="stylesheet" type="text/css" href="css/style.css">
  </head>
  <body>
  	<div style="margin-top: 10px; margin-left: 10px">
  		<a href="logout.php" class="btn btn-danger">Sign Out</a>
  	</div>
    <div class="row" style="margin-left: 15px; margin-right: 15px;">
    	<div class="col-8">
	    	<div class="Home_container" style="width: 500px; height: 550px;margin: 10px auto;">
	    	<CENTER><H3><U>Insert Data</U></H3></CENTER>
	    	<form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="POST" enctype="multipart/form-data" style="margin-top: 10px">
	    		<label for="Type" style="margin-top: 10px"><b>Select your File to upload.</b></label>
	    		<input type="file" name="file">
	    		<label for="Type" style="margin-top: 10px"><b>Select your background image to upload.</b></label>
	    		<input type="file" name="background_image">
	    		<label for="Type" style="margin-top: 10px"><b>Enter Name of the file that will display on the app.</label>
	    		<input type="text" name="name" placeholder="Name" style="border-radius: 10px" required >
	    		 <label for="Type" style="margin-top: 10px"><b>Enter the type of file</b> (Wallpapers, Ringtones, Fonts, Themes, Slider).</label>
	    		<input type="text" name="type" placeholder="Type" style="border-radius: 10px" required >
	    		<label for="Type" style="margin-top: 10px"><b>Enter tags to the file.</label>
	    		<input type="text" name="tag" placeholder="Tags" style="border-radius: 10px" required >
	    		<button type="submit">Upload file</button>
	    	</form>
	        </div>
    	</div>
    	<div class="col-4">
    		<div class="Home_container" style="width: 400px; height: auto; margin-top: 10px auto;">
    			<CENTER><H5><U>Catagory(Tag) List</U></H5></CENTER>
    		</div>
    	</div>
    </div>
    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
  </body>
</html>