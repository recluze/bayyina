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
    	$all_matches = array(); 
    	$matched = preg_match_all($pattern_to_search, $feed_contents, $matches); 
    	if($matched) { 
    		$num_matches = count($matches[0]); 
    		logit("Found $num_matches instances of pattern"); 

    		for($i = 0; $i < $num_matches; $i++) { 
    			$match = array(); 
    			$match['title'] = $matches[1][$i]; 
    			$match['url'] = $matches[2][$i]; 

    			logit("Found instance of pattern: " . $match['title'] . " - " . $match['url']); 
    			$all_matches[] = $match; 
    		}	  
    	} else { 
    		logit("Failed to find pattern in feed"); 
    	}

    	return $all_matches;
    	
    } 
  
}



function logit($msg) {
    echo $msg . "<br />"; 
}