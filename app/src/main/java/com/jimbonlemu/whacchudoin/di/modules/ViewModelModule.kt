package com.jimbonlemu.whacchudoin.di.modules

import com.jimbonlemu.whacchudoin.view_models.AddStoryViewModel
import com.jimbonlemu.whacchudoin.view_models.AuthViewModel
import com.jimbonlemu.whacchudoin.view_models.GetAllStoryViewModel
import com.jimbonlemu.whacchudoin.view_models.GetDetailStoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { GetAllStoryViewModel(get()) }
    viewModel { GetDetailStoryViewModel(get()) }
    viewModel { AddStoryViewModel(get()) }
}