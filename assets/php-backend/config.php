<?php
    $host      = "localhost";
    $user      = "recluze";
    $pass      = "password";
    $dbname    = "bayyina_tasawwuf";

    $admin_email = "email_to_inform@domain.com"; 
    $from_email = "email_to_send_from@domain.com"; 

    $source_title = "Tasawwuf.org";
    $base_url  = "/bayyina/tasawwuf/";


    $source_url = "http://www.sunnahacademy.org" . $base_url; 

    $admin_username = "admin"; 
    $admin_userpass = "d033e22ae348aeb5660fc2140aec35850c4da997"; // use [source_url]/sha1.php to generate new one. 

    if($admin_userpass == "d033e22ae348aeb5660fc2140aec35850c4da997")
        die("Please change admin password hash by editting config.php"); 


    /* END OF CONFIG */ 

    try {
        $PDO =  new PDO( "mysql:host=".$host.";"."dbname=".$dbname, $user, $pass);  
    } catch(PDOException $e) {
        die($e->getMessage());  
    }

    // Uncomment the line below and browse to config.php to test connection first  
    // echo "DB Connection is good ... "; 