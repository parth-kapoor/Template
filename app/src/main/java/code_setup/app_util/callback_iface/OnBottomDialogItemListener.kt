package code_setup.app_util.callback_iface

import android.view.View

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License

        */
interface OnBottomDialogItemListener<T> {

    fun onItemClick(
            view: View,
            position: Int,
            type: Int,
            t: Any)

}