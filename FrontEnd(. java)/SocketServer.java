import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SocketServer {
	private static ServerSocket serverSocket; 
	private static Connection connection; 
	private static Statement statement;

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(8001); //port number should be a valid, unused port on the system. 
			System.out.println("Server started. Listening for incoming connections...");
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/projectdad", "root", ""); 
			statement = connection.createStatement();
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Incoming connection from " + socket.getInetAddress());
				
				new Thread(new SocketHandler (socket)).start();
			}
		}catch (IOException e) {
			System.out.println("Error starting server: " + e.getMessage());
		}catch (SQLException e) {
			System.out.println("Error connecting to database: "  + e.getMessage());
		}
	}

	static class SocketHandler implements Runnable{ 
		private Socket socket;
		
		public SocketHandler(Socket socket) { 
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				String request = reader.readLine();
				System.out.println("Received request: " + request);
				
				if (request.startsWith("LOAD_DATA")) {
					loadData(writer);
				} else if (request.startsWith("SAVE_TO_DATABASE")) { 
					saveToDatabase(reader, writer);
				} else if (request.startsWith("VIEW_REGISTERED_SUBJECTS")) { 
					viewRegisteredSubjects (reader, writer);
				}
			} catch (IOException e) {
				System.out.println("Error handling request: " + e.getMessage());
			} catch (SQLException e) {
				System.out.println("Error executing query: " + e.getMessage());
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Error closing socket: " + e.getMessage());
				}
			}
		}

		private void loadData (PrintWriter writer) throws SQLException { 
			ResultSet resultSet = statement.executeQuery("SELECT * FROM course"); 
			writer.println("LOAD_DATA_RESPONSE");
			while (resultSet.next()) {
				int courseID = resultSet.getInt("CourseID");
				String courseName = resultSet.getString("CourseName"); String creditHours = resultSet.getString("CreditHours");
				writer.println(courseID + "," + courseName + "," + creditHours);
			}
			writer.println("END_OF_DATA");
		}

		private void saveToDatabase (BufferedReader reader, PrintWriter writer) throws SQLException, IOException { 
			String courseID = reader.readLine();
			int userID = Integer.parseInt(reader.readLine());
			PreparedStatement preparedStatement = connection.prepareStatement ("INSERT INTO enrolment (CourseID, UserID) VALUES (?, ?)"); 
			preparedStatement.setString(1, courseID);
			preparedStatement.setInt(2, userID);
			preparedStatement.executeUpdate();
			writer.println("SAVE_TO_DATABASE_RESPONSE");
		}
		
		private void viewRegisteredSubjects (BufferedReader reader, PrintWriter writer) throws SQLException, IOException { 
			int userID = Integer.parseInt(reader.readLine());
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT course.CourseID, course.CourseName, course.CreditHours FROM "
					+ "course INNER JOIN enrolment ON course.CourseID=enrolment.CourseID WHERE enrolment.UserID = ?");
			preparedStatement.setInt(1, userID);
			ResultSet resultSet = preparedStatement.executeQuery();
			writer.println("VIEW_REGISTERED_SUBJECTS_RESPONSE");
			while (resultSet.next()) {
				int courseID = resultSet.getInt("CourseID");
				String courseName = resultSet.getString("CourseName");
				String creditHours = resultSet.getString("CreditHours");
				writer.println(courseID + "," + courseName + "," + creditHours);
			}
			writer.println("END_OF_DATA");
		}
	}
}


