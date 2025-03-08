package com.example.tp1appmobil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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

        holder.tdScore.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String tdText = holder.tdScore.getText().toString();
                if (!tdText.isEmpty()) {
                    module.setTdScore(Double.parseDouble(tdText));
                }
            }
        });

        holder.tpScore.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String tpText = holder.tpScore.getText().toString();
                if (!tpText.isEmpty()) {
                    module.setTpScore(Double.parseDouble(tpText));
                }
            }
        });

        holder.examScore.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String examText = holder.examScore.getText().toString();
                if (!examText.isEmpty()) {
                    module.setExamScore(Double.parseDouble(examText));
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