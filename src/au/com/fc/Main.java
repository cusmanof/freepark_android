package au.com.fc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import au.com.fc.dialogs.Dialogs;
import au.com.fc.dialogs.DlgConfig;
import au.com.fc.dialogs.IDialog;
import au.com.fc.models.MdlConfig;
import au.com.fc.utils.Defines;
import au.com.fc.utils.IOUtils;
import au.com.fc.utils.RequestedDecorator;
import au.com.fc.utils.UsedDecorator;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;

import java.io.File;
import java.util.*;

public class Main extends Activity {
    private Dialogs dialogs;
    private CalendarPickerView calendar;
    private Button btnUpdate;
    private TextView label;
    private MdlConfig config;
    private Date dStart;
    private Date dEnd;
    private IOUtils ioUtils;


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
                btnUpdate = new Button(this);
                btnUpdate.setText(getString(R.string.refresh_dates));
                LinearLayout ll = (LinearLayout) findViewById(R.id.layout);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.addView(btnUpdate, lp);
                final String name = config.getName();
                final String parkId = config.getParkId();
                ioUtils = new IOUtils(name, parkId, calendar);
                if (config.isOwner()) {

                    calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(Date date) {
                            ioUtils.sendDatesFree();
                        }

                        @Override
                        public void onDateUnselected(Date date) {
                            ioUtils.sendDatesFree();
                        }
                    });
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnUpdate.setEnabled(false);
                            doOwner();
                            btnUpdate.setEnabled(true);
                        }
                    });
                    doOwner();
                } else {

                    calendar.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
                        @Override
                        public boolean onCellClicked(Date date) {
                            if (ioUtils.isReservedByMe(date)) {
                                ioUtils.release(date);
                            } else {
                                if (ioUtils.canReserve(date)) {
                                    ioUtils.reserveForMe(date);
                                } else {
                                    ioUtils.requestForMe(date);
                                }
                            }
                            updateReservedDays();
                            calendar.scrollToDate(date);
                            return true;
                        }
                    });
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnUpdate.setEnabled(false);
                            doUser();
                            btnUpdate.setEnabled(true);
                        }
                    });
                    doUser();
                }
            }
        }
    }

    private void doUser() {
        label.setText("User: " + ioUtils.getName());
        if (!ioUtils.loadAll()) {
            dialogs.showMessage(getString(R.string.try_again), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    System.exit(1);
                }
            });

        }
        updateReservedDays();
    }

    private void doOwner() {
        label.setText("Owner : " + ioUtils.getName() + " bay : " + ioUtils.getParkId());
        if (!ioUtils.loadDatesFree()) {
            dialogs.showMessage(getString(R.string.try_again), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    System.exit(1);
                }
            });
        }
        updateFreeDays();
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
                dialogs.showMessage(getString(R.string.me));
                return true;
            case R.id.reset:
                dialogs.showYesNo(new IDialog() {
                    @Override
                    public void aSelection(String sel) {

                    }

                    @Override
                    public void aIndex(int sel) {

                    }

                    @SuppressWarnings("ResultOfMethodCallIgnored")
                    @Override
                    public void aYes() {
                        File fil = new File(Defines.downDir, Defines.FP_CONF);
                        if (fil.exists()) {
                            fil.delete();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void aNo() {

                    }
                }, getString(R.string.reset_config));
                return true;
            case R.id.help:
                if (config != null) {
                    if (config.isOwner()) {
                        dialogs.showMessage(getString(R.string.help_owner));
                    } else {
                        dialogs.showMessage(getString(R.string.help_user));
                    }
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateFreeDays() {
        calendar.setDecorators(Arrays.<CalendarCellDecorator>asList(new UsedDecorator(ioUtils)));
        calendar.init(dStart, dEnd) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                .withSelectedDates(ioUtils.getFreeDates());
        calendar.highlightDates(ioUtils.getUnFreeDates());
    }

    private void updateReservedDays() {
        calendar.setDecorators(Arrays.<CalendarCellDecorator>asList(new RequestedDecorator(ioUtils)));
        calendar.init(dStart, dEnd) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                .withSelectedDates(ioUtils.getUnreservedDates());
        calendar.highlightDates(ioUtils.getReservedByMe());
    }

}
