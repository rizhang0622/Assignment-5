package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PrimeActivity extends AppCompatActivity {

    private Button btnFindPrimes, btnTerminateSearch;
    private Switch switchPacifier;
    private TextView tvCurrentNumber, tvLatestPrime;
    private volatile boolean isSearching = false;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Thread primeSearchThread;
    private int currentNumber = 3;
    private int latestPrime = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime);

        btnFindPrimes = findViewById(R.id.btnFindPrimes);
        btnTerminateSearch = findViewById(R.id.btnTerminateSearch);
        switchPacifier = findViewById(R.id.switchPacifier);
        tvCurrentNumber = findViewById(R.id.tvCurrentNumber);
        tvLatestPrime = findViewById(R.id.tvLatestPrime);

        // Initialization code...

        if (savedInstanceState != null) {
            switchPacifier.setChecked(savedInstanceState.getBoolean("PacifierState", false));
            currentNumber = savedInstanceState.getInt("CurrentNumber", 3);
            latestPrime = savedInstanceState.getInt("LatestPrime", 2);
            isSearching = savedInstanceState.getBoolean("IsSearching", false); // Restore the isSearching state
        }

        // Ensure UI reflects current state immediately upon recreation
        updateUI();

        // Conditionally start the search based on saved state
        if (isSearching) {
            startPrimeSearch(true); // Adjust to a method that checks thread state
        }

        btnFindPrimes.setOnClickListener(v -> startPrimeSearch(false));
        btnTerminateSearch.setOnClickListener(v -> terminateSearch());

    }

    private void startPrimeSearch(boolean continueSearch) {
        if (!isSearching || primeSearchThread == null || !primeSearchThread.isAlive()) {
            if (!continueSearch) {
                currentNumber = 3; // Only reset if not continuing an ongoing search
            }
            isSearching = true;
            primeSearchThread = new Thread(() -> {
                while (isSearching && !Thread.currentThread().isInterrupted()) {
                    if (isPrime(currentNumber)) {
                        latestPrime = currentNumber;
                        updateUI();
                    }
                    currentNumber += 2;
                    updateUI();
                    try {
                        Thread.sleep(100); // Slow down for visibility
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            primeSearchThread.start();
        }
    }

    private void terminateSearch() {
        isSearching = false;
        if (primeSearchThread != null) {
            primeSearchThread.interrupt();
        }
    }

    private boolean isPrime(int number) {
        if (number <= 1) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    private void updateUI() {
        mainHandler.post(() -> {
            tvCurrentNumber.setText("Current Number: " + currentNumber);
            tvLatestPrime.setText("Latest Prime Found: " + latestPrime);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("PacifierState", switchPacifier.isChecked());
        outState.putInt("CurrentNumber", currentNumber);
        outState.putInt("LatestPrime", latestPrime);
        outState.putBoolean("IsSearching", isSearching); // Save the isSearching state
    }

    public void onBackPressed() {
        if (isSearching) {
            // Confirm with the user if they want to terminate the search
            new AlertDialog.Builder(this)
                    .setTitle("Terminate Search")
                    .setMessage("Are you sure you want to stop searching for prime numbers?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        terminateSearch();
                        PrimeActivity.super.onBackPressed();
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        terminateSearch();
        super.onDestroy();
    }
}
