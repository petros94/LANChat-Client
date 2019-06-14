import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;


import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class GepChat_ChatWindow {

	protected Shell shell;
	private Text message;
	private Main_controller controller;
	private String title_var;
	public Text chat;
	public volatile String temp_text;
	private Label icon;
	public volatile boolean update_text_flag;
	

	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public GepChat_ChatWindow(String settings_path) {
		title_var = "";
		temp_text = "";
		update_text_flag = false;
		createContents();
		controller = new Main_controller(this, settings_path);
		String icon_path = controller.get_icon_path(settings_path);
		icon.setImage(SWTResourceManager.getImage(icon_path));
		open();
	}
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		shell.open();
		shell.layout();
		controller.start_updater();
		display.timerExec(1000, controller.updater);


		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
			if (update_text_flag == true) {
				display.wake();
				update_Text();
				update_text_flag = false;
			}
		}				
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		String s = "Gep Chat - " + title_var;
		shell.setText(s);
		
		icon = new Label(shell, SWT.NONE);
		icon.setBounds(360, 5, 70, 53);
		
		Label label_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(17, 223, 430, 2);
		
		message = new Text(shell, SWT.BORDER);
		message.addKeyListener(new MessageKeyListener());
		message.setBounds(18, 238, 328, 29);
		
		Button btnSend = new Button(shell, SWT.NONE);
		btnSend.addSelectionListener(new BtnSendSelectionListener());
		btnSend.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		btnSend.setBounds(363, 235, 64, 35);
		btnSend.setText("Send");
		
		chat = new Text(shell, SWT.READ_ONLY | SWT.V_SCROLL);
		chat.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		chat.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		chat.addModifyListener(new ChatModifyListener());
		chat.setBounds(0, 0, 447, 211);
		chat.setTopIndex(chat.getLineCount()-1);

	
	

	}
	
	public void update_Text() {
		chat.setText(temp_text);
		chat.setTopIndex(chat.getLineCount() - 1);
	}
	
	private class BtnSendSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			controller.send_message(message.getText());
			message.setText("");
		}
	}
	private class MessageKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.character == 13) {
				controller.send_message(message.getText());
				message.setText("");
			}
		}
	}
	private class ChatModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent arg0) {
		}
	}
	public void set_title_var(String title) {
		// TODO Auto-generated method stub
		title_var = title;
		String s = "LAN Chat - " + title_var;
		shell.setText(s);
		
	}

}
