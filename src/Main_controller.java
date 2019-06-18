

import org.eclipse.swt.widgets.Text;


public class Main_controller {
	private Backend_Proxy active_comm;
	private GepChat_ChatWindow chatWindow_GUI;
	public Chat_Updater updater;
	private String user;
	private String settings_path;
	
	public Main_controller(GepChat_ChatWindow chatWindow_GUI, String settings_path) {
		this.settings_path = settings_path;
		configure_client(settings_path);
		init_gui(chatWindow_GUI, settings_path);
	}
	
	public void configure_client(String settings_path) {
		/* Import settings file - using json parser*/
		try {
			Settings_Parser settings_parser = new json_Settings_Parser();
			
	
			String server = settings_parser.get_server(settings_path);
			String sender = settings_parser.get_sender(settings_path);
		
			active_comm = new Http_Proxy(sender, sender, server);
			user = sender;
		
		} catch (Exception e) {
			System.out.println("Error configuring client");
		}
		
		
	}
	
	public void init_gui(GepChat_ChatWindow chatWindow_GUI, String settings_path) {
		/* Initialize chat window*/
		this.chatWindow_GUI = chatWindow_GUI;
		
		/* set titles and text*/
		String title = "";
		try {
			title = (new json_Settings_Parser()).get_sender(settings_path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chatWindow_GUI.set_title_var(title);
	}
	
	
	public void send_message(String message) {
		try {
			/* Call backend_proxy*/
			active_comm.send(user +": " + message);
			
			/*Update chat text*/
			updater.update_chat();
			updater.increment_counter();

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			set_chat_text("Message failed to send!\n");
		}
	}
	
	
	public Backend_Proxy get_coms() {
		return active_comm;
	}
	
	public Backend_Proxy get_active_comm() {
		return active_comm;
	}
	
	
	public void start_updater() {
		updater = new Chat_Updater(chatWindow_GUI, this);
		try {
			updater.set_player(settings_path);
		} catch (Exception e) {
		}
	}
	
	public void update_chat_text() {
		try {
			updater.update_chat();
		} catch (Exception e) {
			System.out.println("Failed to update chat");
		}
	}
	
	public void set_chat_text(String new_text) {
		chatWindow_GUI.temp_text += new_text;
	}
	
	public Text get_chat_text() {
		return chatWindow_GUI.chat;
	}


	public void set_update_text_flag(boolean value) {
		// TODO Auto-generated method stub
		chatWindow_GUI.update_text_flag = value;
		
	}
	
	public boolean get_window_status() {
		return !chatWindow_GUI.shell.isDisposed();
	}

	public void clear_chat_text() {
		// TODO Auto-generated method stub
		chatWindow_GUI.temp_text = "";
		
	}
	
	public String get_icon_path(String settings_path) {
		try {
			return new json_Settings_Parser().get_icon_path(settings_path);
		} catch (Exception e) {
			return "";
		}
		
	}
	
	public String get_sound_path(String settings_path) {
		try {
			return new json_Settings_Parser().get_sound_path(settings_path);
		} catch (Exception e) {
			return "";
		}
		
	}


}
