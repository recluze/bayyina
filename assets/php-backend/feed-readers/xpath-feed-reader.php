<?php 
require_once(dirname(__FILE__) . '/feed-reader.php'); 

class XPathFeedReader extends FeedReader { 

	var $xpath; 
	var $feed_url; 

	public function __construct($feed_url, $xpath) { 
		$this->feed_url = $feed_url;
		$this->xpath = $xpath;  
	}

	function update_from_feed() { 
		if ($this->feed_url == '') return ''; 
    	$feed_url = $this->feed_url; 

    	try { 
    		logit("Reading from: $feed_url"); 
  			$feed_contents = $this->read_feed($feed_url); 

  			// logit(htmlentities($feed_contents)); 
  			logit("Successfully read feed"); 

  			$bayans = $this->extract_bayan_info_xpath($feed_contents, $this->xpath); 

  			return $bayans; 
  		} catch(Exception $e) { 
  			logit("Failed to read feed: " + $e->getMessage()); 
  		}	
	} 


	function extract_bayan_info_xpath($feed_contents, $xpath) { 
		logit("Extracting xpath: " . $xpath); 
		$doc = new DOMDocument();
        @$doc->loadHTML($feed_contents);
        $dx = new DOMXPath($doc);

	    $entries = $dx->query($xpath);

	    $all_bayans = array(); 

        foreach($entries as $entry) {
        	$bayan = array(); 
        	$children = $entry->childNodes;

        	$title_node = $children->item(0);
        	$title_str = $title_node->nodeValue;   

        	$tags_node = $children->item(4);
        	$tags_str = $tags_node->nodeValue;           	

        	$uploaded_on_node = $children->item(8);
        	$uploaded_on_str = $uploaded_on_node->nodeValue;   
        	// TODO: FIX the date format 


    			$url_node = $children->item(10);

    			$children = $url_node->childNodes; 
    			$url_str = ""; 
        	foreach ($children as $child) { 
      			$node_str = $child->ownerDocument->saveXML( $child ); 
        		if(preg_match('/href="(.*\.mp3)/', $node_str, $matches)) {
        			$url_str = $matches[1];
        		}
      	  } 

          	// var_dump($title_str, $tags_str, $uploaded_on_str, $url_str); 
   			$bayan['title'] = $title_str; 
   			$bayan['tags'] = $tags_str; 
   			$bayan['uploaded_on'] = $uploaded_on_str; 
   			$bayan['url'] = $url_str; 

   			$all_bayans [] = $bayan; 
      } 
      return $all_bayans; 

	}
}