<?php
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	if ((isset($_POST['name'])) && (isset($_POST['item'])) && (isset($_POST['quantity']))) {
		include 'cart_config.php';
		$conn = new mysqli($host, $username, $password, $db);

		if (!$conn->connect_error) {
			$name = $conn->real_escape_string($_POST['name']);
			$item = $conn->real_escape_string($_POST['item']);
			$quantity = (int)$conn->real_escape_string($_POST['quantity']);
			
			$uid = $conn->query("SELECT id FROM Users WHERE username = '$name'");
			$iid = $conn->query("SELECT idItem FROM Items WHERE itemName = '$item'");
			
			
			if(($cartid = $conn->query("SELECT id FROM $table WHERE idUser = '$uid'")=== FALSE){
				if ($conn->query("INSERT INTO $table (idUser, idItem, numOfItems) VALUES ('$uid', '$iid', '$quantity')") === TRUE) { 
					echo json_encode('Success');
				} 
				else
					echo json_encode(array('Error' => $conn->error));
			}
			else {
				if (($numOfItems = $conn->query("SELECT numOfItems FROM $table WHERE idCart='$cartid' and idItem='$iid'")) === TRUE){
					if ($conn->query("UPDATE $table SET numOfItems = $numOfItems + $quantity WHERE idCart='$cartid' and idItem='$iid'") === TRUE) { 
						echo json_encode('Success');
					} else
						echo json_encode(array('Error' => $conn->error));
				}
				else{
					if ($conn->query("INSERT INTO $table (idCart, idUser, idItem, numOfItems) VALUES ('$cartid', '$uid', '$iid', '$quantity')") === TRUE) { 
						echo json_encode('Success');
					} else
						echo json_encode(array('Error' => $conn->error));
				}
			}
			$conn->close();
		} else 
			echo json_encode(array('Error' => $conn->connect_error));
	
	}
}
// delete cart after transaction somewhere.
else if ($_SERVER['REQUEST_METHOD'] === 'GET') {
	if (isset($_POST['name'])) {
		include 'cart_config.php';
		$conn = new mysqli($host, $username, $password, $db);
		$uid = $conn->query("SELECT id FROM Users WHERE username = '$name'");
		$result = mysql_query("SELECT * FROM $table WHERE idUser = '$uid' ");
		$outp = $result->fetch_all(MYSQLI_ASSOC);
		echo json_encode($outp);

		$conn->close();
	}else 
		echo json_encode(array('Error' => $conn->connect_error));
}
else if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
	if ((isset($_POST['name'])) && isset($_POST['item'])) {
		include 'cart_config.php';
		$conn = new mysqli($host, $username, $password, $db);
		$iid = $conn->query("SELECT idItem FROM Items WHERE itemName = '$item'");
		$uid = $conn->query("SELECT id FROM Users WHERE username = '$name'");

		if ($conn->query("DELETE FROM $table WHERE itemId='$iid' and userId='$uid'") === TRUE ){
			echo json_encode('Success');
		}
		else 
			echo json_encode(array('Error' => $conn->connect_error));
		$conn->close();
	}else 
		echo json_encode(array('Error' => $conn->connect_error));
}
?>
