/**
 * Copyright 2014  XCL-Charts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Project XCL-Charts 
 * @Description Android图表基类库演示
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.7
 */
package com.app.pao.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XChart;
import org.xclcharts.view.ChartView;

import java.util.List;

/**
 * @ClassName DemoView
 * @Description 各个例子view的view基类
 * @author XiongChuanLiang<br/>
 *         (xcl_168@aliyun.com)
 */
public class DemoView extends ChartView {

	public DemoView(Context context) {
		super(context);
	}

	public DemoView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public DemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	public List<XChart> bindChart() {
		return null;
	}

	// Demo中bar chart所使用的默认偏移值。
	// 偏移出来的空间用于显示tick,axistitle....
	protected int[] getBarLnDefaultSpadding() {
		int[] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 28); // left
		ltrb[1] = DensityUtil.dip2px(getContext(), 20); // top
		ltrb[2] = DensityUtil.dip2px(getContext(), 0); // right
		ltrb[3] = DensityUtil.dip2px(getContext(), 20); // bottom
		return ltrb;
	}

	protected int[] getPieDefaultSpadding() {
		int[] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 10); // left
		ltrb[1] = DensityUtil.dip2px(getContext(), 10); // top
		ltrb[2] = DensityUtil.dip2px(getContext(), 10); // right
		ltrb[3] = DensityUtil.dip2px(getContext(), 10); // bottom
		return ltrb;
	}

	protected int[] getPlayBackSpadding() {
		int[] ltrb = new int[4];
		ltrb[0] = DensityUtil.dip2px(getContext(), 16); // left
		ltrb[1] = DensityUtil.dip2px(getContext(), 0); // top
		ltrb[2] = DensityUtil.dip2px(getContext(), 16); // right
		ltrb[3] = DensityUtil.dip2px(getContext(), 0); // bottom
		return ltrb;
	}

	protected int[] getHistorySpadding() {
		int[] ltrb = new int[4];
		ltrb[0] = 0; // left
		ltrb[1] = 0;// top
		ltrb[2] = 0; // right
		ltrb[3] = DensityUtil.dip2px(getContext(), 3);; // bottom
		return ltrb;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

	}

}
