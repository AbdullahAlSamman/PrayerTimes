package com.gals.prayertimes.viewmodel.observer

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import com.gals.prayertimes.BR
import com.gals.prayertimes.model.NotificationType

class RadioGroupObserver : BaseObservable() {
    var notificationType: ObservableField<String> = ObservableField()

    @get:Bindable
    var fullAthan: Boolean = false
        set(value) {
            field = value
            if (value) {
                notificationType.set(NotificationType.FULL.value)
            }
            notifyPropertyChanged(BR.fullAthan)
        }

    @get:Bindable
    var halfAthan: Boolean = false
        set(value) {
            field = value
            if (value) {
                notificationType.set(NotificationType.HALF.value)
            }
            notifyPropertyChanged(BR.halfAthan)
        }

    @get:Bindable
    var toneAthan: Boolean = false
        set(value) {
            field = value
            if (value) {
                notificationType.set(NotificationType.TONE.value)
            }
            notifyPropertyChanged(BR.toneAthan)
        }

    @get:Bindable
    var silentAthan: Boolean = false
        set(value) {
            field = value
            if (value) {
                notificationType.set(NotificationType.SILENT.value)
            }
            notifyPropertyChanged(BR.silentAthan)
        }
}