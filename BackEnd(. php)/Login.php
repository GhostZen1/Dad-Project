<?php
// Database configuration
$servername = "localhost";
$username_db = "root";
$password_db = "";
$dbname = "projectdad";

// Create connection
$conn = new mysqli($servername, $username_db, $password_db, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Initialize response array
$response = array();

// Check if both username and password parameters exist in POST request
if (isset($_POST['username']) && isset($_POST['password'])) {
    // Receive the username and password sent from Java
    $Username = $_POST['username'];
    $Password = $_POST['password'];

    // Prepare SQL query to check credentials
    $sql = "SELECT * FROM account WHERE Username = '" . $conn->real_escape_string($Username) . "' AND Password = '" . $conn->real_escape_string($Password) . "'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        // User authenticated
        $row = $result->fetch_assoc();
        $response['success'] = true;
        $response['message'] = "Login successful";
        $response['username'] = $row['Username']; // You can include any additional data you want to return
        $response['isadmin'] = $row['isAdmin'];
        $response['userid'] = $row['UserID'];
    } else {
        // Authentication failed
        $response['success'] = false;
        $response['message'] = "Incorrect username or password";
    }
} else {
    // Handle case where username or password parameter is missing
    $response['success'] = false;
    $response['message'] = "Username or password not provided";
}

// Close connection
$conn->close();

// Return JSON response
header('Content-Type: application/json');
echo json_encode($response);
?>