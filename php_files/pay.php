<?php
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	if ((isset($_POST['name']))) {
		include 'pay_config.php';
		$conn = new mysqli($host, $username, $password, $db);

		if (!$conn->connect_error) {
			$name = $conn->real_escape_string($_POST['name']);
			
			//cartid == $cartid
			$uid = $conn->query("SELECT id FROM Users WHERE username = '$name'");
			if(($cartid = $conn->query("SELECT idCart FROM $table WHERE idUser = '$uid'"))=== TRUE){
				$res = $conn->query("SELECT * FROM $table WHERE idCart = '$cartid'");
				$total_cost = $conn->query ( "SELECT SUM(Items.price*Cart.numOfItems) 
										FROM Cart INNER JOIN Items ON (Items.itemId = Cart.itemId )
										INNER JOIN Users ON (Users.id = cart.userId)
										WHERE Users.username = '$name'");
				$conn->query("INSERT INTO Transactions (itemId, userId, numOfItems, totalCost) VALUES('$res['idItem']','$res['idUser']','$res['numOfItems']','$total_cost')");
				echo json_encode('Success');
			}
			else {
				echo json_encode(array('Error' => $conn->error));
			}
			$conn->close();
		} else 
			echo json_encode(array('Error' => $conn->connect_error));
	}
}
?>