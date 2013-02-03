package hu.ichors.taskassistant;

import android.os.Bundle;
import android.app.Activity;
import android.text.ClipboardManager;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	/**
	 * TaskAssistant
	 *
	 * Two Functions:
	 *
	 * 1) GetDateTime
	 * Gets current date and time and write it in quasi ISO format to the clipboard and to the screen.
	 *
	 * 2) CreateHeader
	 * Creates task header with status and creation date.
	 *
	 * @author andras
	 *
	 */

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView view = new TextView(this);
        Button btnGetDateTime = (Button)findViewById(R.id.btnGetDateTime);
        btnGetDateTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		        Time now = new Time();
		        now.setToNow();
		        String dateStr = now.format("%a %y-%m-%d %H:%M ");

		        ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		        clipboard.setText(dateStr);

		        Toast.makeText(MainActivity.this, clipboard.getText(),
		        		Toast.LENGTH_SHORT ).show();
		        
		        finish();
			}
		});

        Button btnCreateHeader = (Button)findViewById(R.id.btnCreateHeader);
        btnCreateHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

		        Time now = new Time();
		        now.setToNow();
		        String dateStr = now.format("%a %y-%m-%d %H:%M ");

		        String header = "";
		        header += "Status: ";
		        header += System.getProperty("line.separator");

		        header += "==================================";
		        header += System.getProperty("line.separator");

		        header += dateStr;
		        header += System.getProperty("line.separator");

		        header += "==================================";
		        header += System.getProperty("line.separator");

		        header += "Created: ";
		        header += now.format("%y-%m-%d");

		        ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		        clipboard.setText(header);

		        Toast.makeText(MainActivity.this, header, Toast.LENGTH_SHORT ).show();
		        
		        finish();
			}
		});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
