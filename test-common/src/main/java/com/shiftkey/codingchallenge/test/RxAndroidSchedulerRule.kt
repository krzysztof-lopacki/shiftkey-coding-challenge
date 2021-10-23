package com.shiftkey.codingchallenge.test

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

private val INSTANT_SCHEDULER = Schedulers.trampoline()

class RxAndroidSchedulerRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxAndroidPlugins.reset()
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { INSTANT_SCHEDULER }

                base?.evaluate()

                RxAndroidPlugins.reset()
            }
        }
    }
}