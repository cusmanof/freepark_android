package au.com.fc.dialogs;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import au.com.fc.Main;
import au.com.fc.models.MdlConfig;

import java.io.IOException;

/**
 * @author Frank Cusmano
 */
public class DlgConfig implements IDialog {
    private final Dialogs dialogs;
    MdlConfig mdlConfig = new MdlConfig();
    private String cpName;
    private String usName;

    public DlgConfig(Context context) {
        dialogs = new Dialogs(context);
    }

    @Override
    public void aSelection(String sel) {

    }

    @Override
    public void aIndex(int sel) {

    }

    @Override
    public void aYes() {
        dialogs.getInput(new IDialog() {

            public void aSelection(String sel) {
                cpName = sel;
                if (cpName == null || cpName.length() < 1) {
                    cpName = "Unknown";
                }
            }

            public void aIndex(int sel) {
            }

            public void aYes() {
                mdlConfig.setParkId(cpName);
                doUserName();
            }

            public void aNo() {
                //ignore
            }
        }, "Enter your Car park ID", cpName);
    }

    @Override
    public void aNo() {
        doUserName();
    }


    private void doUserName() {
        dialogs.getInput(new IDialog() {

            public void aSelection(String sel) {
                usName = sel;
                if (usName == null || usName.length() < 1) {
                    usName = "Unknown";
                }
            }

            public void aIndex(int sel) {
            }

            public void aYes() {
                mdlConfig.setName(usName);
                try {
                    mdlConfig.save();
                    Intent intent = new Intent(dialogs.getContext(), Main.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    dialogs.getContext().startActivity(intent);
                } catch (IOException e) {
                    Toast.makeText(dialogs.getContext(), "Error saving the data " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }

            public void aNo() {
                //ignore
            }
        }, "Enter your name", usName);
    }
}
