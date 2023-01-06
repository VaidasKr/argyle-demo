package lt.vaidas.argyledemo.links

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val SEARCH_SKIP_DURATION = 500L

val linkModule = module {
    viewModel {
        val resources = androidContext().resources
        LinksViewModel(
            useCase = LoadLinksUseCase(get(), LinkItemMapper(resources)),
            errorFactory = LinkLoadErrorFactory(resources),
            messageFactory = EmptyResultMessageFactory(resources),
            searchThrottle = LinkSearchThrottle(SEARCH_SKIP_DURATION)
        )
    }
}
