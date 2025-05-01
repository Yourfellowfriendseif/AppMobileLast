package com.example.tp1appmobil;

import android.content.Intent;
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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // This is where the library button is configured
        binding.calculateButton.setOnClickListener(v -> calculateWeightedAverage());
        binding.libraryButton.setOnClickListener(v -> openLibraryActivity()); // <-- HERE
        startActivity(new Intent(MainActivity.this, LibraryActivity.class));
        fetchModulesData();
    }


    private void openLibraryActivity() {
        startActivity(new Intent(MainActivity.this, LibraryActivity.class));
    }

    private void calculateWeightedAverage() {
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
        resultText += weightedAverage >= 10 ? " (Pass)" : " (Fail)";
        binding.resultTextView.setText(resultText);
    }

    private void fetchModulesData() {
        String jsonUrl = "https://num.univ-biskra.dz/psp/formations/get_modules_json?sem=1&spec=184";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, jsonUrl, null,
                response -> parseJsonData(response),
                error -> Log.e("JSON Error", error.toString())
        );

        queue.add(jsonArrayRequest);
    }

    private void parseJsonData(JSONArray jsonArray) {
        List<Module> moduleList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject moduleObject = jsonArray.getJSONObject(i);
                Module module = new Module(
                        moduleObject.getString("Nom_module"),
                        moduleObject.getInt("Coefficient")
                );
                moduleList.add(module);
            }
            moduleAdapter = new ModuleAdapter(moduleList);
            recyclerView.setAdapter(moduleAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}