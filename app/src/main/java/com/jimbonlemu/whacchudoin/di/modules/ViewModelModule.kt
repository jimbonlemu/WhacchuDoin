package com.jimbonlemu.whacchudoin.di.modules

import com.jimbonlemu.whacchudoin.view_models.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
}