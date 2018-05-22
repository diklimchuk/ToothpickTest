package com.github.toothpicktest.presentation.mvp

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.ViewCommand
import com.arellomobile.mvp.viewstate.strategy.StateStrategy

/**
 * Command will be added to the end of commands queue.
 * If command with the same tag is already in the queue it will be removed.
 */
class AddToEndSingleByTagStateStrategy : StateStrategy {

    override fun <View : MvpView> beforeApply(
            currentState: MutableList<ViewCommand<View>>,
            incomingCommand: ViewCommand<View>
    ) {
        val iterator = currentState.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()

            if (entry.tag == incomingCommand.tag) {
                iterator.remove()
                break
            }
        }

        currentState.add(incomingCommand)
    }

    override fun <View : MvpView> afterApply(
            currentState: List<ViewCommand<View>>,
            incomingCommand: ViewCommand<View>
    ) { // pass
    }
}