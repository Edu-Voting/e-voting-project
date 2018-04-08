package gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.LayoutStyle.ComponentPlacement;

import controller.Controller;
import preparationForVoting.Ballot;
import preparationForVoting.BulletinBoard;
import systemCreation.Election;

//GUIController
public class MainWindow {

	private JFrame frame;
	private JFrame menu;
	public JPanel mainPanel;
	public AdminPanel adminPanel;
	public VoterRegistrationPanel voterRegistrationPanel;
	public VoterLoginPanel voterLoginPanel ;
	public QueryMyVotePanel queryMyVotePanel;
	public ElectionPanel electionPanel;	
	public VotingPanel votingPanel ;	
	public BulletinBoardPanel bb ;
	public JPanel currentPanel = new JPanel();
	public StatusPanel statusPanel;
	
	private Controller controller=null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws Exception 
	 */
	public MainWindow() throws Exception {
		controller = Controller.getInstance();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.textHighlight);
		frame.setBounds(100, 100, 900, 438);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH); 
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(SystemColor.textHighlight);
		rightPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(SystemColor.textHighlight);
		
		mainPanel = new JPanel();
		mainPanel.setBackground(SystemColor.textHighlight);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 800, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(rightPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
						.addComponent(mainPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
						.addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))
					.addContainerGap())
		);
		frame.getContentPane().setLayout(groupLayout);
		mainPanel.setLayout(null);
		
		
		voterRegistrationPanel = new VoterRegistrationPanel();
		voterRegistrationPanel.setLocation(0,0);	
			
		voterLoginPanel = new VoterLoginPanel();
		voterLoginPanel.setLocation(0, 0);
		voterLoginPanel.btnLogin.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					voterLoginPanel.loginClick();
					loginActivity();
					JOptionPane.showMessageDialog(null, "Oyunu bozdunuz... ");
					openElectionPanel();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} //
			}
		});
		
		queryMyVotePanel = new QueryMyVotePanel();
		queryMyVotePanel.setLocation(0, 0);
		
		electionPanel = new ElectionPanel();
		electionPanel.setLocation(0, 0);
		electionPanel.btnCastVote.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							openBallotPanel();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				
		electionPanel.btnQueryVote.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "Bilmem ki programlanmadim..");
						
					}
				});
		
		adminPanel = new AdminPanel();
		adminPanel.setLocation(0, 0);
		
		debug deb = new debug();
		//mainPanel.add(deb);
		menu = new JFrame();
		//menu.getContentPane().setLayout(groupLayout);
		menu.setSize(400,150);
		menu.add(deb);
		menu.show();
		
		statusPanel = new StatusPanel();
		statusPanel.setLocation(0, 0);
		statusPanel.publishStatus("System Started");
		
		openAdminPanel();
		
		//openBulletinBoardPanel();
		
		
		
		//birthDate'i Datepicker'dan alýnacak!!
		voterRegistrationPanel.getBtnSign().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!voterRegistrationPanel.signButtonClick()){//burada hepsi doldurulmazsa kontrol gerekecek.
					return;
				}
				//þimdilik ilk election alýndý
				Election election = voterRegistrationPanel.getSelectedElection();
				try {
					System.out.println("registration method is started");
					controller.registerVoter(election, voterRegistrationPanel.getName()+" "+voterRegistrationPanel.getSurname(), voterRegistrationPanel.getEmail(), voterRegistrationPanel.getBirthdate(), voterRegistrationPanel.getCertInfo());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
	
		/*deb.btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openAdminPanel();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		deb.btnReg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openRegistrationPanel();
			}
		});
		
		deb.btnVote.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openBallotPanel();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		
		deb.btnElectionPan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openElectionPanel();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		deb.btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openLoginPanel();
			}
		});
		
		deb.btnBullet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openBulletinBoardPanel();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		deb.btnQueryMy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openQueryMyVotePanel();
			}
		});*/
		
		
		TimeManager t = new TimeManager(this); 
		deb.startreg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable r = t.startRegistration();
				SwingUtilities.invokeLater(r);
				
			}
		});
		deb.endreg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable r = t.stopRegistration();
				SwingUtilities.invokeLater(r);
				
			}
		});
		
		deb.startvote.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable r = t.startElection();
				SwingUtilities.invokeLater(r);
				
			}
		});
		deb.endvote.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable r = t.endElection();
				SwingUtilities.invokeLater(r);
				
			}
		});
	} 
		

	

	/* reset eklencek
	public void resetElectionClick() throws Exception {
					
	}
	*/
	
	public void openBallotPanel() throws Exception {
		mainPanel.remove(currentPanel);
		
		votingPanel = new VotingPanel(); 
		votingPanel.setLocation(0, 0);
		votingPanel.btnVote.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {				
				castVoteActivity();
				openLoginPanel();
			}
		});
		mainPanel.add(votingPanel);	
		currentPanel = votingPanel;	
		votingPanel.repaint();
		mainPanel.repaint();
		
	}
	
	protected void openAdminPanel() throws Exception {
		mainPanel.remove(currentPanel);
		adminPanel = new AdminPanel();
		mainPanel.add(adminPanel);	
		adminPanel.btnCreateElection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {					
					controller.createElection(adminPanel.txtElectionTitle.getText(), adminPanel.startRegDp.getDate(), adminPanel.endRegDp.getDate(), adminPanel.startElDp.getDate(), adminPanel.endElDp.getDate());
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				
			}
		});
		currentPanel = adminPanel;
		mainPanel.repaint();
	}

	protected void openRegistrationPanel() {
		mainPanel.remove(currentPanel);
		mainPanel.add(voterRegistrationPanel);	
		currentPanel = voterRegistrationPanel;
		mainPanel.repaint();
		
	}

	protected void openElectionPanel() throws Exception {
		mainPanel.remove(currentPanel);
		mainPanel.add(electionPanel);	
		currentPanel = electionPanel;	
		mainPanel.repaint();
		
	}

	public void openQueryMyVotePanel() {
		mainPanel.remove(currentPanel);
		mainPanel.add(queryMyVotePanel);	
		currentPanel = queryMyVotePanel;	
		mainPanel.repaint();
		
		queryMyVotePanel.btnCheckMyVote.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				queryMyVotePanel.checkMyVoteClick();
				controller.setQueriedRegCode(queryMyVotePanel.getRegCode());
			}
		});
		
	}
	public void openLoginPanel() {
		mainPanel.remove(currentPanel);
		mainPanel.add(voterLoginPanel);	
		currentPanel = voterLoginPanel;	
		mainPanel.repaint();

		
	}

	public void openBulletinBoardPanel() throws Exception{
		mainPanel.remove(currentPanel);
		int electionID = controller.getPreparedElection().getElectionID();
		BulletinBoard bulletinBoard = controller.prepareBulletinBoard();
		bb = new BulletinBoardPanel(bulletinBoard);
		bb.setLocation(0,0);
		mainPanel.add(bb);	
		currentPanel = bb;	
		mainPanel.repaint();
	}
	public void openStatusPanel() {
		mainPanel.remove(currentPanel);
		mainPanel.add(statusPanel);	
		currentPanel = statusPanel;	
		mainPanel.repaint();
	}
	
	public void castVoteActivity(){
		controller.resetParameters();
		votingPanel.castVoteButtonClick();
		JOptionPane.showMessageDialog(null,votingPanel.getSelectedCandidateID() );
		controller.setSelectedCandidateID(votingPanel.getSelectedCandidateID());
	}
	
	public void loginActivity() throws Exception{
		controller.setEmail(voterLoginPanel.getEmail());
		controller.setRegCode(voterLoginPanel.getRegCode());
		controller.saveVote(); //kaydedildi, kaydedilmedi yazabilir voter'a.
	}


	
	
		
}
