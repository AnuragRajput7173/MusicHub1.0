package developer.anurag.musichub.temp_data_adder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import developer.anurag.musichub.MainActivity;
import developer.anurag.musichub.R;
import developer.anurag.musichub.databinding.FragmentTempDataAdderBinding;


public class TempDataAdderFragment extends Fragment {
    private int trackDefaultID;

    private FragmentTempDataAdderBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding=FragmentTempDataAdderBinding.inflate(inflater,container,false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.binding.saveBtn.setOnClickListener(listener);
        this.binding.showButton.setOnClickListener(showBtnListener);
    }

    private final View.OnClickListener listener= v -> {
        DatabaseReference database= FirebaseDatabase.getInstance().getReference("track_data");
        String title=this.binding.trackTitleET.getText().toString().trim();
        String singer=this.binding.singerNameET.getText().toString().trim();
        String id=this.binding.trackIdET.getText().toString().trim();
        this.trackDefaultID=Integer.parseInt(id);
        TempDataAdder track=new TempDataAdder(this.trackDefaultID,title,singer,"null","null");
        Task<Void> task=database.push().setValue(track);
        task.addOnSuccessListener(unused -> {
            Toast.makeText(getContext(), "Data added successfully.", Toast.LENGTH_SHORT).show();
            this.trackDefaultID++;
            this.binding.trackIdET.setText(String.valueOf(this.trackDefaultID));
        });


    };

    private final View.OnClickListener showBtnListener= v -> {
        DatabaseReference database=FirebaseDatabase.getInstance().getReference("track_data");
        ArrayList<String> titles=new ArrayList<>();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titles.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    int counter=0;
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        if (counter == 3) {
                            Log.i("title","title is:- "+ Objects.requireNonNull(data.getValue()));
                            titles.add(Objects.requireNonNull(data.getValue()).toString());
                        }
                        counter++;
                    }
                }
                binding.lvItemsTv.setText(String.valueOf(titles.size()));
                ArrayAdapter<String> adapter=new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,titles);
                binding.showTracksLV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    };
}