<?php 
    die("disabled");

    require_once(dirname(__FILE__) . '/feed-reader.php'); 
    require_once(dirname(__FILE__) . '/../config.php'); 

	  $prefix_to_url = "http://tasawwuf.org"; 

  	$feed_url = "http://tasawwuf.org/feed/"; 
  	$pattern_to_search = '/a title="(.*?)" href="(.*?\.mp3)">/'; 
  	$category_search_xpath = "//item/title";
  
    echo "<html><body><pre>";

  	$feed_reader = new FeedReader($feed_url, $pattern_to_search, $category_search_xpath);
  	$bayans = $feed_reader->update_from_feed(); 

  	var_dump($bayans); 

  	$found_some_new = $feed_reader->insert_unique($bayans); 

    if($found_some_new) { 
      $feed_reader->send_update_email(); 
    }

  	echo "</pre></body></html>";