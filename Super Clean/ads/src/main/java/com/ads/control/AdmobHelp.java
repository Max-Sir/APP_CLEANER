package com.ads.control;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class AdmobHelp {
    private static AdmobHelp instance;
    private InterstitialAd mInterstitialAd;
    private AdCloseListener adCloseListener;
    private NativeAd nativeAd;

    private long timeLoad = 0;
    private long TimeReload = 40 * 1000;
    private boolean isReload = false;

    public static AdmobHelp getInstance() {
        if (instance == null) {
            instance = new AdmobHelp();
        }
        return instance;
    }

    private AdmobHelp() {

    }

    public void init(Context context) {
        MobileAds.initialize(context, initializationStatus -> {
        });
        loadInterstitialAd(context);
    }

    private void loadInterstitialAd(final Context context) {
        if (mInterstitialAd != null)
            return;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, context.getString(R.string.admob_full), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                isReload = false;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        adCloseListener.onAdClosed();
                        loadInterstitialAd(context);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        adCloseListener.onAdClosed();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
                if (!isReload) {
                    isReload = true;
                    loadInterstitialAd(context);
                }
            }
        });
    }

    public void showInterstitialAd(Activity activity, final AdCloseListener adCloseListener) {
        if ((timeLoad + TimeReload) < System.currentTimeMillis()) {
            if (canShowInterstitialAd(activity)) {
                this.adCloseListener = adCloseListener;
                mInterstitialAd.show(activity);
                timeLoad = System.currentTimeMillis();
            } else {
                adCloseListener.onAdClosed();
            }
        } else {
            adCloseListener.onAdClosed();
        }

    }

    private boolean canShowInterstitialAd(Context context) {
        return mInterstitialAd != null && context instanceof Activity;
    }

    public interface AdCloseListener {
        void onAdClosed();
    }

    public void loadBanner(final Activity mActivity) {
        final ShimmerFrameLayout containerShimmer = mActivity.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.GONE);
        if (!containerShimmer.isShimmerStarted())
            containerShimmer.startShimmer();
        final LinearLayout adContainer = mActivity.findViewById(R.id.banner_container);

        try {
            AdView adView = new AdView(mActivity);
            adView.setAdUnitId(mActivity.getString(R.string.admob_banner));
            adContainer.addView(adView);
            AdSize adSize = getAdSize(mActivity);
            // Step 4 - Set the adaptive ad size on the ad view.
            adView.setAdSize(adSize);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (containerShimmer.isShimmerStarted())
                        containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    adContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    adContainer.setVisibility(View.GONE);
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            });
        } catch (Exception e) {
        }
    }

    private AdSize getAdSize(Activity mActivity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(mActivity, adWidth);

    }

    public void loadBannerFragment(final Activity mActivity, final View rootView) {
        final ShimmerFrameLayout containerShimmer = rootView.findViewById(R.id.shimmer_container);

        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        final LinearLayout adContainer = rootView.findViewById(R.id.banner_container);

        try {
            AdView adView = new AdView(mActivity);
            adView.setAdUnitId(mActivity.getString(R.string.admob_banner));
            adContainer.addView(adView);
            AdSize adSize = getAdSize(mActivity);
            // Step 4 - Set the adaptive ad size on the ad view.
            adView.setAdSize(adSize);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    adContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    adContainer.setVisibility(View.GONE);
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdClosed() {
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }
            });


        } catch (Exception e) {
        }
    }

    public void loadNative(final Activity mActivity) {
        final ShimmerFrameLayout containerShimmer = mActivity.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        AdLoader.Builder builder = new AdLoader.Builder(mActivity, mActivity.getString(R.string.admob_native))
                .forNativeAd(nati -> {
                    nativeAd = nati;
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    FrameLayout frameLayout =
                            mActivity.findViewById(R.id.fl_adplaceholder);
                    if (frameLayout != null) {
                        frameLayout.setVisibility(View.VISIBLE);
                        NativeAdView adView = (NativeAdView) mActivity.getLayoutInflater()
                                .inflate(R.layout.native_admob_ad, null);
                        populateUnifiedNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build()).build());
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        }).build().loadAd(new AdRequest.Builder().build());

    }

    public void loadNativeFragment(final Context context, final View rootView) {
        final ShimmerFrameLayout containerShimmer = rootView.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(R.string.admob_native))
                .forNativeAd(nati -> {
                    nativeAd = nati;
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    FrameLayout frameLayout =
                            rootView.findViewById(R.id.fl_adplaceholder);
                    if (frameLayout != null) {
                        frameLayout.setVisibility(View.VISIBLE);
                        NativeAdView adView = (NativeAdView) LayoutInflater.from(context)
                                .inflate(R.layout.native_admob_ad, null);
                        populateUnifiedNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build()).build());
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    public void loadNativeWindow(final Context context, final View rootView) {
        final ShimmerFrameLayout containerShimmer =
                (ShimmerFrameLayout) rootView.findViewById(R.id.shimmer_container);
        containerShimmer.setVisibility(View.VISIBLE);
        containerShimmer.startShimmer();
        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(R.string.admob_native))
                .forNativeAd(nati -> {
                    nativeAd = nati;
                    containerShimmer.stopShimmer();
                    containerShimmer.setVisibility(View.GONE);
                    FrameLayout frameLayout =
                            rootView.findViewById(R.id.fl_adplaceholder);
                    if (frameLayout != null) {
                        frameLayout.setVisibility(View.VISIBLE);
                        NativeAdView adView = (NativeAdView) LayoutInflater.from(context)
                                .inflate(R.layout.native_admob_ad, null);
                        populateUnifiedNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build()).build());
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                containerShimmer.stopShimmer();
                containerShimmer.setVisibility(View.GONE);
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {


        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));

        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

    }

}
