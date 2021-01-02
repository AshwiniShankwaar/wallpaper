		<?php
		// Initialize the session
		session_start();
		 
		// Check if the user is already logged in, if yes then redirect him to welcome page
		if(isset($_SESSION["loggedin"]) && $_SESSION["loggedin"] === true){
		   header("location: Home.php");
		    exit;
		}
		 
		// Include config file
		require_once "config.php";
		 
		// Define variables and initialize with empty values
		$username = $password = "";
		$username_err = $password_err = "";
		 
		// Processing form data when form is submitted
		if($_SERVER["REQUEST_METHOD"] == "POST"){
		 
		    // Check if username is empty
		    if(empty(trim($_POST["username"]))){
		        $username_err = "Please enter username.";
		    } else{
		        $username = trim($_POST["username"]);
		    }
		    
		    // Check if password is empty
		    if(empty(trim($_POST["password"]))){
		        $password_err = "Please enter your password.";
		    } else{
		        $password = trim($_POST["password"]);
		    }
		    
		    // Validate credentials
		    if(empty($username_err) && empty($password_err)){
		        $sql = "SELECT id FROM admin WHERE Username = '$username' and Password = '$password'";
	      $result = mysqli_query($link,$sql);
	      $row = mysqli_fetch_array($result,MYSQLI_ASSOC);
	      
	      $count = mysqli_num_rows($result);
	      
	      // If result matched $myusername and $mypassword, table row must be 1 row
			
	      if($count == 1) {
	         
	         $_SESSION['login_user'] = $username;
	         
	         header("location: Home.php");
	      }else {
	         $error = "Your Login Name or Password is invalid";
	      }	
		      
		    }
		    
		    // Close connection
		    mysqli_close($link);
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
		    
		    <title>NANB Admin</title>
		    <link rel="icon" href="res/Logo.jpg">
		    <style type="text/css">
		    	body {
		  background-image: url('res/backgroundWebpage.jpg');
		  background-repeat: no-repeat;
		  background-attachment: fixed;
		  background-size: cover;
		}
		    </style>
		    <link rel="stylesheet" type="text/css" href="css/style.css">
		  </head>
		  <body >
		    <div class="container">
		    	<form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
		  <div class="imgcontainer">
		    <img src="res/Logo.jpg" alt="Logo" class="logo">
		  </div>
		    <label for="uname"><b>Username</b></label>
		    <input type="text" placeholder="Enter Username" name="username" required>

		    <label for="psw"><b>Password</b></label>
		    <input type="password" placeholder="Enter Password" name="password" required>

		    <button type="submit">Login</button>
		    <label>
		      <input type="checkbox" checked="checked" name="remember"> Remember me
		    </label>
		</form>

		  
		    </div>

		    <!-- Optional JavaScript -->
		    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
		    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
		    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
		    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
		  </body>
		</html>