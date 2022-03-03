package at.ac.univie.t0306.expensetracker.ui.accounts;

import android.view.View;

/**
 * defines an interface to handle the event when more option button is clicked
 *
 * @param <T>
 */
public interface OnMoreOptionsButtonListener<T> {
    public void clicked(View view, T data, int position);
}
