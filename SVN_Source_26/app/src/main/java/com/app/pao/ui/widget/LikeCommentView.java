package com.app.pao.ui.widget;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.app.pao.R;
import com.app.pao.entity.adapter.DynamicLineItem;
import com.app.pao.entity.network.GetDynamicListResult;
import com.app.pao.ui.widget.helper.LinkTouchMovementMethod;
import com.app.pao.ui.widget.helper.TouchSpan;
import com.app.pao.utils.DeviceUtils;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raulfan on 16/1/12.
 */
public class LikeCommentView extends FrameLayout {

    private Context context;
    private TextView likeView;
    private View likeLayout;
    private View dividerView;
    private LinearLayout commentLayout;

    private List<TextView> commentViews = new ArrayList<>();

    public LikeCommentView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public LikeCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        initView();
    }


    private void initView() {

        View view = LayoutInflater.from(context).inflate(R.layout.like_comment, null);
        addView(view);

        likeView = (TextView) view.findViewById(R.id.likes);
        likeView.setMovementMethod(new LinkTouchMovementMethod());
        likeView.setClickable(false);
        likeView.setLinksClickable(true);

        likeLayout = view.findViewById(R.id.likeLayout);


        dividerView = view.findViewById(R.id.divider);

        commentLayout = (LinearLayout) view.findViewById(R.id.commentLayout);

    }

    public void updateWithItem(int position, DynamicLineItem item) {
        //评论列表
        List<GetDynamicListResult.CommentsEntity> comments = item.result.getComments();

        //点赞不显示
        likeLayout.setVisibility(GONE);
        dividerView.setVisibility(GONE);

        //显示点赞内容
        //        if (item.likeSpanStr != null){
        //            likeView.setText(item.likeSpanStr);
        //    }
        //显示评论内容
        if (comments.isEmpty()) {
            commentLayout.setVisibility(GONE);
            for (TextView textView : commentViews) {
                textView.setText("");
                textView.setVisibility(GONE);
            }
        } else {
            commentLayout.setVisibility(VISIBLE);
            int textViewCount = commentViews.size();
            for (int i = 0; i < textViewCount; i++) {
                TextView textView = commentViews.get(i);
                textView.setText("");
                if (i < comments.size()) {
                    textView.setVisibility(VISIBLE);
                } else {
                    textView.setVisibility(GONE);
                }
            }

            for (int i = 0; i < comments.size(); i++) {

                TextView textView;

                if (textViewCount > 0 && i < textViewCount) {
                    textView = commentViews.get(i);
                } else {
                    textView = new TextView(context);
                    textView.setMovementMethod(new LinkTouchMovementMethod());
                    textView.setClickable(false);
                    textView.setLinksClickable(true);
                    textView.setBackgroundResource(R.drawable.bg_list_item);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                            .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, DeviceUtils.dp2px(context, 2));
                    commentLayout.addView(textView, params);
                    commentViews.add(textView);
                }
                GetDynamicListResult.CommentsEntity commentItem = comments.get(i);

                LinkTouchMovementMethod method = (LinkTouchMovementMethod) textView.getMovementMethod();
                method.setPosition(position);
                method.setUserId(commentItem.getUserid());
                method.setNickName(commentItem.getNickname());

                textView.setVisibility(VISIBLE);
                SpannableString spannableString = genCommentSpanStr(commentItem);
                textView.setText(spannableString);
            }
        }
    }

    /**
     * 组合SpanString
     *
     * @param item
     * @return
     */
    private SpannableString genCommentSpanStr(GetDynamicListResult.CommentsEntity item) {

        StringBuilder builder = new StringBuilder();
        builder.append(item.getNickname());
        if (item.getReplyuserid() != 0) {
            builder.append("回复");
            builder.append(item.getReplynickname());
        }
        builder.append(": ");
        builder.append(item.getComment());
        SpannableString spannableString = new SpannableString(builder.toString());

        if (item.getReplyuserid() == 0) {
            spannableString.setSpan(new TouchSpan(context, item.getUserid()), 0, item.getNickname().length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            int position = 0;
            spannableString.setSpan(new TouchSpan(context, item.getUserid()), position, position + item.getNickname()
                            .length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            position += item.getNickname().length() + 2; //2="回复"
            spannableString.setSpan(new TouchSpan(context, item.getReplyuserid()), position, position + item
                            .getReplynickname()
                            .length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

}


