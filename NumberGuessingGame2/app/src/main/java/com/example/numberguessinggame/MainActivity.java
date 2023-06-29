package com.example.numberguessinggame;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String BALANCE_KEY = "balance";
    AdView adView;
    private RewardedAd rewardedAd,rewardedAd2;
    private EditText guessEditText;
    private Button guessButton,ad_btn,ad_btn2;
    private int randomNumber;
    private TextView chance_count,coin_count,random_number;
    private int count = 10;
    private int walletBalance = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Reward Ad start
        AdRequest adRequest3 = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.rewarded_ad_id),
                adRequest3, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

        //reward Ad end
        //Reward Ad start
        AdRequest adRequest2 = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.rewarded_ad_id2),
                adRequest2, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd2 = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd2 = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

        //reward Ad end


        adView = findViewById(R.id.adView);


        //banner ad start hear
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {


            }
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //banner ad end hear


        guessEditText = findViewById(R.id.guessEditText);
        guessButton = findViewById(R.id.guessButton);
        ad_btn = findViewById(R.id.ad_btn);
        ad_btn2 = findViewById(R.id.ad_btn2);
        chance_count = findViewById(R.id.chance_count);
        coin_count = findViewById(R.id.coin_count);
        random_number = findViewById(R.id.random_number);

        // Load the wallet balance from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        walletBalance = prefs.getInt(BALANCE_KEY, 100);
        updateBalance();

//        walletBalance = 100;
        ad_btn.setEnabled(false);
        ad_btn2.setEnabled(false);



        // Generate a random number between 1 and 100
        Random random = new Random();
        randomNumber = random.nextInt(100) + 1;
        random_number.setText(""+randomNumber);
        coin_count.setText("Balance: $" + walletBalance);
        chance_count.setText("Chance: "+count);





        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String guessText = guessEditText.getText().toString();

                if (!guessText.isEmpty()) {
                    int guess = Integer.parseInt(guessText);
                    checkGuess(guess);
                    if (count <= 0)
                        count = 0;
                    else
                        count--;
                    chance_count.setText("Chance: " + count);
                } else {
                    Toast.makeText(MainActivity.this, "Enter a number", Toast.LENGTH_SHORT).show();
                }

                if (count == 0) {
                    guessButton.setEnabled(false);
                    ad_btn.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Game Over....Go back and Restart the Game", Toast.LENGTH_SHORT).show();
                }
                if (walletBalance<10){
                    guessButton.setEnabled(false);
                    ad_btn2.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Required More Wallet Balance...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Ad Button for wallet
        ad_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rewarded ad Start
                rewardedAd2.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
//                         Called when ad is dismissed.
//                         Set the ad reference to null so you don't show the ad a second time.
                        Log.d(TAG, "Ad dismissed fullscreen content.");
                        rewardedAd = null;
//                        walletBalance+=100;
//                        coin_count.setText("Balance: $"+walletBalance);
//                        guessButton.setEnabled(true);
                        ad_btn2.setEnabled(false);
//                        ad_btn2.setText("Go Back");
//                        updateBalance();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.e(TAG, "Ad failed to show fullscreen content.");
                        rewardedAd2 = null;
                    }

                    @Override
                    public void onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Ad showed fullscreen content.");
                    }
                });

                if (rewardedAd2 != null) {

                    Activity activityContext = MainActivity.this;
                    rewardedAd2.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {


                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                            walletBalance+=100;
                            coin_count.setText("Balance: $"+walletBalance);
                            guessButton.setEnabled(true);
                            ad_btn2.setEnabled(false);
//                    ad_btn2.setText("Go Back");
                            updateBalance();

                        }
                    });
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
//                rewaeded Ad End

            }
        });


        //Ad Button for chance
        ad_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Rewarded ad Start
                rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.");
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Set the ad reference to null so you don't show the ad a second time.
                        Log.d(TAG, "Ad dismissed fullscreen content.");
                        rewardedAd = null;


                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.e(TAG, "Ad failed to show fullscreen content.");
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "Ad showed fullscreen content.");
                    }
                });

                if (rewardedAd != null) {

                    Activity activityContext = MainActivity.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {


                            // Handle the reward.
                            Log.d(TAG, "The user earned the reward.");
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                            count +=1;
//                        updateBalance();
                            chance_count.setText("Chance: "+count);
                            guessButton.setEnabled(true);
                            ad_btn.setEnabled(false);
                            ad_btn.setText("Go Back");

                        }
                    });
                } else {
                    Log.d(TAG, "The rewarded ad wasn't ready yet.");
                }
//                rewaeded Ad End

            }
        });


    }

    private void checkGuess(int guess) {
        if (guess > randomNumber) {
            Toast.makeText(MainActivity.this, " Choose Lower Number", Toast.LENGTH_SHORT).show();
            walletBalance -= 10;
            guessEditText.setText("");
//            ad_btn2.setEnabled(true);
            updateBalance();

        } else if (guess < randomNumber) {
            Toast.makeText(MainActivity.this, "Choose Higher Number", Toast.LENGTH_SHORT).show();
            walletBalance -= 10;
            guessEditText.setText("");
//            ad_btn2.setEnabled(true);
            updateBalance();

        } else if (guess == randomNumber){

            Toast.makeText(MainActivity.this, "Congratulations!  "+ randomNumber + " is the correct number.", Toast.LENGTH_LONG).show();
            walletBalance += 100;
//            ad_btn2.setEnabled(true);
            count=11;
            guessEditText.setText("");
            updateBalance();



            // Reset the game
            Random random = new Random();
            guessButton.setEnabled(true);
            ad_btn2.setEnabled(false);
            randomNumber = random.nextInt(100) + 1;
            random_number.setText(""+randomNumber);
            updateBalance();



        }
        if (walletBalance==0){
            ad_btn2.setEnabled(true);
        }
        coin_count.setText("Balance: $" + walletBalance);
        chance_count.setText("Chance: "+count);

    }

    private void updateBalance() {
        coin_count.setText("Balance: $" + walletBalance);

        // Save the wallet balance to SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(BALANCE_KEY, walletBalance);
        editor.apply();
    }
}