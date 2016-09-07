package fiesta.hevs.ch.myapplication;

/**
 * Interface to "communicate" between TimePickerFragment and the affected class
 * @author  Sylvain on 02.09.2016.
 * @see TimePickerFragment
 * @see NewTransportActivity
 * @see UpdateTransportActivity
 */
public interface TransportTimeInterface {

    void setTime(int hour, int minutes);
}
