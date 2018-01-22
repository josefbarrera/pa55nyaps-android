package prototype.pa55nyaps.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.pa55nyaps.pa55nyaps.R;

import prototype.pa55nyaps.gui.model.PasswordDatabaseFile;

/**
 * Delete database dialogue
 *
 * It asks users to confirm whether to delete the file or not.
 */
public class DeleteDatabaseFragment extends DialogFragment {

    public interface DeleteDatabaseFragmentListener {
        void onDeleteDatabaseFragmentPositiveClick(DialogFragment dialog, int position);
    }

    DeleteDatabaseFragmentListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DeleteDatabaseFragmentListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DeleteDatabaseFragmentListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Resources res = getResources();
        String fileName = getArguments().getString("fileName");
        builder.setMessage(String.format(res.getString(R.string.confirm_delete_database), fileName))
                .setTitle(R.string.confirm_delete_database_title)
                .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Delete database", "delete database");
                        String fileName = getArguments().getString("fileName");
                        int position = getArguments().getInt("position");
                        PasswordDatabaseFile file = new PasswordDatabaseFile(fileName, "", getActivity());
                        file.delete();
                        mListener.onDeleteDatabaseFragmentPositiveClick(DeleteDatabaseFragment.this, position);
                    }
                })
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Cancel", "Not deleting anything");
                    }
                });
        return builder.create();
    }
}
