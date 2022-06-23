package com.gals.prayertimes.ui

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup

class CustomRadioGroup {
    var radios: MutableList<RadioButton> = ArrayList()
    lateinit var selectedRadio: RadioButton

    /**
     * This occurs everytime when one of RadioButtons is clicked,
     * and deselects all others in the group.
     */
    private var onClick = View.OnClickListener { v: View ->

        // let's deselect all radios in group
        for (rb in radios) {
            val p = rb.parent
            if (p.javaClass == RadioGroup::class.java) {
                // if RadioButton belongs to RadioGroup,
                // then deselect all radios in it
                val rg = p as RadioGroup
                rg.clearCheck()
            } else {
                // if RadioButton DOES NOT belong to RadioGroup,
                // just deselect it
                rb.isChecked = false
            }
        }
        // now let's select currently clicked RadioButton
        //            if (v.getClass().equals(RadioButton.class)) {
        val rb = v as RadioButton
        rb.isChecked = true
        selectedRadio = rb
    }

    /**
     * Constructor, which allows you to pass number of RadioButton instances,
     * making a group.
     *
     * @param radios One RadioButton or more.
     */
    constructor(vararg radios: RadioButton) : super() {
        for (rb in radios) {
            this.radios.add(rb)
            rb.setOnClickListener(onClick)
        }
    }

    /**
     * Constructor, which allows you to pass number of RadioButtons
     * represented by resource IDs, making a group.
     *
     * @param view      Current View (or Activity) to which those RadioButtons
     * belong.
     * @param radiosIDs One RadioButton or more.
     */
    constructor(
        view: View,
        vararg radiosIDs: Int
    ) : super() {
        for (radioButtonID in radiosIDs) {
            val rb = view.findViewById<View>(radioButtonID) as RadioButton
            radios.add(rb)
            rb.setOnClickListener(onClick)
        }
    }

    fun setAllDisabled() {
        for (current in radios) {
            current.isEnabled = false
        }
    }

    fun setAllEnabled() {
        for (current in radios) {
            current.isEnabled = true
        }
    }
}