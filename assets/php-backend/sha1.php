<?php 
    if ( !empty($_POST)) {
        // validation errors
        $password = null;
        $password = $_POST['password'];
        $hash = sha1($password); 
    } 
 ?>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bayyina Hash Calulator</title>
    <meta charset="utf-8">
    <link href="res/css/bootstrap.min.css" rel="stylesheet">
    <script src="res/js/bootstrap.min.js"></script>
</head>
 
<body>
<div class="container">
    <div class="row">
        <h3>Create a User</h3>
    </div>

    <div class="row"> 
        <form method="POST" action="">
            <div class="form-group">
                <label for="inputPassword">Enter Password</label>
                <input type="text" class="form-control" required="required" 
                        id="inputPassword" 
                        value="<?php echo !empty($password)?$password:'';?>" 
                        name="password" placeholder="Password">
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">Create</button>
            </div>
        </form>
                
    </div> <!-- /row -->
    <?php if (!empty($_POST)): ?> 
    <div class="row"> 
        <div>The hash is:</div> 
        <div class='well'><?php echo $hash ?></div>
    </div>
    <?php endif?>
    </div> <!-- /container -->
</body>
</html>