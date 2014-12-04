<?php 

require_once(dirname(__FILE__) . '/config.php'); 

session_start(); 
/* Helper functions */ 

function check_auth() { 
	if(!isset($_SESSION['loggedin']) || !$_SESSION['loggedin'])	{ 
		header("Location: login.php");
	}
} 

function login() { 
} 

function validate_date($input, $format = 'Y-m-d') {
    $time = strtotime($input);
	$is_valid = date($format, $time) == $input;
	return $is_valid; 
}