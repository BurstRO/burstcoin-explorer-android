package com.harrysoft.burstcoinexplorer.di;

import com.harrysoft.burstcoinexplorer.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivitiesModule.class,
        FragmentsModule.class,
        ToolsModule.class,
        RepositoriesModule.class,
        BurstServiceModule.class,
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App app);
        AppComponent build();
    }
    void inject(App app);
}
