<?php

include('keycheck.php');   //verify the key
include('connection.php'); //connection to the database

$sql = "SELECT first, last \n"
     . "FROM `test_friends` \n"
     . "ORDER BY first LIMIT 0, 30 ";

$result = mysql_query($sql);
$rows = mysql_num_rows($result);

$data = array();

if($rows > 0)
{
    while($row = mysql_fetch_array($result))
    {
        $temp = $row['first'] . " " . $row['last'];
        $data['names'][] = $temp;
    }
    echo json_encode($data);
}

?>
