package prototype.pa55nyaps.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.pa55nyaps.pa55nyaps.R;

import prototype.pa55nyaps.gui.model.PasswordDatabaseFile;

/**
 */
public class CreateDatabaseFragment extends DialogFragment {

    public interface DialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    DialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View promptsView = inflater.inflate(R.layout.fragment_create_database, null);
        final TextInputEditText userInput = (TextInputEditText) promptsView.findViewById(R.id.password);

        builder.setView(promptsView)
                .setMessage(R.string.new_password_prompt)
                .setTitle(R.string.new_password_prompt_title)
                .setPositiveButton(R.string.continue_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String filename = "db" + (System.currentTimeMillis()/1000L) + ".pa55";
                        String password = userInput.getText().toString();
                        // TODO: do we need to check if password is not empty?
                        // create the password settings database
                        PasswordDatabaseFile database_file = new PasswordDatabaseFile(
                                filename,
                                password,
                                getContext()
                        );
                        database_file.save();
                        mListener.onDialogPositiveClick(CreateDatabaseFragment.this);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();

        // Verify that the host activity implements the callback interface
        try {
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogListener");
        }
    }
}
