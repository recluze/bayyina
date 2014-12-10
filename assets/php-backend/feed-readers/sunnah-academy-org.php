<?php 

    //die("disabled"); 

    require_once(dirname(__FILE__) . '/xpath-feed-reader.php'); 
    require_once(dirname(__FILE__) . '/../config.php'); 

    // need this for some bayan titles in farsi and russian 
	  header('Content-Type: text/html; charset=utf-8');
  	
  	$feed_url = "http://www.sunnahacademy.org/audios/";
  	$xpath = "//tr[td/a/text()='Download']"; 
  
    echo "<html><body><pre>";

  	$feed_reader = new XPathFeedReader($feed_url, $xpath);
  	$bayans = $feed_reader->update_from_feed(); 

  	var_dump($bayans); 

  	$found_some_new = $feed_reader->insert_unique($bayans); 

  	if($found_some_new) { 
  		$feed_reader->send_update_email(); 
  	}





  	echo "</pre></body></html>";