package hu.ichors.taskassistant;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
import android.text.ClipboardManager;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

	/// TODO: add another layout for landscape view

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
	 * @author ichors
	 *
	 */

	private boolean ignoreTime = false;
	private boolean append = false;
	DatePicker datePicker;
    TextView tvClipboard;
    CheckBox chbxIgnoreTime;
	CheckBox chbxAppend;
	TimePicker timePicker;
	TextView tvNameOfDay;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        Button btnGetDateTime = (Button)findViewById(R.id.btnGetDateTime);
        btnGetDateTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		        ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		        if (append) {
		        	clipboard.setText(getDateTimeStr() + " " + clipboard.getText());
		        } else {
		        	clipboard.setText(getDateTimeStr());
		        }

		        Toast.makeText(MainActivity.this, clipboard.getText(),
		        		Toast.LENGTH_SHORT ).show();

		        finish();
			}
		});

        Button btnCreateHeader = (Button)findViewById(R.id.btnCreateHeader);
        btnCreateHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

		        ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

		        String header = "";
		        header += "Status: ";
		        header += System.getProperty("line.separator");

		        header += "==================================";
		        header += System.getProperty("line.separator");

		        header += getDateTimeStr();
		        if (append) {
		        	header += (String)clipboard.getText();
		        }
		        header += System.getProperty("line.separator");

		        header += "==================================";
		        header += System.getProperty("line.separator");

		        header += "Created: ";
		        header += getDateStr();

				clipboard.setText(header);

		        Toast.makeText(MainActivity.this, header, Toast.LENGTH_SHORT ).show();

		        finish();
			}
		});

		chbxAppend = (CheckBox) findViewById(R.id.chbxAppend);
        chbxAppend.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if ( isChecked != append ) {
						append = isChecked;
					}
				}
			});

        append = chbxAppend.isChecked();

        tvClipboard = (TextView) findViewById(R.id.tvClipboard);
        tvClipboard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				append = !append;
				chbxAppend.setChecked(append);
			}
		});

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Calendar c = Calendar.getInstance();
        Time now = new Time();
        now.setToNow();
        datePicker.init(now.year, now.month, now.monthDay, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// date changed, using the (current) time is not expected
				if (!ignoreTime) {
					ignoreTime = false;
					chbxIgnoreTime.setChecked(true);
					timePicker.setVisibility(View.GONE);
				}
				// update name of day
				UpdateNameOfDay();
			}
		});

        tvNameOfDay = (TextView)findViewById(R.id.tvNameOfDay);
        UpdateNameOfDay();
        
        chbxIgnoreTime = (CheckBox) findViewById(R.id.chbxIgnoreTime);
        chbxIgnoreTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if ( isChecked != ignoreTime ) {
					ignoreTime = isChecked;
					timePicker.setVisibility(ignoreTime ? View.GONE : View.VISIBLE);
				}
			}
		});

        ignoreTime = chbxIgnoreTime.isChecked();
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setVisibility(ignoreTime ? View.GONE : View.VISIBLE);
        timePicker.setIs24HourView(true);
    }

	@Override
	protected void onResume() {
		super.onResume();

        tvClipboard = (TextView) findViewById(R.id.tvClipboard);
        ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        tvClipboard.setText(clipboard.getText());

        Calendar c = Calendar.getInstance();
        Time now = new Time();
        now.setToNow();
        datePicker.updateDate(now.year, now.month, now.monthDay);

        timePicker.setCurrentHour(now.hour - 1);	// force redraw of hours (known TimePicker bug)
        timePicker.setCurrentHour(now.hour);
        timePicker.setCurrentMinute(now.minute);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private String getDateTimeStr() {
    	// get date and time from the controls
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(
    			datePicker.getYear(),
    			datePicker.getMonth(),
    			datePicker.getDayOfMonth(),
    	        ignoreTime ? 0 : timePicker.getCurrentHour(),
    	        ignoreTime ? 0 : timePicker.getCurrentMinute(),
    	        0);
    	Time dateTime = new Time();
    	dateTime.set(calendar.getTimeInMillis());

    	String dateTimeStr;
    	if (!ignoreTime) {
    		dateTimeStr = dateTime.format("%a %y-%m-%d %H:%M ");
    	} else {
    		dateTimeStr = dateTime.format("%a %y-%m-%d");
    	}

    	return dateTimeStr;
    }

    private String getDateStr() {
    	// get date and time from the controls
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(
    			datePicker.getYear(),
    			datePicker.getMonth(),
    			datePicker.getDayOfMonth(),
    	        0, 0, 0);
    	Time date = new Time();
    	date.set(calendar.getTimeInMillis());

    	String dateStr = date.format("%a %y-%m-%d");

    	return dateStr;
    }
    
    private void UpdateNameOfDay() {
    	String nameOfDay = "";
    	Calendar calendar = new GregorianCalendar(datePicker.getYear(), 
    			datePicker.getMonth(), datePicker.getDayOfMonth());
    	switch ( calendar.get(Calendar.DAY_OF_WEEK) ) {
    	case Calendar.MONDAY:
    		nameOfDay = "Monday";
    		break;
    	case Calendar.TUESDAY:
    		nameOfDay = "Tuesday";
    		break;
    	case Calendar.WEDNESDAY:
    		nameOfDay = "Wednesday";
    		break;
    	case Calendar.THURSDAY:
    		nameOfDay = "Thursday";
    		break;
    	case Calendar.FRIDAY:
    		nameOfDay = "Friday";
    		break;
    	case Calendar.SATURDAY:
    		nameOfDay = "Saturday";
    		break;
    	case Calendar.SUNDAY:
    		nameOfDay = "Sunday";
    		break;
    	default:
    		nameOfDay = "" + calendar.get(Calendar.DAY_OF_WEEK);
    		break;    		
    	}
    	
    	tvNameOfDay.setText(nameOfDay);
    }
}
