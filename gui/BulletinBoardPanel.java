package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;
import preparationForVoting.BulletinBoard;
import preparationForVoting.Candidate;

import javax.swing.JTextArea;

public class BulletinBoardPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextArea status;
	private Controller c;
	private ArrayList<String> candidateNamesList;
	private ArrayList<JTextField> scoreTexts; 
	private ArrayList<Candidate> candidateList;

	/**
	 * Create the panel.
	 * @throws Exception 
	 */
	public BulletinBoardPanel(BulletinBoard bulletinBoard) throws Exception {
		
		
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		setSize(new Dimension(800, 800));
		
		JPanel surrodingPanel = new JPanel();
		surrodingPanel.setBackground(SystemColor.inactiveCaptionBorder);
		surrodingPanel.setBounds(33, 152, 684, 376);
		add(surrodingPanel);
		surrodingPanel.setLayout(null);
		
		JPanel panelCandidates = new JPanel();
		panelCandidates.setBackground(SystemColor.inactiveCaptionBorder);
		panelCandidates.setBounds(10, 11, 334, 211);
		surrodingPanel.add(panelCandidates);
		panelCandidates.setLayout(new BoxLayout(panelCandidates, BoxLayout.Y_AXIS));
		
		JLabel lblNoOfVotes = new JLabel("No of Votes Casted:");
		lblNoOfVotes.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNoOfVotes.setBounds(354, 14, 150, 27);
		surrodingPanel.add(lblNoOfVotes);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(503, 11, 171, 27);
		textField.setText(String.valueOf(bulletinBoard.getNoOfVotes()));
		surrodingPanel.add(textField);
		
		JLabel lblValidVoteCount = new JLabel("Valid Vote Count:");
		lblValidVoteCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblValidVoteCount.setBounds(354, 70, 150, 27);
		surrodingPanel.add(lblValidVoteCount);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(503, 67, 171, 27);
		textField_1.setText(String.valueOf(bulletinBoard.getNoOfValidVotes()));
		surrodingPanel.add(textField_1);
		
		JLabel lblInvalidVoteCount = new JLabel("Invalid Vote Count:");
		lblInvalidVoteCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInvalidVoteCount.setBounds(354, 123, 150, 27);
		surrodingPanel.add(lblInvalidVoteCount);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(503, 120, 171, 27);
		textField_2.setText(String.valueOf(bulletinBoard.getNoOfInvalidVotes()));
		surrodingPanel.add(textField_2);
		
		JPanel panelAnnouncements = new JPanel();
		panelAnnouncements.setBackground(new Color(244, 247, 252));
		panelAnnouncements.setBounds(10, 233, 664, 132);
		surrodingPanel.add(panelAnnouncements);
		panelAnnouncements.setLayout(null);
		
		JLabel lblAnnouncements = new JLabel("Announcements");
		lblAnnouncements.setBounds(10, 11, 134, 26);
		panelAnnouncements.add(lblAnnouncements);
		
		status = new JTextArea();
		status.setBounds(10, 39, 642, 80);
		panelAnnouncements.add(status);
		
		
		//int candidateCount = 6;
		//String[] candidateNames = {"Candidate1", "Candidate 2", "Candidate 3", "Candidate 4", "Candidate 5", "Candidate 6"};

		candidateList = Controller.getInstance().getPreparationForVotingManager().getCandidateManager().getCandidates();
		
		
		int candidateCount = candidateList.size();
		String[] candidateNames = new String[candidateCount];
		
		for(int i=0;i<candidateCount;i++){
			candidateNames[i] = candidateList.get(i).getName();
		}
		 
		
		int y1 = 0;
		int y2 = 0;
		for (int i = 0; i <candidateCount ;i++){
			JLabel lblInvalidVoteCount1 = new JLabel(candidateNames[i]);
			JTextField textField = new JTextField();
			candidateNamesList.add(candidateNames[i]);
			scoreTexts.add(textField);
			//textField.setColumns(1);
			textField.setBounds(180, y1, 30, 20);
			lblInvalidVoteCount1.setBounds(20, y2, 130, 30);			
			panelCandidates.add(lblInvalidVoteCount1);
			panelCandidates.add(textField);	
			y1 += 31;
			y2 += 30;
			
		}	
		
		
		
		
		JLabel lblNewLabel_4 = new JLabel("Bulletin Board");
		lblNewLabel_4.setBounds(33, 25, 268, 65);
		add(lblNewLabel_4);
		lblNewLabel_4.setForeground(new Color(0, 0, 51));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 30));

	}
	public void publishStatus(String str){
		status.setText(str);
	}
	
	public void updateResult() throws Exception {
		c = Controller.getInstance();
		Map<String, String> scoreMap = c.getScoreMap();
		for (int j = 0; j<candidateNamesList.size();j++) {
			String score = scoreMap.get(candidateNamesList.get(j));
			scoreTexts.get(j).setText(score);
			
		}
		
		String str = "";
		Map<String, String> privateKeyMap = c.getPrivateKeyMap();
		str += "System Private Key: \n\t Lambda:"+ privateKeyMap.get("lambda: ")+ "\n\t u:" + privateKeyMap.get("u: ");

		Map<String, String> publicKeyMap = c.getPublicKeyMap();
		str += "\nSystem Public Key: \n\t n:"+ publicKeyMap.get("n: ")+ "\n\t g:" + publicKeyMap.get("g: ");
		status.setText(str);
	}
}
