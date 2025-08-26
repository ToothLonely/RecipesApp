package com.example.recipesapp.ui.recipes.recipe

import android.widget.SeekBar

class PortionSeekBarListener(val onChangeOnClickListener: (Int) -> Unit) :
    SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onChangeOnClickListener(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}