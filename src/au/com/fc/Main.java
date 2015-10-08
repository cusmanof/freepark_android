package au.com.fc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import au.com.fc.dialogs.Dialogs;
import au.com.fc.dialogs.DlgConfig;
import au.com.fc.models.MdlConfig;
import au.com.fc.models.MdlDates;
import au.com.fc.utils.IOUtils;
import com.squareup.timessquare.CalendarPickerView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main extends Activity {
    private Dialogs dialogs;
    private CalendarPickerView calendar;
    private Button btnUpdate;
    private TextView label;
    private MdlConfig config;
    private Date dStart;
    private Date dEnd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        label = (TextView) findViewById(R.id.label);
        dialogs = new Dialogs(this);

        dStart = Calendar.getInstance().getTime();
        Calendar cc = Calendar.getInstance();
        cc.add(Calendar.MONTH, 5);
        dEnd = cc.getTime();
        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        calendar.init(dStart, dEnd).inMode(CalendarPickerView.SelectionMode.MULTIPLE);

        if (!MdlConfig.isConfigured()) {
            dialogs.showYesNo(new DlgConfig(this), getString(R.string.r_u_owner));
        } else {


            config = MdlConfig.load();
            if (config != null) {
                if (config.isOwner()) {
                    label.setText(getString(R.string.select_free));
                    btnUpdate = new Button(this);
                    btnUpdate.setText(getString(R.string.update));
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendDatesFree();
                        }
                    });
                    LinearLayout ll = (LinearLayout) findViewById(R.id.layout);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    ll.addView(btnUpdate, lp);
                    MdlDates dts = MdlDates.load();
                    if (dts != null) {
                        calendar.init(dStart, dEnd) //
                                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                                .withSelectedDates(dts.getFreeDates());
                    }
                } else {

                }
            }
        }
    }

    private void sendDatesFree() {
        btnUpdate.setEnabled(false);
        List<Date> freeDates = calendar.getSelectedDates();
        MdlDates dts = new MdlDates(config.getParkId(), config.getName(), freeDates);
        try {
            dts.save();
        } catch (IOException e) {
            Toast.makeText(this, " Error saving the data " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //send out to server.
        if (!IOUtils.uploadGson(dts.getGson())) {
            Toast.makeText(this, " Error send data to the server. Try again later. ", Toast.LENGTH_LONG).show();
        }
        btnUpdate.setEnabled(true);

    }


    // Initiating Menu XML file (menu.xml)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                 dialogs.showMessage("Free Park\nCopyright Frank Cusmano\nfrankcusmano@hotmail.com");
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
