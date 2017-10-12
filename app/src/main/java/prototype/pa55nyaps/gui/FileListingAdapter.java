package prototype.pa55nyaps.gui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pa55nyaps.pa55nyaps.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jose on 10/4/17.
 */

public class FileListingAdapter extends RecyclerView.Adapter<FileListingAdapter.ViewHolder> {

    private static final int FOOTER_VIEW = 1;
    private ArrayList<HashMap<String, String>> mFilesList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder (LinearLayout v) {
            super(v);
        }
    }

    public static class NormalViewHolder extends FileListingAdapter.ViewHolder {
        public LinearLayout fileEntry;
        public TextView fileName;
        public TextView lastModified;
        public ImageButton deleteEntry;

        public NormalViewHolder(LinearLayout v) {
            super(v);
            fileEntry = (LinearLayout) v.findViewById(R.id.fileEntry);
            fileName = (TextView) fileEntry.findViewById(R.id.fileName);
            lastModified = (TextView) fileEntry.findViewById(R.id.lastModified);
            deleteEntry = (ImageButton) v.findViewById(R.id.delete_file);

            fileEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("NormalViewHolder", "Clicked on a file line item, go to add entries screen");
                }
            });

            deleteEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("DeleteEntry", "Delete entry clicked");
                }
            });
        }
    }

    public static class FooterViewHolder extends FileListingAdapter.ViewHolder {
        public FooterViewHolder(LinearLayout v) {
            super(v);
        }
    }

    public FileListingAdapter(ArrayList<HashMap<String, String>> myFilesList) {
        mFilesList = myFilesList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FileListingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view of the appropriate type
        LinearLayout v;
        ViewHolder vh;

        if (viewType == FOOTER_VIEW) {
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.file_listing_footer_layout, parent, false);
            vh = new FooterViewHolder(v);
        } else {
            v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.file_list_layout, parent, false);
            // set the view's size, margins, padding and layout parameters

            vh = new NormalViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            if (holder instanceof NormalViewHolder) {
                // Get element from dataset at this position
                // Replace the contents of the view with that element
                HashMap<String, String> fileDetails = mFilesList.get(position);
                NormalViewHolder nvHolder = (NormalViewHolder) holder;
                nvHolder.fileName.setText(fileDetails.get("fileName"));
                nvHolder.lastModified.setText(fileDetails.get("lastModified"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mFilesList.size()) {
            return FOOTER_VIEW;
        }
        return super.getItemViewType(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mFilesList == null) {
            return 0;
        }

        if (mFilesList.size() == 0) {
            return 1;
        }

        return mFilesList.size() + 1;
    }
}
