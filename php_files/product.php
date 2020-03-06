<?php
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
	if (isset($_POST['itemName'])) {
		include 'product_config.php';
		$conn = new mysqli($host, $username, $password, $db);
		//$iid = $conn->query("SELECT id FROM Items WHERE itemName LIKE '$itemName'");
		$result = query("SELECT (itemName,storename,price) FROM $table INNER JOIN GroceryStore on (Items.idStore = GroceryStore.idStore) WHERE itemName = '$itemName' ORDER BY price ASC");
		$outp = $result->fetch_all(MYSQLI_ASSOC);
		echo json_encode($outp);

		$conn->close();
	}else 
		echo json_encode(array('Error' => $conn->connect_error));
}
?>


<?php
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
	if (isset($_POST['itemName'])) {
		include 'product_config.php';
		$conn = new mysqli($host, $username, $password, $db);
		//$iid = $conn->query("SELECT id FROM Items WHERE itemName LIKE '$itemName'");
		$result = query("SELECT (itemName,storename,price) FROM $table INNER JOIN GroceryStore on (Items.idStore = GroceryStore.idStore) WHERE itemName = '$itemName' ORDER BY price ASC");
		$rows= array();
		$i = 0 ;
		while($r = mysql_fetch_assoc($result)){
			$rows[] = array( $i => $r );
			$i++ ;
		}
		echo json_encode($rows);
		$conn->close();
	}else 
		echo json_encode(array('Error' => $conn->connect_error));
}
?>
