<?php

// Database connection parameters
$servername = "localhost"; // Replace with your server name
$username = "root"; // Replace with your database username
$password = ""; // Replace with your database password
$dbname = "projectdad"; // Replace with your database name

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// SQL query to fetch courses from database
$sql = "SELECT CourseID, CourseName, CreditHours FROM course";
$result = $conn->query($sql);

$response = array();

if ($result->num_rows > 0) {
    // Courses array to hold fetched courses
    $courses = array();

    // Fetch data from each row and add to $courses array
    while($row = $result->fetch_assoc()) {
        $course = array(
            "course_id" => $row["CourseID"],
            "course_name" => $row["CourseName"],
            "credit_hours" => $row["CreditHours"]
        );
        $courses[] = $course;
    }

    // Set success true and add courses array to response
    $response["success"] = true;
    $response["courses"] = $courses;
} else {
    // No courses found
    $response["success"] = false;
    $response["message"] = "No courses found.";
}

// Close connection
$conn->close();

// Convert PHP array to JSON and output response
header("Content-type: application/json");
echo json_encode($response);
?>