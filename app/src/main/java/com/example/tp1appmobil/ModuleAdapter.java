package com.example.tp1appmobil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private List<Module> moduleList;

    public ModuleAdapter(List<Module> moduleList) {
        this.moduleList = moduleList;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_module, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = moduleList.get(position);

        holder.moduleName.setText(module.getName());

        // Set initial values
        holder.tdScore.setText(String.valueOf(module.getTdScore()));
        holder.tpScore.setText(String.valueOf(module.getTpScore()));
        holder.examScore.setText(String.valueOf(module.getExamScore()));

        // Validate TD score input
        holder.tdScore.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String tdText = holder.tdScore.getText().toString();
                if (!tdText.isEmpty()) {
                    double tdScore = Double.parseDouble(tdText);
                    if (tdScore < 0 || tdScore > 20) {
                        showError(holder.itemView.getContext(), "TD score must be between 0 and 20.");
                        holder.tdScore.setText("0"); // Reset to default value
                        module.setTdScore(0);
                    } else {
                        module.setTdScore(tdScore);
                    }
                }
            }
        });

        // Validate TP score input
        holder.tpScore.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String tpText = holder.tpScore.getText().toString();
                if (!tpText.isEmpty()) {
                    double tpScore = Double.parseDouble(tpText);
                    if (tpScore < 0 || tpScore > 20) {
                        showError(holder.itemView.getContext(), "TP score must be between 0 and 20.");
                        holder.tpScore.setText("0"); // Reset to default value
                        module.setTpScore(0);
                    } else {
                        module.setTpScore(tpScore);
                    }
                }
            }
        });

        // Validate Exam score input
        holder.examScore.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String examText = holder.examScore.getText().toString();
                if (!examText.isEmpty()) {
                    double examScore = Double.parseDouble(examText);
                    if (examScore < 0 || examScore > 20) {
                        showError(holder.itemView.getContext(), "Exam score must be between 0 and 20.");
                        holder.examScore.setText("0"); // Reset to default value
                        module.setExamScore(0);
                    } else {
                        module.setExamScore(examScore);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    // Helper method to show error messages
    private void showError(android.content.Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        EditText tdScore, tpScore, examScore;
        TextView moduleName;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.moduleName);
            tdScore = itemView.findViewById(R.id.tdScore);
            tpScore = itemView.findViewById(R.id.tpScore);
            examScore = itemView.findViewById(R.id.examScore);
        }
    }
}