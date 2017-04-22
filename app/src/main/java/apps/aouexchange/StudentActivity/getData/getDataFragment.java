package apps.aouexchange.StudentActivity.getData;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.aouexchange.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class getDataFragment extends Fragment {

    public getDataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViewsInLayout();
        }

        return inflater.inflate(R.layout.fragment_get_data, container, false);
    }
}
