<?php 
    /// die("disabled ...");

    require_once(dirname(__FILE__) . '/feed-reader.php'); 
    require_once(dirname(__FILE__) . '/../config.php'); 


    class Tasawuff_all extends FeedReader { 
      var $main_url; 
      var $prefix_to_url; 

        public function __construct($prefix_to_url) { 
          $this->main_url = "http://tasawwuf.org/all-audio/";
          $this->prefix_to_url = $prefix_to_url; 
        } 

        function validate_date($date, $format = 'm.d.Y') {
          $d = DateTime::createFromFormat($format, $date);
          return $d && $d->format($format) == $date;
        }

        function convert_date($date, $format_from = 'm.d.Y', $format_to = 'Y-m-d') { 
          $d = DateTime::createFromFormat($format_from, $date);
          return $d->format($format_to);
        } 


        public function update_from_feed() { 

          $xpath = "//a[contains(@href, \"mp3\")]";

          $url = $this->main_url; 
          logit("Gathering ... $url"); 
          logit("Xpath: $xpath"); 

          $feed_contents = $this->read_feed($url); 

          $doc = new DOMDocument();
          @$doc->loadHTML($feed_contents);
          $dx = new DOMXPath($doc);

          $entries = $dx->query($xpath);

          $all_bayans = array(); 

          $count = 0; 

          foreach($entries as $entry) {
            // if($count++ >= 40) return $all_bayans;

            logit("Found: " . htmlentities($doc->saveXML($entry)));
            $ps1 = $entry->previousSibling; 
            $ps2 = $ps1->previousSibling; 
            $ps3 = $ps2->previousSibling; 
            $ps4 = $ps3->previousSibling; 
            $ps5 = $ps4->previousSibling; 
            $ps6 = $ps5->previousSibling; 

            /* 
            logit(" -- ps1: " . htmlentities($doc->saveXML($ps1)));  
            logit(" -- ps2: " . htmlentities($doc->saveXML($ps2)));  
            logit(" -- ps3: " . htmlentities($doc->saveXML($ps3)));  
            logit(" -- ps4: " . htmlentities($doc->saveXML($ps4)));  
            logit(" -- ps5: " . htmlentities($doc->saveXML($ps5)));  
            logit(" -- ps6: " . htmlentities($doc->saveXML($ps6)));  
            */ 

            $ps4_dt = trim(strip_tags($ps4->nodeValue)); 
            $ps6_dt = trim(strip_tags($ps6->nodeValue)); 

            // check which format we have 
            if($this->validate_date($ps4_dt)) { 
              // older format 
              $date_str = $this->convert_date($ps4_dt);
              $title_str = trim(strip_tags($ps2->nodeValue)); 
              
            } else if ($this->validate_date($ps6_dt)){ 
              // newer format 
              $date_str = $this->convert_date($ps6_dt);
              $title_str = trim(strip_tags($ps4->nodeValue)); 

            } else { 
              logit(" =====> ERROR: COULDN'T UNDERSTAND THE FORMAT"); 
              continue; 
            } 

            $url_str = $this->prefix_to_url . $entry->getAttribute('href'); 

            logit(" --- url: " . $url_str);
            logit(" --- date: " . $date_str);
            logit(" --- title: " . $title_str); 

            logit(" ------ "); 

            $bayan = array('url' => $url_str, 
                           'title' => $title_str, 
                           'uploaded_on' => $date_str
                           );
            $all_bayans []= $bayan;  

          }

          return $all_bayans; 

        } 
    } 


	  $prefix_to_url = "http://tasawwuf.org"; 

  	$feed_url = "http://tasawwuf.org/feed/"; 
  
    echo "<html><body><pre>";

  	$feed_reader = new Tasawuff_all($prefix_to_url);
  	$bayans = $feed_reader->update_from_feed(); 

  	var_dump($bayans); 

  	$found_some_new = $feed_reader->insert_unique($bayans); 

    if($found_some_new) { 
      $feed_reader->send_update_email(); 
    }

  	echo "</pre></body></html>";



