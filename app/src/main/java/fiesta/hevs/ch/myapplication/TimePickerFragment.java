package fiesta.hevs.ch.myapplication;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * This class is used to create the Time Picker
 * This is a fragment
 * @author Sylvain
 * @see NewTransportActivity
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TransportTimeInterface listener;

    /**
     * Constructor of the fragment
     * @param listener This is an interface used in NewTransportActivity
     *                 it is an instance of the class who call this Fragment.
     * @see TransportTimeInterface
     * @see NewTransportActivity
     */
    public TimePickerFragment(TransportTimeInterface listener){
        this.listener = listener;
    }

    /**
     * Create the dialog withe the time
     * @param savedInstanceState
     * @returnn The dialog to be diplayed
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * Method who get the time selected by the user
     * The time is return "by the interface"
     * @param view
     * @param hourOfDay hour selected
     * @param minute minute selected
     * @see TransportTimeInterface
     * @see NewTransportActivity
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        listener.setTime(hourOfDay, minute);
    }
}