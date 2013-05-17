package com.example.citywalkapplayout;

import com.google.android.gms.plus.model.people.Person.Cover.Layout;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LeadingMarginSpan.LeadingMarginSpan2;

class MyLeadingMarginSpan2 implements LeadingMarginSpan2 {
    private int margin;
    private int lines;

    MyLeadingMarginSpan2(int lines, int margin) {
        this.margin = margin;
        this.lines = lines;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        if (first) {
            return margin;
        } else {
            return 0;
        }
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, 
            int top, int baseline, int bottom, CharSequence text, 
            int start, int end, boolean first, Layout layout) {}

    @Override
    public int getLeadingMarginLineCount() {
        return lines;
    }

	@Override
	public void drawLeadingMargin(Canvas arg0, Paint arg1, int arg2, int arg3,
			int arg4, int arg5, int arg6, CharSequence arg7, int arg8,
			int arg9, boolean arg10, android.text.Layout arg11) {
		// TODO Auto-generated method stub
		
	}
};
