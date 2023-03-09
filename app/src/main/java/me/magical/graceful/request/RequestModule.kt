package me.magical.graceful.request

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RequestModule {

    var apiOpenUrl: String = "https://api.apiopen.top"
    var wallPagerUrl: String = "http://service.picasso.adesk.com"

    @Provides
    @Named("apiOpen")
    @Singleton
    fun getApiOpen():RequestApi{
        return MyRequest.instance.create(apiOpenUrl,RequestApi::class.java)
    }

    @Provides
    @Named("wallPagerApi")
    @Singleton
    fun getWallPagerApi():WallPagerApi{
        return MyRequest.instance.create(wallPagerUrl,WallPagerApi::class.java)
    }
}