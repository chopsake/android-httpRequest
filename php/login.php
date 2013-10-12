<?php

include('keycheck.php');
include('connection.php');

$email =    $_POST['email'];
$pass =     $_POST['password'];

$query =    "SELECT id FROM users WHERE email=\"" . $email . "\" AND password=MD5(\"" . $pass . "\");";

$result = mysql_query($query);
$rows = mysql_num_rows($result);

$data = array();

if($rows == 1)
{
    $row = mysql_fetch_array($result);
    $data['response'][] = 1;
    $data['response'][] = $row['id'];
}
else
{
    $data['response'][] = 0;
}

echo json_encode($data);

mysql_close($con);

?>
