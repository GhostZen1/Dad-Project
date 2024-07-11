import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.awt.SystemColor;
import javax.swing.*;
import java.awt.*;

public class CourseManagement {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CourseManagement window = new CourseManagement();
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
	public CourseManagement() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		lblNewLabel.setBounds(53, 113, 120, 13);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblAdminPassword = new JLabel("Password");
		lblAdminPassword.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		lblAdminPassword.setBounds(53, 148, 120, 13);
		frame.getContentPane().add(lblAdminPassword);
		
		textField = new JTextField();
		textField.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		textField.setBounds(183, 110, 160, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(183, 145, 160, 19);
		frame.getContentPane().add(textField_1);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setBackground(new Color(255, 235, 205));
		btnLogin.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String adminName = textField.getText();
		        String AdminPass = textField_1.getText();

		        if (adminName.isEmpty() || AdminPass.isEmpty()) {
		            JOptionPane.showMessageDialog(frame, "Please fill in both username and password", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
				
				//String url = "http://10.200.87.164/Project%20DAD/Login.php";
				String url = "http://localhost/ProjectDad/Login.php";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", adminName));
				params.add(new BasicNameValuePair("password", AdminPass));
				
				JSONObject response = makeHttpRequest(url, "POST", params);

		        if (response != null) {
		            String message = response.optString("message", "Unknown response");
		            int isAdmin = response.optInt("isadmin");

		            if (isAdmin == 0) {
		                frame.setVisible(false);
		                StudentPage stud = new StudentPage(response.optInt("userid")); // Pass userid to StudentPage
		                stud.student();
		            } else if (isAdmin == 1) {
		                frame.setVisible(false);
		                AdminPage admin = new AdminPage();
		                admin.Admin();
		            }
		        } else {
		            JOptionPane.showMessageDialog(frame, "Failed to receive response from server", "Error", JOptionPane.ERROR_MESSAGE);
		        }
				
			}
			
			private JSONObject makeHttpRequest(String strUrl, String method, List<NameValuePair> params) {
				InputStream is = null;
				String json = "";
				JSONObject jObj = null;
				
				try {
					if (method == "POST") {
						DefaultHttpClient httpClient = new DefaultHttpClient();
						HttpPost httpPost = new HttpPost(strUrl);
						httpPost.setEntity(new UrlEncodedFormEntity(params));
						
						HttpResponse httpResponse = httpClient.execute(httpPost);
						HttpEntity httpEntity = httpResponse.getEntity();
						is = httpEntity.getContent();
						}
					else if (method == "GET") {
						DefaultHttpClient httpClient = new DefaultHttpClient();
						String paramString = URLEncodedUtils.format(params, "utf-8");
						strUrl += "?" + paramString;
						HttpGet httpGet = new HttpGet(strUrl);
						
						HttpResponse httpResponse = httpClient.execute(httpGet);
						HttpEntity httpEntity = httpResponse.getEntity();
						is = httpEntity.getContent();
					}
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while((line = reader.readLine()) !=null) {
						sb.append(line + "\n");
					}
					is.close();
					json = sb.toString();
					
					jObj = new JSONObject(json);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				return jObj;
				
			}
		});
		btnLogin.setBounds(99, 198, 102, 21);
		frame.getContentPane().add(btnLogin);
		
		JButton btnRegister = new JButton("REGISTER");
		btnRegister.setBackground(new Color(255, 235, 205));
		btnRegister.setFont(new Font("Tw Cen MT", Font.BOLD, 12));
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				//RegisterForm register = new RegisterForm();
				//register.main();
			}
		});
		btnRegister.setBounds(222, 198, 102, 21);
		frame.getContentPane().add(btnRegister);
		
		JLabel lblLoginPage = new JLabel("LOGIN PAGE");
		lblLoginPage.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginPage.setFont(new Font("Tw Cen MT", Font.BOLD, 30));
		lblLoginPage.setBounds(119, 33, 189, 37);
		frame.getContentPane().add(lblLoginPage);
		
		 // Load the image
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\Asus\\eclipse-workspace\\GroupProject\\images\\key.png");
        
        // Get the image from the ImageIcon
        Image originalImage = originalIcon.getImage();
        
        // Scale the image to fit the JLabel size (85x70)
        int labelWidth = 45;
        int labelHeight = 45;
        Image scaledImage = originalImage.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
        
        // Create a new ImageIcon from the scaled image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
		
		JLabel imageLabel_1 = new JLabel(scaledIcon);
		imageLabel_1.setLabelFor(frame);
		imageLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		imageLabel_1.setForeground(SystemColor.infoText);
		imageLabel_1.setBounds(306, 33, 52, 45);
		frame.getContentPane().add(imageLabel_1);
		

	}
}
