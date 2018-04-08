package gui;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import controller.Controller;

public class TimeManager extends TimerTask {
	   private MainWindow mainWindow = null;
	   private Date lastActivity = null;
	   private Controller c;
	   
	   public TimeManager(MainWindow mainWindow) throws Exception {
		   this.mainWindow = mainWindow;
		    this.c = Controller.getInstance();
	       Timer timer = new Timer();
	       timer.scheduleAtFixedRate(this, new Date(), 1000);
	   }

	    public void run(){
	    	Runnable r = getEvent();
	    	if( r != null){
	    		SwingUtilities.invokeLater(r);
	    	}
	    		      
	    }

		private Runnable getEvent() {
			if (lastActivity == null){
				lastActivity = new Date();
			}
			Date regStart = c.getRegStartTime();
			Date regEnd = c.getRegEndTime();
			Date elStart = c.getElectionStartTime();
			Date elEnd = c.getElectionEndTime();
			Date now = new Date();
			
			if(regStart==null || regEnd==null || elStart==null || elEnd==null){
				return null;
			}
			
			/*registration zamaný geldiðinde registration paneli aç
			 * eger o zaman içinde panel açýlmýþsa bir daha paneli açma
			 */
			if( isInRange(regStart.getTime(),regEnd.getTime(),now.getTime()) && !isInRange(regStart.getTime(),regEnd.getTime(),lastActivity.getTime()))
				{
					lastActivity = now;
					return startRegistration();
				}
			
			/*
			 * registration zamaný dolunca, kaydý durdur
			 */
			if( regEnd.getTime() < now.getTime() && isInRange(regStart.getTime(),regEnd.getTime(),lastActivity.getTime()))
			{
				lastActivity = now;
				return stopRegistration();
			}
			
			/*
			 * Election baþlat
			 * 
			 */
			if( isInRange(elStart.getTime(),elEnd.getTime(),now.getTime()) && !isInRange(elStart.getTime(),elEnd.getTime(),lastActivity.getTime()))
			{
				lastActivity = now;
				return startElection();
			}
			/*
			 * Election bitir
			 */
			if( elEnd.getTime() < now.getTime() && isInRange(elStart.getTime(),elEnd.getTime(),lastActivity.getTime()))
			{
				lastActivity = now;
				return endElection();
			}
			//System.out.println("Event Checked..");
			return new Runnable() {
				@Override
				public void run() {
					//mainWindow.openRegistrationPanel();
					System.out.println("Checked:" + now.toString());
				}
			}; 
		}
	    

		private boolean isInRange(Long lower, Long upper, Long current){
			return (lower<=current && upper>=current);
		}
		
		public Runnable stopRegistration() {
			return new Runnable() {
				@Override
				public void run() {
					try {
						c = Controller.getInstance();
						c.stopRegistration();
						
						mainWindow.openStatusPanel();
						mainWindow.statusPanel.publishStatus("Registration period ended.");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Registration End.");
				}
			};
		}
	    
		public Runnable startRegistration(){
			return new Runnable() {
				@Override
				public void run() {
					try {
						Controller.getInstance().startRegistration();
						mainWindow.openRegistrationPanel();
						System.out.println("Registration started");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}; 
		}
		
		public Runnable startElection() {
			return new Runnable() {
				@Override
				public void run() {
					try {
						Controller.getInstance().startVoting();
						mainWindow.openElectionPanel();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Election started");
				}
			};
		}
		
		public Runnable endElection() {
			return new Runnable() {
				@Override
				public void run() {
					System.out.println("Election End.");
					
					try {
						Controller.getInstance().endVoting();
						mainWindow.openStatusPanel();
						mainWindow.statusPanel.announceResult();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}

}
