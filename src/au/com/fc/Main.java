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
import android.widget.Toast;
import au.com.fc.dialogs.Dialogs;
import au.com.fc.dialogs.DlgConfig;
import au.com.fc.dialogs.IDialog;
import au.com.fc.models.MdlConfig;
import au.com.fc.utils.Defines;
import au.com.fc.utils.IOUtils;
import com.squareup.timessquare.CalendarPickerView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class Main extends Activity {
    private Dialogs dialogs;
    private CalendarPickerView calendar;
    private Button btnUpdate;
    private TextView label;
    private MdlConfig config;
    private Date dStart;
    private Date dEnd;
    private IOUtils ioUtils;
    private Date lastDate;

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
                LinearLayout ll = (LinearLayout) findViewById(R.id.layout);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ll.addView(btnUpdate, lp);
                final String name = config.getName();
                final String parkId = config.getParkId();
                ioUtils = new IOUtils(this, name, parkId, calendar);
                if (config.isOwner()) {
                    calendar.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
                        @Override
                        public boolean onCellClicked(Date date) {
                            if (ioUtils.isReserved(date)) {
                                Toast.makeText(dialogs.getContext(), getString(R.string.reserved_by) + ioUtils.getReservedName(date),
                                        Toast.LENGTH_LONG).show();
                                return true;
                            } else {

                            }
                            return false;
                        }
                    });
                    doOwner();
                } else {
                    calendar.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
                        @Override
                        public boolean onCellClicked(Date date) {
                            if (ioUtils.isReservedByMe(date)) {
                                Toast.makeText(dialogs.getContext(), getString(R.string.bay_reserved) + ioUtils.getReservedBay(date),
                                        Toast.LENGTH_SHORT).show();
                                if (date.equals(lastDate)) {
                                    dialogs.showYesNo(new IDialog() {
                                        @Override
                                        public void aSelection(String sel) {

                                        }

                                        @Override
                                        public void aIndex(int sel) {

                                        }

                                        @Override
                                        public void aYes() {
                                            ioUtils.release(lastDate);
                                            updateReservedDays();
                                            lastDate=null;
                                        }

                                        @Override
                                        public void aNo() {
                                          lastDate=null;
                                        }
                                    }, "Do you want to release this date?");
                                }
                                lastDate = date;
                                return true;
                            }
                            lastDate = null;
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
        label.setText("Click twice to release bay");
        btnUpdate.setText(getString(R.string.refresh_dates));
        btnUpdate.setEnabled(false);
        if (!ioUtils.loadUnreserved()) {
            dialogs.showMessage(getString(R.string.try_again), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    System.exit(1);
                }
            });

        }
        btnUpdate.setEnabled(true);
        updateReservedDays();
    }

    private void doOwner() {

        btnUpdate.setText(getString(R.string.update));
        label.setText(getString(R.string.getting_pls_wait));
        //load initial dates
        btnUpdate.setEnabled(false);
        if (!ioUtils.loadDatesFree()) {
            dialogs.showMessage(getString(R.string.try_again), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    System.exit(1);
                }
            });

        }
        btnUpdate.setEnabled(true);
        label.setText(getString(R.string.select_free));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpdate.setEnabled(false);
                ioUtils.sendDatesFree();
                updateFreeDays();
                btnUpdate.setEnabled(true);
            }
        });
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
                dialogs.showMessage("Free Park\nCopyright Frank Cusmano\nfrankcusmano@hotmail.com");
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
                }, "Reset the configuration, ARE YOU SURE?");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateFreeDays() {
        calendar.init(dStart, dEnd) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                .withSelectedDates(ioUtils.getFreeDates());
        calendar.highlightDates(ioUtils.getUnFreeDates());
    }
    private void updateReservedDays() {
        calendar.init(dStart, dEnd) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                .withSelectedDates(ioUtils.getUnreservedDates());
        calendar.highlightDates(ioUtils.getReservedbyMe());
    }

}
