import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.*;
import java.awt.Color;

public class AdminPage {

	private JFrame frame;
	private JTextField textFieldCourseID;
	private JTextField textFieldCourseName;
	private JTextField textFieldCreditHours;
	private DefaultTableModel tableModel;

	/**
	 * Launch the application.
	 */
	public static void Admin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminPage window = new AdminPage();
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
	 */
	public AdminPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.setBounds(100, 100, 700, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ADMIN PAGE");
		lblNewLabel.setFont(new Font("Tw Cen MT", Font.BOLD, 30));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(170, 27, 224, 30);
		frame.getContentPane().add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(39, 122, 426, 191);
		frame.getContentPane().add(scrollPane);
		
		JTable table = new JTable();
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
		table.getColumnModel().getColumn(0).setPreferredWidth(100); 
	    table.getColumnModel().getColumn(1).setPreferredWidth(350); 
	    table.getColumnModel().getColumn(2).setPreferredWidth(100);
	    
	    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	        @Override
	        public void valueChanged(ListSelectionEvent e) {
	            if (!e.getValueIsAdjusting()) {
	                int selectedRow = table.getSelectedRow();
	                if (selectedRow != -1) {
	                    // Retrieve data from the selected row and set it to the text fields
	                    String courseID = (String) tableModel.getValueAt(selectedRow, 0);
	                    String courseName = (String) tableModel.getValueAt(selectedRow, 1);
	                    String creditHours = (String) tableModel.getValueAt(selectedRow, 2);

	                    textFieldCourseID.setText(courseID);
	                    textFieldCourseName.setText(courseName);
	                    textFieldCreditHours.setText(creditHours);
	                }
	            }
	        }
	    });
		
		JLabel lblNewLabel_1 = new JLabel("List Of Courses");
		lblNewLabel_1.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		lblNewLabel_1.setBounds(39, 86, 84, 13);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("ADD NEW COURSE");
		lblNewLabel_1_1.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		lblNewLabel_1_1.setBounds(39, 353, 147, 13);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Course ID");
		lblNewLabel_1_1_1.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		lblNewLabel_1_1_1.setBounds(39, 376, 147, 13);
		frame.getContentPane().add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Course Name");
		lblNewLabel_1_1_1_1.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		lblNewLabel_1_1_1_1.setBounds(39, 409, 147, 13);
		frame.getContentPane().add(lblNewLabel_1_1_1_1);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Credit Hours");
		lblNewLabel_1_1_1_1_1.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		lblNewLabel_1_1_1_1_1.setBounds(39, 439, 147, 13);
		frame.getContentPane().add(lblNewLabel_1_1_1_1_1);
		
		textFieldCourseID = new JTextField();
		textFieldCourseID.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		textFieldCourseID.setBounds(196, 373, 141, 19);
		frame.getContentPane().add(textFieldCourseID);
		textFieldCourseID.setColumns(10);
		
		textFieldCourseName = new JTextField();
		textFieldCourseName.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		textFieldCourseName.setColumns(10);
		textFieldCourseName.setBounds(196, 406, 141, 19);
		frame.getContentPane().add(textFieldCourseName);
		
		textFieldCreditHours = new JTextField();
		textFieldCreditHours.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		textFieldCreditHours.setColumns(10);
		textFieldCreditHours.setBounds(196, 436, 141, 19);
		frame.getContentPane().add(textFieldCreditHours);
		
		JButton btnAddCourse = new JButton("ADD COURSE");
		btnAddCourse.setBackground(new Color(255, 235, 205));
		btnAddCourse.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnAddCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String CourseName = textFieldCourseName.getText();
				String CreditHour = textFieldCreditHours.getText();
				
				if(CourseName.equals("") || CreditHour.equals("")) {
					JOptionPane.showMessageDialog(frame,"Please choose the data from the table","Alert",JOptionPane.WARNING_MESSAGE); 
				}
				
				//String url = "http://10.200.87.164/Project%20DAD/InsertUpdateCourse.php";
				String url = "http://localhost/ProjectDad/InsertUpdateCourse.php";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("CourseName", CourseName));
				params.add(new BasicNameValuePair("CreditHour", CreditHour));
				
				JSONObject response = makeHttpRequest(url, "POST", params);
				
				if (response != null) {
                    String message = response.optString("message", "Unknown response");
                    JOptionPane.showMessageDialog(frame, message, "Insert Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to receive response from server", "Error", JOptionPane.ERROR_MESSAGE);
                }
			}
		});
		btnAddCourse.setBounds(149, 480, 120, 21);
		frame.getContentPane().add(btnAddCourse);
		
		JButton btnEditCourse = new JButton("EDIT COURSE");
		btnEditCourse.setBackground(new Color(255, 235, 205));
		btnEditCourse.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnEditCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String CourseName = textFieldCourseName.getText();
				String CreditHour = textFieldCreditHours.getText();
				String CourseID = textFieldCourseID.getText();
				
				if(CourseName.equals("") || CreditHour.equals("") || CourseID.equals("")) {
					JOptionPane.showMessageDialog(frame,"Please choose the data from the table","Alert",JOptionPane.WARNING_MESSAGE); 
				}
				
				//String url = "http://10.200.87.164/Project%20DAD/InsertUpdateCourse.php";
				String url = "http://localhost/ProjectDad/InsertUpdateCourse.php";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("CourseID", CourseID));
				params.add(new BasicNameValuePair("CourseName", CourseName));
				params.add(new BasicNameValuePair("CreditHour", CreditHour));
				
				JSONObject response = makeHttpRequest(url, "POST", params);
				
				if (response != null) {
                    String message = response.optString("message", "Unknown response");
                    JOptionPane.showMessageDialog(frame, message, "Edit Result", JOptionPane.INFORMATION_MESSAGE);
                    
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to receive response from server", "Error", JOptionPane.ERROR_MESSAGE);
                }
			}
		});
		btnEditCourse.setBounds(503, 292, 120, 21);
		frame.getContentPane().add(btnEditCourse);
		
		JButton btnUpdateCourse = new JButton("UPDATE COURSE");
		btnUpdateCourse.setBackground(new Color(255, 235, 205));
		btnUpdateCourse.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnUpdateCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String CourseName = textFieldCourseName.getText();
				String CreditHour = textFieldCreditHours.getText();
				String CourseID = textFieldCourseID.getText();
				
				if(CourseName.equals("") || CreditHour.equals("") || CourseID.equals("")) {
					JOptionPane.showMessageDialog(frame,"Please choose the data from the table","Alert",JOptionPane.WARNING_MESSAGE); 
				}
				
				//String url = "http://10.200.87.164/Project%20DAD/InsertUpdateCourse.php";
				String url = "http://localhost/ProjectDad/InsertUpdateCourse.php";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("CourseID", CourseID));
				params.add(new BasicNameValuePair("CourseName", CourseName));
				params.add(new BasicNameValuePair("CreditHour", CreditHour));
				
				JSONObject response = makeHttpRequest(url, "POST", params);
				
				if (response != null) {
                    String message = response.optString("message", "Unknown response");
                    JOptionPane.showMessageDialog(frame, message, "Update Result", JOptionPane.INFORMATION_MESSAGE);
                    
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to receive response from server", "Error", JOptionPane.ERROR_MESSAGE);
                }
			}
		});
		btnUpdateCourse.setBounds(312, 480, 153, 21);
		frame.getContentPane().add(btnUpdateCourse);
		
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Account");
		menuBar.add(mnNewMenu);
		
		JButton button = new JButton("LogOut");
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
        // Replace with your PHP script URL
        //String url = "http://10.200.87.164/Project%20DAD/LoadSubjectData.php";
        String url = "http://localhost/ProjectDad/LoadSubjectData.php";
        
        JSONObject response = makeHttpRequest(url, "GET", null);

        if (response != null) {
            System.out.println("Response from server: " + response.toString());

            try {
                if (response.getBoolean("success")) {
                    JSONArray courses = response.getJSONArray("courses");

                    tableModel.setRowCount(0); // Clear existing table data

                    for (int i = 0; i < courses.length(); i++) {
                        JSONObject course = courses.getJSONObject(i);
                        String courseID = course.getString("course_id");
                        String courseName = course.getString("course_name");
                        String creditHours = course.getString("credit_hours");

                        tableModel.addRow(new Object[]{courseID, courseName, creditHours});
                    }
                } else {
                    System.out.println("Failed to fetch data: " + response.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Response from server was null.");
        }
    }
	
	private JSONObject makeHttpRequest(String strUrl, String method, List<NameValuePair> params) {
	    InputStream is = null;
	    String json = "";
	    JSONObject jObj = null;

	    try {
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpResponse httpResponse = null;

	        if (method.equals("POST")) {
	            HttpPost httpPost = new HttpPost(strUrl);
	            httpPost.setEntity(new UrlEncodedFormEntity(params));

	            httpResponse = httpClient.execute(httpPost);
	        } else if (method.equals("GET")) {
	            if (params != null) {
	                String paramString = URLEncodedUtils.format(params, "utf-8");
	                strUrl += "?" + paramString;
	            }
	            HttpGet httpGet = new HttpGet(strUrl);

	            httpResponse = httpClient.execute(httpGet);
	        }

	        HttpEntity httpEntity = httpResponse.getEntity();
	        is = httpEntity.getContent();

	        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line).append("\n");
	        }
	        is.close();
	        json = sb.toString();

	        jObj = new JSONObject(json);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return jObj;
	}
}
