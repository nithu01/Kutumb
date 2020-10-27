package com.app.kutumb.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kutumb.Activity.AddressActivity;
import com.app.kutumb.Activity.MainActivity;
import com.app.kutumb.Activity.SplashActivity;
import com.app.kutumb.Config.Constant;
import com.app.kutumb.Database.Local.CartDatabase;
import com.app.kutumb.Database.ModelDB.AddressTable;
import com.app.kutumb.R;

import java.util.List;

import static com.app.kutumb.Activity.MainActivity.txtDeliveryName;
import static com.app.kutumb.Activity.MainActivity.txtLocation;



public class AddressTableAdapter extends RecyclerView.Adapter<AddressTableAdapter.AddresstableViewHolder> {

    private Context mCtx;
    private List<AddressTable> addresstable;
    private AddressTable task;

    public AddressTableAdapter(Context mCtx, List<AddressTable> taskList, AddressTable task) {
        this.mCtx = mCtx;
        this.addresstable = taskList;
        this.task=task;
    }

    @NonNull
    @Override
    public AddresstableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.address_list_item, parent, false);
        return new AddresstableViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddresstableViewHolder holder, int position) {
        AddressTable t = addresstable.get(position);
        holder.txtName.setText(t.getFirstname()+" "+t.getLastname());
        holder.txtPincode.setText(t.getPincode());
        holder.txtAddressLineOne.setText(t.getAddress_line_one());
        holder.txtAddressLineTwo.setText(t.getAddress_line_two());
        holder.txtCity.setText(t.getCity());
        holder.txtState.setText(t.getState());
        holder.txtStatus.setText(t.getAddressname());
        holder.cardView.setOnClickListener(v -> {

        });
        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
            builder.setTitle("Are you sure?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                AddressTable task = addresstable.get(position);
                if (position==0) {
                    MainActivity.txtLocation.setText("Search delivery Location");
                }
                deleteTask(task);
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> {

            });

            AlertDialog ad = builder.create();
            ad.show();
        });
        holder.btnSelect.setOnClickListener(v -> {
            txtDeliveryName.setText("Delivery to : "+ t.address_name);
            txtDeliveryName.setAllCaps(true);
            txtLocation.setText("Delivery to : "+ t.address_name);
            ((Activity)mCtx).finish();
        });

        /*if (t.isFinished())
            holder.txtStatus.setText("Completed");
        else
            holder.txtStatus.setText("Not Completed");*/
    }
    private void deleteTask(final AddressTable task) {
        @SuppressLint("StaticFieldLeak")
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                CartDatabase.getInstance(mCtx)
                        .addressTableDao()
                        .deleteAddressItem(task);
                return null;
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
              //  AddressActivity.getAddressTableData();
                Toast.makeText(mCtx, "Deleted", Toast.LENGTH_LONG).show();
            }
        }
        DeleteTask dt = new DeleteTask();
        dt.execute();

    }


    @Override
    public int getItemCount() {
        return addresstable.size();
    }

    class AddresstableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtName, txtPincode, txtAddressLineOne, txtAddressLineTwo,txtCity,txtState,txtStatus;
        Button btnSelect,btnDelete;
        CardView cardView;

        AddresstableViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txt_address_name);
            txtPincode = itemView.findViewById(R.id.txt_pincode);
            txtAddressLineOne = itemView.findViewById(R.id.txt_address_line_one);
            txtAddressLineTwo = itemView.findViewById(R.id.txt_address_line_two);
            txtCity = itemView.findViewById(R.id.txt_city_address);
          //  txtStatus=itemView.findViewById(R.id.txt_status);
            txtState = itemView.findViewById(R.id.txt_state_address);
            btnSelect=itemView.findViewById(R.id.btn_select_deliver_address);
            btnDelete=itemView.findViewById(R.id.btn_delete_deliver_address);
            cardView=itemView.findViewById(R.id.cardview_booking);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AddressTable task = addresstable.get(getAdapterPosition());
            Intent intent = new Intent(mCtx,MainActivity.class);
            intent.putExtra("task", task);
            SplashActivity.savePreferences(Constant.ADDRESS, String.valueOf(task));
            mCtx.startActivity(intent);
        }
    }
}
