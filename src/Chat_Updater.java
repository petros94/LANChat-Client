import org.eclipse.swt.widgets.Display;

public class Chat_Updater implements Runnable {
	private Main_controller controller;
	private int messageCounter;
	json_parser parser;
	String_modifier modifier;

	
	public Chat_Updater(GepChat_ChatWindow chatWindow_GUI, Main_controller controller) {
		this.controller = controller;
		this.messageCounter = 0;
		parser = new json_parser();
		modifier = new String_modifier("&");

	}
	
	public void run() {
		
		try {
			update_chat();

		} catch (Exception e) {
			System.out.println(e);
			controller.set_chat_text("Connection Timeout or Backend Error.\n");
			controller.set_update_text_flag(true);
		}
		Display.getCurrent().timerExec(1000, this);
	}
	
	public void update_chat() throws Exception {
		controller.clear_chat_text();
		String ret = controller.get_active_comm().receive();
		String ret_msg = parser.parse_message(ret);
		String ret_from = parser.parse_from(ret);

		String[] messages = modifier.string_to_messages(ret_msg);
		
		
		for (int i = 0; i<messages.length; i++) {
			controller.set_chat_text(messages[i] + "\n");
			System.out.println( messages[i]);
		}

		
		controller.set_update_text_flag(true);		
	}

}
	
