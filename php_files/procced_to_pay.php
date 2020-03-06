<?php
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	if ((isset($_POST['name']))) {
		include 'procced_to_pay_config.php';
		$conn = new mysqli($host, $username, $password, $db);

		if (!$conn->connect_error) {
			
			$name = $conn->real_escape_string($_POST['name']);
			
			$total_cost = $conn->query ( "SELECT SUM(Items.price*Cart.numOfItems) 
										FROM Cart INNER JOIN Items ON (items.itemId = Cart.itemId )
										INNER JOIN Users ON (Users.userId = cart.userId)
										WHERE Users.username = '$name'");
			echo json_encode(array('Success' => $total_cost));
			}
			else {
				echo json_encode(array('Error' => $conn->error));
			}
			$conn->close();
		} else 
			echo json_encode(array('Error' => $conn->connect_error));
	}

// delete cart after transaction somewhere.
?>
