<?php
    include(dirname(__FILE__) . '/functions.php'); 
    check_auth(); 

    $title_error = ""; 
    $url_error = ""; 
    $uploaded_on_error = ""; 
    $active = false; 

    $id = null;
    if(!empty($_GET['id'])) {
        $id = $_GET['id'];
    }
    if($id == null) {
        header("Location: list.php");
    } 
    if ( !empty($_POST)) {
        require 'config.php';
        // validation errors
        $title_error = null;
        $url_error  = null;
        $uploaded_on_error = null;
        
        // post values
        $title  = $_POST['title'];
        $url  = $_POST['url'];
        $tags    = $_POST['tags'];
        $uploaded_on = $_POST['uploaded_on'];
        $active = (isset($_POST['active']) && $_POST['active'] == 'on'); 
    
        // validate input
        $valid = true;
        if(empty($title)) {
            $title_error = 'Please enter Title';
            $valid = false;
        }
        
        if(empty($url)) {
            $url_error = 'Please enter URL';
            $valid = false;
        }
        
        if(empty($uploaded_on)) {
            $uploaded_on_error = 'Please enter Upload Date';
            $valid = false;
        }

        if(!validate_date($uploaded_on)) {
            $uploaded_on_error = 'Please enter upload date in format YYYY-MM-dd';
            $valid = false;
        }
                
        // insert data
        if ($valid) {
            $PDO->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $sql = "Update bayans set `title`=?, `url`=?, `tags`=?, `uploaded_on`=?, `timestamp`=CURRENT_TIMESTAMP, `active` = ? where id=?";
            $stmt = $PDO->prepare($sql);
            $stmt->execute(array($title, $url, $tags, $uploaded_on, $active, $id));
            $PDO = null;
            header("Location: list.php");
        }
    } else {
        $PDO->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $sql = "SELECT * FROM bayans where id = ?";
        $stmt = $PDO->prepare($sql);
        $stmt->execute(array($id));
        $data = $stmt->fetch(PDO::FETCH_ASSOC);
        $PDO = null;
        if (empty($data)){
            header("Location: list.php");
        }
        $title = stripslashes($data['title']);
        $url  = $data['url'];
        $tags    = $data['tags'];
        $uploaded_on = date('Y-m-d', strtotime($data['uploaded_on']));
        $active = $data['active']; 
    }
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bayyina Edit Bayan</title>
    <meta charset="utf-8">
    <link   href="res/css/bootstrap.min.css" rel="stylesheet">
    <script src="res/js/bootstrap.min.js"></script>
</head>
 
<body>
<div class="container">
    <div class="row">
        <h3>Update Bayan Information</h3>
    </div>

    <div class="row"> 
        <form method="POST" action="">
            <div class="form-group <?php echo !empty($title_error)?'has-error':'';?>">
                <label for="inputTitle">Title</label>
                <input type="text" class="form-control" required="required" 
                        id="inputTitle" 
                        value="<?php echo !empty($title)?$title:'';?>" 
                        name="title" placeholder="Title">
                <span class="help-block"><?php echo $title_error;?></span>
            </div>
            <div class="form-group <?php echo !empty($url_error)?'has-error':'';?>">
                <label for="inputURL">URL</label>
                <input type="text" class="form-control" required="required" i
                        d="inputURL" 
                        value="<?php echo !empty($url)?$url:'';?>" 
                        name="url" placeholder="URL">
                <span class="help-block"><?php echo $url_error;?></span>
            </div>
            <div class="form-group">
                <label for="inputTags">Tags</label>
                <input type="text" class="form-control" 
                        id="inputTags" 
                        value="<?php echo !empty($tags)?$tags:'';?>" 
                        name="tags" placeholder="Tags">
            </div>
            <div class="form-group <?php echo !empty($uploaded_on_error)?'has-error':'';?>">
                <label for="inputUploadedOn">Uploaded On</label>
                <input type="date" class="form-control" 
                        id="inputUploadedOn" 
                        value="<?php echo !empty($uploaded_on)?$uploaded_on:'';?>" 
                        name="uploaded_on" placeholder="Uploaded On">
            <span class="help-block"><?php echo $uploaded_on_error;?></span>
                
            </div>
            
            <div class="form-group">
                <label for="inputActive">Active</label>
                <input type="checkbox" 
                        id="inputActive"
                        <?php echo ($active ? 'checked' : ''); ?> 
                        name="active">

            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-success">Update</button>
                <a class="btn btn-default" href="list.php">Back</a>
            </div>
        </form>
                
    </div> <!-- /row -->
    </div> <!-- /container -->
</body>
</body>
</html>