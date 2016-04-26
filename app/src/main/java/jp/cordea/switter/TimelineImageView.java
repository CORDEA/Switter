package jp.cordea.switter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.MediaEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Yoshihiro Tanaka on 16/04/21.
 */
public class TimelineImageView extends LinearLayout {

    @Bind(R.id.container_start)
    View startContainer;

    @Bind(R.id.container_end)
    View endContainer;

    @Bind(R.id.image_view_start_top)
    ImageView startTopImageView;

    @Bind(R.id.image_view_start_bottom)
    ImageView startBottomImageView;

    @Bind(R.id.image_view_end_top)
    ImageView endTopImageView;

    @Bind(R.id.image_view_end_bottom)
    ImageView endBottomImageView;

    public TimelineImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_timeline_image, this);
        ButterKnife.bind(this);
    }

    public void setImages(List<MediaEntity> mediaEntities) {
        // TODO
        setVisibility(VISIBLE);
        switch (mediaEntities.size()) {
            case 1:
                oneImageLayout(mediaEntities);
                break;
            case 2:
                twoImageLayout(mediaEntities);
                break;
            case 3:
                threeImageLayout(mediaEntities);
                break;
            case 4:
                fourImageLayout(mediaEntities);
                break;
        }
    }

    private void oneImageLayout(List<MediaEntity> mediaEntities) {
        LayoutParams layoutParams = (LayoutParams) endContainer.getLayoutParams();
        layoutParams.weight = 0;
        endContainer.setLayoutParams(layoutParams);
        layoutParams = (LayoutParams) startBottomImageView.getLayoutParams();
        layoutParams.weight = 0;
        startBottomImageView.setLayoutParams(layoutParams);

        setImage(mediaEntities.get(0).mediaUrl, startTopImageView);
    }

    private void twoImageLayout(List<MediaEntity> mediaEntities) {
        LayoutParams layoutParams = (LayoutParams) startBottomImageView.getLayoutParams();
        layoutParams.weight = 0;
        startBottomImageView.setLayoutParams(layoutParams);
        layoutParams = (LayoutParams) endBottomImageView.getLayoutParams();
        layoutParams.weight = 0;
        endBottomImageView.setLayoutParams(layoutParams);

        setImage(mediaEntities.get(0).mediaUrl, startTopImageView);
        setImage(mediaEntities.get(1).mediaUrl, endTopImageView);
    }

    private void threeImageLayout(List<MediaEntity> mediaEntities) {
        LayoutParams layoutParams = (LayoutParams) startBottomImageView.getLayoutParams();
        layoutParams.weight = 0;
        startBottomImageView.setLayoutParams(layoutParams);

        setImage(mediaEntities.get(0).mediaUrl, startTopImageView);
        setImage(mediaEntities.get(1).mediaUrl, endTopImageView);
        setImage(mediaEntities.get(2).mediaUrl, endBottomImageView);
    }

    private void fourImageLayout(List<MediaEntity> mediaEntities) {
        setImage(mediaEntities.get(0).mediaUrl, startTopImageView);
        setImage(mediaEntities.get(1).mediaUrl, startBottomImageView);
        setImage(mediaEntities.get(2).mediaUrl, endTopImageView);
        setImage(mediaEntities.get(3).mediaUrl, endBottomImageView);
    }

    private void setImage(String url, ImageView imageView) {
        Picasso.with(getContext())
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .into(imageView);
    }
}
