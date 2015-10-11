package youtubebackgroundplayer.youtubebackgroundplayer;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by vuki on 02.09.15..
 */
public class YouTubePlayer extends YouTubeBaseActivity implements com.google.android.youtube.player.YouTubePlayer.OnInitializedListener, YouTubeThumbnailView.OnInitializedListener {


    private static final String API_KEY = "AIzaSyCaWL5Jf4uZgSdpHxVj1CUdtH510UXdiSs";//"API KEY";
    private static final String VIDEO_ID = "7bDLIV96LD4";
    private static final String VIDEO_URL = "https://www.youtube.com/watch?v=7bDLIV96LD4";

    @Bind(R.id.youtube_player)
    YouTubePlayerView videoPlayer;

    @Bind(R.id.youtube_thumbnail)
    YouTubeThumbnailView videoThumbnail;

    private String VIDEO_CODE;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.youtube_layout);

        ButterKnife.bind(this);
        YouTubePlayer player = new YouTubePlayer();

        videoThumbnail.initialize(API_KEY, this);
        videoPlayer.initialize(API_KEY, this);

        String[] url = VIDEO_URL.split("=", 2);
        VIDEO_CODE = url[1];
    }


    @Override
    public void onInitializationSuccess(com.google.android.youtube.player.YouTubePlayer.Provider provider, com.google.android.youtube.player.YouTubePlayer youTubePlayer, boolean complete) {
        if (!complete) {
            youTubePlayer.cueVideo(VIDEO_CODE);
        }

    }

    @Override
    public void onInitializationFailure(com.google.android.youtube.player.YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, youTubeInitializationResult.toString(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
        youTubeThumbnailLoader.setVideo(VIDEO_ID);
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Thumbnail failure", Toast.LENGTH_SHORT).show();
    }
}
