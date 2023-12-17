package com.team.classicrealm.GameUtility;
import android.content.Context;
import android.media.MediaPlayer;

/** Plays music and one-off sound files while managing the resources efficiently */
public class MusicManager {

    private static MusicManager mInstance;
    private MediaPlayer mMediaPlayer;

    private MusicManager() { }

    /**
     * Returns the single instance of this class
     *
     * @return the instance
     */
    public static MusicManager getInstance() {
        if (mInstance == null) {
            mInstance = new MusicManager();
        }

        return mInstance;
    }

    /**
     * Plays the sound with the given resource ID
     *
     * @param context a valid `Context` reference
     * @param soundResourceId the resource ID of the sound (e.g. `R.raw.my_sound`)
     */
    public synchronized void play(final Context context, final int soundResourceId) {
        // if there's an existing stream playing already
        if (mMediaPlayer != null) {
            // stop the stream in case it's still playing
            try {
                mMediaPlayer.stop();
            }
            catch (Exception e) { }

            // release the resources
            mMediaPlayer.release();

            // unset the reference
            mMediaPlayer = null;
        }

        // create a new stream for the sound to play
        mMediaPlayer = MediaPlayer.create(context.getApplicationContext(), soundResourceId);

        // if the instance could be created
        if (mMediaPlayer != null) {
            // set a listener that is called when playback has been finished
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(final MediaPlayer mp) {
                    // if the instance is set
                    if (mp != null) {
                        // release the resources
                        mp.release();

                        // unset the reference
                        mMediaPlayer = null;
                    }
                }

            });

            // start playback
            mMediaPlayer.start();
        }
    }

}
