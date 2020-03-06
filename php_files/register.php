<?php
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
	if ((isset($_POST['name'])) && (isset($_POST['email'])) && (isset($_POST['password']))) {
		include 'register_config.php';
		$conn = new mysqli($host, $username, $password, $db);

		if (!$conn->connect_error) {
			$name = $conn->real_escape_string($_POST['name']);
			$email = $conn->real_escape_string($_POST['email']);
			$password = $conn->real_escape_string($_POST['password']);
			
			if ($conn->query("INSERT INTO $table (username, email, password) VALUES ('$name', '$email', '$password')") === TRUE) { 
				echo json_encode('Success');
			} else
				echo json_encode(array('Error' => $conn->error));

			$conn->close();
		} else 
			echo json_encode(array('Error' => $conn->connect_error));
	}
}
?>
