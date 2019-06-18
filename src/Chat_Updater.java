import org.eclipse.swt.widgets.Display;

public class Chat_Updater implements Runnable {
	private Main_controller controller;
	private int messageCounter;
	json_parser parser;
	String_modifier modifier;
	AudioPlayer player;

	
	public Chat_Updater(GepChat_ChatWindow chatWindow_GUI, Main_controller controller) {
		this.controller = controller;
		this.messageCounter = 0;
		parser = new json_parser();
		modifier = new String_modifier("&");
	}
	
	public void run() {
		//Main run function of thread
		try {
			//Download new messages if they exist
			if (check_for_new_messages()){
				if (player != null) {
					//If a sound file is available
					//play sound notification
					player.restart();
				}
			}

		} catch (Exception e) {
			//Failed to download messages
			System.out.println(e);
			controller.set_chat_text("Connection Timeout or Backend Error.\n");
			controller.set_update_text_flag(true);
		}
		//execute same function after 1000ms
		Display.getCurrent().timerExec(1000, this);
	}
	
	public void update_chat() throws Exception {
		//clear text chat
		controller.clear_chat_text();
		
		//receive messages from server
		String ret = controller.get_active_comm().receive();
		String ret_msg = parser.parse_message(ret);
		String ret_from = parser.parse_from(ret);

		//parse actual messages
		String[] messages = modifier.string_to_messages(ret_msg);
		
		//copy to GUI
		for (int i = 0; i<messages.length; i++) {
			controller.set_chat_text(messages[i] + "\n");
			System.out.println( messages[i]);
		}

		//Set GUI update flag
		controller.set_update_text_flag(true);		
	}
	
	public void set_player(String settings) {
		try {
			//Init new player
			this.player = new AudioPlayer(controller.get_sound_path(settings));
		} catch (Exception e){
			//if the sound path doesn't exist player is null
		}
	}
	
	public boolean check_for_new_messages() throws Exception{
		//Check message counter to see if a new message is available from server
		String ret = controller.get_coms().receive();
		String ret_msg_counter = parser.parse_counter(ret);
		if (Integer.parseInt(ret_msg_counter) <= messageCounter) {
			return false;
		}
		else {
			//if it is available update GUI
			controller.clear_chat_text();
			messageCounter = Integer.parseInt(ret_msg_counter);
			String ret_msg = parser.parse_message(ret);
			String ret_from = parser.parse_from(ret);

			String[] messages = modifier.string_to_messages(ret_msg);
			
			
			for (int i = 0; i<messages.length; i++) {
				controller.set_chat_text(messages[i] + "\n");
				System.out.println( messages[i]);
			}
			controller.set_update_text_flag(true);		
			return true;
		}
	}
	
	public void init_counter() throws Exception{
		String ret = controller.get_coms().receive();
		String ret_msg_counter = parser.parse_counter(ret);
		messageCounter = Integer.parseInt(ret_msg_counter);
	}
	
	public void increment_counter() {
		messageCounter ++;
	}


}
	
