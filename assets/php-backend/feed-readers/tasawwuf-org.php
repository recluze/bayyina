<?php 
    require_once(dirname(__FILE__) . '/feed-reader.php'); 
    require_once(dirname(__FILE__) . '/../config.php'); 

	$title = "Tasawwuf.org";
	$base_url = "http://tasawwuf.org"; 

  	$feed_url = "http://tasawwuf.org/feed/"; 
  	$pattern_to_search = '/a title="(.*?)" href="(.*?\.mp3)">/'; 
  	$category_search_xpath = "//item/title";
  
    echo "<html><body><pre>";

  	$feed_reader = new FeedReader($feed_url, $pattern_to_search, $category_search_xpath);
  	$bayans = $feed_reader->update_from_feed(); 

  	var_dump($bayans); 

  	$found_some_new = false; 

  	foreach($bayans as $bayan) { 
  		$title = $bayan['title']; 
  		$url = $bayan['url']; 
  		$abs_url = $base_url . $url; 

  		// echo "<br /><br />Found bayan [$title] -- [$abs_url] <br/>"; 

  		// check if we already have this URL in the db
  		$sql = "SELECT * FROM bayans WHERE url = '$abs_url'";
  		$already_saved = false; 
    	foreach ($PDO->query($sql) as $row) { 
    		$already_saved = true; 
    		break; 
    	} 

    	if ($already_saved) { 
    		echo "[SKIP] Already have bayan with url [$abs_url] <br />"; 
    	} else { 
    		echo "Adding new bayan [$title] <br />"; 
    		$uploaded_on = date('Y-m-d'); 
  			$tags = ''; 

  			$PDO->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $sql = "INSERT INTO bayans (title, url, tags, uploaded_on) values(?, ?, ?, ?)";
            $stmt = $PDO->prepare($sql);
            $stmt->execute(array($title, $abs_url, $tags, $uploaded_on));

            $found_some_new = true; 
    	}


  	}


  	if($found_some_new) { 
  		$msg = "[BAYYINA] New bayans were found for $source_title. Please click here to view: {$source_url}list.php"; 
  		$subject = "New bayans found for $source_title";
  		$headers = "From: Bayyina <webmaster@example.com>" . "\r\n" .
    			   	"Reply-To: webmaster@example.com" . "\r\n" .
    				"X-Mailer: PHP/" . phpversion(); 

  		mail($admin_email, $subject, $msg, $headers);
  	}


  	echo "</pre></body></html>";