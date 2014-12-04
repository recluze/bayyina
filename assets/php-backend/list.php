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
    <script src="res/js/bootstrap.min.js"></script>
</head>
 
<body>
<div class="container">
    <div class="row">
    <h3>Bayyina Backend</h3>
    </div>
    <div class="row">
    <p><a class="btn btn-lg btn-success" href="create.php">Upload New Bayan</a>
        <a class="btn btn-lg btn-warning" href="login.php?logout">Logout</a></p>
    <table class="table table-striped table-bordered table-hover">
    <tr>
        <th>Title</th>
        <th>URL</th>
        <th>Tags</th>
        <th>Uploaded On</th>
        <th>Actions</th>
    </tr>
    <tbody>
    <?php
    include 'config.php';
    $sql = 'SELECT * FROM bayans ORDER BY ID DESC';
    foreach ($PDO->query($sql) as $row) {
        echo '<tr>';
        echo '<td>'. $row['title'] . '</td>';
        echo '<td><a href="'. $row['url'] .  '">'. $row['url'] .'</td>';
        echo '<td>'. $row['tags'] . '</td>';
        echo '<td>'. date('Y-m-d', strtotime($row['uploaded_on'])) . '</td>';
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