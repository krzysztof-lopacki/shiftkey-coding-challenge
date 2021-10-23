package com.shiftkey.codingchallenge.test

import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

private val INSTANT_SCHEDULER = Schedulers.trampoline()

class RxJavaSchedulerRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler { INSTANT_SCHEDULER }
                RxJavaPlugins.setNewThreadSchedulerHandler { INSTANT_SCHEDULER }
                RxJavaPlugins.setComputationSchedulerHandler { INSTANT_SCHEDULER }

                base?.evaluate()

                RxJavaPlugins.reset()
            }
        }
    }
}