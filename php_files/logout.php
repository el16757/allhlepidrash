<?php
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
	if ((isset($_GET['email'])) && (isset($_GET['password']))) {
		include 'logout_config.php';
		$conn = new mysqli($host, $username, $password, $db);

		if (!$conn->connect_error) {
			$email = $conn->real_escape_string($_GET['email']);
			$password = $conn->real_escape_string($_GET['password']);

			$res = $conn->query("SELECT id, name FROM $table WHERE email = '$email' AND password = '$password'");

			if ($res->num_rows > 0) {
				echo json_encode(array('Success'));
			} else 
				echo json_encode(array('Error' => 'Invalid username or password'));

			$conn->close();
		} else 
			echo json_encode(array('Error' => $conn->connect_error));
	}
}
?>
