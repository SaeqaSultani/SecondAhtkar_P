package com.example.women.adaptrs;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.women.R;
import com.example.women.networking_getdata.ClassGetReport;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import java.util.List;


public class HoardRecyclerViewAdapter extends RecyclerView.Adapter<HoardRecyclerViewAdapter.ViewHolder> {

    private SparseBooleanArray expandState = new SparseBooleanArray();
    private List<ClassGetReport> classGetReports;
    private Context context;

    public HoardRecyclerViewAdapter(List<ClassGetReport> classGetReports, Context context) {
        this.classGetReports = classGetReports;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hoard,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {

        holder.title.setText(classGetReports.get(position).getName());
        holder.zone.setText(classGetReports.get(position).getAddress());
        holder.province.setText(classGetReports.get(position).getProvince());
        holder.county.setText(classGetReports.get(position).getDistrict());
        holder.address.setText(classGetReports.get(position).getEhtakarPositionName());
        holder.name.setText(classGetReports.get(position).getName());
        holder.cost.setText(classGetReports.get(position).getCost());
        Glide.with(context).load("http://asoodaowar.com/ehtakar/storage/app/public/image/"+classGetReports.get(position).getImage()).into(holder.image_View);
//        Glide.with(context).load("http://www.asoodaowar.com/ehtakar/storage/app/public/image/"+classGetReports.get(position).getImage()).into(holder.image_View);
        holder.answer.setText(classGetReports.get(position).getAnswer());
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {

            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.relativeLayout, 0f, 180f).start();
                expandState.put(position, true);

            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.relativeLayout, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.relativeLayout.setRotation(expandState.get(position) ? 180f : 0f);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classGetReports.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,address,answer,province,county,zone,name,cost;
        ImageView image_View;
        ExpandableLinearLayout expandableLayout;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_list_TextView_title);
            address = itemView.findViewById(R.id.TextView_address);
            answer = itemView.findViewById(R.id.TextView_answer);
            province = itemView.findViewById(R.id.tv_province);
            county = itemView.findViewById(R.id.tv_county);
            zone = itemView.findViewById(R.id.tv_place);
            name = itemView.findViewById(R.id.tv_name);
            cost = itemView.findViewById(R.id.tv_cost);
            image_View = itemView.findViewById(R.id.item_list_ImageView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            relativeLayout = itemView.findViewById(R.id.button);
        }
    }

    //methods that are dependent on expandableLayout
    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}