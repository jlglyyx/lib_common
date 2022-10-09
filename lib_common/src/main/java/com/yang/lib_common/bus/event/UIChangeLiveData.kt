package com.yang.lib_common.bus.event

class UIChangeLiveData {

    val showLoadingEvent:SingleLiveEvent<String> = SingleLiveEvent()

    val dismissDialogEvent:SingleLiveEvent<Boolean> = SingleLiveEvent()

    val refreshEvent:SingleLiveEvent<Boolean> = SingleLiveEvent()

    val loadMoreEvent:SingleLiveEvent<Boolean> = SingleLiveEvent()

    val finishActivityEvent:SingleLiveEvent<Boolean> = SingleLiveEvent()

    val requestSuccessEvent:SingleLiveEvent<Any> = SingleLiveEvent()

    val requestFailEvent:SingleLiveEvent<Any> = SingleLiveEvent()

    val showRecyclerViewEvent:SingleLiveEvent<Int> = SingleLiveEvent()

}