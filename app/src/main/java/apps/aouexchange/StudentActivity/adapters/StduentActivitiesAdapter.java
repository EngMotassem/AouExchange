package apps.aouexchange.StudentActivity.adapters;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.aouexchange.R;
import apps.aouexchange.StudentActivity.admin.ActivityDetailsFragment;
import apps.aouexchange.StudentActivity.admin.StudentActivitiesListFragment;
import apps.aouexchange.StudentActivity.getData.getDataFragment;
import apps.aouexchange.StudentActivity.admin.notificationlist;
import apps.aouexchange.StudentActivity.models.NotificationMessages;
import apps.aouexchange.StudentActivity.models.StudentActivities;

/**
 * Created by mac on 3/14/17.
 */

public class StduentActivitiesAdapter extends RecyclerView.Adapter<StduentActivitiesAdapter.ViewHolder> {

    private Context mContext;
    private List<StudentActivities> mStudentActivities;
    private StudentActivities studentActivities;


    StudentActivitiesListFragment  getDataFragment1=new StudentActivitiesListFragment();

    public StduentActivitiesAdapter(Context mContext, List<StudentActivities> mStudentActivities, StudentActivitiesListFragment getDataFragment1) {
        this.mContext = mContext;
        this.mStudentActivities = mStudentActivities;
        this.getDataFragment1 = getDataFragment1;
    }

    @Override
    public StduentActivitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return
                new
                        StduentActivitiesAdapter.ViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item, parent, false));

    }

    @Override
    public void onBindViewHolder(StduentActivitiesAdapter.ViewHolder holder, int position) {

        StudentActivities noticationMessage=mStudentActivities.get(position);


        holder.getmMessageTitle().setText(noticationMessage.getTitle());

        holder.getmUserDisplayName().setText(noticationMessage.getUserName());
        holder.getmNotifyId().setText(noticationMessage.getId());

    }

    @Override
    public int getItemCount() {
        return mStudentActivities.size();
    }




    public void refill(StudentActivities notifiM) {
        mStudentActivities.add(notifiM);
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mUserDisplayName;
        private TextView mMessageTitle;
        private TextView mNotifyId;

        public TextView getmNotifyId() {
            return mNotifyId;
        }

        public void setmNotifyId(TextView mNotifyId) {
            this.mNotifyId = mNotifyId;
        }

        private Context mContextViewHolder;

        public TextView getmUserDisplayName() {

            return mUserDisplayName;
        }

        public void setmUserDisplayName(TextView mUserDisplayName) {
            this.mUserDisplayName = mUserDisplayName;
        }

        public TextView getmMessageTitle() {

            return mMessageTitle;
        }

        public void setmMessageTitle(TextView mMessageTitle) {

            this.mMessageTitle = mMessageTitle;
        }

        public ViewHolder(Context mContext, View itemView) {
            super(itemView);

            mUserDisplayName= (TextView) itemView.findViewById(R.id.display_name);
            mMessageTitle= (TextView) itemView.findViewById(R.id.meesage_title);
            mNotifyId=(TextView) itemView.findViewById(R.id.notifyIdText);
            mContextViewHolder = mContext;

            itemView.setOnClickListener(this);




        }

        @Override
        public void onClick(View view) {


            studentActivities  = mStudentActivities.get(getLayoutPosition());

            Fragment fragment=new ActivityDetailsFragment();
            Bundle args = new Bundle();

            args.putString("notifyId", studentActivities.getId());
            fragment.setArguments(args);
            android.support.v4.app.FragmentTransaction transaction;
            transaction= getDataFragment1.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.actlist, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();



        }
    }

}
