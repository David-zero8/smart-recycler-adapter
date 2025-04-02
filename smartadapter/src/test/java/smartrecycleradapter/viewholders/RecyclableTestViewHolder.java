package smartrecycleradapter.viewholders;

/*
 * Created by Manne Öhlund on 2019-07-16.
 * Copyright (c) All rights reserved.
 */

import android.view.View;

import androidx.annotation.NonNull;

import smartrecycleradapter.viewholder.RecyclableViewHolder;
import smartrecycleradapter.viewholder.SmartViewHolder;

public class RecyclableTestViewHolder extends SmartViewHolder implements RecyclableViewHolder {

    public RecyclableTestViewHolder(View view) {
        super(view);
    }

    @Override
    public void bind(@NonNull Object item) { }

    @Override
    public boolean onFailedToRecycleView() {
        return true;
    }
}
