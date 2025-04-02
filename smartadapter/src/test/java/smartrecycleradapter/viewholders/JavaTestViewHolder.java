package smartrecycleradapter.viewholders;

/*
 * Created by Manne Öhlund on 2019-12-16.
 * Copyright (c) All rights reserved.
 */

import android.view.View;

import org.jetbrains.annotations.NotNull;

import smartrecycleradapter.viewholder.SmartViewHolder;

public class JavaTestViewHolder extends SmartViewHolder<Object> {

    public JavaTestViewHolder(@NotNull View view) {
        super(view);
    }

    @Override
    public void bind(@NotNull Object item) {

    }

    public static class Abc {

    }
}
