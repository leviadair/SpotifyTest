package spg01.SpotifyTest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.spg01.spotifytest.MainActivity;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import kaaes.spotify.webapi.android.SpotifyApi;

/**
 * Created by spg01 on 11/25/2017.
 */

public class Controller implements ConnectionStateCallback {

    public static Controller main;

    public Player mPlayer;
    public SpotifyApi spotifyApi;

    private Context context;

    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "53398bdf6a4c4f77ab76021fd093347d";
    private static final String REDIRECT_URI = "http://facebook.com";

    public Controller(Activity activity) {
        this.context = activity.getBaseContext();

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(activity, REQUEST_CODE, request);
    }

    public void authenticate(Intent intent, int requestCode, int resultCode,
                             final Player.NotificationCallback notificationCallback) {
        spotifyApi = new SpotifyApi();

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(context, response.getAccessToken(), CLIENT_ID);
                spotifyApi.setAccessToken(response.getAccessToken());
                Spotify.getPlayer(playerConfig, context, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        main.mPlayer = spotifyPlayer;
                        main.mPlayer.addConnectionStateCallback(Controller.main);
                        main.mPlayer.addNotificationCallback(notificationCallback);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("InitializationObserver", "Get Player Error");
                    }
                });
            }
        }
    }


    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");

        mPlayer.playUri(null, "spotify:track:3CRDbSIZ4r5MsZ0YwxuEkn", 0, 0);
    }

    @Override
    public void onLoggedOut() {
        Log.e("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.e("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.e("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

}
