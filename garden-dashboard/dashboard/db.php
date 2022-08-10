<?php
    class DataBaseHelper{
        private $db;

        public function __construct($servername, $username, $password, $dbname, $port){
            $this->db = new mysqli($servername, $username, $password, $dbname, $port);
            if($this->db->connect_error){
                die("Connesione fallita al db");
            }
        }

        //Funzioni prodotti

        public function getValue($device){
            $stmt = $this->db->prepare("SELECT val FROM info WHERE device = '".$device."'");
            $stmt->execute();
            $result = $stmt->get_result();

            return $result->fetch_all(MYSQLI_ASSOC);
        }
    }
?>