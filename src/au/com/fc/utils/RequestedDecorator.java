package au.com.fc.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarCellView;

import java.util.Date;
import java.util.List;

/**
 * @author Frank Cusmano
 */
public class RequestedDecorator implements CalendarCellDecorator {


    private IOUtils ioUtils;

    public RequestedDecorator(IOUtils ioUtils) {
        this.ioUtils = ioUtils;
    }

    @Override
    public void decorate(CalendarCellView cellView, Date date) {
        String parkId =ioUtils.getMyParkId(date);
        if (parkId != null) {
            SpannableString string = new SpannableString( date.getDate() +"\n" + parkId);
            string.setSpan(new RelativeSizeSpan(0.75f), 0, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            cellView.setText(string);
        }

    }
}
