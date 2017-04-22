package apps.aouexchange.StudentActivity.adapters;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.List;

import apps.aouexchange.R;

import apps.aouexchange.StudentActivity.admin.NotificationDetailsFragment;
import apps.aouexchange.StudentActivity.admin.notificationlist;

import apps.aouexchange.StudentActivity.models.NotificationMessages;

/**
 * Created by mac on 3/14/17.
 */

public class NotifiAdapter extends RecyclerView.Adapter<NotifiAdapter.ViewHolder> {

    private Context mContext;
    private List<NotificationMessages> mNotificationMessages;
    private String mCurrentNotifytitle;
    private String mCurrentNotifyrId;

    notificationlist notificationlist1;

    public NotifiAdapter(Context mContext, List<NotificationMessages> mNotificationMessages,notificationlist notificationlist1) {
        this.mContext = mContext;
        this.mNotificationMessages = mNotificationMessages;
        this.notificationlist1=notificationlist1;
    }


    @Override
    public NotifiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new NotifiAdapter.ViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_item, parent, false));

    }

    @Override
    public void onBindViewHolder(NotifiAdapter.ViewHolder holder, int position) {

        NotificationMessages noticationMessage=mNotificationMessages.get(position);


        holder.getmMessageTitle().setText(noticationMessage.getTitle());

        holder.getmUserDisplayName().setText(noticationMessage.getUserName());
        holder.getmNotifyId().setText(noticationMessage.getId());

    }

    @Override
    public int getItemCount() {
        return mNotificationMessages.size();
    }


    public void setInfo(String notifyId, String title) {
        mCurrentNotifyrId = notifyId;
        mCurrentNotifytitle = title;

    }

    public void refill(NotificationMessages notifiM) {
        mNotificationMessages.add(notifiM);
        notifyDataSetChanged();
    }

    public void changeNot(int index, NotificationMessages notifiM) {
        mNotificationMessages.set(index,notifiM);
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
            /*

            NotificationMessages notificationMessages = mNotificationMessages.get(getLayoutPosition());


            Intent notifyIntent = new Intent(mContextViewHolder, NotificationDetails.class);
           notifyIntent.putExtra("notifyId", notificationMessages.getId());


            mContextViewHolder.startActivity(notifyIntent);
            */
            NotificationMessages notificationMessages = mNotificationMessages.get(getLayoutPosition());



           // fragment = new NotificationDetailsFragment();
            android.support.v4.app.Fragment fragment=new NotificationDetailsFragment();
            Bundle args = new Bundle();
            args.putString("notifyId", notificationMessages.getId());
            fragment.setArguments(args);
            android.support.v4.app.FragmentTransaction transaction = notificationlist1.getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.notilist, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();


        }
    }

}
