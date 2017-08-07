package de.misi.idea.junit5.extensions

import com.intellij.testFramework.TestLoggerFactory
import com.intellij.testFramework.runInEdtAndWait
import java.lang.reflect.InvocationTargetException

class TestcaseRunner {

    @Throws(Throwable::class)
    fun run(caller: (() -> Unit)) {
        val throwables = arrayOfNulls<Throwable>(1)

        val runnable = Runnable {
            try {
                caller()
                TestLoggerFactory.onTestFinished(true)
            } catch (e: InvocationTargetException) {
                TestLoggerFactory.onTestFinished(false)
                e.fillInStackTrace()
                throwables[0] = e.targetException
            } catch (e: IllegalAccessException) {
                TestLoggerFactory.onTestFinished(false)
                e.fillInStackTrace()
                throwables[0] = e
            } catch (e: Throwable) {
                TestLoggerFactory.onTestFinished(false)
                throwables[0] = e
            }
        }

        runInEdtAndWait {
            runnable.run()
        }

        if (throwables[0] != null) {
            throw throwables[0] as Throwable
        }
    }
}