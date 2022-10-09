package com.yang.lib_common.base.di.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yang.lib_common.base.repository.BaseRepository
import java.lang.reflect.InvocationTargetException

class BaseViewModelFactory <B : BaseRepository>(
    private val application: Application,
    private val baseRepository: B
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            modelClass.canonicalName?.run {
                val viewModel = Class.forName(this)
                    .getConstructor(Application::class.java,BaseRepository::class.java)
                    .newInstance(application,baseRepository) as ViewModel
                viewModel as T
            }!!
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } catch (e: InstantiationException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}