package prototype.pa55nyaps.gui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.pa55nyaps.pa55nyaps.R;


/**
 */
public class CreateDatabaseFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_create_database, null))
                .setMessage(R.string.new_password_prompt)
                .setTitle(R.string.new_password_prompt_title)
                .setPositiveButton(R.string.continue_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // create the password settings database
                    }
                })
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        CreateDatabaseFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
