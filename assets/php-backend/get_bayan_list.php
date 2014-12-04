<?php 
	require_once(dirname(__FILE__) . '/functions.php'); 

	$sql = 'SELECT * FROM bayans ORDER BY ID ASC';

	$all_bayans = array(); 

    foreach ($PDO->query($sql) as $row) {
    	$bayan = array(); 
    	$bayan['title'] = $row['title']; 
    	$bayan['url'] = $row['url']; 
    	$bayan['tags'] = $row['tags']; 
    	$bayan['uploaded_on'] = date('Y-m-d', strtotime($row['uploaded_on'])); 
        $bayan['server_id'] = $row['id']; 

    	$all_bayans[] = $bayan; 
    } 

    echo json_encode($all_bayans, JSON_UNESCAPED_SLASHES); 
