package com.prudhvilearning.movieslot.koin

import com.prudhvilearning.movieslot.repo.MoviesRepo
import com.prudhvilearning.movieslot.viewModel.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repoModule = module {
    single { MoviesRepo(get()) } // get() will inject ApiInterface
}

val viewModelModule = module {
    viewModel { MovieViewModel(get()) } // get() will inject MoviesRepo
}