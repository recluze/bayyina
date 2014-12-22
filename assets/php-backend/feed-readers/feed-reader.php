<?php 

class FeedReader { 

	var $feed_url; 
	var $pattern_to_search; 
	var $category_search_xpath; 
    
    function __construct ($feed_url, $pattern_to_search, $category_search_xpath) { 
		$this->feed_url = $feed_url;
		$this->pattern_to_search = $pattern_to_search; 
		$this->category_search_xpath = $category_search_xpath;
    }

    function update_from_feed() { 
    	if ($this->feed_url == '') return ''; 
    	$feed_url = $this->feed_url; 

		try { 
    		logit("Reading from: $feed_url"); 
  			$feed_contents = $this->read_feed($feed_url); 

  			logit("Successfully read feed"); 

  			$bayans = $this->extract_bayan_info($feed_contents, $this->pattern_to_search); 

  			return $bayans; 
  		} catch(Exception $e) { 
  			logit("Failed to read feed: " + $e->getMessage()); 
  		}	



    }

    function read_feed($feed_url) { 
    	$s = curl_init(); 

        curl_setopt($s,CURLOPT_URL,$feed_url); 
        curl_setopt($s,CURLOPT_RETURNTRANSFER, true); 
        curl_setopt($s,CURLOPT_FOLLOWLOCATION, true); 

        $res = curl_exec($s); 
        return $res; 
    }

    function extract_bayan_info($feed_contents, $pattern_to_search){ 
        global $prefix_to_url; 

    	$all_matches = array(); 
    	$matched = preg_match_all($pattern_to_search, $feed_contents, $matches); 
    	if($matched) { 
    		$num_matches = count($matches[0]); 
    		logit("Found $num_matches instances of pattern"); 

    		for($i = 0; $i < $num_matches; $i++) { 
    			$match = array(); 
    			$match['title'] = $matches[1][$i]; 
    			$match['url'] = rtrim($prefix_to_url, '/') . $matches[2][$i]; 

    			logit("Found instance of pattern: " . $match['title'] . " - " . $match['url']); 
    			$all_matches[] = $match; 
    		}	  
    	} else { 
    		logit("Failed to find pattern in feed"); 
    	}

    	return $all_matches;
    	
    } 

    function insert_unique($bayans) { 
        global $PDO; 

        $found_some_new = false; 

        foreach($bayans as $bayan) { 
            $title = $bayan['title']; 
            $url = $bayan['url']; 
            $tags = isset($bayan['tags']) ? $bayan['tags'] : ''; 
            $abs_url = $url; 

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
                $uploaded_on = isset($bayan['uploaded_on']) ? $bayan['uploaded_on'] : date('Y-m-d'); 

                $PDO->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                $sql = "INSERT INTO bayans (title, url, tags, uploaded_on) values(?, ?, ?, ?)";
                $stmt = $PDO->prepare($sql);
                $stmt->execute(array($title, $abs_url, $tags, $uploaded_on));
                
                $found_some_new = true; 
            }
        }
        return $found_some_new; 
    }

    function send_update_email() { 
        global $source_url; 
        global $source_title; 
        global $admin_email; 
        global $from_email; 

        $msg = "[BAYYINA] New bayans were found for $source_title. Please click here to view: {$source_url}list.php"; 
        $subject = "New bayans found for $source_title";
        $headers = "From: Bayyina <$from_email>" . "\r\n" .
                    "X-Mailer: PHP/" . phpversion(); 

        mail($admin_email, $subject, $msg, $headers);
        logit("Sent email"); 
    }
  
}



function logit($msg) {
    echo $msg . "<br />"; 
}