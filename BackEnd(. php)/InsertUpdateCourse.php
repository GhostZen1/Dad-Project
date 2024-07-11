<?php
$servername = "localhost"; 
$username = "root";
$password = ""; 
$dbname = "projectdad"; 


// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $course_id = isset($_POST['CourseID']) ? $_POST['CourseID'] : '';
    $course_name = $_POST['CourseName'];
    $credit_hours = $_POST['CreditHour'];

    if ($course_id == '') {
        // Insert new course
        $sql = "INSERT INTO `course`(`CourseName`, `CreditHours`) VALUES ('$course_name', '$credit_hours')";
    } else {
        // Update existing course   
        $sql = "UPDATE `course` SET `CourseName`='$course_name', `CreditHours`='$credit_hours' WHERE `CourseID`='$course_id'";
    }

    if ($conn->query($sql) === TRUE) {
        $response["success"] = true;
        $response["message"] = "Course successfully saved";
    } else {
        $response["success"] = false;
        $response["message"] = "Error: " . $sql . "<br>" . $conn->error;
    }
} else {
    $response["success"] = false;
    $response["message"] = "Invalid request method";
}

$conn->close();

echo json_encode($response);
?>