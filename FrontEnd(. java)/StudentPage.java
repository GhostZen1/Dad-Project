import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

import java.awt.Button;

public class StudentPage {

	private JFrame frame;
	private DefaultTableModel tableModel;
	private JTable table;
	private static int userid;

	/**
	 * Launch the application.
	 */
	public static void student() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentPage window = new StudentPage(userid);
					window.loadData();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param userid 
	 * @param i 
	 */
	public StudentPage(int userid) { // Modify the constructor to accept userid
        this.userid = userid;
        initialize();
    }

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.setBounds(100, 100, 700, 521);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("STUDENT ENROLLMENT");
		lblNewLabel.setFont(new Font("Tw Cen MT", Font.BOLD, 30));
		lblNewLabel.setBounds(156, 38, 299, 21);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Enter Course ID");
		lblNewLabel_1.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		lblNewLabel_1.setBounds(39, 302, 107, 23);
		frame.getContentPane().add(lblNewLabel_1);
		
		JTextField textFieldCourseID = new JTextField();
		textFieldCourseID.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		textFieldCourseID.setBounds(156, 298, 311, 32);
		frame.getContentPane().add(textFieldCourseID);
		textFieldCourseID.setColumns(10);
		
		JTextField txtUserId = new JTextField();
		txtUserId.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		txtUserId.setColumns(10);
		txtUserId.setText(String.valueOf(userid)); 
		//txtUserId.setVisible(false);
		txtUserId.setBounds(156, 364, 311, 32);
		frame.getContentPane().add(txtUserId);
		
		JButton btnSaveToDatabase = new JButton("Save to Database");
		btnSaveToDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Socket socket = new Socket("localhost", 8001);
					PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader reader = new BufferedReader(new InputStreamReader (socket.getInputStream()));
					
					writer.println("SAVE_TO_DATABASE"); 
					writer.println(textFieldCourseID.getText()); 
					writer.println(txtUserId.getText());
					
					String response = reader.readLine();
					if (response.equals("SAVE_TO_DATABASE_RESPONSE")) {
						//JOptionPane.showMessageDialog(StudentPage.this, "Data saved to database successfully");
						JOptionPane.showMessageDialog(frame, "Data saved to database successfully", null, JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(frame, "Error saving data to database: ", null, JOptionPane. YES_OPTION);
					}
					socket.close();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(frame, "Error saving data to dabase: ", null, JOptionPane.OK_OPTION);
				}
			}
		});
		btnSaveToDatabase.setBackground(new Color(255, 235, 205));
		btnSaveToDatabase.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnSaveToDatabase.setBounds(205, 428, 126, 32);
		btnSaveToDatabase.setVisible(false);
		frame.getContentPane().add(btnSaveToDatabase);
		
		JButton btnAddSubject = new JButton("Add Subject");
		btnAddSubject.setBackground(new Color(255, 235, 205));
		btnAddSubject.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnAddSubject.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSaveToDatabase.setVisible(true);
			}
		});
		btnAddSubject.setBounds(475, 158, 162, 32);
		frame.getContentPane().add(btnAddSubject);
		
		JButton btnViewRegisterSubject = new JButton("View Register Subject");
		btnViewRegisterSubject.setBackground(new Color(255, 235, 205));
		btnViewRegisterSubject.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnViewRegisterSubject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSaveToDatabase.setVisible(false);
				//table.setModel(new DefaultTableModel(null, new String[] {"Course ID", "Course Name", "Credit Hours"}));
				int userID = Integer.parseInt(txtUserId.getText());
				try {
			        Socket socket = new Socket("localhost", 8001);
			        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			        writer.println("VIEW_REGISTERED_SUBJECTS");
			        writer.println(userID); // send the user ID to the server

			        String response = reader.readLine();
			        if (response!= null && response.equals("VIEW_REGISTERED_SUBJECTS_RESPONSE")) {
			            tableModel.setRowCount(0); // clear the table model
			            while (true) {
			                String line = reader.readLine();
			                if (line == null || line.equals("END_OF_DATA")) {
			                    break;
			                }
			                String[] columns = line.split(",");
			                tableModel.addRow(columns);
			            }
			            table.setModel(tableModel);
			        } else {
			            JOptionPane.showMessageDialog(frame, "Error loading data: ", null, JOptionPane.ERROR_MESSAGE);
			        }
			        socket.close();
			    } catch (IOException ex) {
			        JOptionPane.showMessageDialog(frame, "Error loading data: ", null, JOptionPane.ERROR_MESSAGE);
			    }
			}
		});
		btnViewRegisterSubject.setBounds(475, 200, 162, 32);
		frame.getContentPane().add(btnViewRegisterSubject);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 83, 426, 191);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable(); // initialize the JTable instance
        scrollPane.setViewportView(table);
        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] { "Course ID", "Course Name", "Credit Hours" }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showInternalConfirmDialog(null,
						"Are you confirm to logout?",
						"Logout",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
					if (confirm == JOptionPane.YES_OPTION) {
						frame.setVisible(false);
						CourseManagement courseManagement = new CourseManagement();
						courseManagement.main(null);
						}
			}
		});
		btnLogout.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnLogout.setBackground(new Color(255, 235, 205));
		btnLogout.setBounds(475, 242, 162, 32);
		frame.getContentPane().add(btnLogout);
		
		JButton btnViewSubject = new JButton("View Subject");
		btnViewSubject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadData();
			}
		});
		btnViewSubject.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnViewSubject.setBackground(new Color(255, 235, 205));
		btnViewSubject.setBounds(475, 116, 162, 32);
		frame.getContentPane().add(btnViewSubject);
		
		JLabel lblUserId = new JLabel("User ID");
		lblUserId.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		lblUserId.setBounds(39, 369, 107, 23);
		frame.getContentPane().add(lblUserId);
		table.getColumnModel().getColumn(0).setPreferredWidth(100); 
	    table.getColumnModel().getColumn(1).setPreferredWidth(350); 
	    table.getColumnModel().getColumn(2).setPreferredWidth(100);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Account");
		menuBar.add(mnNewMenu);
		
		Button button = new Button("LogOut");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				CourseManagement courseManagement = new CourseManagement();
				courseManagement.main(null);
			}
		});
		mnNewMenu.add(button);
	}
		
	private void loadData() {
		try {
            Socket socket = new Socket("localhost", 8001);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println("LOAD_DATA");

            String response = reader.readLine();
            if (response.equals("LOAD_DATA_RESPONSE")) {
                tableModel.setRowCount(0); // clear the table model
                while (true) {
                    String line = reader.readLine();
                    if (line.equals("END_OF_DATA")) {
                        break;
                    }
                    String[] columns = line.split(",");
                    tableModel.addRow(columns);
                }
                table.setModel(tableModel);
            } else {
                JOptionPane.showMessageDialog(frame, "Error loading data: ", null, JOptionPane.ERROR_MESSAGE);
            }
            socket.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading data: ", null, JOptionPane.ERROR_MESSAGE);
        }
	}
}
