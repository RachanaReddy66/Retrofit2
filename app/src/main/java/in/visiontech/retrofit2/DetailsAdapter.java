package in.visiontech.retrofit2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.viewHolder> {
    Context context;
    ArrayList<fpsDetails> details;
    public DetailsAdapter(Context context, ArrayList<fpsDetails> details){
        this.context=context;
        this.details=details;
    }
    @NonNull
    @Override
    public DetailsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,null);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DetailsAdapter.viewHolder holder, int position) {
        fpsDetails dealer=details.get(position);
        holder.uid.setText(dealer.dealer_uid);
        holder.name.setText(dealer.dealer_name);
        if(dealer.dealer_type.equals("D")) {
            holder.type.setText("Dealer");
        }
        else if(dealer.dealer_type.equals("N1")){
            holder.type.setText("Nominee 1");
        }
        else if(dealer.dealer_type.equals("N2")){
            holder.type.setText("Nominee 2");
        }
        System.out.println("Dealer name:"+dealer.dealer_name);
        System.out.println("Dealer uid:"+dealer.dealer_uid);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }
    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView uid,name,type;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            uid=itemView.findViewById(R.id.uid);
            name=itemView.findViewById(R.id.name);
            type=itemView.findViewById(R.id.type);

        }
    }

}
