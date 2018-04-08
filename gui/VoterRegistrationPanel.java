package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;
import systemCreation.Election;

public class VoterRegistrationPanel extends JPanel {

	//name ve surname þeklinde alýp, tek name olarak döndürebilirler !!
	private static final long serialVersionUID = 1L;
	private JTextField txtEmail;
	private JTextField txtName;
	private Datepicker txtBirthDate;
	private JTextField txtCertificateInfo;
	private JButton btnSign;
	private String email;
	private String name;
	private String surname;
	private Date birthdate;
	private String certInfo;
	private JTextField txtSurname;
	private ArrayList<Election> activeElectionList;
	private JComboBox activeElectionCb;
	private int selectedElectionIndex;
	

	/**
	 * Create the panel.
	 * @throws Exception 
	 */
	public VoterRegistrationPanel() throws Exception {
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		setSize(new Dimension(800, 800));
		
		JPanel surrodingPanel = new JPanel();
		surrodingPanel.setBackground(SystemColor.inactiveCaptionBorder);
		surrodingPanel.setBounds(33, 152, 684, 376);
		add(surrodingPanel);
		surrodingPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("E-mail Adress:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(58, 63, 140, 27);
		surrodingPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Name:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(58, 107, 140, 27);
		surrodingPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Birth Date:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(58, 208, 140, 27);
		surrodingPanel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Certificate Info:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_3.setBounds(58, 259, 140, 27);
		surrodingPanel.add(lblNewLabel_3);
		
		txtEmail = new JTextField();
		txtEmail.setText("arzumkaratas@iyte.edu.tr");
		txtEmail.setBounds(225, 60, 400, 27);
		surrodingPanel.add(txtEmail);
		txtEmail.setColumns(10);
		
		txtName = new JTextField();
		txtName.setText("arzum");
		txtName.setBounds(225, 106, 400, 27);
		surrodingPanel.add(txtName);
		txtName.setColumns(10);
		
		txtBirthDate = new Datepicker();
		txtBirthDate.setBounds(227, 208, 226, 27);
		surrodingPanel.add(txtBirthDate);
		
		txtCertificateInfo = new JTextField();
		txtCertificateInfo.setText("arzumkaratas.txt");
		txtCertificateInfo.setBounds(225, 261, 400, 27);
		surrodingPanel.add(txtCertificateInfo);
		txtCertificateInfo.setColumns(10);
		
		btnSign = new JButton("Sign");
		/*
		btnSign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				signButtonClick();
				
			}
		});
		*/
		btnSign.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSign.setBounds(513, 319, 112, 46);
		surrodingPanel.add(btnSign);
		
		JLabel lblSurname = new JLabel("Surname:");
		lblSurname.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSurname.setBounds(58, 157, 140, 27);
		surrodingPanel.add(lblSurname);
		
		txtSurname = new JTextField();
		txtSurname.setText("karata\u015F");
		txtSurname.setColumns(10);
		txtSurname.setBounds(225, 156, 400, 27);
		surrodingPanel.add(txtSurname);
		
		JLabel lblElection = new JLabel("Election:");
		lblElection.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblElection.setBounds(58, 11, 140, 27);
		surrodingPanel.add(lblElection);
		
		activeElectionCb = new JComboBox();
		activeElectionCb.setBackground(SystemColor.inactiveCaptionBorder);
		activeElectionList = Controller.getInstance().getDbHelper().getElectionsIfActiveRegistration();
		String[] elecStr = new String[activeElectionList.size()];
		for(int i = 0; i<activeElectionList.size();i++) {
			elecStr[i] =  activeElectionList.get(i).getElectionTitle();
		}/*
		elecStr = new String[3];
		elecStr[0] = "1 or el";
		elecStr[1] = "2 or el";
		elecStr[2] = "3 or el";*/
		activeElectionCb.setModel(new DefaultComboBoxModel(elecStr));
		activeElectionCb.setBounds(225, 13, 400, 27);
		surrodingPanel.add(activeElectionCb);
		
		JLabel lblNewLabel_4 = new JLabel("Voter Registration");
		lblNewLabel_4.setBounds(33, 25, 268, 65);
		add(lblNewLabel_4);
		lblNewLabel_4.setForeground(new Color(0, 0, 51));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 30));

	}
	
	public boolean signButtonClick() {
		boolean retVal = true;
		emptyFields();
		selectedElectionIndex = activeElectionCb.getSelectedIndex();
		
		if (txtEmail.getText().trim().equals("") || txtEmail.getText().trim()==null ) {
			JOptionPane.showMessageDialog(null, "Email Cannot Be Empty!" );	
			retVal = false;
			
		} else {
			emailCheck();
		}
			
		if (txtName.getText().trim().equals("") || txtName.getText().trim()==null ) {
			JOptionPane.showMessageDialog(null, "Name Cannot Be Empty" );		
			retVal = false;
		} else {
			name = txtName.getText().trim();
		}	
		
		if (txtSurname.getText().trim().equals("") || txtSurname.getText().trim()==null ) {
			JOptionPane.showMessageDialog(null, "Surname Cannot Be Empty" );
			retVal = false;
		} else {
			surname = txtSurname.getText().trim();
		}	
		
		birthdate = txtBirthDate.getDate();
		
		if (txtCertificateInfo.getText().trim().equals("") || txtCertificateInfo.getText().trim()==null ) {
			JOptionPane.showMessageDialog(null, "Certificate Information Cannot Be Empty" );	
			retVal = false;
		} else {
			certInfo = txtCertificateInfo.getText().trim();
		}		
		JOptionPane.showMessageDialog(null, "Id:"+Integer.toString(selectedElectionIndex)+"\nEmail: "+ email + "\nName: "+ name+"\nSurname: "+surname+ "\nbd:"+birthdate.toString() +"\nCert: "+certInfo );
		
		return retVal;	
	}

	
	public void emailCheck() {
		if(EmailValidator.isValidEmailAddress(txtEmail.getText()) && (EmailValidator.isIyteEmailAddress(txtEmail.getText()) || EmailValidator.isIyteStudentEmailAddress(txtEmail.getText()))) {
			email = txtEmail.getText().trim();		
		} else{		
			JOptionPane.showMessageDialog(null, "Email is not valid or not authorized to vote." );
		}
	}
	
	public void emptyFields() {
		email = "";
		name = "";
		surname = "";
		certInfo = "";		
	}

	public JButton getBtnSign() {
		return btnSign;
	}

	public void setBtnSign(JButton btnSign) {
		this.btnSign = btnSign;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getCertInfo() {
		return certInfo;
	}

	public void setCertInfo(String certInfo) {
		this.certInfo = certInfo;
	}
	
	public Election getSelectedElection() {
		return activeElectionList.get(selectedElectionIndex);
	}
	
	
}
