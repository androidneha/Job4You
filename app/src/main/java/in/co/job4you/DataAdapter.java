package in.co.job4you;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {
    private ArrayList<JobGetterSetter> mArrayList;
    private ArrayList<JobGetterSetter> mFilteredList;
    private  Context context;
    public DataAdapter(ArrayList<JobGetterSetter> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.job_title.setText(mFilteredList.get(i).getJob_title());
        viewHolder.company_name.setText(mFilteredList.get(i).getCompanyName());
        viewHolder.exp.setText(mFilteredList.get(i).getExp() + "");
        viewHolder.location.setText(mFilteredList.get(i).getLocation());
        viewHolder.skills.setText(mFilteredList.get(i).getRequired_qualification()+" -  "+mFilteredList.get(i).getSort_description());
        viewHolder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context,Webvieww.class);
                it.putExtra("url","http://job4you.co.in/guest/"+mFilteredList.get(i).getId());
                context.startActivity(it);
            }
        });
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent =  new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Jobs From Job4You");
                String shareMessage=mFilteredList.get(i).getJob_title() + "( "+mFilteredList.get(i).getExp() + " Yrs" +" )"+"\n"+"http://job4you.co.in/guest/"+mFilteredList.get(i).getId();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareMessage);
                context.startActivity(Intent.createChooser(shareIntent,"Sharing via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<JobGetterSetter> filteredList = new ArrayList<>();

                    for (JobGetterSetter androidVersion : mArrayList) {

                        if (androidVersion.getCompanyName().toLowerCase().contains(charString) || androidVersion.getCompanyName().toLowerCase().contains(charString) || androidVersion.getCompanyName().toLowerCase().contains(charString) ||
                                androidVersion.getExp().toLowerCase().contains(charString) || androidVersion.getExp().toLowerCase().contains(charString) || androidVersion.getExp().toLowerCase().contains(charString) ||
                                androidVersion.getSort_description().toLowerCase().contains(charString) || androidVersion.getSort_description().toLowerCase().contains(charString) || androidVersion.getSort_description().toLowerCase().contains(charString) ||
                                androidVersion.getJob_title().toLowerCase().contains(charString) || androidVersion.getJob_title().toLowerCase().contains(charString) || androidVersion.getJob_title().toLowerCase().contains(charString) ||
                                androidVersion.getLocation().toLowerCase().contains(charString) || androidVersion.getLocation().toLowerCase().contains(charString) || androidVersion.getLocation().toLowerCase().contains(charString) ||
                                androidVersion.getRequired_qualification().toLowerCase().contains(charString) || androidVersion.getRequired_qualification().toLowerCase().contains(charString) || androidVersion.getRequired_qualification().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<JobGetterSetter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView job_title,company_name,exp,location,skills;Button apply;ImageView share;
        public ViewHolder(View view) {
            super(view);
            context = view.getContext();
            apply=(Button)view.findViewById(R.id.apply);
            job_title = (TextView)view.findViewById(R.id.job_title);
            company_name = (TextView)view.findViewById(R.id.company_name);
            exp = (TextView)view.findViewById(R.id.exp);
            location = (TextView)view.findViewById(R.id.location);
            skills = (TextView)view.findViewById(R.id.skills);
            share=(ImageView)view.findViewById(R.id.share);
        }
    }

}