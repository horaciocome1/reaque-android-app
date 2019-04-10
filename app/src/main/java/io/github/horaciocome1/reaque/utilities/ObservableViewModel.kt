package io.github.horaciocome1.reaque.utilities

import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.ViewModel

open class ObservableViewModel: ViewModel(), Observable {

    private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    // notifies listeners that all properties of this instance have changed
    @Suppress("unused")
    fun notifyChange() = callbacks.notifyCallbacks(this, 0, null)

    // notifies listeners that a specific property has changed.
    // the getter for the property that changes should be marked with [@Bindable] to generate a field in `BR` to be used as `fieldId`
    fun notifyPropertyChanged(fieldId: Int) = callbacks.notifyCallbacks(this, fieldId, null)

}