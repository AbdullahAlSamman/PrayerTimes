package com.gals.prayertimes.ui.components.constraint

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet

object PrayerHeaderConstraint {
    val header = ConstraintSet {
        val imageComposable = createRefFor("backgroundImage")
        val remainingTextBoxComposable = createRefFor("prayerRemainingText")
        val remainingTimeTextComposable = createRefFor("prayerRemainingTime")
        val settingsButtonComposable = createRefFor("settingsButton")

        constrain(imageComposable) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(remainingTextBoxComposable) {
            bottom.linkTo(imageComposable.bottom, 48.dp)
            end.linkTo(imageComposable.end, 24.dp)
        }

        constrain(remainingTimeTextComposable) {
            bottom.linkTo(imageComposable.bottom, 24.dp)
            start.linkTo(remainingTextBoxComposable.start)
            end.linkTo(remainingTextBoxComposable.end)
            top.linkTo(remainingTextBoxComposable.bottom, 12.dp)
        }

        constrain(settingsButtonComposable) {
            top.linkTo(imageComposable.top, 12.dp)
            start.linkTo(imageComposable.start, 12.dp)
        }
    }

    val row = ConstraintSet {
        val prayerRemainingString = createRefFor("prayerRemainingString")
        val prayerNameString = createRefFor("prayerNameString")

        constrain(prayerRemainingString) {
            start.linkTo(parent.start)
        }

        constrain(prayerNameString) {
            start.linkTo(prayerRemainingString.end, 4.dp)
            end.linkTo(parent.end)
        }
    }
}

