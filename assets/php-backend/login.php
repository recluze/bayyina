<?php 
 require_once(dirname(__FILE__) . '/functions.php'); 

    if( isset($_GET['logout']) ) { 
        session_destroy(); 
        header("Location: login.php");
    }

    if ( !empty($_POST)) {
        // validation errors
        $password = null;
        $username = $_POST['username'];
        $password = $_POST['password'];
        
        if($admin_username == $username && $admin_userpass == sha1($password)) { 
            @session_start(); 
            $_SESSION['loggedin'] = true; 
            header("Location: list.php");
        } else { 
            session_destroy(); 
            $message = "Invalid credentials";
        }
    } 
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bayyina Login</title>
    <meta charset="utf-8">
    <link href="res/css/bootstrap.min.css" rel="stylesheet">
    <script src="res/js/bootstrap.min.js"></script>
</head>
 
<body>
<div class="container">
    <div class="row">
        <h3>Login to Bayyina Backend</h3>
    </div>

    <div class="row"> 
        <form method="POST" action="">
            <div class="form-group">
                <label for="inputUsername">Username</label>
                <input type="text" class="form-control" required="required" 
                        id="inputUsername" 
                        value="<?php echo !empty($username)?$username:'';?>" 
                        name="username" placeholder="Username">
            </div>
            <div class="form-group">
                <label for="inputPassword">Password</label>
                <input type="password" class="form-control" required="required" 
                        id="inputPassword" 
                        value="<?php echo !empty($password)?$password:'';?>" 
                        name="password" placeholder="Password">
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">Login</button>
            </div>
        </form>
                
    </div> <!-- /row -->
    <?php if (!empty($_POST)): ?> 
    <div class="row"> 
        <p>
            <div class='alert alert-danger'><?php echo $message ?></div>
        </p>
    </div>
    <?php endif?>
    </div> <!-- /container -->
</body>
</html>