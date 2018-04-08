package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import controller.Controller;
import preparationForVoting.Ballot;
import preparationForVoting.Candidate;




public class VotingPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private ArrayList<JRadioButton> candidateRBList ;
	private int selectedCandidateID;
	private String candName;

	public JButton btnVote;
	private ArrayList<Candidate> candidateList;
	
	/**
	 * Create the panel.
	 * @throws Exception 
	 */
	public VotingPanel() throws Exception {
		// test datasý olusturuldu
		//test();
		
		
		
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		setSize(new Dimension(800, 800));
		
		JPanel surrodingPanel = new JPanel();
		surrodingPanel.setBackground(SystemColor.inactiveCaptionBorder);
		surrodingPanel.setBounds(33, 152, 684, 376);
		add(surrodingPanel);
		surrodingPanel.setLayout(null);
		
		btnVote = new JButton("Vote");
		btnVote.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnVote.setBounds(513, 270, 112, 46);
		surrodingPanel.add(btnVote);
				
		

		/*
		JPanel panelCandidate = new JPanel();
		
		panelCandidate.setBackground(SystemColor.inactiveCaptionBorder);
		panelCandidate.setLayout(new BoxLayout(panelCandidate, BoxLayout.Y_AXIS));
*/
		/*
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		scrollPane.setAutoscrolls(true);
		scrollPane.setBounds(10, 11, 664, 243);
		
		scrollPane.setViewportView(panelCandidate);
		*/
		
		
		JPanel panelCandidate = new JPanel();
		panelCandidate.setBackground(SystemColor.inactiveCaptionBorder);
		panelCandidate.setLayout(new BoxLayout(panelCandidate, BoxLayout.Y_AXIS));
		panelCandidate.setBounds(10, 11, 664, 248);
		surrodingPanel.add(panelCandidate);
		

		//int x = 0;
		int y = 0;
		try {
			candidateRBList = new ArrayList<JRadioButton>();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		candidateList = Controller.getInstance().getPreparationForVotingManager().getCandidateManager().getCandidates();
		for (int i = 0; i <candidateList.size();i++){
				final JRadioButton rdbtnNewRadioButton = new JRadioButton(candidateList.get(i).getName());
			 	candidateRBList.add(rdbtnNewRadioButton);
			 	buttonGroup.add(rdbtnNewRadioButton);
				rdbtnNewRadioButton.setBounds(70, 21+y, 109, 23);
				rdbtnNewRadioButton.setBackground(SystemColor.inactiveCaptionBorder);
				panelCandidate.add(rdbtnNewRadioButton);
				y += 30;
				
		}

//		JScrollPane scrollPane_1 = new JScrollPane(panelCandidate);
//		scrollPane_1.setBounds(10, 11, 664, 248);
		
		//scrollPane_1.setViewportView(scrollPane);

		
		JLabel lblNewLabel_4 = new JLabel("Voting");
		lblNewLabel_4.setBounds(33, 25, 268, 65);
		add(lblNewLabel_4);
		lblNewLabel_4.setForeground(new Color(0, 0, 51));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 30));

	
	
	}
	
	//vote butonuna basýldýðýnda candidate seçilen butonun ismi deðiþtirildi!!
	public void castVoteButtonClick(){
		
		for(int i = 0; i<candidateRBList.size();i++){
			JRadioButton rb = candidateRBList.get(i);
			if (rb.isSelected()) {
				selectedCandidateID = candidateList.get(i).getCid();
				candName = candidateList.get(i).getName();
				JOptionPane.showMessageDialog(null, "ID: " + selectedCandidateID +"\nName:"+ candName);
				
				//controller.setSelectedCandidateID(selectedCandidateID);
				
			}
		}
	}
	
	public void test() {
		/*specifies specific dates for the election
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date bdate1 = null,bdate2 = null,bdate3 = null,bdate4 = null;
               
		try {
			bdate1 = sdf.parse("1-1-1993");
			bdate2 = sdf.parse("15-1-1995");
	    	bdate3 = sdf.parse("17-1-1991");
	    	bdate4 = sdf.parse("20-1-1992");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		create candidates
		Candidate c1 = new Candidate("Candidate1","Computer Engineer",bdate1);
		Candidate c2 = new Candidate("Candidate2","Chemical Engineer",bdate2);
		Candidate c3 = new Candidate("Candidate3","BioEngineer",bdate3);
		Candidate c4 = new Candidate("Candidate4","Physicist",bdate4);
		
		ArrayList<Candidate> candidates = new ArrayList<>();
		candidates.add(c1);
		candidates.add(c2);
		candidates.add(c3);
		candidates.add(c4);
		
		ballot = new Ballot(candidates);*/
	}



	//yeni eklendi
	public int getSelectedCandidateID() {
		return selectedCandidateID;
	}
}
