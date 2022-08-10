<?php
    session_start();
    require_once "db.php";
    $dbh = new DataBaseHelper('localhost', 'root', '', 'garden_service', 3306);
?>