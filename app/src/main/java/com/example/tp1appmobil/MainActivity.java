package com.example.tp1appmobil;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tp1appmobil.databinding.ActivityMainBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private ModuleAdapter moduleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize RecyclerView
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Fetch JSON data
        fetchModulesData();
        // Set up the calculate button
        binding.calculateButton.setOnClickListener(v -> {
            double totalWeightedAverage = 0;
            int totalCoefficients = 0;

            for (Module module : moduleAdapter.getModuleList()) {
                double moduleAverage = module.calculateModuleAverage();
                int coefficient = module.getCoefficient();

                totalWeightedAverage += moduleAverage * coefficient;
                totalCoefficients += coefficient;
            }

            double weightedAverage = totalWeightedAverage / totalCoefficients;

            String resultText = String.format("Did you pass?1: %.2f", weightedAverage);
            if (weightedAverage >= 10) {
                resultText += " (Pass)";
            } else {
                resultText += " (Fail)";
            }

            binding.resultTextView.setText(resultText);
        });
    }

    private void fetchModulesData() {
        String jsonUrl = "https://num.univ-biskra.dz/psp/formations/get_modules_json?sem=1&spec=184";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, jsonUrl, null,
                response -> {
                    Log.d("JSON Response", response.toString());

                    parseJsonData(response);
                },
                error -> {
                    Log.e("JSON Error", error.toString());
                }
        );

        queue.add(jsonArrayRequest);
    }

    private void parseJsonData(JSONArray jsonArray) {
        List<Module> moduleList = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject moduleObject = jsonArray.getJSONObject(i);
                String name = moduleObject.getString("Nom_module");
                int coefficient = moduleObject.getInt("Coefficient");

                Log.d("Parsed Data", "Module: " + name + ", Coefficient: " + coefficient);

                Module module = new Module(name, coefficient);
                moduleList.add(module);
            }

            moduleAdapter = new ModuleAdapter(moduleList);
            recyclerView.setAdapter(moduleAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}