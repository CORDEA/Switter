package jp.cordea.switter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.twitter.sdk.android.core.models.MediaEntity;

import java.util.List;

import butterknife.Bind;

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
    }

    public void setImages(List<MediaEntity> mediaEntities) {
        // TODO
        switch (mediaEntities.size()) {
            case 1:
                oneImageLayout();
                break;
            case 2:
                twoImageLayout();
                break;
            case 3:
                threeImageLayout();
                break;
            case 4:
                break;
        }
    }

    private void oneImageLayout() {
        LayoutParams layoutParams = (LayoutParams) endContainer.getLayoutParams();
        layoutParams.weight = 0;
        endContainer.setLayoutParams(layoutParams);
        layoutParams = (LayoutParams) startBottomImageView.getLayoutParams();
        layoutParams.weight = 0;
        startBottomImageView.setLayoutParams(layoutParams);
    }

    private void threeImageLayout() {
        LayoutParams layoutParams = (LayoutParams) startBottomImageView.getLayoutParams();
        layoutParams.weight = 0;
        startBottomImageView.setLayoutParams(layoutParams);
    }

    private void twoImageLayout() {
        LayoutParams layoutParams = (LayoutParams) startBottomImageView.getLayoutParams();
        layoutParams.weight = 0;
        startBottomImageView.setLayoutParams(layoutParams);
        layoutParams = (LayoutParams) endBottomImageView.getLayoutParams();
        layoutParams.weight = 0;
        endBottomImageView.setLayoutParams(layoutParams);
    }
}
