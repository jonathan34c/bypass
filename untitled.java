
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import in.uncod.android.bypass.Bypass;


/**
 * Created by Jonathan1 on 8/12/15.
 */
public class Markdown {
    private List<CharSequence> contentlist=new ArrayList<CharSequence>();
    private SpannableStringBuilder raw=new SpannableStringBuilder();
    private TextView textview;
    boolean hastext=false;
    private String TAG="MARKDOWN";
    private Boolean fullscreen;
    private YouTubePlayer u2player;
    private int height, width;




    public View parser(final Context Context, String markdowntxt, LinearLayout main, final Activity activity, final String title, final int price) {

        Bypass bypass=new Bypass(Context);
        String markdownfinal;

        ImageLoader.getInstance().init(new UtilsSetConfig(Context).getConfig());
        BitmapFactory.Options option=new BitmapFactory.Options();

        option.inDither = false;
        option.inPurgeable=true;

       // option.inJustDecodeBounds=true;
        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.NONE)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .cacheInMemory(false)
                .decodingOptions(option)
                .resetViewBeforeLoading()
                .build();
        fullscreen=false;
        final DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        markdownfinal=markdowntxt;
//        if(markdowntxt.contains("\n") ){
//
//            String finish =markdowntxt.replace("\n", "  \n").replace("  ","  \n");
//            markdownfinal=finish;
//        }
        if(markdowntxt.contains("![]") ){
            String finish =markdowntxt.replace("![]", "\n![]");
            markdownfinal=finish;
        }

        CharSequence txt = bypass.markdownToSpannable(markdownfinal);

        int i=0;

        for (i=0; i<bypass.getcontent().size();i++){
            contentlist.add(bypass.getcontent().get(i));
            Log.i("rounding", bypass.getcontent().get(i).toString());
            KuoLog.d(TAG, "TYPE" + bypass.gettype().toString() + bypass.getcontent().get(i).toString());
            //KuoLog.d(TAG, bypass.getcontent().get(i).toString());



//            Log.i("BYPASSTYPE", bypass.gettype().get(i).toString());

            if(bypass.getcontent().get(i).toString().contains("https://youtu.be/")||bypass.getcontent().get(i).toString().contains("http://youtu.be/")){
                Log.i("youtubelink111", bypass.getcontent().get(i).toString());

                    final YouTubePlayerView youTubePlayerView = new YouTubePlayerView(Context);

                if(bypass.getcontent().get(i).toString().contains("https://youtu.be/")){
                    String [] stringtext=bypass.getcontent().get(i).toString().split("https://youtu.be/");
                    final String youtube=stringtext[1].replace("/", "").trim();
                    KuoLog.d(TAG, youtube);
                    KuoLog.d(TAG, stringtext[0]);
                    textview=new TextView(Context);
                    textview.setTextColor(Context.getResources().getColor(R.color.black_web));
                    textview.setTypeface(ApplicationService.getFont());

                    main.addView(textview);
                    textview.setText(stringtext[0]);
                    main.addView(youTubePlayerView);
                    youTubePlayerView.initialize(activity.getResources().getString(R.string.google_api_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                            Log.i("YOUTUBE", "initialized");

                            youTubePlayer.cueVideo(youtube);
                            youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                @Override
                                public void onFullscreen(boolean b) {
                                    if(b){
                                        fullscreen=true;
                                        u2player=youTubePlayer;

                                    }else {
                                        fullscreen=false;
                                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                                    }
                                }
                            });

                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                            Log.i("YOUTUBE", youTubeInitializationResult.toString());
                        }
                    });


                }else if (bypass.getcontent().get(i).toString().contains("http://youtu.be/")){
                    String [] stringtext=bypass.getcontent().get(i).toString().split("https://youtu.be/");
                    final String youtube=stringtext[1].replace("/", "").trim();
                    KuoLog.d(TAG, youtube);
                    KuoLog.d(TAG, stringtext[0]);
                    textview=new TextView(Context);
                    textview.setTypeface(ApplicationService.getFont());
                    textview.setTextColor(Context.getResources().getColor(R.color.black_web));
                    main.addView(textview);
                    textview.setText(stringtext[0]);
                    main.addView(youTubePlayerView);
                    youTubePlayerView.initialize(activity.getResources().getString(R.string.google_api_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                            Log.i("YOUTUBE", "initialized");

                            youTubePlayer.cueVideo(youtube);
                            youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                @Override
                                public void onFullscreen(boolean b) {
                                    if(b){
                                        fullscreen=true;
                                        u2player=youTubePlayer;

                                    }else {
                                        fullscreen=false;
                                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                                    }
                                }
                            });

                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                            Log.i("YOUTUBE", youTubeInitializationResult.toString());
                        }
                    });


                }

                //    String youtubelink=bypass.getcontent().get(i).toString().replace("https://youtu.be/","");
                   // final String youtube=youtubelink.replace("/","").trim();

                textview=new TextView(Context);
                textview.setTypeface(ApplicationService.getFont());
                textview.setTextColor(Context.getResources().getColor(R.color.black_web));
                    main.addView(textview);
                    hastext=true;
                    raw.clear();

            }  else if(bypass.getcontent().get(i).toString().contains("https://www.youtube.com/watch")||bypass.getcontent().get(i).toString().contains("http://www.youtube.com/watch")){
                Log.i("youtubelink111", bypass.getcontent().get(i).toString());

                final YouTubePlayerView youTubePlayerView = new YouTubePlayerView(Context);

                if(bypass.getcontent().get(i).toString().contains("https://www.youtube.com/watch")){
                    String [] stringtext=bypass.getcontent().get(i).toString().split("https://www.youtube.com/watch?v=");
                    final String youtube=stringtext[1].replace("/", "").trim();
                    KuoLog.d(TAG, youtube);
                    KuoLog.d(TAG, stringtext[0]);
                    textview=new TextView(Context);
                    textview.setTextColor(Context.getResources().getColor(R.color.black_web));
                    main.addView(textview);
                    textview.setTypeface(ApplicationService.getFont());
                    textview.setText(stringtext[0]);
                    main.addView(youTubePlayerView);
                    youTubePlayerView.initialize(activity.getResources().getString(R.string.google_api_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                            Log.i("YOUTUBE", "initialized");

                            youTubePlayer.cueVideo(youtube);
                            youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                @Override
                                public void onFullscreen(boolean b) {
                                    if(b){
                                        fullscreen=true;
                                        u2player=youTubePlayer;

                                    }else {
                                        fullscreen=false;
                                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                                    }
                                }
                            });

                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                            Log.i("YOUTUBE", youTubeInitializationResult.toString());
                        }
                    });


                }else if (bypass.getcontent().get(i).toString().contains("http://www.youtube.com/watch")){
                    String [] stringtext=bypass.getcontent().get(i).toString().split("http://www.youtube.com/watch?v=");
                    final String youtube=stringtext[1].replace("/", "").trim();
                    KuoLog.d(TAG, youtube);
                    KuoLog.d(TAG, stringtext[0]);
                    textview=new TextView(Context);
                    textview.setTypeface(ApplicationService.getFont());
                    textview.setTextColor(Context.getResources().getColor(R.color.black_web));
                    main.addView(textview);
                    textview.setText(stringtext[0]);
                    main.addView(youTubePlayerView);
                    youTubePlayerView.initialize(activity.getResources().getString(R.string.google_api_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                            Log.i("YOUTUBE", "initialized");

                            youTubePlayer.cueVideo(youtube);
                            youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                                @Override
                                public void onFullscreen(boolean b) {
                                    if(b){
                                        fullscreen=true;
                                        u2player=youTubePlayer;

                                    }else {
                                        fullscreen=false;
                                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                                    }
                                }
                            });

                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                            Log.i("YOUTUBE", youTubeInitializationResult.toString());
                        }
                    });


                }

                //    String youtubelink=bypass.getcontent().get(i).toString().replace("https://youtu.be/","");
                // final String youtube=youtubelink.replace("/","").trim();

                textview=new TextView(Context);
                main.addView(textview);
                textview.setTypeface(ApplicationService.getFont());
                textview.setTextColor(Context.getResources().getColor(R.color.black_web));
                hastext=true;
                raw.clear();

            } else if(bypass.gettype().get(i).toString().equals("IMAGE")){


                Log.i("totalcount", "this is image");
                Log.i("link", bypass.getcontent().get(i).toString());
//                final ImageView imageView = new ImageView(Context);

                final ImageView imageView = new ImageView(Context);

                final ZoomView zoomView=new ZoomView(Context);
                final LinearLayout linear=new LinearLayout(Context);
                linear.setOrientation(LinearLayout.VERTICAL);

                //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setAdjustViewBounds(true);



                final String imagelink;
                final ImageLoader imageLoader = ImageLoader.getInstance();
                if(bypass.getcontent().get(i).toString().startsWith("https:")){
                    imagelink=bypass.getcontent().get(i).toString();


                    imageLoader.displayImage(imagelink, imageView, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
                            KuoLog.d(TAG, "Imagesize" + loadedImage.getHeight() + "   b      width" + loadedImage.getWidth());
                            DisplayMetrics metrics = new DisplayMetrics();
                            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                            KuoLog.d(TAG, "GLMAX"+ Float.toString(GL10.GL_MAX_TEXTURE_SIZE) );


                            if (loadedImage.getHeight() > GL10.GL_MAX_TEXTURE_SIZE) {
                                //float scale = (float) loadedImage.getWidth() / metrics.widthPixels;

                                float scale = (float) GL10.GL_MAX_TEXTURE_SIZE / loadedImage.getHeight();
                                Bitmap[] largeimg=splitBitmap(loadedImage,GL10.GL_MAX_TEXTURE_SIZE);
                                int w=0;
                                for(w=0;w<largeimg.length;w++){
                                    ImageView imagedispaly=new ImageView(Context);
                                    imagedispaly.setImageBitmap(largeimg[w]);
                                    imagedispaly.setAdjustViewBounds(true);
                                    linear.addView(imagedispaly);
                                }

//                                Bitmap bitmap = Bitmap.createBitmap(loadedImage, 0, 0,
//                                        loadedImage.getWidth(),
//                                        (int) (GL10.GL_MAX_TEXTURE_SIZE * scale));
                                //Bitmap bitmap = Bitmap.createScaledBitmap(loadedImage,(int)(loadedImage.getWidth()*scale),GL10.GL_MAX_TEXTURE_SIZE,true);
                                //zoomView.setMinimumHeight((int) (GL10.GL_MAX_TEXTURE_SIZE * scale));
                               // zoomView.setMinimumHeight(GL10.GL_MAX_TEXTURE_SIZE);
                                //imageView.setMinimumHeight(GL10.GL_MAX_TEXTURE_SIZE);
                                    zoomView.addView(linear);
                                zoomView.setMinimumHeight(loadedImage.getHeight());
                                //imageView.setImageBitmap(bitmap);
                            }else{
                                zoomView.addView(imageView);

                            }

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });


                    Log.i("imagelink", "no https");
                }else{
                    imagelink="https:"+bypass.getcontent().get(i).toString();

                    imageLoader.displayImage(imagelink, imageView, options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
                            KuoLog.d(TAG, "Imagesize" + loadedImage.getHeight() + "   b      width" + loadedImage.getWidth());
                            DisplayMetrics metrics = new DisplayMetrics();
                            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                            KuoLog.d(TAG, "GLMAX"+ Float.toString(GL10.GL_MAX_TEXTURE_SIZE) );


                            if (loadedImage.getHeight() > GL10.GL_MAX_TEXTURE_SIZE) {
                                //float scale = (float) loadedImage.getWidth() / metrics.widthPixels;

                                float scale = (float) GL10.GL_MAX_TEXTURE_SIZE / loadedImage.getHeight();

                                int count=(int)(loadedImage.getHeight()/GL10.GL_MAX_TEXTURE_SIZE);
                                Bitmap[] largeimg=splitBitmap(loadedImage,count+1);
                                int w=0;
                                for(w=0;w<largeimg.length;w++){
                                    ImageView imagedispaly=new ImageView(Context);
                                    imagedispaly.setImageBitmap(largeimg[w]);

                                    imagedispaly.setAdjustViewBounds(true);
                                    linear.addView(imagedispaly);
                                }


//                                Bitmap bitmap = Bitmap.createBitmap(loadedImage, 0, 0,
//                                        loadedImage.getWidth(),
//                                        (int) (GL10.GL_MAX_TEXTURE_SIZE * scale));
                                //Bitmap bitmap = Bitmap.createScaledBitmap(loadedImage,(int)(loadedImage.getWidth()*scale),GL10.GL_MAX_TEXTURE_SIZE,true);
                                //zoomView.setMinimumHeight((int) (GL10.GL_MAX_TEXTURE_SIZE * scale));
                                // zoomView.setMinimumHeight(GL10.GL_MAX_TEXTURE_SIZE);
                                //imageView.setMinimumHeight(GL10.GL_MAX_TEXTURE_SIZE);
                                zoomView.addView(linear);
                                zoomView.setMinimumHeight(loadedImage.getHeight());

                                //imageView.setImageBitmap(bitmap);
                            }else{
                                zoomView.addView(imageView);

                            }
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });


                    Log.i("imagelink", "has https");
                }





                main.addView(zoomView);


                textview=new TextView(Context);
                main.addView(textview);


                textview.setTypeface(ApplicationService.getFont());
                textview.setTextColor(Context.getResources().getColor(R.color.black_web));
                hastext=true;
                raw.clear();
            } else{
                raw.append(contentlist.get(i));
                if(hastext!=true){

                    textview=new TextView(Context);
                    textview.setPadding(15,0,0,0);
                    textview.setTextColor(Context.getResources().getColor(R.color.black_web));
                    textview.setText(raw);

                    textview.setTypeface(ApplicationService.getFont());
                    main.addView(textview);
                    hastext=true;
                    KuoLog.d(TAG, "texxt"+raw.toString());
                    Log.i("false", raw.toString());
                }else{
                    textview.setPadding(15,0,0,0);
                    textview.setTextColor(Context.getResources().getColor(R.color.black_web));
                    textview.setTypeface(ApplicationService.getFont());
                    textview.setText(raw);
                    KuoLog.d(TAG, "texxt"+raw.toString());

                }


            }

        }

        return main;
    }



    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };
    public Boolean getfullscreen(){


        return fullscreen;


    }
;
    public void setfullscreen(Boolean full){
        fullscreen=full;

    }
    public YouTubePlayer getYoutubeplayer(){


        return u2player;


    }
    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public Bitmap[] splitBitmap(Bitmap bitmap, int yCount) {
        // Allocate a two dimensional array to hold the individual images.
        Bitmap[] bitmaps = new Bitmap[yCount];
        int width, height;
        // Divide the original bitmap width by the desired vertical column count
        width=bitmap.getWidth();
        // Divide the original bitmap height by the desired horizontal row count
        height = bitmap.getHeight() / yCount;
        // Loop the array and create bitmaps for each coordinate
            for(int y = 0; y < yCount; ++y) {
                // Create the sliced bitmap
                bitmaps[y] = Bitmap.createBitmap(bitmap,  0, y * height, width, height);
            }
        System.gc();
        // Return the array

        return bitmaps;

    }


}