package au.com.fc.dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.EditText;

import java.util.List;

/**
 * @author Frank Cusmano
 */
public class Dialogs {


    private final Context context;
    private static final char[] ILLEGAL_CHARACTERS = {' ', '%', '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':'};

    public Dialogs(Context parent) {
        this.context = parent;
    }

    public void showYesNo(final IDialog answer, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm...");

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                answer.aYes();
                dialog.cancel();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                answer.aNo();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void showList(final IDialog answer, String msg, final List<String> items) {
        final int[] selectedItem = {-1};
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        // Setting Dialog Title
        alertDialog.setTitle(msg);

        // Setting Dialog Message
        alertDialog.setSingleChoiceItems(items.toArray(new String[0]), -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
                selectedItem[0] = i;
            }
        });
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke YES event
                if (selectedItem[0] != -1) {
                    answer.aIndex(selectedItem[0]);
                }
                answer.aYes();
                dialog.cancel();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                answer.aNo();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void getInput(final IDialog answer, String msg, String val) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        // Setting Dialog Title
        alertDialog.setTitle("input");
        alertDialog.setMessage(msg);

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        if (val != null) {
            input.setText(val);
        }
        alertDialog.setView(input);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke YES event
                answer.aSelection(input.getText().toString());
                answer.aYes();
                dialog.cancel();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                answer.aNo();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    /**
     * message with dismiss listener
     *
     * @param msg             the message
     * @param dismissListener the listener.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showMessage(String msg, DialogInterface.OnDismissListener dismissListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setIcon(android.R.drawable.ic_dialog_info);

        // Setting Dialog Title
        alertDialog.setTitle("Information");

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        if (dismissListener != null) {
            alertDialog.setOnDismissListener(dismissListener);
        }
        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * show message.
     *
     * @param msg the message.
     */
    public void showMessage(String msg) {
        showMessage(msg, null);
    }

    public static boolean isFilenameValid(String filename) {
        for (char ILLEGAL_CHARACTER : ILLEGAL_CHARACTERS) {
            if (filename.indexOf(ILLEGAL_CHARACTER) != -1) {
                return false;
            }
        }
        return true;
    }

    public Context getContext() {
        return context;
    }
}
