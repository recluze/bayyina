<?php 
 require_once(dirname(__FILE__) . '/functions.php'); 
 check_auth(); 
?> 
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Bayyina List</title>
    <meta charset="utf-8">
    <link   href="res/css/bootstrap.min.css" rel="stylesheet">
    <link   href="res/css/bootstrap-theme.css" rel="stylesheet">
    
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    
    <script src="res/js/bootstrap.min.js"></script>
    
    <script src="//cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>
    <link   href="//cdn.datatables.net/1.10.4/css/jquery.dataTables.css" rel="stylesheet">


    
    <script language='javascript'> 
       $(document).ready(function() {
         $('#bayanList').DataTable({
            "order": [[ 3, "desc" ]]
         }); 
       } );
    </script> 
    
    

</head>
 
<body>
<div class="container">
    <div class="row">
    <h3>Bayyina Backend</h3>
    </div>
    <div class="row">
    <p><a class="btn btn-lg btn-success" href="create.php">Upload New Bayan</a>
        <a class="btn btn-lg btn-warning" href="login.php?logout">Logout</a></p>
    <table id='bayanList' class="table table-striped table-condensed table-hover" cellspacing="0" width="100%">
    <thead> 
    <tr>
        <th>Title</th>
        <th>URL</th>
        <th>Tags</th>
        <th>Uploaded</th>
        <th>Active</th>
        <th>Actions</th>
    </tr>
    </thead> 
    <tbody>
    <?php
    include 'config.php';
    $sql = 'SELECT * FROM bayans ORDER BY ID DESC';
    foreach ($PDO->query($sql) as $row) {
        echo '<tr>';
        echo '<td>'. stripslashes($row['title']) . '</td>';
        echo '<td><a href="'. $row['url'] .  '">'. $row['url'] .'</td>';
        echo '<td>'. $row['tags'] . '</td>';
        echo '<td>'. date('Y-m-d', strtotime($row['uploaded_on'])) . '</td>';
        echo '<td>'. ($row['active'] ? 'Yes' : 'No') . '</td>';
        echo "<td>
                <a class='btn btn-xs btn-success' href='update.php?id=".$row['id']."'>Edit</a>
              </td>";
        echo '</tr>';
    }
    $PDO = null;
    ?>
    </tbody>
    </table>
    </div><!-- /row -->
</div><!-- /container -->
</body>
</html>