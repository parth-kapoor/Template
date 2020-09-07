package com.base.util

import io.reactivex.Scheduler

/**
 * Created by arischoice on 20/1/2019.
 */
interface SchedulerProvider {
    fun ui(): Scheduler
    fun computation(): Scheduler
    fun io(): Scheduler
}