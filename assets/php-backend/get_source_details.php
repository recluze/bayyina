<?php 
	require_once(dirname(__FILE__) . '/functions.php'); 

	$sql = 'SELECT max(timestamp) FROM bayans';

	$source_details = array('title' => $source_title); 

    foreach ($PDO->query($sql) as $row) {
		$max_date = $row[0]; 
    } 

    $max_date = date('T Y-m-d H:i:s', strtotime($max_date)); 
    $source_details['last_updated_on'] = $max_date; 

    echo json_encode($source_details);
